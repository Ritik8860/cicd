package com.example.demo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelloServiceTest {

    private final HelloService helloService = new HelloService();

    @Test
    void testGreet() {
        String result = helloService.greet("Ritik");
        assertEquals("Hello, Ritik!", result);
    }
}
