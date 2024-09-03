package org.zerock.api02.util;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
@Log4j2
public class JWTUtilTests {

    @Autowired
    private JWTUtil jwtUtil;

    @Test
    public void generate() {
        Map<String, Object> claimMap = Map.of("mid", "ABCDE");
        String jwtStr = jwtUtil.generateToken(claimMap, 1);
        log.info(jwtStr);
    }
/*
2024-09-03T11:29:01.046+09:00  INFO 10160 --- [api01] [    Test worker] org.zerock.api02.util.JWTUtil            : generateToken... key=hello1234567890
2024-09-03T11:29:01.108+09:00  INFO 10160 --- [api01] [    Test worker] org.zerock.api02.util.JWTUtilTests       : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3MjUzMzA2MDEsIm1pZCI6IkFCQ0RFIiwiaWF0IjoxNzI1MzMwNTQxfQ.LYauvtXVRMm48L4jQ0CJcA00OJhRLjSQqkI1eqejNHg

 */

    @Test
    public void validate() {
        String jwtStr = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3MjUzMzA2MDEsIm1pZCI6IkFCQ0RFIiwiaWF0IjoxNzI1MzMwNTQxfQ.LYauvtXVRMm48L4jQ0CJcA00OJhRLjSQqkI1eqejNHg";
        Map<String, Object> claim = jwtUtil.validateToken(jwtStr);
        log.info(claim);
    }
//io.jsonwebtoken.ExpiredJwtException: JWT expired at 2024-09-03T11:30:01Z. Current time: 2024-09-03T11:32:45Z, a difference of 164094 milliseconds.  Allowed clock skew: 0 milliseconds.

    @Test
    public void testAll() {
        String jwtStr = jwtUtil.generateToken(Map.of("mid", "AAAA", "email", "aaaa@bbb.com"), 1);
        log.info(jwtStr);
        Map<String, Object> claim = jwtUtil.validateToken(jwtStr);
        log.info("mid:{}", claim.get("mid"));
        log.info("email:{}", claim.get("email"));
    }

    @Test
    public void string() {
        String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3MjU0MjA1ODYsIm1pZCI6ImFwaXVzZXIxMCIsImlhdCI6MTcyNTMzNDE4Nn0.luWr3ZHZ8LxLwTKPRU0ilc4Wc-sM3sI3ZHIJc220xtA";
        log.info(accessToken.length()); // 152
        log.info(accessToken.substring(140));
    }

}
