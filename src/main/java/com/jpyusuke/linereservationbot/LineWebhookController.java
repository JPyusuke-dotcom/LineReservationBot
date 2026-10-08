package com.jpyusuke.linereservationbot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // application.propertiesからLINEのアクセストークンを取得
    @Value("${line.channel-access-token}")
    private String channelAccessToken;

    // LINEからWebhookで送られてきた情報を受け取る
    @PostMapping("/callback")
    public String callback(@RequestBody String body) throws Exception {

        // JSONをJavaで扱えるように変換するためのObjectMapperを作成
        ObjectMapper mapper = new ObjectMapper();

        // LINEから届いたJSON文字列を解析する
        JsonNode json = mapper.readTree(body);

        // JSONの中から「events」を取得
        JsonNode events = json.get("events");

        // イベントが1件以上存在する場合
        if (events != null && events.size() > 0) {

            // 最初のイベントを取得
            JsonNode event = events.get(0);

            // LINEから送られてきた返信用トークンを取得
            String replyToken = event.get("replyToken").asText();

            // メッセージ情報を取得
            JsonNode message = event.get("message");

            // メッセージが存在する場合
            if (message != null) {

                // ユーザーが送ったメッセージ本文を取得
                String text = message.get("text").asText();

                // 「予約」と送られてきた場合
                if (text.equals("予約")) {

                    System.out.println("予約が入力されました！");

                    // LINEユーザーのIDを取得
                    String userId =
                            event.get("source").get("userId").asText();

                    // コンソールでLINEユーザーIDを確認できるように表示
                    System.out.println("LINEユーザーID：" + userId);

                    // 予約フォームのURLを作成
                    // LINEユーザーIDをURLに付けて予約フォームへ渡す
                    String reservationUrl =
                            "https://brisket-volley-ploy.ngrok-free.dev/reservation?lineUserId="
                            + userId;

                    // LINEに予約フォームのURLを返信する
                    sendReply(
                            replyToken,
                            "予約フォームはこちらです。\n"
                            + reservationUrl
                    );
                }
            }
        }

        // LINE側にWebhookの処理が正常終了したことを返す
        return "OK";
    }

    // LINEへメッセージを返信する処理
    private void sendReply(
            String replyToken,
            String messageText) throws Exception {

        // LINE Messaging APIの返信用URL
        String url =
                "https://api.line.me/v2/bot/message/reply";

        // HTTP通信を行うためのRestTemplateを作成
        RestTemplate restTemplate =
                new RestTemplate();

        // HTTPヘッダーを作成
        HttpHeaders headers =
                new HttpHeaders();

        // JSON形式で送信することを指定
        headers.setContentType(
                MediaType.APPLICATION_JSON
        );

        // LINEのアクセストークンを設定
        headers.setBearerAuth(
                channelAccessToken
        );

        // LINEに送るメッセージを作成
        Map<String, Object> message =
                new HashMap<>();

        // メッセージの種類を「text」に設定
        message.put("type", "text");

        // 実際に送る文章を設定
        message.put("text", messageText);

        // LINE APIへ送るリクエスト本文を作成
        Map<String, Object> requestBody =
                new HashMap<>();

        // 返信対象のreplyTokenを設定
        requestBody.put("replyToken", replyToken);

        // 返信するメッセージを設定
        requestBody.put("messages", List.of(message));

        // JavaのMapをJSON文字列に変換
        ObjectMapper mapper =
                new ObjectMapper();

        String jsonBody =
                mapper.writeValueAsString(requestBody);

        // JSONとHTTPヘッダーをまとめてリクエストを作成
        HttpEntity<String> request =
                new HttpEntity<>(
                        jsonBody,
                        headers
                );

        // LINE Messaging APIへPOST送信
        restTemplate.postForEntity(
                url,
                request,
                String.class
        );
    }
}
