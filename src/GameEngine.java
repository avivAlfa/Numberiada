import Exceptions.CellNumberOutOfBoundsException;
import Exceptions.CursorCellException;
import Exceptions.EmptyCellException;
import Exceptions.InvalidPlayerTypeException;
import generated.GameDescriptor;

import java.util.*;

public class GameEngine {
    private Board gameBoard;
    private List<Player> players = null;
    private List<Player> resignedPlayers;
    private int playerTurnIndex;
    private int cursorRow;
    private int cursorCol;
    private int movesCnt = 0;
    private long startingTime;

    public GameEngine(){}

    public int getMovesCnt() { return movesCnt; }

    public void setStartingTime() {
        Date time = new Date();
        startingTime = time.getTime();
    }

    public Player getPlayerByIndex(int index){
        return players.get(index);
    }

    public void setPlayers(List<Player> i_players){ players = i_players;}

    public void setPlayerByIndex(Player player, int index){ players.add(index, player);
    }

    public int getPlayerTurnIndex() {
        return playerTurnIndex;
    }

    public void setPlayerTurnIndex(int playerTurnIndex) {
        this.playerTurnIndex = playerTurnIndex;
    }

    public Board getGameBoard() { return gameBoard; }

    public List<Player> getPlayers(){ return players; }

    public List<Player> getResignedPlayers() {return resignedPlayers; }

    public int getCursorRow() {
        return cursorRow;
    }

    public int getCursorCol(){
        return  cursorCol;
    }

    public void setCursor(int row, int col){
        this.cursorRow = row;
        this.cursorCol = col;
    }

    public String getCurrentPlayerName()
    {
        return players.get(playerTurnIndex).getName();
    }

    public boolean isCurrentPlayerHuman(){
        return players.get(playerTurnIndex).isHuman();
    }


    public Cell getChosenCellAccordingToIndex(int cellNumber) {
       // Cell chosenCell = new Cell();

        if (playerTurnIndex % 2 == 0) { //even - row player}
           // chosenCell = gameBoard.getCell(cursor, cellNumber);
            return gameBoard.getCell(cursorRow, cellNumber);
        }
        else {
            //chosenCell = gameBoard.getCell(cellNumber, cursor);;
            return gameBoard.getCell(cellNumber, cursorCol);
        }
       // return  chosenCell;
    }

    public void updateCursor(int cellNumber){
        if (playerTurnIndex % 2 == 0) { //even - row player}
            cursorCol = cellNumber;
        }
        else { //col player
            cursorRow = cellNumber;
        }
    }

    public boolean isValidCell(int cellNumber) throws Exception {
        if(!gameBoard.isIndexInBorders(cellNumber)) {
            throw new CellNumberOutOfBoundsException();
        }
        Cell chosenCell = getChosenCellAccordingToIndex((cellNumber));
        if(chosenCell.isEmpty()){
            throw new EmptyCellException();
        } else if(chosenCell.isCursor()) {
            throw new CursorCellException();
        }

        return true;
    }

    public void playMove(int chosenNumber){
        Cell chosenCell;

        Cell cursorCell = gameBoard.getCell(cursorRow, cursorCol);
        cursorCell.setAsEmpty();

        if(!players.get(playerTurnIndex).isHuman()){
            chosenNumber = getComputerChosenCellNumber();
        }

        chosenCell = getChosenCellAccordingToIndex(chosenNumber);
        players.get(playerTurnIndex).addScore(chosenCell.getValue());
        chosenCell.setAsCursor();
        updateCursor(chosenNumber);
        changeTurn();
    }


    public int getComputerChosenCellNumber(){
        int maxCellIndex = 0;
        int maxValue = -999;

        if(playerTurnIndex%2 == 0) {//row Player
            for(int i=0; i <gameBoard.getSize(); i++) {
                Cell currentCell = gameBoard.getCell(cursorRow, i);
                if(!currentCell.isCursor() && !currentCell.isEmpty()) {
                    if (currentCell.getValue() > maxValue) {
                        maxValue = currentCell.getValue();
                        maxCellIndex = i;
                    }
                }
            }
        }
        else {
            for(int i=0; i < gameBoard.getSize(); i++) {
                Cell currentCell = gameBoard.getCell(i, cursorCol);
                if(!currentCell.isCursor() && !currentCell.isEmpty()) {
                    if (currentCell.getValue() > maxValue) {
                        maxValue = currentCell.getValue();
                        maxCellIndex = i;
                    }
                }
            }
        }
        return maxCellIndex;
    }

