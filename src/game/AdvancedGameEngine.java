package game;


import generated.GameDescriptor;
import generated.GameDescriptor.Players;

import java.awt.*;
import java.util.ArrayList;
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
        boolean gameEnded = true;
        for(int j=0; j < gameBoard.getSize(); j++){
            if(gameBoard.getCell(cursorRow, j).isEmpty() == false && j != cursorCol){
                gameEnded = false;
            }
        }
        for (int i = 0; i < gameBoard.getSize(); i++) {
            if (gameBoard.getCell(i, cursorCol).isEmpty() == false && i != cursorRow) {
                gameEnded = false;
            }
        }

        return gameEnded || players.size() == 1;
    }


    @Override
    public List<PoolElement> createPool(int boardSize, int rangeFrom, int rangeTo, List<GameDescriptor.Players.Player> players) {
    //public List<PoolElement> createPool(int boardSize, int rangeFrom, int rangeTo, int numOfPlayers) {
        List<PoolElement> pool = new ArrayList<PoolElement>();
        int rangeSize = rangeTo - rangeFrom + 1;
        PoolElement tempElem;
        int numOfPlayers = players.size();
        List<Integer> playerColors = new ArrayList<>();

        for(GameDescriptor.Players.Player player : players) {
            playerColors.add(player.getColor());
        }

        int numOfImpressions = (int)(Math.pow(boardSize,2) - 1) / (rangeSize*numOfPlayers);

        for(int i = rangeFrom; i <= rangeTo; i++) {
            for (int j = 0; j < numOfImpressions; j++) {
               for(int colorIndex = 0; colorIndex < numOfPlayers; colorIndex++) {
                    tempElem = new PoolElement(i, playerColors.get(colorIndex));
                    pool.add(tempElem);
               }
            }
        }
        for(int i = 0; i < ((int)((Math.pow(boardSize,2) - 1) % (rangeSize*numOfPlayers))); i++) { //empty cells
            tempElem = new PoolElement(-999, 0);
            pool.add(tempElem);
            //pool.add(-999);
        }
        tempElem = new PoolElement(999, 0);
        pool.add(tempElem);
        //pool.add(999); //cursor

        return pool;
    }

    @Override
    public String getPlayerColor(Player player) {
        return Colors.getColor(player.getColor());
    }






}
