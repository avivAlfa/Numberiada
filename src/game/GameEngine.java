package game;

import Exceptions.CellNumberOutOfBoundsException;
import Exceptions.CursorCellException;
import Exceptions.EmptyCellException;
import Exceptions.InvalidPlayerTypeException;
import generated.GameDescriptor;


import java.awt.*;
import java.util.*;
import java.util.List;

public abstract class GameEngine {
    protected Board gameBoard;
    protected List<Player> players = null;
    protected List<Player> resignedPlayers;
    protected int playerTurnIndex;
    protected int cursorRow;
    protected int cursorCol;
    protected int movesCnt = 0;
    protected long startingTime;

    //public GameEngine(){}

    public abstract List<Point> getPossibleCells();
    public abstract boolean endGame();
    protected abstract List<PoolElement> createPool(int boardSize, int rangeFrom, int rangeTo, List<GameDescriptor.Players.Player> players);
    public abstract String getPlayerColor(Player player);
    public abstract List<Point> getNextPlayerOpportunities(int selectedRow, int selectedCol);
    public abstract List<Point> getAllPossibleCells();
    public abstract Point getComputerChosenCellIndexes();


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

    public int getCurrentPlayerID(){ return players.get(playerTurnIndex).getId();}

    public int getNextPlayerIndex(){
        int index = playerTurnIndex + 1;
        if(index == players.size()){
            index = 0;
        }
        return index;
    }

    public int getPreviousPlayerIndex(){
        int index = playerTurnIndex - 1;
        if(index < 0)
            index = players.size() - 1;
        return index;
    }

    public String getPlayerInfo(int index){
        Player player = players.get(index);
        String playerInfo;
       // playerInfo = player.getName() + " " + player.getScore() + " " + Colors.getColor(player.getColor());
        playerInfo = String.format("%1$-12s",player.getName());
        playerInfo += String.format("%1$-5s",player.getScore());
        playerInfo += String.format("%1$10s",getPlayerColor(player));

        return playerInfo;
    }


//    public boolean isCurrentPlayerHuman(){
//        return players.get(playerTurnIndex).isHuman();
//    }


//    public Cell getChosenCellAccordingToIndex(int cellNumber) {
//       // Cell chosenCell = new Cell();
//
//        if (playerTurnIndex % 2 == 0) { //even - row player}
//           // chosenCell = gameBoard.getCell(cursor, cellNumber);
//            return gameBoard.getCell(cursorRow, cellNumber);
//        }
//        else {
//            //chosenCell = gameBoard.getCell(cellNumber, cursor);;
//            return gameBoard.getCell(cellNumber, cursorCol);
//        }
//       // return  chosenCell;
//    }

//    public void updateCursor(int cellNumber){
//        if (playerTurnIndex % 2 == 0) { //even - row player}
//            cursorCol = cellNumber;
//        }
//        else { //col player
//            cursorRow = cellNumber;
//        }
//    }


//    public boolean isValidCell(int cellNumber) throws Exception {
//        if(!gameBoard.isIndexInBorders(cellNumber)) {
//            throw new CellNumberOutOfBoundsException();
//        }
//        Cell chosenCell = getChosenCellAccordingToIndex((cellNumber));
//        if(chosenCell.isEmpty()){
//            throw new EmptyCellException();
//        } else if(chosenCell.isCursor()) {
//            throw new CursorCellException();
//        }
//
//        return true;
//    }

//    public void playMove(int chosenNumber){
//        Cell chosenCell;
//
//        Cell cursorCell = gameBoard.getCell(cursorRow, cursorCol);
//        cursorCell.setAsEmpty();
//
//        if(!players.get(playerTurnIndex).isHuman()){
//            chosenNumber = getComputerChosenCellNumber();
//        }
//
//        chosenCell = getChosenCellAccordingToIndex(chosenNumber);
//        players.get(playerTurnIndex).addScore(chosenCell.getValue());
//        chosenCell.setAsCursor();
//        updateCursor(chosenNumber);
//        changeTurn();
//    }

//    public void playMove(int chosenRow, int chosenCol){
//        Cell chosenCell;
//
//        Cell cursorCell = gameBoard.getCell(cursorRow, cursorCol);
//        cursorCell.setAsEmpty();
//
////        if(!players.get(playerTurnIndex).isHuman()){
////            chosenNumber = getComputerChosenCellNumber();
////        }
//
//      //  chosenCell = getChosenCellAccordingToIndex(chosenNumber);
//        chosenCell = gameBoard.getCell(chosenRow, chosenCol);
//        players.get(playerTurnIndex).addScore(chosenCell.getValue());
//        chosenCell.setAsCursor();
//        updateCursor(chosenRow, chosenCol);
//        changeTurn();
//    }




//    public int getComputerChosenCellNumber(){
//        int maxCellIndex = 0;
//        int maxValue = -999;
//
//        if(playerTurnIndex%2 == 0) {//row Player
//            for(int i=0; i <gameBoard.getSize(); i++) {
//                Cell currentCell = gameBoard.getCell(cursorRow, i);
//                if(!currentCell.isCursor() && !currentCell.isEmpty()) {
//                    if (currentCell.getValue() > maxValue) {
//                        maxValue = currentCell.getValue();
//                        maxCellIndex = i;
//                    }
//                }
//            }
//        }
//        else {
//            for(int i=0; i < gameBoard.getSize(); i++) {
//                Cell currentCell = gameBoard.getCell(i, cursorCol);
//                if(!currentCell.isCursor() && !currentCell.isEmpty()) {
//                    if (currentCell.getValue() > maxValue) {
//                        maxValue = currentCell.getValue();
//                        maxCellIndex = i;
//                    }
//                }
//            }
//        }
//        return maxCellIndex;
//    }

