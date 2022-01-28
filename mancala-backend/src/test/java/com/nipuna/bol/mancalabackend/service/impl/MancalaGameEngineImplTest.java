package com.nipuna.bol.mancalabackend.service.impl;

import com.nipuna.bol.mancalabackend.domain.model.MancalaGame;
import com.nipuna.bol.mancalabackend.domain.model.MancalaPlayer;
import com.nipuna.bol.mancalabackend.domain.model.Status;
import com.nipuna.bol.mancalabackend.exception.MancalaBaseException;
import com.nipuna.bol.mancalabackend.exception.MancalaInvalidPitIdException;
import com.nipuna.bol.mancalabackend.exception.MancalaInvalidPlayerException;
import com.nipuna.bol.mancalabackend.service.MancalaGameEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest()
@ExtendWith(MockitoExtension.class)
class MancalaGameEngineImplTest {

    MancalaGame mancalaGame;

    @Autowired
    MancalaGameEngine mancalaGameEngine;

    @BeforeEach
    public void setUp() {

        mancalaGame = new MancalaGame(14, 4);

    }


    @Test
    public void should_sow_stones() throws MancalaBaseException {
        MancalaGameEngine gameEngine = mock(MancalaGameEngineImpl.class);
        doNothing().when(gameEngine).sowStones(anyInt(), any(MancalaGame.class));
        gameEngine.sowStones(1, mancalaGame);

        verify(gameEngine, times(1)).sowStones(1, mancalaGame);

    }

    @Test
    public void should_throw_exception_with_invalid_pit_id() {

        mancalaGame.setCurrentMancalaPlayer(MancalaPlayer.MANCALA_PLAYER_TWO);

        MancalaInvalidPitIdException mancalaInvalidPitIdException = assertThrows(MancalaInvalidPitIdException.class, () -> mancalaGameEngine.sowStones(14, mancalaGame));
        assertEquals("Pit Id should be between range", mancalaInvalidPitIdException.getMessage());

    }

    @Test
    public void should_throw_exception_with_invalid_player() {

        mancalaGame.setCurrentMancalaPlayer(MancalaPlayer.MANCALA_PLAYER_TWO);

        MancalaInvalidPlayerException mancalaInvalidPlayerException = assertThrows(MancalaInvalidPlayerException.class, () -> mancalaGameEngine.sowStones(3, mancalaGame));
        assertEquals("Invalid player", mancalaInvalidPlayerException.getMessage());

    }

    @Test
    public void should_throw_exception_with_invalid_pit_id_for_big_pit() {

        mancalaGame.setCurrentMancalaPlayer(MancalaPlayer.MANCALA_PLAYER_TWO);

        MancalaInvalidPitIdException mancalaInvalidPitIdException = assertThrows(MancalaInvalidPitIdException.class, () -> mancalaGameEngine.sowStones(13, mancalaGame));
        assertEquals("Selected Pit is Big pit", mancalaInvalidPitIdException.getMessage());

    }

    @Test
    public void should_updateWinner_success() {
        mancalaGame.getPit(mancalaGame.getPlayerOneIndex()).setStones(28);
        mancalaGame.getPit(mancalaGame.getPlayerTwoIndex()).setStones(20);
        mancalaGameEngine.updateWinner(mancalaGame);
        assertEquals(MancalaPlayer.MANCALA_PLAYER_ONE, mancalaGame.getWinner());
    }

    @Test
    public void should_update_game_status_to_over() {

        mancalaGameEngine.updateGameStatusToOver(mancalaGame);
        assertEquals(Status.GAME_OVER, mancalaGame.getStatus());
    }

    @Test
    public void should_move_all_stones_to_big_pit() {
        mancalaGameEngine.moveAllStonesToBigPit(MancalaPlayer.MANCALA_PLAYER_TWO, mancalaGame);
        assertEquals(mancalaGame.getPit(mancalaGame.getPlayerOneIndex()).getStones(), 24);
    }

    @Test
    public void should_update_player_turn() throws MancalaBaseException {

        mancalaGame.setCurrentMancalaPlayer(MancalaPlayer.MANCALA_PLAYER_TWO);
        mancalaGameEngine.sowStones(11, mancalaGame);
        assertEquals(MancalaPlayer.MANCALA_PLAYER_ONE, mancalaGame.getCurrentMancalaPlayer());
    }

    @Test
    public void should_handle_last_stone_in_empty_pit() throws MancalaBaseException {

        mancalaGame.getPit(5).clear();
        mancalaGameEngine.sowStones(1, mancalaGame);
        assertEquals(0, mancalaGame.getPit(5).getStones());
    }


}