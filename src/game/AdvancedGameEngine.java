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
    public List<Point> getNextPlayerOpportunities(int selectedRow, int selectedCol){
        List<Point> nextPlayerOpportunities = new ArrayList<Point>();
        Cell currCell;
        int nextPlayerIndex = getNextPlayerIndex();

        for (int j = 0; j < gameBoard.getSize(); j++) {
            currCell = gameBoard.getCell(selectedRow, j);
            if ((!currCell.isEmpty()) && ((!currCell.isCursor())) && currCell.getColor()==players.get(nextPlayerIndex).getColor()) {
                nextPlayerOpportunities.add(new Point(selectedRow, j));
            }
        }

        for (int i = 0; i < gameBoard.getSize(); i++) {
            currCell = gameBoard.getCell(i, selectedCol);
            if ((!currCell.isEmpty()) && ((!currCell.isCursor())) && (currCell.getColor() == players.get(nextPlayerIndex).getColor())) {
                nextPlayerOpportunities.add(new Point(i, selectedCol));
            }
        }

        return nextPlayerOpportunities;
    }


    @Override
    public boolean endGame() {
        return false;
    }


    @Override
    public List<Integer> createPool(int boardSize, int rangeFrom, int rangeTo, int numOfPlayers) {
        List<Integer> pool = new ArrayList<Integer>();
        int rangeSize = rangeTo - rangeFrom + 1;

        int numOfImpressions = (int)(Math.pow(boardSize,2) - 1) / (rangeSize*numOfPlayers);


        for(int i = rangeFrom; i <= rangeTo; i++) {
            for (int j = 0; j < numOfImpressions; j++) {
               // for(int colorIndex; colorIndex < numOfPlayers; colorIndex++)
                pool.add(i);
            }
        }
        for(int i = 0; i < ((int)((Math.pow(boardSize,2) - 1) % rangeSize*numOfPlayers)); i++) { //empty cells
            pool.add(-999);
        }
        pool.add(999); //cursor

        return pool;
    }

    @Override
    public String getPlayerColor(Player player) {
        return Colors.getColor(player.getColor());
    }






}