    public void playMove(int chosenRow, int chosenCol){
        Cell chosenCell;

        Cell cursorCell = gameBoard.getCell(cursorRow, cursorCol);
        cursorCell.setAsEmpty();

//        if(!players.get(playerTurnIndex).isHuman()){
//            chosenNumber = getComputerChosenCellNumber();
//        }

        //  chosenCell = getChosenCellAccordingToIndex(chosenNumber);
        chosenCell = gameBoard.getCell(chosenRow, chosenCol);
        players.get(playerTurnIndex).addScore(chosenCell.getValue());
        chosenCell.setAsCursor();
        updateCursor(chosenRow, chosenCol);
        movesCnt++;
        changeTurn();
    }

    public void updateCursor(int row, int col){
        cursorRow = row;
        cursorCol = col;
    }

    public void changeTurn() {
        playerTurnIndex++;
        if(playerTurnIndex == players.size()){
            playerTurnIndex = 0;
        }
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
            List<GameDescriptor.Players.Player> players = (gd.getPlayers()!=null)?gd.getPlayers().getPlayer():null;
            gameBoard = buildRandomBoard(gd.getBoard().getSize().intValue(), gd.getBoard().getStructure().getRange().getFrom(), gd.getBoard().getStructure().getRange().getTo(), players);
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

    protected List<Player> buildPlayersListFromDescriptor(List<GameDescriptor.Players.Player> playersListFromGameDescriptor) {
        List<Player> playersList = new ArrayList<Player>();
        Player newPlayer;
        for(int i = 0; i < playersListFromGameDescriptor.size(); i++){
            newPlayer = new Player();
            newPlayer.setId(playersListFromGameDescriptor.get(i).getId().intValue());
            newPlayer.setName(playersListFromGameDescriptor.get(i).getName());
            newPlayer.setScore(0);
            newPlayer.setColor(playersListFromGameDescriptor.get(i).getColor());
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

    public Cell[][] createEmptyBoard(int boardSize) {
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

    public Board buildExplicitBoard(GameDescriptor gd){
        int boardSize = gd.getBoard().getSize().intValue();
        int currRow,currCol;
        Cell[][] boardArray = createEmptyBoard(boardSize);

        List<GameDescriptor.Board.Structure.Squares.Square> squareList = gd.getBoard().getStructure().getSquares().getSquare();
        GameDescriptor.Board.Structure.Squares.Square curSquare;
        for(int i=0; i< squareList.size(); i++) {
            curSquare = squareList.get(i);
            currRow = curSquare.getRow().intValue()-1;
            currCol = curSquare.getColumn().intValue()-1;
            boardArray[currRow][currCol].setValue(curSquare.getValue().intValue());
            boardArray[currRow][currCol].setColor(curSquare.getColor());
        }
        boardArray[gd.getBoard().getStructure().getSquares().getMarker().getRow().intValue() - 1][gd.getBoard().getStructure().getSquares().getMarker().getColumn().intValue() - 1].setAsCursor();

        return new Board(boardArray, boardSize);
    }

    public Board buildRandomBoard(int boardSize, int rangeFrom, int rangeTo, List<GameDescriptor.Players.Player> players) {
        Cell[][] boardArray = createEmptyBoard(boardSize);
        List<PoolElement> poolOfNumbers = createPool(boardSize, rangeFrom, rangeTo, players);
        Collections.shuffle(poolOfNumbers);
        int index=0;
        for(int i = 0; i < boardSize; i++) {
            for(int j = 0; j < boardSize; j++) {
                boardArray[i][j].setValue(poolOfNumbers.get(index).getNumber());
                boardArray[i][j].setColor(poolOfNumbers.get(index).getColor());
                if(poolOfNumbers.get(index).getNumber() == -999)
                    boardArray[i][j].setAsEmpty();
                else if(poolOfNumbers.get(index).getNumber() == 999){
                    boardArray[i][j].setAsCursor();
                    cursorRow = i;
                    cursorCol = j;
                }
                index++;
            }
        }
        return new Board(boardArray, boardSize);
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
        //TODO: rewrite restart
    //    loadGameParamsFromDescriptor(XML_Handler.getGameDescriptorFromXml(xml_path));

    }

    public String createEndGameMessage(){
        List<Player> gameWinners = getGameWinners();
        StringBuilder endGameMessage = new StringBuilder();

        if(players.size()==1){
            endGameMessage.append("Game ended due to all other players resignment\n");
        }

        if(gameWinners.size() == 1){
            endGameMessage.append("The winner is " + gameWinners.get(0).getName() + " with score of " + gameWinners.get(0).getScore() + "!\n");
        }else{
            endGameMessage.append("It's a Tie!\n");
        }

        endGameMessage.append("\n");
        endGameMessage.append("Players total moves: " + movesCnt + "\n");
        for(int i = 0 ; i < players.size() ; i++) {
            endGameMessage.append(players.get(i).getName() + " score: " + players.get(i).getScore()+"\n");
        }
        if(resignedPlayers != null){
            for(int i = 0 ; i < resignedPlayers.size() ; i++) {
                endGameMessage.append(resignedPlayers.get(i).getName() + " score: " + resignedPlayers.get(i).getScore()+"\n");
            }
        }
        return endGameMessage.toString();
    }
}