    public void changeTurn() {
        playerTurnIndex++;
        movesCnt++;
        if(playerTurnIndex == players.size()){
            playerTurnIndex = 0;
        }
    }

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
        return gameEnded;
    }

    public List<Player> getGameWinners(){
        List<Player> gameWinners = new ArrayList<Player>();
        int maxScore = players.get(0).getScore();

        for(int i = 0 ; i < players.size() ; i++) {
            if(players.get(i).getScore() > maxScore){
                maxScore = players.get(i).getScore();
            }
        }

        for(int i = 0 ; i < players.size() ; i++) {
            if(players.get(i).getScore() == maxScore){
                gameWinners.add(players.get(i));
            }
        }
        return gameWinners;
    }

    public String getTimeDuration() {
        Date curr = new Date();
        long diff = curr.getTime() - startingTime;

        return ((diff / (60*1000) % 60) + ":" + (diff / 1000 % 60));
    }

    public void loadGameParams(){
        playerTurnIndex = 0;
        movesCnt = 0;
        resignedPlayers = null;
        players = null;
    }

    public void loadGameParamsFromDescriptor(GameDescriptor gd){

        if(gd.getBoard().getStructure().getType().toLowerCase().equals("random")) {
            gameBoard = buildRandomBoard(gd.getBoard().getSize().intValue(), gd.getBoard().getStructure().getRange().getFrom(), gd.getBoard().getStructure().getRange().getTo());
        } else {
           gameBoard = buildExplicitBoard(gd);
           GameDescriptor.Board.Structure.Squares.Marker marker = gd.getBoard().getStructure().getSquares().getMarker();
           cursorRow = marker.getRow().intValue() - 1;
           cursorCol = marker.getColumn().intValue() - 1;
        }
        if(gd.getPlayers() != null) {
            players = buildPlayersListFromDescriptor(gd.getPlayers().getPlayer());
        }
        else {
            buildBasicPlayers();
        }
    }

    private Board buildRandomBoard(int boardSize, int rangeFrom, int rangeTo) {
        Cell[][] boardArray = createEmptyBoard(boardSize);
        List<PoolElement> poolOfImpression = createPool(boardSize, rangeFrom, rangeTo);
        Random rand = new Random();
        int randomNumber;

        for(int i = 0; i < boardSize; i++) {
            for(int j = 0; j< boardSize; j++) {
                randomNumber = rand.nextInt(poolOfImpression.size());
                if(poolOfImpression.get(randomNumber).getNumber() == 999) {
                    boardArray[i][j].setAsEmpty();
                }
                else{
                    boardArray[i][j].setValue(poolOfImpression.get(randomNumber).getNumber());
                }
                poolOfImpression.get(randomNumber).decreaseNum2();

                if(poolOfImpression.get(randomNumber).num2 == 0) {
                    poolOfImpression.remove(randomNumber);
                }
            }
        }

        //set the cursor in one of the remain empty cells
        boolean foundEmpty = false;
        for(int i = 0; i < boardSize && !foundEmpty; i++) {
            for (int j = 0; j < boardSize && !foundEmpty; j++) {
                if(boardArray[i][j].isEmpty()) {
                    foundEmpty = true;
                    boardArray[i][j].setAsCursor();
                    cursorRow = i;
                    cursorCol = j;
                }
            }
        }

        return new Board(boardArray, boardSize);
    }

    public ArrayList<PoolElement> createPool(int boardSize, int rangeFrom, int rangeTo){
        ArrayList<PoolElement> pool = new ArrayList<PoolElement>();
        int rangeSize = rangeTo - rangeFrom + 1;
        int numOfImpressions = (int)(Math.pow(boardSize,2) - 1) / rangeSize;
        
        for(int i = rangeFrom; i <= rangeTo; i++){
            pool.add(new PoolElement(i, numOfImpressions));
        }
        pool.add(new PoolElement(999, ((int)(Math.pow(boardSize,2) - 1) % rangeSize) + 1));
        
        return pool;
    } 

    private Cell[][] createEmptyBoard(int boardSize) {
        //Board board;

        //int boardSize = gd.getBoard().getSize().intValue();
        Cell[][] boardArray = new Cell[boardSize][boardSize];

        for(int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                boardArray[i][j] = new Cell();
                boardArray[i][j].setAsEmpty();
            }
        }
        return boardArray;
    }

    private Board buildExplicitBoard(GameDescriptor gd){
        Board board;
        int boardSize = gd.getBoard().getSize().intValue();
        Cell[][] boardArray = createEmptyBoard(boardSize);

        List<GameDescriptor.Board.Structure.Squares.Square> squareList = gd.getBoard().getStructure().getSquares().getSquare();
        GameDescriptor.Board.Structure.Squares.Square curSquare;
        for(int i=0; i< squareList.size(); i++) {
            curSquare = squareList.get(i);
            boardArray[curSquare.getRow().intValue()-1][curSquare.getColumn().intValue()-1].setValue(curSquare.getValue().intValue());
        }
        boardArray[gd.getBoard().getStructure().getSquares().getMarker().getRow().intValue() - 1][gd.getBoard().getStructure().getSquares().getMarker().getColumn().intValue() - 1].setAsCursor();

        return new Board(boardArray, boardSize);
    }

    private List<Player> buildPlayersListFromDescriptor(List<GameDescriptor.Players.Player> playersListFromGameDescriptor) {
        List<Player> playersList = new ArrayList<Player>();
        Player newPlayer;
        for(int i = 0; i < playersListFromGameDescriptor.size(); i++){
            newPlayer = new Player();
            newPlayer.setName(playersListFromGameDescriptor.get(i).getName());
            newPlayer.setScore(0);
            if(playersListFromGameDescriptor.get(i).getType().equals("Human")){
                newPlayer.setAsHuman();
            }else if(playersListFromGameDescriptor.get(i).getType().equals("Computer") ){
                newPlayer.setAsComputer();
            }
            playersList.add(newPlayer);
        }
        return playersList;
    }

    public void buildBasicPlayers(){
        players = new ArrayList<Player>();
        Player player1 = new Player("RowPlayer", 0, true);
        Player player2 = new Player("ColPlayer", 0, true);
        players.add(player1);
        players.add(player2);
    }


    public void removeCurrentPlayerFromGame(){
        if(resignedPlayers == null){
            resignedPlayers = new ArrayList<>();
        }
        resignedPlayers.add(players.get(playerTurnIndex));
        players.remove(playerTurnIndex);
        if(playerTurnIndex == players.size()) {//last player resigned
            playerTurnIndex--;
        }
    }

    public int getNumOfPlayingPlayers(){
        return players.size();
    }

    public void restartGame(String xml_path)throws Exception{
        loadGameParams();
        loadGameParamsFromDescriptor(XML_Handler.getGameDescriptorFromXml(xml_path));

    }
}
