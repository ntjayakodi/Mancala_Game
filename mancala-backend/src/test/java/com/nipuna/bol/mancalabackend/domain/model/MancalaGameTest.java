package com.nipuna.bol.mancalabackend.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class MancalaGameTest {

    MancalaGame mancalaGame;
    @BeforeEach
    void initializeGame() {
        mancalaGame = new MancalaGame(14, 6);

    }

    @Test
    public void should_get_player_with_no_stones_for_player_one(){
        IntStream.rangeClosed(0, 6).forEach(p -> mancalaGame.getPit(p).setStones(0));
        assertEquals(Optional.of(MancalaPlayer.MANCALA_PLAYER_ONE),mancalaGame.getPlayerWithNoStones());
    }

    @Test
    public void should_get_player_with_no_stones_for_player_two(){
        IntStream.rangeClosed(7, 13).forEach(p -> mancalaGame.getPit(p).setStones(0));
        assertEquals(Optional.of(MancalaPlayer.MANCALA_PLAYER_TWO),mancalaGame.getPlayerWithNoStones());
    }

    @Test
    public void should_get_player_with_no_stones_is_empty(){
        assertEquals(Optional.empty(),mancalaGame.getPlayerWithNoStones());
    }

    @Test
    public void should_get_game_over(){
        mancalaGame.setStatus(Status.GAME_OVER);
        assertEquals(true,mancalaGame.isGameOver());
    }
}