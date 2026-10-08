package com.jpyusuke.linereservationbot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class LinePushService {

    // LINE Messaging APIで使用するアクセストークン
    @Value("${line.channel-access-token}")
    private String channelAccessToken;

    // LINEユーザーへメッセージを送信する
    public void sendMessage(
            String userId,
            String messageText) throws Exception {

        // LINE Push Message APIのURL
        String url =
                "https://api.line.me/v2/bot/message/push";

        // HTTP通信を行うためのRestTemplate
        RestTemplate restTemplate =
                new RestTemplate();

        // HTTPヘッダーを作成
        HttpHeaders headers =
                new HttpHeaders();

        // JSON形式で送信する
        headers.setContentType(
                MediaType.APPLICATION_JSON
        );

        // LINEのアクセストークンを設定
        headers.setBearerAuth(
                channelAccessToken
        );

        // 送信するメッセージを作成
        Map<String, Object> message =
                new HashMap<>();

        // メッセージの種類
        message.put("type", "text");

        // 実際に送る文章
        message.put("text", messageText);

        // LINE APIへ送るデータを作成
        Map<String, Object> requestBody =
                new HashMap<>();

        // メッセージを送るLINEユーザーを指定
        requestBody.put("to", userId);

        // 送信するメッセージを指定
        requestBody.put("messages", List.of(message));

        // JavaのMapをJSON文字列に変換
        ObjectMapper mapper =
                new ObjectMapper();

        String jsonBody =
                mapper.writeValueAsString(requestBody);

        // HTTPリクエストを作成
        HttpEntity<String> request =
                new HttpEntity<>(
                        jsonBody,
                        headers
                );

        // LINE Messaging APIへ送信
        restTemplate.postForEntity(
                url,
                request,
                String.class
        );
    }
}
