import java.util.Date;

public class GameEngine {
    private Board gameBoard;
    private Player [] players;
    private int playerTurnIndex;
    private int cursorRow;
    private int cursorCol;
    private int movesCnt = 0;
    private long startingTime;

    public GameEngine(Board gameBoard, Player[] players, int playerTurnIndex, int cursorRow, int cursorCol) {
        this.gameBoard = gameBoard;
        this.players = players;
        this.playerTurnIndex = playerTurnIndex;
        this.cursorRow = cursorRow;
        this.cursorCol = cursorCol;
    }

    public int getMovesCnt() { return movesCnt; }

    public void setStartingTime() {
        Date time = new Date();
        startingTime = time.getTime();
    }

    public Player getPlayerByIndex(int index){
        return players[index];
    }

    public void setPlayerByIndex(Player player, int index){
        players[index] = player;
    }

    public int getPlayerTurnIndex() {
        return playerTurnIndex;
    }

    public void setPlayerTurnIndex(int playerTurnIndex) {
        this.playerTurnIndex = playerTurnIndex;
    }

    public Board getGameBoard() { return gameBoard; }

    public Player[] getPlayers(){ return players; }

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
        return players[playerTurnIndex].getName();
    }

    public Cell getChosenCell(int cellNumber) {
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
        Cell chosenCell = getChosenCell((cellNumber));
        if(chosenCell.isEmpty()){
            throw new EmptyCellException();
        }

        return true;
    }

    public void playMove(int chosenNumber){
        Cell cursorCell = gameBoard.getCell(cursorRow, cursorCol);
        cursorCell.setAsEmpty();

        Cell chosenCell = getChosenCell(chosenNumber);
        players[playerTurnIndex].addScore(chosenCell.getValue() - '0');
        chosenCell.setAsCursor();

        updateCursor(chosenNumber);
    }

    public void changeTurn()
    {
        playerTurnIndex++;
        movesCnt++;
        if(playerTurnIndex == players.length){
            playerTurnIndex = 0;
        }
    }

    public String getTimeDuration() {
        Date curr = new Date();
        long diff = curr.getTime() - startingTime;

        return ((diff / (60*1000) % 60) + " : " + (diff / 1000 % 60));
    }



}
