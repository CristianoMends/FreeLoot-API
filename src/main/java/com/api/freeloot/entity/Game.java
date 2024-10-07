package com.api.freeloot.entity;

import com.api.freeloot.dto.GameView;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String image;
    private String name;
    private String status;
    private String period;

    private String link;
    private LocalDateTime updateDate;

    public Game() {}

    public Game(String image, String name, String status, String period, String link, LocalDateTime updateDate) {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public GameView toView(){
        return new GameView(image, name, status, period, link);
    }

}
