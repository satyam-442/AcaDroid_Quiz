package com.example.acadroidquiz.Modal;

public class QuestionM {
    public String question, optiona, optionb, optionc, optiond, correctAns;
    public int setNo;

    /*public QuestionM(String question, String optiona, String optionb, String optionc, String optiond, String correctAns) {
        this.question = question;
        this.optiona = optiona;
        this.optionb = optionb;
        this.optionc = optionc;
        this.optiond = optiond;
        this.correctAns = correctAns;
    }

    public String getQuestionn() {
        return question;
    }

    public void setQuestionn(String question) {
        this.question = question;
    }

    public String getOptionaa() {
        return optiona;
    }

    public void setOptionaa(String optiona) {
        this.optiona = optiona;
    }

    public String getOptionbb() {
        return optionb;
    }

    public void setOptionbb(String optionb) {
        this.optionb = optionb;
    }

    public String getOptioncc() {
        return optionc;
    }

    public void setOptioncc(String optionc) {
        this.optionc = optionc;
    }

    public String getOptiondd() {
        return optiond;
    }

    public void setOptiondd(String optiond) {
        this.optiond = optiond;
    }

    public String getCorrectAnss() {
        return correctAns;
    }

    public void setCorrectAnss(String correctAns) {
        this.correctAns = correctAns;
    }*/

    public QuestionM() {
    }

    public QuestionM(String question, String optiona, String optionb, String optionc, String optiond, String correctAns, int setNo) {
        this.question = question;
        this.optiona = optiona;
        this.optionb = optionb;
        this.optionc = optionc;
        this.optiond = optiond;
        this.correctAns = correctAns;
        this.setNo = setNo;
    }

    public String getQuestionn() {
        return question;
    }

    public void setQuestionn(String question) {
        this.question = question;
    }

    public String getOptionaa() {
        return optiona;
    }

    public void setOptionaa(String optiona) {
        this.optiona = optiona;
    }

    public String getOptionbb() {
        return optionb;
    }

    public void setOptionbb(String optionb) {
        this.optionb = optionb;
    }

    public String getOptioncc() {
        return optionc;
    }

    public void setOptioncc(String optionc) {
        this.optionc = optionc;
    }

    public String getOptiondd() {
        return optiond;
    }

    public void setOptiondd(String optiond) {
        this.optiond = optiond;
    }

    public String getCorrectAnss() {
        return correctAns;
    }

    public void setCorrectAnss(String correctAns) {
        this.correctAns = correctAns;
    }

    public int getSetNoo() {
        return setNo;
    }

    public void setSetNoo(int setNo) {
        this.setNo = setNo;
    }
}
