package com.example.dits.service;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

@SpringBootConfiguration
@ActiveProfiles("test")
@ComponentScan(value = "com.example.dits")
public class TestSpringBootConfiguration {
}
