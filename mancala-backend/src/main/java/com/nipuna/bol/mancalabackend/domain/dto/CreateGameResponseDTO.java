package com.nipuna.bol.mancalabackend.domain.dto;

import com.nipuna.bol.mancalabackend.domain.model.MancalaGame;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateGameResponseDTO {

    private final Long gameId;
    private final int stonesPerPit;
    private final int pitCount;

    public CreateGameResponseDTO(MancalaGame mancalaGame) {
        this.gameId = mancalaGame.getId();
        this.stonesPerPit = mancalaGame.getStartingStonesPerPit();
        this.pitCount = mancalaGame.getNoOfPits();
    }
}
