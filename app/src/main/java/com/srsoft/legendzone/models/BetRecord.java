package com.srsoft.legendzone.models;

public class BetRecord {
 private String dateTime;
 private float betAmount;
 private String betOn;

 private String game;

    public BetRecord(String dateTime, Float betAmount, String betOn,String game) {
        this.dateTime = dateTime;
        this.betAmount = betAmount;
        this.betOn = betOn;
        this.game = game;
    }

    public BetRecord() {
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public float getBetAmount() {
        return betAmount;
    }

    public void setBetAmount(Float betAmount) {
        this.betAmount = betAmount;
    }

    public String getBetOn() {
        return betOn;
    }

    public void setBetOn(String betOn) {
        this.betOn = betOn;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }
}

