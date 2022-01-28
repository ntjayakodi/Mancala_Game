package com.nipuna.bol.mancalabackend.repository;

import com.nipuna.bol.mancalabackend.domain.model.MancalaGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MancalaGameRepository extends JpaRepository<MancalaGame,Long> {
}
