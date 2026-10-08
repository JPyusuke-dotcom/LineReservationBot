package com.jpyusuke.linereservationbot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    // 予約通知を受け取るメールアドレス
    @Value("${reservation.notification.email}")
    private String notificationEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // 予約通知メールを送信する
    public void sendReservationNotification(Reservation reservation) {

        SimpleMailMessage message = new SimpleMailMessage();

        // 送信先
        message.setTo(notificationEmail);

        // 件名
        message.setSubject("【予約受付】新しい予約が入りました");

        // 本文
        message.setText(
                "新しい予約が入りました。\n\n"
                + "名前：" + reservation.getName() + "\n"
                + "かな：" + reservation.getKana() + "\n"
                + "メール：" + reservation.getEmail() + "\n"
                + "電話番号：" + reservation.getPhone() + "\n"
                + "年齢：" + reservation.getStudentAge() + "\n"
                + "経験：" + reservation.getExperience() + "\n"
                + "クラス：" + reservation.getClassType() + "\n"
                + "メッセージ：" + reservation.getMessage()
        );

        // メール送信
        mailSender.send(message);
    }
}
