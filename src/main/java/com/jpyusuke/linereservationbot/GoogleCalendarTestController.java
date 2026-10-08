package com.jpyusuke.linereservationbot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GoogleCalendarTestController {

    // Google Calendarサービス
    private final GoogleCalendarService googleCalendarService;

    // GoogleCalendarServiceを受け取る
    public GoogleCalendarTestController(
            GoogleCalendarService googleCalendarService) {

        this.googleCalendarService =
                googleCalendarService;
    }

    // Google Calendarへの接続テスト
    @GetMapping("/google-calendar/test")
    public String testGoogleCalendar() {

        try {

            // Google Calendarへ接続
            googleCalendarService.getCalendarService();

            // 接続成功
            return "Google Calendarへの接続に成功しました！";

        } catch (Exception e) {

            // エラー内容をターミナルに表示
            e.printStackTrace();

            return "Google Calendarへの接続に失敗しました。"
                    + e.getMessage();
        }
    }
}
