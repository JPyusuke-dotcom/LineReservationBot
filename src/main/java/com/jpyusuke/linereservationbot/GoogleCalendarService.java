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

    // Google Calendar APIで使用するJSON処理
    private static final JsonFactory JSON_FACTORY =
            GsonFactory.getDefaultInstance();

    // OAuth認証情報を保存するフォルダ
    private static final String TOKENS_DIRECTORY_PATH =
            "tokens";

    // Google Calendarの予定を操作するための権限
    private static final java.util.List<String> SCOPES =
            Collections.singletonList(
                    CalendarScopes.CALENDAR_EVENTS
            );

    // Google Calendarへ接続する
    public Calendar getCalendarService() throws Exception {

        // GoogleのHTTP通信に必要な設定
        final NetHttpTransport HTTP_TRANSPORT =
                GoogleNetHttpTransport.newTrustedTransport();

        // Google Cloudからダウンロードした認証情報JSON
        File credentialsFile =
                new File(
                        "credentials/client_secret_1029442115423-hg3mk48vvfueojduitjalkqbgr4a43fm.apps.googleusercontent.com.json"
                );

        // 認証情報JSONを読み込む
        InputStream in =
                new FileInputStream(credentialsFile);

        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(
                        JSON_FACTORY,
                        new InputStreamReader(in)
                );

        // OAuth認証の流れを作成
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT,
                        JSON_FACTORY,
                        clientSecrets,
                        SCOPES
                )
                .setDataStoreFactory(
                        new FileDataStoreFactory(
                                new File(TOKENS_DIRECTORY_PATH)
                        )
                )
                .setAccessType("offline")
                .build();

        // ブラウザを使ってGoogleアカウントを認証
        LocalServerReceiver receiver =
                new LocalServerReceiver.Builder()
                        .setPort(8888)
                        .build();

        Credential credential =
                new AuthorizationCodeInstalledApp(
                        flow,
                        receiver
                ).authorize("user");

        // Google Calendar APIへ接続
        return new Calendar.Builder(
                HTTP_TRANSPORT,
                JSON_FACTORY,
                credential
        )
        .setApplicationName("LINE Reservation Bot")
        .build();
    }
}
