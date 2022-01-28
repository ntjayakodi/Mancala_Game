package com.nipuna.bol.mancalabackend.service.impl;

import com.nipuna.bol.mancalabackend.domain.model.MancalaGame;
import com.nipuna.bol.mancalabackend.domain.model.MancalaPlayer;
import com.nipuna.bol.mancalabackend.domain.model.Status;
import com.nipuna.bol.mancalabackend.exception.MancalaBaseException;
import com.nipuna.bol.mancalabackend.exception.MancalaInvalidPitIdException;
import com.nipuna.bol.mancalabackend.exception.MancalaInvalidPlayerException;
import com.nipuna.bol.mancalabackend.service.MancalaGameEngine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.stream.IntStream;

@Service
public class MancalaGameEngineImpl implements MancalaGameEngine {

    @Value("${mancala.pit.count}")
    private int pitCount;


    @Override
    public void sowStones(int pitId, MancalaGame game) throws MancalaBaseException {

        MancalaPlayer currentMancalaPlayer = game.getCurrentMancalaPlayer();
        isValidPitIdForMove(pitId, currentMancalaPlayer);
        int tmpPitStones = game.getPit(pitId).getStones();
        int nextPit = pitId + 1;
        int restrictedPitId = currentMancalaPlayer == MancalaPlayer.MANCALA_PLAYER_ONE ? getPlayerTwoBigPitIndex() : getPlayerOneBigPitIndex();
        int playerBigPit = currentMancalaPlayer == MancalaPlayer.MANCALA_PLAYER_ONE ? getPlayerOneBigPitIndex() : getPlayerTwoBigPitIndex();
        /*
          can check if the count of current pit equals distance of pit from its player's Big pit.
          next player is still current player and give chance to run again
         */
        while (tmpPitStones > 0) {
            if (nextPit > getPlayerTwoBigPitIndex())
                nextPit = 0;

            /*
              check if last stone and current pit is empty, then calculate opposite pit, get its stone ,
               add opposite pit stones with current pit stones and then add them to Big pit
             */
            tmpPitStones = handleLastStoneInEmptyPit(currentMancalaPlayer, tmpPitStones, nextPit, restrictedPitId, playerBigPit, game);
            nextPit++;
        }
        game.getPit(pitId).clear();
        /*
          next pit is 7 or 14 i.e. last stone was dumped in Big pit,
          so next player is current player so no need to change
          */
        updatePlayerTurn(nextPit - 1, currentMancalaPlayer, game);
    }

    /**
     * To get the index of the Player one big index
     * @return int
     */
    private int getPlayerOneBigPitIndex() {
        return (pitCount / 2) - 1;
    }

    /**
     * To get the index of the Player two's starting pit index
     * @return int
     */
    private int getPlayerTwoFirstPitIndex() {
        return (pitCount / 2);
    }

    /**
     * To get the index of the Player two big index
     * @return int
     */
    private int getPlayerTwoBigPitIndex() {
        return pitCount - 1;
    }

    /**
     * To Update player turn
     *
     * @param lastPit       last pit index of the player
     * @param mancalaPlayer player object
     * @param game          {@link MancalaGame}
     */
    private void updatePlayerTurn(int lastPit, MancalaPlayer mancalaPlayer, MancalaGame game) {
        if ((mancalaPlayer == MancalaPlayer.MANCALA_PLAYER_ONE && lastPit != getPlayerOneBigPitIndex())
                || (mancalaPlayer == MancalaPlayer.MANCALA_PLAYER_TWO && lastPit != getPlayerTwoBigPitIndex()))
            game.setCurrentMancalaPlayer(getNextPlayer(mancalaPlayer));
    }

    /**
     * Get next player
     *
     * @param mancalaPlayer {@link MancalaPlayer}
     * @return next player
     */
    private MancalaPlayer getNextPlayer(MancalaPlayer mancalaPlayer) {
        return mancalaPlayer == MancalaPlayer.MANCALA_PLAYER_ONE ? MancalaPlayer.MANCALA_PLAYER_TWO : MancalaPlayer.MANCALA_PLAYER_ONE;
    }

    /**
     * handle the last stone of the sow stone in the empty pit move all stones
     * in the opposite pit to current player only if opposite pit is not empty
     *
     * @param currentMancalaPlayer {@link MancalaPlayer} with the move
     * @param tmpPitStones         iterator value of stones
     * @param nextPit              next pit index
     * @param restrictedPitId      big pit of the opposite {@link MancalaPlayer}
     * @param playerBigPit         big pit of the current {@link MancalaPlayer}
     * @param game                 {@link MancalaGame}
     * @return remaining stone count
     */
    private int handleLastStoneInEmptyPit(MancalaPlayer currentMancalaPlayer, int tmpPitStones,
                                          int nextPit, int restrictedPitId, int playerBigPit, MancalaGame game) {

        int oppositePit = calculateOppositePit(nextPit, pitCount);
        if (isLastStone(tmpPitStones - 1) && isPitIdBetweenPlayerPits(currentMancalaPlayer, nextPit)
                && !game.getPit(oppositePit).isEmpty() && game.getPit(nextPit).isEmpty()) {

            game.getPit(playerBigPit).addStones(1 + game.getPit(oppositePit).getStones());
            game.getPit(nextPit).clear();
            game.getPit(oppositePit).clear();
            tmpPitStones--;

        } else if (nextPit != restrictedPitId) {

            game.getPit(nextPit).addStones(1);
            tmpPitStones--;
        }

        return tmpPitStones;
    }

