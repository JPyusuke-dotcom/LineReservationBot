package com.jpyusuke.linereservationbot;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;

import org.springframework.stereotype.Service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;

@Service
public class GoogleCalendarService {

    // Google Calendar APIでJSONを扱うための設定
    private static final JsonFactory JSON_FACTORY =
            GsonFactory.getDefaultInstance();

    // Google OAuth認証後に取得したトークンを保存するフォルダ
    private static final String TOKENS_DIRECTORY_PATH =
            "tokens";

    // Google Calendarの予定を操作するために必要な権限
    private static final java.util.List<String> SCOPES =
            Collections.singletonList(
                    CalendarScopes.CALENDAR_EVENTS
            );

    // Google Calendarへ接続するための処理
    public Calendar getCalendarService() throws Exception {

        // GoogleとHTTPS通信を行うための設定
        final NetHttpTransport HTTP_TRANSPORT =
                GoogleNetHttpTransport.newTrustedTransport();

         // Google CloudからダウンロードしたOAuth認証情報JSONを指定
        File credentialsFile =
                new File(
                        "credentials/client_secret_100328985002-epmkf9623t69bhm5s89v6aohg27o88nv.apps.googleusercontent.com.json"
                );

        // 認証情報JSONをファイルから読み込む
        InputStream in =
                new FileInputStream(credentialsFile);

        // 読み込んだJSONからGoogle OAuthの設定情報を取得
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(
                        JSON_FACTORY,
                        new InputStreamReader(in)
                );

        // Google OAuth認証の流れを作成
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT,
                        JSON_FACTORY,
                        clientSecrets,
                        SCOPES
                )
                // 認証後に取得したトークンを保存する場所を指定
                .setDataStoreFactory(
                        new FileDataStoreFactory(
                                new File(TOKENS_DIRECTORY_PATH)
                        )
                )
                // 認証後も利用できるように設定
                .setAccessType("offline")
                .build();

        // Google認証後に戻ってくるためのローカルサーバーを作成
        // 127.0.0.1は自分のPC自身を表すアドレス
        // ポート番号0は、空いているポートを自動的に選択する指定
        LocalServerReceiver receiver =
                new LocalServerReceiver.Builder()
                        .setHost("127.0.0.1")
                        .setPort(0)
                        .build();

        // Googleのログイン・アクセス許可画面を表示し、
        // 認証が完了したらアクセストークンを取得
        Credential credential =
                new AuthorizationCodeInstalledApp(
                        flow,
                        receiver
                ).authorize("user");

        // 認証済みの情報を使ってGoogle Calendar APIへ接続
        return new Calendar.Builder(
                HTTP_TRANSPORT,
                JSON_FACTORY,
                credential
        )
        .setApplicationName("LINE Reservation Bot")
        .build();
    }
}

