package org.zerock.api02.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/sample")
public class SampleController {

    @GetMapping("/doA")
    public List<String> doA() {
        return Arrays.asList("AAA", "BBB", "CCC");
    }

    @GetMapping("/")
    public String index() {
        System.out.println("index&&");
        return "index^^";
    }
}
