package javafxUI;


import game.Cell;
import javafx.scene.layout.GridPane;

public class BoardUI {
    private CellUI[][] gameBoardUI;

    public BoardUI(){}

    public BoardUI(CellUI[][] gameBoard){
        gameBoardUI = gameBoard;
    }

    public CellUI[][] getGameBoardUI(){
        return gameBoardUI;
    }

    public void setGameBoardUI(CellUI[][] board){
        gameBoardUI = board;
    }

    public CellUI getCell(int i, int j){
        return gameBoardUI[i][j];
    }

    public void setCellUI(CellUI cell, int i, int j){
        gameBoardUI[i][j] = cell;
    }
}
