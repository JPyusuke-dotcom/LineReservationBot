package com.jpyusuke.linereservationbot;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

// このクラスをDBのテーブルにする
@Entity
public class Reservation {

    // 予約を識別するためのID
    @Id

    // IDをDB側で自動的に1,2,3...と発行する
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 予約者の名前
    private String name;

    // 予約者のフリガナ
    private String kana;

    // 予約者のメールアドレス
    private String email;

    // 予約者の電話番号
    private String phone;

    // 予約者の年齢
    private String studentAge;

    // 経験の有無・経験内容
    private String experience;

    // 希望するクラスの種類
    private String classType;

    // その他のメッセージ
    private String message;


    // IDを取得する
    public Long getId() {
        return id;
    }

    // 名前を取得する
    public String getName() {
        return name;
    }

    // 名前を設定する
    public void setName(String name) {
        this.name = name;
    }

    // フリガナを取得する
    public String getKana() {
        return kana;
    }

    // フリガナを設定する
    public void setKana(String kana) {
        this.kana = kana;
    }

    // メールアドレスを取得する
    public String getEmail() {
        return email;
    }

    // メールアドレスを設定する
    public void setEmail(String email) {
        this.email = email;
    }

    // 電話番号を取得する
    public String getPhone() {
        return phone;
    }

    // 電話番号を設定する
    public void setPhone(String phone) {
        this.phone = phone;
    }

    // 年齢を取得する
    public String getStudentAge() {
        return studentAge;
    }

    // 年齢を設定する
    public void setStudentAge(String studentAge) {
        this.studentAge = studentAge;
    }

    // 経験を取得する
    public String getExperience() {
        return experience;
    }

    // 経験を設定する
    public void setExperience(String experience) {
        this.experience = experience;
    }

    // クラス種類を取得する
    public String getClassType() {
        return classType;
    }

    // クラス種類を設定する
    public void setClassType(String classType) {
        this.classType = classType;
    }

    // メッセージを取得する
    public String getMessage() {
        return message;
    }

    // メッセージを設定する
    public void setMessage(String message) {
        this.message = message;
    }
}
