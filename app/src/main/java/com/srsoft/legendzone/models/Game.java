package com.srsoft.legendzone.models;

public class Game {

    private String gameId;
    private String gameName;
    private String gameImgUrl;

    public Game(String gameId, String gameName, String gameImgUrl) {
        this.gameId = gameId;
        this.gameName = gameName;
        this.gameImgUrl = gameImgUrl;
    }

    public Game() {
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameImgUrl() {
        return gameImgUrl;
    }

    public void setGameImgUrl(String gameImgUrl) {
        this.gameImgUrl = gameImgUrl;
    }
}
