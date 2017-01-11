package game;
import java.awt.*;
import java.util.*;
import java.util.List;

public class BasicGameEngine extends GameEngine {
    @Override
    public List<Point> getPossibleCells() {
        List<Point> possibleCells = new ArrayList<Point>();
        Cell currCell;

        if (playerTurnIndex % 2 == 0) { //rowPlayer{
            for (int j = 0; j < gameBoard.getSize(); j++) {
                currCell = gameBoard.getCell(cursorRow, j);
                if ((!currCell.isEmpty()) && ((!currCell.isCursor()))) {
                    possibleCells.add(new Point(cursorRow, j));
                }
            }

        }
        else {
            for (int i = 0; i < gameBoard.getSize(); i++) {
                currCell = gameBoard.getCell(i, cursorCol);
                if ((!currCell.isEmpty()) && ((!currCell.isCursor()))) {
                    possibleCells.add(new Point(i, cursorCol));
                }
            }
        }
        return possibleCells;
    }

    @Override
    public boolean endGame(){
        boolean gameEnded = true;
        if (playerTurnIndex % 2 == 0) { //even - row player}
            for(int j=0; j < gameBoard.getSize(); j++){
                if(gameBoard.getCell(cursorRow, j).isEmpty() == false && j != cursorCol){
                    gameEnded = false;
                }
            }
        }
        else { //col player
            for (int i = 0; i < gameBoard.getSize(); i++) {
                if (gameBoard.getCell(i, cursorCol).isEmpty() == false && i != cursorRow) {
                    gameEnded = false;
                }
            }
        }

        return gameEnded || players.size() == 1;
    }

//    @Override
//    protected Board buildRandomBoard(int boardSize, int rangeFrom, int rangeTo) {
//        Cell[][] boardArray = createEmptyBoard(boardSize);
//        List<PoolElement> poolOfImpression = createPool(boardSize, rangeFrom, rangeTo);
//        Random rand = new Random();
//        int randomNumber;
//
//        for(int i = 0; i < boardSize; i++) {
//            for(int j = 0; j< boardSize; j++) {
//                randomNumber = rand.nextInt(poolOfImpression.size());
//                if(poolOfImpression.get(randomNumber).getNumber() == 999) {
//                    boardArray[i][j].setAsEmpty();
//                }
//                else{
//                    boardArray[i][j].setValue(poolOfImpression.get(randomNumber).getNumber());
//                }
//                poolOfImpression.get(randomNumber).decreaseNum2();
//
//                if(poolOfImpression.get(randomNumber).getNum2() == 0) {
//                    poolOfImpression.remove(randomNumber);
//                }
//            }
//



//    @Override
//    protected ArrayList<PoolElement> createPool(int boardSize, int rangeFrom, int rangeTo){
//        ArrayList<PoolElement> pool = new ArrayList<PoolElement>();
//        int rangeSize = rangeTo - rangeFrom + 1;
//        int numOfImpressions = (int)(Math.pow(boardSize,2) - 1) / rangeSize;
//
//        for(int i = rangeFrom; i <= rangeTo; i++){
//            pool.add(new PoolElement(i, numOfImpressions));
//        }
//        pool.add(new PoolElement(999, ((int)(Math.pow(boardSize,2) - 1) % rangeSize) + 1));
//
//        return pool;
//    }

    @Override
    protected List<Integer> createPool(int boardSize, int rangeFrom, int rangeTo,int numOfPlayers){
        List<Integer> pool = new ArrayList<Integer>();
        int rangeSize = rangeTo - rangeFrom + 1;
        int numOfImpressions = (int)(Math.pow(boardSize,2) - 1) / rangeSize;

        for(int i = rangeFrom; i <= rangeTo; i++) {
            for (int j = 0; j < numOfImpressions; j++) {
                pool.add(i);
            }
        }
        for(int i = 0; i < ((int)((Math.pow(boardSize,2) - 1) % rangeSize)); i++) { //empty cells
            pool.add(-999);
        }
        pool.add(999); //cursor

        return pool;
    }

    @Override
    public String getPlayerColor(Player player) {
        return "";
    }

}
