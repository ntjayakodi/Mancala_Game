package com.nipuna.bol.mancalabackend.service;

import com.nipuna.bol.mancalabackend.domain.model.MancalaGame;
import com.nipuna.bol.mancalabackend.exception.MancalaBaseException;

public interface MancalaGameService {


    /**
     * Creates a new {@link MancalaGame }
     *
     * @return game object created in the database.
     */
    MancalaGame createGame();

    /**
     * @param gameId game id
     * @param pitId  pit id
     * @return updated game object
     * @throws MancalaBaseException if any exception
     */
    MancalaGame makeMove(int gameId, int pitId) throws MancalaBaseException;
}
