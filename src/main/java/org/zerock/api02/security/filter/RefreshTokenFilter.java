/*
820
 */
package org.zerock.api02.security.filter;

import com.google.gson.Gson;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;
import org.zerock.api02.security.exception.RefreshTokenException;
import org.zerock.api02.util.JWTUtil;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Log4j2
@RequiredArgsConstructor
public class RefreshTokenFilter extends OncePerRequestFilter {

    private final String refreshPath;
    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        log.info(path);
        log.info(refreshPath);

        if (!path.equals(refreshPath)) {
            log.info("skip refresh token filter........................");
            filterChain.doFilter(request, response);
            return;
        }
        log.info("Refresh Token Filter..... run......................");
        //p828
        Map<String, String> tokens = parseRequestJSON(request);

        String accessToken = tokens.get("accessToken");
        String refreshToken = tokens.get("refreshToken");

        log.info("old accessToken={}", accessToken);
        log.info("old refreshToken={}", refreshToken);

        try {
            checkAccessToken(accessToken);
        } catch (RefreshTokenException e) {
            e.sendResponseError(response);
            return; // 더이상실행할필요없음
        }

        // 830
        Map<String, Object> refreshClaims = null;

        try {
            refreshClaims = checkRefreshToken(refreshToken);
            log.info(refreshClaims);
            // 831
            //Refresh유효기간이 얼마남지않은경우
            Integer exp = (Integer) refreshClaims.get("exp");

            Date expTime = new Date(Instant.ofEpochMilli(exp).toEpochMilli() * 1000);
            Date current = new Date(System.currentTimeMillis());

            // 만료기간과 현재시간의 간격계산
            //만일 3일미만인 경우에는 refresh token도 재생성
            long gapTime = (expTime.getTime() - current.getTime());

            log.info("---------------------------");
            log.info("current: {}", current);
            log.info("expTime: {}", expTime);
            log.info("gap: {}", gapTime);

            String mid = (String) refreshClaims.get("mid");

            String accessTokenValue = jwtUtil.generateToken(Map.of("mid", mid), 1);
            String refreshTokenValue = tokens.get("refreshToken");

            if (gapTime < (10000 * 60 * 60 * 24 * 3)) {
                log.info("new refresh token required....");
                refreshTokenValue = jwtUtil.generateToken(Map.of("mid", mid), 30 );
            }
            log.info("new refresh token required..........................");
            log.info("accessToken={}", accessToken);
            log.info("refreshToken={}", refreshToken);

            sendTokens(accessTokenValue, refreshTokenValue, response);

        } catch (RefreshTokenException e) {
            e.sendResponseError(response);
            return;
        }
    }

    private Map<String, String> parseRequestJSON(HttpServletRequest request) {
        try (Reader reader = new InputStreamReader(request.getInputStream())) {
            Gson gson = new Gson();
            return gson.fromJson(reader, Map.class);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private void checkAccessToken(String accessToken) throws RefreshTokenException {
        try {
            jwtUtil.validateToken(accessToken);
        } catch (ExpiredJwtException e) {
            log.info("Access Token has expired");
        } catch (Exception e) {
            throw new RefreshTokenException(RefreshTokenException.ErrorCase.NO_ACCESS);
        }
    }

    private Map<String, Object> checkRefreshToken(String refreshToken) throws RefreshTokenException {

        try {
            Map<String, Object> values = jwtUtil.validateToken(refreshToken);
            return values;
        } catch (ExpiredJwtException e) {
            throw new RefreshTokenException(RefreshTokenException.ErrorCase.OLD_REFRESH);
        } catch (MalformedJwtException e) {
            log.error("MalformedJwtException");
            throw new RefreshTokenException(RefreshTokenException.ErrorCase.NO_REFRESH);
        } catch (Exception e) {
            new RefreshTokenException(RefreshTokenException.ErrorCase.NO_REFRESH);
        }
        return null;
    }

    private void sendTokens(String accessTokenValue, String refreshTokenValue, HttpServletResponse response) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Gson gson = new Gson();

        String jsonStr = gson.toJson(Map.of("accessToken", accessTokenValue, "refreshToken", refreshTokenValue));

        try {
            response.getWriter().println(jsonStr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
