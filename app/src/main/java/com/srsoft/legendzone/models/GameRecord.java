package com.srsoft.legendzone.models;

public class GameRecord {

    private String timeofgame;
    private String result;

    public GameRecord(String timeofgame, String result) {
        this.timeofgame = timeofgame;
        this.result = result;
    }

    public GameRecord(){

    }

    public String getPeriod() {
        return timeofgame;
    }

    public void setPeriod(String timeofgame) {
        this.timeofgame = timeofgame;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
