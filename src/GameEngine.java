
public class GameEngine {
    Board gameBoard;
    Player [] players;
    int playerTurnIndex;
    int cursor;

    public GameEngine(Board gameBoard, Player[] players, int playerTurnIndex, int cursor) {
        this.gameBoard = gameBoard;
        this.players = players;
        this.playerTurnIndex = playerTurnIndex;
        this.cursor = cursor;
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

    public int getCursor() {
        return cursor;
    }

    public void setCursor(int cursor) {
        this.cursor = cursor;
    }

    public String getCurrentPlayerName()
    {
        return players[playerTurnIndex].getName();
    }

    public Cell getChosenCell(int cellNumber) {
       // Cell chosenCell = new Cell();

        if (playerTurnIndex % 2 == 0) { //even - row player}
           // chosenCell = gameBoard.getCell(cursor, cellNumber);
            return gameBoard.getCell(cursor, cellNumber);
        }
        else {
            //chosenCell = gameBoard.getCell(cellNumber, cursor);;
            return gameBoard.getCell(cellNumber, cursor);
        }
       // return  chosenCell;
    }

    public boolean isValidCell(int cellNumber)
    {
        if(gameBoard.isIndexInBorders(cellNumber)){
            Cell chosenCell = getChosenCell((cellNumber));
            if(!chosenCell.isEmpty())
                return true;
        }

        return false;
    }

    public void playMove(int chosenNumber){
        Cell chosenCell = getChosenCell(chosenNumber);
        players[playerTurnIndex].addScore(chosenCell.getValue());
        chosenCell.setAsEmpty();
        this.cursor = chosenNumber;
    }

    public void changeTurn()
    {
        playerTurnIndex++;
        if(playerTurnIndex == players.length){
            playerTurnIndex = 0;
        }
    }





}
