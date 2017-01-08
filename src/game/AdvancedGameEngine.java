package game;


import generated.GameDescriptor;

import java.awt.*;
import java.util.*;
import java.util.List;

public class AdvancedGameEngine extends GameEngine {


    @Override
    public List<Point> getPossibleCells() {
        List<Point> possibleCells = new ArrayList<Point>();
        Cell currCell;

        for (int j = 0; j < gameBoard.getSize(); j++) {
            currCell = gameBoard.getCell(cursorRow, j);
            if ((!currCell.isEmpty()) && ((!currCell.isCursor())) && currCell.getColor()==players.get(playerTurnIndex).getColor()) {
                possibleCells.add(new Point(cursorRow, j));
            }
        }

        for (int i = 0; i < gameBoard.getSize(); i++) {
            currCell = gameBoard.getCell(i, cursorCol);
            if ((!currCell.isEmpty()) && ((!currCell.isCursor())) && (currCell.getColor()==players.get(playerTurnIndex).getColor())) {
                possibleCells.add(new Point(i, cursorCol));
            }
        }

        return possibleCells;
    }

    @Override
    public boolean endGame() {
        return false;
    }

    @Override
    protected Board buildRandomBoard(int boardSize, int rangeFrom, int rangeTo) {
        return null;
    }

    @Override
    protected ArrayList<PoolElement> createPool(int boardSize, int rangeFrom, int rangeTo) {
        return null;
    }

}
