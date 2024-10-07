package com.api.freeloot.dto;

import com.api.freeloot.entity.Game;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class GameView {

    private String image;
    private String name;
    private String status;
    private String period;

    private String link;
    private LocalDateTime updateDate;

    public GameView(String image, String name, String status, String period, String link) {
        this.image = image;
        this.name = name;
        this.status = status;
        this.period = period;
        this.link = link;
        updateDate = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toLocalDateTime();
    }
    public GameView(String image, String name, String status, String period, String link, LocalDateTime updateDate) {
        this.image = image;
        this.name = name;
        this.status = status;
        this.period = period;
        this.link = link;
        this.updateDate = updateDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Game toEntity(){
        return new Game(image, name, status, period, link, updateDate);
    }

    @Override
    public String toString() {
        return "GameEpicView{" +
                "image='" + image + '\'' +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", period='" + period + '\'' +
                ", link='"+link+"'"+
                ", updateDate=" + updateDate +
                '}';
    }
}
