package com.api.freeloot.service;

import com.api.freeloot.dto.GameView;

import java.io.IOException;
import java.util.List;

public interface SearchService {

    List<GameView> getFreeGamesEpic() throws IOException;

    List<GameView> getAll();

}
