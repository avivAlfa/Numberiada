package game;

import java.util.List;

public class GamePosition {

    Player currPlayer;
    List<Player> allPlayers;
    Cell selectedCell;

    public GamePosition(Player currPlayer, List<Player> allPlayers, Cell selectedCell) {
        this.currPlayer = currPlayer;
        this.allPlayers = allPlayers;
        this.selectedCell = selectedCell;
    }

    public Player getCurrPlayer() {
        return currPlayer;
    }

    public void setCurrPlayer(Player currPlayer) {
        this.currPlayer = currPlayer;
    }

    public List<Player> getAllPlayers() {
        return allPlayers;
    }

    public void setAllPlayers(List<Player> allPlayers) {
        this.allPlayers = allPlayers;
    }

    public Cell getSelectedCell() {
        return selectedCell;
    }

    public void setSelectedCell(Cell selectedCell) {
        this.selectedCell = selectedCell;
    }

}
