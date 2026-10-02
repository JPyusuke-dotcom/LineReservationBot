package com.jpyusuke.linereservationbot;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ReservationForm {

    // ① お名前
    @NotBlank(message = "お名前を入力してください")
    private String name;

    // ② フリガナ
    @NotBlank(message = "フリガナを入力してください")
    @Pattern(
        regexp = "^[ァ-ヶー　 ]+$",
        message = "フリガナはカタカナで入力してください"
    )
    private String kana;

    // ③ メールアドレス
    @NotBlank(message = "メールアドレスを入力してください")
    @Email(message = "正しいメールアドレスを入力してください")
    private String email;

    // ④ 電話番号
    @NotBlank(message = "電話番号を入力してください")
    @Pattern(
        regexp = "^[0-9-]+$",
        message = "電話番号は数字とハイフンで入力してください"
    )
    private String phone;

    // ⑤ 学生・年齢
    @Size(max = 50, message = "学生・年齢は50文字以内で入力してください")
    private String studentAge;

    // ⑥ ソフトテニス経験
    @NotBlank(message = "ソフトテニス経験を選択してください")
    private String experience;

    // ⑦ 希望クラス
    @NotBlank(message = "希望クラスを選択してください")
    private String classType;

    // ⑧ お問い合わせ内容
    @Size(max = 1000, message = "お問い合わせ内容は1000文字以内で入力してください")
    private String message;


    // getter / setter

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKana() {
        return kana;
    }

    public void setKana(String kana) {
        this.kana = kana;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStudentAge() {
        return studentAge;
    }

    public void setStudentAge(String studentAge) {
        this.studentAge = studentAge;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
