package com.hetongre4per.ita.modules.system.controller;

import com.hetongre4per.ita.config.DeepSeekComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/ai")
public class AIController {

    @Autowired
    private DeepSeekComponent deepSeekService;

    @PostMapping("/chat")
    public String chat(@RequestBody Map<String, String> request) throws IOException {
        String message = request.get("message");
        return deepSeekService.chatWithDeepSeek(message);
    }
}