package com.nipuna.bol.mancalabackend.controller;

import com.nipuna.bol.mancalabackend.domain.model.MancalaBigPit;
import com.nipuna.bol.mancalabackend.domain.model.MancalaGame;
import com.nipuna.bol.mancalabackend.domain.model.MancalaSmallPit;
import com.nipuna.bol.mancalabackend.exception.MancalaGameNotFoundException;
import com.nipuna.bol.mancalabackend.exception.MancalaGameOverException;
import com.nipuna.bol.mancalabackend.exception.MancalaInvalidPitIdException;
import com.nipuna.bol.mancalabackend.exception.MancalaInvalidPlayerException;
import com.nipuna.bol.mancalabackend.service.MancalaGameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MancalaGameController.class)
class MancalaGameControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MancalaGameService gameService;


    MancalaGame game;

    @BeforeEach
    private void setUp() {
        game = new MancalaGame();
        game.setId((long) 1);
        List<MancalaSmallPit> pits = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            MancalaSmallPit pit;
            if (i == 6 || i == 13) {
                pit = new MancalaBigPit(i);
            } else {
                pit = new MancalaSmallPit(i, 6);
            }
            pit.setGame(game);
            pits.add(pit);
        }
        game.setPits(pits);

    }

    @Test
    public void should_create_a_new_game() throws Exception  {

        Mockito.when(gameService.createGame()).thenReturn(game);
        mockMvc.perform(post("/mancala/newgame")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("gameId", is(notNullValue())))
                .andExpect(jsonPath("stonesPerPit", is(0)))
                .andExpect(jsonPath("pitCount", is(0)));

        Mockito.verify(gameService, Mockito.times(1))
                .createGame();

    }

    @Test
    public void should_make_move() throws Exception {
        when(gameService.makeMove(1,1)).thenReturn(game);
        mockMvc.perform(MockMvcRequestBuilders.put("/mancala/1/pits/1")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is("1")))
                .andExpect( jsonPath("status", is(instanceOf(HashMap.class))));

        verify(gameService, times(1))
                .makeMove(1,1);
    }

    @Test
    public void should_throw_exception_with_invalid_game_id() throws Exception {
        when(gameService.makeMove(12,1))
                .thenThrow(new MancalaGameNotFoundException("Game not found with game id : 12"));


        mockMvc.perform(MockMvcRequestBuilders.put("/mancala/12/pits/1")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("message", is("Game not found with game id : 12")));

        verify(gameService, times(1))
                .makeMove(12,1);
    }

    @Test
    public void should_throw_exception_with_invalid_pit_id() throws Exception {
        when(gameService.makeMove(1,2))
                .thenThrow(new MancalaInvalidPitIdException("Selected pit is empty"));


        mockMvc.perform(MockMvcRequestBuilders.put("/mancala/1/pits/2")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("message", is("Selected pit is empty")));

        verify(gameService, times(1))
                .makeMove(1,2);
    }

    @Test
    public void should_throw_exception_with_invalid_player() throws Exception {
        when(gameService.makeMove(1,9))
                .thenThrow(new MancalaInvalidPlayerException("Invalid player"));


        mockMvc.perform(MockMvcRequestBuilders.put("/mancala/1/pits/9")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("message", is("Invalid player")));

        verify(gameService, times(1))
                .makeMove(1,9);
    }

    @Test
    public void should_throw_exception_with_game_over() throws Exception {
        when(gameService.makeMove(1,1))
                .thenThrow(new MancalaGameOverException("Game is already over"));


        mockMvc.perform(MockMvcRequestBuilders.put("/mancala/1/pits/1")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("message", is("Game is already over")));

        verify(gameService, times(1))
                .makeMove(1,1);
    }
}