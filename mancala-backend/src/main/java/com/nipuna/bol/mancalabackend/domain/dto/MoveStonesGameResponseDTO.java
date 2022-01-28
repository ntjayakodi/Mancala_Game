package com.nipuna.bol.mancalabackend.domain.dto;

import com.nipuna.bol.mancalabackend.domain.model.MancalaGame;
import com.nipuna.bol.mancalabackend.domain.model.MancalaPlayer;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
public class MoveStonesGameResponseDTO {

    private String id;
    private Map<String, String> status;
    private MancalaPlayer currentPlayer;
    private MancalaPlayer winner;


    public MoveStonesGameResponseDTO(MancalaGame game) {
        this.id = String.valueOf(game.getId());
        this.status = game.getPits().stream().collect(Collectors.toMap(p -> String.valueOf(p.getPitId() + 1), q -> String.valueOf(q.getStones())));
        this.currentPlayer = game.getCurrentMancalaPlayer();
        this.winner = game.getWinner();

    }
}
