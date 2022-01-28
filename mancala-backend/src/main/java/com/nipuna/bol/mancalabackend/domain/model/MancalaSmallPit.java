package com.nipuna.bol.mancalabackend.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "MANCALA_PIT")
@Getter
@Setter
@NoArgsConstructor
public class MancalaSmallPit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "PIT_ID")
    private int pitId;

    @Column(name = "STONES")
    private int stones;

    @ManyToOne
    @JoinColumn(name = "GAME_ID")
    private MancalaGame game;

    public MancalaSmallPit(int pitId, int stones) {
        this.pitId = pitId;
        this.stones = stones;
    }

    public void addStones(int stones) {
        this.stones += stones;
    }

    public void clear() {
        this.stones = 0;
    }

    public boolean isEmpty() {
        return this.stones == 0;
    }

    public String toString() {
        return pitId + ":" + stones;
    }
}
