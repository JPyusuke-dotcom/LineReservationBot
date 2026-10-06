package com.jpyusuke.linereservationbot;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
    public String reservationForm(
            Model model,
            HttpSession session) {

        // セッションに入力途中の予約情報があるか確認する
        ReservationForm form =
                (ReservationForm) session.getAttribute("reservationForm");

        // あれば、その入力内容をフォームに表示する
        if (form != null) {
            model.addAttribute("reservationForm", form);

            // セッションからは削除しない
            return "reservation";
        }

        // なければ新しいフォームを用意する
        model.addAttribute("reservationForm", new ReservationForm());

        // reservation.htmlを表示する
        return "reservation";
    }

    // フォームから送信された内容を受け取る
    @PostMapping("/reservation")
    public String reservation(
            @Valid @ModelAttribute("reservationForm") ReservationForm form,
            BindingResult bindingResult,
            Model model,
            HttpSession session) {

        // 入力エラーがあるか確認する
        if (bindingResult.hasErrors()) {

            // エラーがあればフォーム画面に戻る
            return "reservation";
        }

        // 入力された予約情報をセッションに保存する
        session.setAttribute("reservationForm", form);

        // 確認画面に入力内容を渡す
        model.addAttribute("name", form.getName());
        model.addAttribute("kana", form.getKana());
        model.addAttribute("email", form.getEmail());
        model.addAttribute("phone", form.getPhone());
        model.addAttribute("studentAge", form.getStudentAge());
        model.addAttribute("experience", form.getExperience());
        model.addAttribute("classType", form.getClassType());
        model.addAttribute("message", form.getMessage());

        // 確認画面を表示する
        return "reservation-confirm";
    }

    // 確認画面のOKボタンが押されたとき
    @PostMapping("/reservation/complete")
    public String complete(HttpSession session) {

        // セッションから予約情報を取得する
        ReservationForm form =
                (ReservationForm) session.getAttribute("reservationForm");

        // 予約情報が存在しない場合
        if (form == null) {
            return "redirect:/reservation";
        }

        // DBに保存するためのReservationを作る
        Reservation reservation = new Reservation();

        // フォームの内容をReservationに入れる
        reservation.setName(form.getName());
        reservation.setKana(form.getKana());
        reservation.setEmail(form.getEmail());
        reservation.setPhone(form.getPhone());
        reservation.setStudentAge(form.getStudentAge());
        reservation.setExperience(form.getExperience());
        reservation.setClassType(form.getClassType());
        reservation.setMessage(form.getMessage());

        // DBに保存する
        reservationService.saveReservation(reservation);

        // セッションから予約情報を削除する
        session.removeAttribute("reservationForm");

        // 完了画面を表示する
        return "reservation-complete";
    }

    // 確認画面から入力画面に戻る
    @PostMapping("/reservation/back")
    public String backToForm() {

        // 予約フォームに戻る
        return "redirect:/reservation";
    }
}

