package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.stream.Stream;
import java.util.stream.Stream;

@RestController
@RequestMapping("/info")
public class InfoController {

    @Value("${server.port:8080}")
    private String port;

    @GetMapping("/port")
    public String getPort() {
        return port;
    }

    @GetMapping("/sum")
    public Integer getSum() {
        return Stream.iterate(1, a -> a + 1)
                .limit(1_000_000)
                .parallel()
                .reduce(0, Integer::sum);
    }
}
