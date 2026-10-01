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

        // LINEから届いたJSONを読み込む
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(body);

        // eventsを取得する
        JsonNode events = json.get("events");

        // eventsの中にデータがあるか確認する
        if (events != null && events.size() > 0) {

            // 最初のイベントを取得する
            JsonNode event = events.get(0);

            // メッセージを取得する
            JsonNode message = event.get("message");

            // メッセージの中身があるか確認する
            if (message != null) {

                // ユーザーが入力した文字を取得する
                String text = message.get("text").asText();

                // コンソールに表示する
                System.out.println("LINEから受信：" + text);

                // 「予約」と入力された場合
                if (text.equals("予約")) {
                    System.out.println("予約が入力されました！");
                }
            }
        }

        // LINEに正常に受け取ったことを返す
        return "OK";
    }
}
