package com.api.freeloot.service;

import com.api.freeloot.dto.GameView;

import java.util.List;

public interface SearchService {

    List<GameView> getAllGamesAlwaysFree();
    List<GameView> getAllWeeklyFree();
    List<GameView> getAllExpired();

}
