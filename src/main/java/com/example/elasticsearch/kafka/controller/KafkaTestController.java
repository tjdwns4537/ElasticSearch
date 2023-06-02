package com.example.elasticsearch.kafka.controller;

import com.example.elasticsearch.kafka.service.KafkaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class KafkaTestController {

    @Autowired
    private final KafkaService kafkaService;

    @GetMapping("/kafka")
    public void test() {
        kafkaService.sendMessage("안녕 test1");
    }
}
