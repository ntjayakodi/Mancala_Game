package com.nipuna.bol.mancalabackend.service;

import com.nipuna.bol.mancalabackend.domain.model.MancalaGame;
import com.nipuna.bol.mancalabackend.domain.model.MancalaPlayer;
import com.nipuna.bol.mancalabackend.exception.MancalaBaseException;

public interface MancalaGameEngine {

    void sowStones(int pitId, MancalaGame game) throws MancalaBaseException;

    void updateGameStatusToOver(MancalaGame game);

    void moveAllStonesToBigPit(MancalaPlayer mancalaPlayer, MancalaGame game);

    void updateWinner(MancalaGame game);
}
