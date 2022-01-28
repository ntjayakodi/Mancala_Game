package com.nipuna.bol.mancalabackend.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;


@Table(name = "MANCALA")
@Setter
@Getter
@NoArgsConstructor
@Entity
public class MancalaGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ_MANCALA_GAME")
    @SequenceGenerator(name = "SEQ_MANCALA_GAME", sequenceName = "SEQ_MANCALA_GAME", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    @Column(name = "UPDATED_DATE")
    private LocalDateTime updatedDate;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<MancalaSmallPit> pits;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "CURRENT_PLAYER")
    private MancalaPlayer currentMancalaPlayer;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "WINNER")
    private MancalaPlayer winner;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "STATUS")
    private Status status;

    @Column(name = "PIT_COUNT")
    private int noOfPits;

    @Column(name = "STONES_PER_PIT")
    private int startingStonesPerPit;


    /**
     * Construct a Mancala Game.
     *
     * @param noOfPits  total pit count
     * @param pitStones stones per the pit
     */
    public MancalaGame(int noOfPits, int pitStones) {
        this.noOfPits = noOfPits;
        this.startingStonesPerPit = pitStones;
        pits = new ArrayList<>();
        int playerOnePitIndex = (noOfPits / 2) - 1;
        int playerTwoPitIndex = noOfPits - 1;

        for (int i = 0; i < noOfPits; i++) {
            MancalaSmallPit pit;
            if (i == playerOnePitIndex || i == playerTwoPitIndex) {
                pit = new MancalaBigPit(i);
            } else {
                pit = new MancalaSmallPit(i, pitStones);
            }
            pit.setGame(this);
            pits.add(pit);
        }
        this.currentMancalaPlayer = MancalaPlayer.MANCALA_PLAYER_ONE;
        this.status = Status.RUNNING;
    }

    @PrePersist
    protected void prePersist() {
        if (this.createdDate == null)
            createdDate = LocalDateTime.now();

    }

    @PreUpdate
    protected void preUpdate() {
        this.updatedDate = LocalDateTime.now();
    }

    public MancalaSmallPit getPit(int pitIndex) {
        return this.pits.get(pitIndex);
    }

    /**
     * This method check from the {@link MancalaGame} status of the game.
     *
     * @return boolean if status is equal to {@link Status#GAME_OVER}
     */
    public Boolean isGameOver() {
        return this.status.equals(Status.GAME_OVER);
    }

    /**
     * Getter for player one Big pit index
     *
     * @return index of big pit
     */
    public int getPlayerOneIndex() {
        return (noOfPits / 2) - 1;
    }

    /**
     * Getter for player two starting pit index
     *
     * @return starting pit index of player 2
     */
    public int getPlayerTwoStartingPitIndex() {
        return getPlayerOneIndex() + 1;
    }

    /**
     * Getter for player 2 Big pit index
     *
     * @return index of big pit
     */
    public int getPlayerTwoIndex() {
        return noOfPits - 1;
    }


    /**
     * Checks of the {@link MancalaPlayer} has any stone left in any of the pits.
     *
     * @return {@link MancalaPlayer}
     */
    public Optional<MancalaPlayer> getPlayerWithNoStones() {
        boolean player1HasAnyStoneLeft = IntStream.rangeClosed(0, getPlayerOneIndex() - 1).anyMatch(p -> this.getPit(p).getStones() > 0);
        boolean player2HasAnyStoneLeft = IntStream.rangeClosed(getPlayerTwoStartingPitIndex(), getPlayerTwoIndex() - 1).anyMatch(p -> this.getPit(p).getStones() > 0);
        if (!player1HasAnyStoneLeft)
            return Optional.of(MancalaPlayer.MANCALA_PLAYER_ONE);
        else if (!player2HasAnyStoneLeft)
            return Optional.of(MancalaPlayer.MANCALA_PLAYER_TWO);
        return Optional.empty();
    }


}
