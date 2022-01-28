package com.nipuna.bol.mancalabackend.service.impl;

import com.nipuna.bol.mancalabackend.domain.model.MancalaGame;
import com.nipuna.bol.mancalabackend.domain.model.MancalaPlayer;
import com.nipuna.bol.mancalabackend.exception.MancalaBaseException;
import com.nipuna.bol.mancalabackend.exception.MancalaGameNotFoundException;
import com.nipuna.bol.mancalabackend.exception.MancalaGameOverException;
import com.nipuna.bol.mancalabackend.exception.MancalaInvalidPitIdException;
import com.nipuna.bol.mancalabackend.repository.MancalaGameRepository;
import com.nipuna.bol.mancalabackend.service.MancalaGameEngine;
import com.nipuna.bol.mancalabackend.service.MancalaGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MancalaGameServiceImpl implements MancalaGameService {

    private final MancalaGameRepository mancalaGameRepository;

    private final MancalaGameEngine mancalaGameEngine;

    @Value("${mancala.pit.stones}")
    private int pitStones;

    @Value("${mancala.pit.count}")
    private int pitCount;

    @Autowired
    public MancalaGameServiceImpl(MancalaGameRepository mancalaGameRepository, MancalaGameEngine mancalaGameEngine) {
        this.mancalaGameRepository = mancalaGameRepository;
        this.mancalaGameEngine = mancalaGameEngine;
    }

    @Override
    public MancalaGame createGame() {
        MancalaGame game = new MancalaGame(pitCount, pitStones);
        game = mancalaGameRepository.save(game);
        return game;
    }

    @Override
    public MancalaGame makeMove(int gameId, int pitId) throws MancalaBaseException {
        Optional<MancalaGame> mancalaGame = mancalaGameRepository.findById((long) gameId);
        //Validate the game object
        if (!mancalaGame.isPresent())
            throw new MancalaGameNotFoundException("Game not found with game id : " + gameId);
        MancalaGame game = mancalaGame.get();
        if (game.isGameOver())
            throw new MancalaGameOverException("Game is already over");
        int pitStonesCount = game.getPit(pitId - 1).getStones();
        if (pitStonesCount == 0) //Empty pit
            throw new MancalaInvalidPitIdException("Selected pit is empty");

        mancalaGameEngine.sowStones(pitId - 1, game);
        Optional<MancalaPlayer> playerWithNoStones = game.getPlayerWithNoStones();
        if (playerWithNoStones.isPresent()) {
            mancalaGameEngine.moveAllStonesToBigPit(playerWithNoStones.get(), game);
            mancalaGameEngine.updateGameStatusToOver(game);
            mancalaGameEngine.updateWinner(game);
        }
        mancalaGameRepository.save(game);
        return game;
    }
}
