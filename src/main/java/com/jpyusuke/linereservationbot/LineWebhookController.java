package com.jpyusuke.linereservationbot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class LineWebhookController {

    @Value("${line.channel-access-token}")
    private String channelAccessToken;

    @PostMapping("/callback")
    public String callback(@RequestBody String body) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(body);

        JsonNode events = json.get("events");

        if (events != null && events.size() > 0) {

            JsonNode event = events.get(0);

            String replyToken = event.get("replyToken").asText();

            JsonNode message = event.get("message");

            if (message != null) {

                String text = message.get("text").asText();

                System.out.println("LINEから受信：" + text);

                if (text.equals("予約")) {

                    System.out.println("予約が入力されました！");

                    String reservationUrl =
                            "https://brisket-volley-ploy.ngrok-free.dev/callback";

                    sendReply(
                            replyToken,
                            "予約フォームはこちらです。\n"
                            + reservationUrl
                    );
                }
            }
        }

        return "OK";
    }

    private void sendReply(String replyToken, String messageText) {

        String url =
                "https://api.line.me/v2/bot/message/reply";

        RestTemplate restTemplate =
                new RestTemplate();

        HttpHeaders headers =
                new HttpHeaders();

        headers.setContentType(
                MediaType.APPLICATION_JSON
        );

        headers.setBearerAuth(
                channelAccessToken
        );

        String requestBody =
                "{"
                + "\"replyToken\":\"" + replyToken + "\","
                + "\"messages\":[{"
                + "\"type\":\"text\","
                + "\"text\":\"" + messageText + "\""
                + "}]"
                + "}";

        HttpEntity<String> request =
                new HttpEntity<>(
                        requestBody,
                        headers
                );

        restTemplate.postForEntity(
                url,
                request,
                String.class
        );
    }
}
