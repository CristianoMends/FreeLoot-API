package com.api.freeloot.repository;

import com.api.freeloot.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game,Long> {

    List<Game> findByName(String name);

}
