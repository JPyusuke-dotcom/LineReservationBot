package com.jpyusuke.linereservationbot;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ReservationController {

    // 予約をDBに保存するためのService
    private final ReservationService reservationService;

    // ReservationServiceを受け取る
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // 予約フォームを表示する
    @GetMapping("/reservation")
    public String reservationForm() {

        // reservation.htmlを表示する
        return "reservation";
    }

    // フォームから送信された内容を受け取る
    @PostMapping("/reservation")
    public String reservation(
            @RequestParam String name,
            @RequestParam String kana,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String studentAge,
            @RequestParam String experience,
            @RequestParam String classType,
            @RequestParam String message,
            Model model) {

        // 予約データを入れる箱を作る
        Reservation reservation = new Reservation();

        // フォームから受け取ったデータを箱に入れる
        reservation.setName(name);
        reservation.setKana(kana);
        reservation.setEmail(email);
        reservation.setPhone(phone);
        reservation.setStudentAge(studentAge);
        reservation.setExperience(experience);
        reservation.setClassType(classType);
        reservation.setMessage(message);

        // DBに保存する
        reservationService.saveReservation(reservation);

        // 確認画面に入力内容を渡す
        model.addAttribute("name", name);
        model.addAttribute("kana", kana);
        model.addAttribute("email", email);
        model.addAttribute("phone", phone);
        model.addAttribute("studentAge", studentAge);
        model.addAttribute("experience", experience);
        model.addAttribute("classType", classType);
        model.addAttribute("message", message);

        // 確認画面を表示する
        return "reservation-confirm";
    }

    // 確認画面のOKボタンが押されたとき
    @PostMapping("/reservation/complete")
    public String complete() {

        // 完了画面を表示する
        return "reservation-complete";
    }
}
