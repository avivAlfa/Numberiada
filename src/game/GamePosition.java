package game;

import java.awt.*;
import java.util.List;

public class GamePosition {

    Player currPlayer;
    List<Player> allPlayers;
    Point selectedPoint;
    Cell selectedCell;
    int totalMoves;

    List<Cell> resinedCells;
    List<Point> resinedPoints;

    public GamePosition(Player currPlayer, List<Player> allPlayers, Point selectedPoint, Cell selectedCell, int totalMoves) {
        this.currPlayer = currPlayer;
        this.allPlayers = allPlayers;
        this.selectedPoint = selectedPoint;
        this.selectedCell = selectedCell;
        this.totalMoves = totalMoves;
    }

    public GamePosition(List<Cell> cells, List<Point> points) {
        this.resinedCells = cells;
        this.resinedPoints = points;
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

    public Point getSelectedPoint() {
        return selectedPoint;
    }

    public void setSelectedPoint(Point selectedPoint) {
        this.selectedPoint = selectedPoint;
    }

    public int getTotalMoves() {
        return totalMoves;
    }

    public void setTotalMoves(int totalMoves) {
        this.totalMoves = totalMoves;
    }

}
