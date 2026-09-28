package com.jpyusuke.linereservationbot;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class LineWebhookController {

    @PostMapping("/callback")
    public String callback(@RequestBody String body) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(body);

        System.out.println(json);
        System.out.println(json.get("events"));

        return "OK";
    }
}
