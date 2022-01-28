package com.nipuna.bol.mancalabackend.controller;

import com.nipuna.bol.mancalabackend.domain.dto.CreateGameResponseDTO;
import com.nipuna.bol.mancalabackend.domain.dto.MoveStonesGameResponseDTO;
import com.nipuna.bol.mancalabackend.domain.model.MancalaGame;
import com.nipuna.bol.mancalabackend.exception.MancalaBaseException;
import com.nipuna.bol.mancalabackend.service.MancalaGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = "/mancala")
public class MancalaGameController {


    private final MancalaGameService mancalaGameService;

    @Autowired
    public MancalaGameController(MancalaGameService mancalaGameService) {
        this.mancalaGameService = mancalaGameService;
    }

    /**
     * Create New Mancala game
     *
     * @return response as Json containing game id, Stones per pit, pit count
     */

    @PostMapping(value = "/newgame", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateGameResponseDTO> createGame() {
        MancalaGame mancalaGame = mancalaGameService.createGame();

        CreateGameResponseDTO response = new CreateGameResponseDTO(mancalaGame);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Make stone move from the pit
     *
     * @param gameId given in the uri
     * @param pitId  given in the uri
     * @return response as Json containing game id, map of pit and pit stone count, current player to move
     * @throws MancalaBaseException if any exception occurs
     */
    @PutMapping(value = "/{gameId}/pits/{pitId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MoveStonesGameResponseDTO> makeStoneMove(@PathVariable(name = "gameId") int gameId,
                                                                   @PathVariable(name = "pitId") int pitId) throws MancalaBaseException {
        MancalaGame game = mancalaGameService.makeMove(gameId, pitId);

        MoveStonesGameResponseDTO response = new MoveStonesGameResponseDTO(game);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
