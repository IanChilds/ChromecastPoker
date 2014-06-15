package com.pokercast.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class containing all information about current game
 */
public class Game {
    private static Game instance = null;

    private List<GameStateSnapshot> mGameStateSnapshots = new ArrayList<GameStateSnapshot>();
    private int playerPosition;

    protected Game() {
        // Exists only to defeat instantiation.
    }
    public static Game getInstance() {
        if(instance == null) {
            instance = new Game();
        }
        return instance;
    }

    public int getPlayerPosition() {
        return playerPosition;
    }
    public void setPlayerPosition(int playerPosition) {
        this.playerPosition = playerPosition;
    }

    public GameStateSnapshot getGameStateSnapshot(int index) {
        return mGameStateSnapshots.get(index);
    }

    public void updateGame(GameStateSnapshot gameStateSnapshot) {
        mGameStateSnapshots.add(gameStateSnapshot);
    }

    public String getLastMove() {

        return "";
    }
}
