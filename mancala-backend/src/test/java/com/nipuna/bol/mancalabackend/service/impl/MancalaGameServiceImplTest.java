package com.nipuna.bol.mancalabackend.service.impl;

import com.nipuna.bol.mancalabackend.domain.model.MancalaGame;
import com.nipuna.bol.mancalabackend.domain.model.MancalaPlayer;
import com.nipuna.bol.mancalabackend.domain.model.MancalaSmallPit;
import com.nipuna.bol.mancalabackend.exception.MancalaBaseException;
import com.nipuna.bol.mancalabackend.exception.MancalaGameNotFoundException;
import com.nipuna.bol.mancalabackend.exception.MancalaGameOverException;
import com.nipuna.bol.mancalabackend.exception.MancalaInvalidPitIdException;
import com.nipuna.bol.mancalabackend.repository.MancalaGameRepository;
import com.nipuna.bol.mancalabackend.service.MancalaGameEngine;
import com.nipuna.bol.mancalabackend.service.MancalaGameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@ContextConfiguration(classes = {MancalaGameService.class})
@WebMvcTest
class MancalaGameServiceImplTest {

    @MockBean
    MancalaGameRepository mancalaGameRepository;

    @MockBean
    MancalaGameEngine mancalaGameEngine;

    MancalaGameService mancalaGameService;

    @Mock
    MancalaGame mancalaGame;

    @Mock
    MancalaSmallPit pit;


    @BeforeEach
    public void setUp() {
        mancalaGameService = new MancalaGameServiceImpl(mancalaGameRepository, mancalaGameEngine);
    }


    @Test
    void createGame_Expect_Success() {

        when(mancalaGameRepository.save(any(MancalaGame.class))).thenReturn(mancalaGame);

        MancalaGame game = mancalaGameService.createGame();
        assertNotNull(game);

    }

    @Test
    void makeMove_Expect_SuccessMove() throws MancalaBaseException {

        MancalaPlayer mancalaPlayer = MancalaPlayer.MANCALA_PLAYER_ONE;
        when(mancalaGameRepository.findById((long) 1)).thenReturn(Optional.of(mancalaGame));
        when(mancalaGame.getId()).thenReturn(1L);
        when(mancalaGame.getPit(anyInt())).thenReturn(pit);
        when(pit.getStones()).thenReturn(4);
        doNothing().when(mancalaGameEngine).sowStones(anyInt(), any(MancalaGame.class));
        when(mancalaGame.getPlayerWithNoStones()).thenReturn(Optional.of(mancalaPlayer));
        doNothing().when(mancalaGameEngine).moveAllStonesToBigPit(any(MancalaPlayer.class), any(MancalaGame.class));
        doNothing().when(mancalaGameEngine).updateGameStatusToOver(any(MancalaGame.class));
        doNothing().when(mancalaGameEngine).updateWinner(any(MancalaGame.class));

        MancalaGame game = mancalaGameService.makeMove(1, 1);

        assertEquals(game.getId(), 1);
    }

    @Test
    void makeMove_Expect_InvalidPitIdException() {

        when(mancalaGameRepository.findById((long) 1)).thenReturn(Optional.of(mancalaGame));
        when(mancalaGame.getPit(anyInt())).thenReturn(pit);
        when(pit.getStones()).thenReturn(0);

        MancalaInvalidPitIdException mancalaInvalidPitIdException = assertThrows(MancalaInvalidPitIdException.class,
                () -> mancalaGameService.makeMove(1, 1));
        assertEquals(mancalaInvalidPitIdException.getMessage(), "Selected pit is empty");
    }

    @Test
    void makeMove_Expect_PlayerWithNoStones() {

        when(mancalaGameRepository.findById((long) 1)).thenReturn(Optional.of(mancalaGame));
        when(mancalaGame.getPit(anyInt())).thenReturn(pit);
        when(pit.getStones()).thenReturn(0);
        MancalaInvalidPitIdException mancalaInvalidPitIdException = assertThrows(MancalaInvalidPitIdException.class,
                () -> mancalaGameService.makeMove(1, 1));
        assertEquals(mancalaInvalidPitIdException.getMessage(), "Selected pit is empty");
    }

    @Test
    void makeMove_Expect_GameOverException() {

        when(mancalaGameRepository.findById((long) 1)).thenReturn(Optional.of(mancalaGame));
        when(mancalaGame.getId()).thenReturn(1L);
        when(mancalaGame.isGameOver()).thenReturn(true);

        MancalaGameOverException mancalaGameOverException = assertThrows(MancalaGameOverException.class,
                () -> mancalaGameService.makeMove(1, 1));
        assertEquals(mancalaGameOverException.getMessage(), "Game is already over");
    }

    @Test
    void makeMove_Expect_GameNotFoundException() {

        when(mancalaGameRepository.findById((long) 1)).thenReturn(Optional.empty());


        MancalaGameNotFoundException mancalaGameNotFoundException = assertThrows(MancalaGameNotFoundException.class,
                () -> mancalaGameService.makeMove(1, 1));
        assertEquals(mancalaGameNotFoundException.getMessage(), "Game not found with game id : 1");
    }


}