    /**
     * Checks if the current pit is between current players pits.
     *
     * @param currentMancalaPlayer {@link MancalaPlayer}
     * @param pitId                pid index of current player
     * @return boolean
     */

    private boolean isPitIdBetweenPlayerPits(MancalaPlayer currentMancalaPlayer, int pitId) {
        return currentMancalaPlayer == MancalaPlayer.MANCALA_PLAYER_ONE ? isPitIdBetweenPlayerOnePits(pitId) : isPitIdBetweenPlayerTwoPits(pitId);
    }

    private boolean isLastStone(int i) {
        return i == 0;
    }

    /**
     * Get opposite pit to the given pit
     *
     * @param pitId    pit index
     * @param pitCount total pit count
     * @return pit index of opposite pit
     */
    private int calculateOppositePit(int pitId, int pitCount) {
        return (pitCount - 1) - (pitId + 1);
    }

    /**
     * Check if the move is in valid pit range and not in Big pit
     *
     * @param pitId         pit index
     * @param mancalaPlayer {@link MancalaPlayer}
     * @throws MancalaBaseException if selected pit is big pit or not in the range
     */

    private void isValidPitIdForMove(int pitId, MancalaPlayer mancalaPlayer) throws MancalaBaseException {
        if (isTheBigPit(pitId))
            throw new MancalaInvalidPitIdException("Selected Pit is Big pit");

        if (pitId < 0 || pitId > getPlayerTwoBigPitIndex())
            throw new MancalaInvalidPitIdException("Pit Id should be between range");

        if ((mancalaPlayer == MancalaPlayer.MANCALA_PLAYER_ONE && !isPitIdBetweenPlayerOnePits(pitId))
                || (mancalaPlayer == MancalaPlayer.MANCALA_PLAYER_TWO && !isPitIdBetweenPlayerTwoPits(pitId)))
            throw new MancalaInvalidPlayerException("Invalid player");

    }

    /**
     * Check the given pit belongs to Player Two
     *
     * @param pitId pit index
     * @return boolean
     */
    private boolean isPitIdBetweenPlayerTwoPits(int pitId) {
        return pitId >= getPlayerTwoFirstPitIndex() && pitId <= getPlayerTwoBigPitIndex() - 1;
    }

    /**
     * Check the given pit belongs to Player One
     *
     * @param pitId pit index
     * @return boolean
     */

    private boolean isPitIdBetweenPlayerOnePits(int pitId) {
        return pitId >= 0 && pitId <= getPlayerOneBigPitIndex() - 1;
    }


    /**
     * Check the given pit id is Big pit or not
     *
     * @param pitId pit index
     * @return true if The pit is Big pit
     */

    private boolean isTheBigPit(int pitId) {
        return pitId == getPlayerOneBigPitIndex() || pitId == getPlayerTwoBigPitIndex();
    }


    /**
     * Setter for changing the game status to over in {@link MancalaGame}
     */
    @Override
    public void updateGameStatusToOver(MancalaGame game) {
        game.setStatus(Status.GAME_OVER);
    }


    /**
     * Move all stones from all the pits of a player
     * to Big Pit when given player don't have any pit with stones
     *
     * @param mancalaPlayer {@link MancalaPlayer} with No stones
     * @param game          {@link MancalaGame}
     */
    @Override
    public void moveAllStonesToBigPit(MancalaPlayer mancalaPlayer, MancalaGame game) {
        int start = mancalaPlayer == MancalaPlayer.MANCALA_PLAYER_ONE ? game.getPlayerTwoStartingPitIndex() : 0;
        int end = mancalaPlayer == MancalaPlayer.MANCALA_PLAYER_ONE ? game.getPlayerTwoIndex() : game.getPlayerOneIndex();
        IntStream.range(start, end).forEach(p -> {
            game.getPit(end).addStones(game.getPit(p).getStones());
            game.getPit(p).clear();
        });
    }

    /**
     * To updates the game with the winner.
     *
     * @param game {@link MancalaGame}
     */
    @Override
    public void updateWinner(MancalaGame game) {
        int player1Score = game.getPit(game.getPlayerOneIndex()).getStones();
        int player2Score = game.getPit(game.getPlayerTwoIndex()).getStones();
        game.setWinner(player1Score > player2Score ? MancalaPlayer.MANCALA_PLAYER_ONE : MancalaPlayer.MANCALA_PLAYER_TWO);
    }


}
