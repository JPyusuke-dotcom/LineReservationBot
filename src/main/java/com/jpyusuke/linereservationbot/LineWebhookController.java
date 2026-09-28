package com.jpyusuke.linereservationbot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@RestController
public class LineWebhookController {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestClient restClient = RestClient.create();

    private final String channelAccessToken =
            System.getenv("LINE_CHANNEL_ACCESS_TOKEN");

    @PostMapping("/callback")
    public String callback(@RequestBody String body) throws Exception {

        System.out.println(body);

        JsonNode root = objectMapper.readTree(body);
        JsonNode event = root.path("events").get(0);

        if (event != null
                && "message".equals(event.path("type").asText())
                && "text".equals(event.path("message").path("type").asText())) {

            String text = event.path("message").path("text").asText();
            String replyToken = event.path("replyToken").asText();

            if ("予約".equals(text)) {
                reply(replyToken, "予約ですね！予約フォームを準備中です。");
            }
        }

        return "OK";
    }

    private void reply(String replyToken, String message) {

        Map<String, Object> requestBody = Map.of(
                "replyToken", replyToken,
                "messages", List.of(
                        Map.of(
                                "type", "text",
                                "text", message
                        )
                )
        );

        restClient.post()
                .uri("https://api.line.me/v2/bot/message/reply")
                .header("Authorization", "Bearer " + channelAccessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .toBodilessEntity();
    }
}

