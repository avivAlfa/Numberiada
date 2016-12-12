import generated.GameDescriptor;

import java.awt.*;
import java.util.Scanner;

public class ConsoleUI {

    public enum MenuType
    {
        START_MENU,
        MAIN_MENU
    }
    public enum MainMenuItem //move to other location
    {
        DISPLAY, //Display game status
        PLAY,    //Play move
        SHOW,    //Show game statistics
        END,     //End game
        EXIT     //Exit application
    }

    private static GameEngine gameEngine;
    private static boolean endCurrentGame = false;
    private static boolean exitApplication = false;
    private static Scanner scanner = new Scanner(System.in);


    public static void runGame()
    {
        while(!exitApplication){
            runMenu((MenuType.START_MENU));
        }
    }

    private static void runMenu(MenuType menuType) {
        int userChoice = -1;

        printMenu(menuType);
        userChoice = getMenuUserChoice(menuType);
        executeMenuUserChoice(menuType, userChoice);
    }

    private static void printMenu(MenuType menuType) {
        System.out.println("Please choose an option from the menu");
        switch (menuType) {
            case START_MENU:
                System.out.println("(1) Load level");
                System.out.println("(2) Start game");
                break;
            case MAIN_MENU:
                System.out.println("(1) Display game status");
                System.out.println("(2) Play move");
                System.out.println("(3) Show game statistics");
                System.out.println("(4) End game");
                System.out.println("(5) Exit application");
                break;
        }
    }

    private static int getMenuUserChoice(MenuType menuType)
    {
        int userChoice = -1;
        boolean inputIsValid = false;

        do {
            try{
                userChoice = scanner.nextInt();
                if(isValidInput(menuType, userChoice)){
                    inputIsValid = true;
                }
                else {
                    System.out.println("Input is out of range, Please try again.");
                }

            } catch (Exception e) {
                System.out.println("Input is invalid. Please try again.");
                //scanner.nextLine();
            }finally {
                scanner.nextLine();
            }
        }
        while(!inputIsValid);

        return userChoice;
    }

    private static void executeMenuUserChoice(MenuType menuType, int userChoice){
        switch (menuType){
            case START_MENU:
                executeStartMenuChoice(userChoice);
                break;
            case MAIN_MENU:
                executeMainMenuChoice(userChoice);
                break;
        }
    }

    private static boolean isValidInput(MenuType menuType, int userChoice)
    {
        boolean validInput = false;
        switch (menuType){
            case START_MENU:
                validInput = userChoice == 1 || userChoice == 2;
                break;
            case MAIN_MENU:
                validInput = userChoice > 0 && userChoice < 6; //change to menuItem labels
                break;
        }
        return validInput;
    }

    private static void executeStartMenuChoice(int userChoice)
    {
        switch (userChoice){
            case 1:
                loadLevel();
                break;
            case 2:
                runGameMainLoop();
                break;
        }
    }

//    private static void loadLevel() {
//        Cell[][] board = new Cell[5][5];
//        for(int i = 0; i < 5; i++)
//            for(int j = 0; j < 5; j++){
//                Cell c = new Cell((char)(i+1 + '0'));
//                board[i][j] = c;
//            }
//            board[2][2].setValue('@');
//        Board gameBoard = new Board(board, 5);
//
//        Player p1 = new Player("RowPlayer",0,true);
//        Player p2 = new Player("ColumnPlayer",0,true);
//        Player[] p = {p1,p2};
//
//        gameEngine = new GameEngine(gameBoard, p, 0, 2, 2);
//    }
private static void loadLevel() {
    gameEngine = new GameEngine();
    String xml_path;
    System.out.println("Please enter xml path:");
    //validate file name
    //scanner.next();
    xml_path = scanner.nextLine();
    GameDescriptor gameDescriptor = XML_Handler.getGameDescriptor(xml_path);

    if(XML_Handler.validate(gameDescriptor)){
        gameEngine.loadGameParamsFromDescriptor(gameDescriptor);
    }

    gameEngine.setPlayers(createBasicPlayer());
}

    private static void runGameMainLoop() {
        int choice;
        printBoard();
        gameEngine.setStartingTime();
        do{
            System.out.println();
            runMenu(MenuType.MAIN_MENU);

            //choice = getUserMainMenuChoice();
            //executeMainMenuUserChoice
        }//while(!gameEngine.endGame() && !endCurrentGame && !exitApplication);
        while(!endCurrentGame && !exitApplication);

    }

    private static void printBoard(){
        int boardSize = gameEngine.getGameBoard().getSize();
        Cell currCell;

        System.out.print("    ");
        for(int i = 0; i < boardSize; i++) {
            System.out.print(" " + String.format("%1$-2s",i + 1) + "  ");
        }
            for(int i = 0; i < boardSize; i++){
            printBoardGap(boardSize);
            System.out.print(String.format("%1$-3s",i+1) + "|");
            for(int j = 0; j < boardSize; j++){
                currCell = gameEngine.getGameBoard().getCell(i,j);
                if(currCell.isCursor()){
                    System.out.print(" " + String.format("%1$-2s",'@') + " |");
                }
                else if(currCell.isEmpty()){
                    System.out.print(" " + String.format("%1$-2s",' ') + " |");
                }
                else {
                    System.out.print(" " + String.format("%1$-2s", currCell.getValue()) + " |");
                }
            }
        }
        printBoardGap(boardSize);
        System.out.println("Current Player: " + gameEngine.getCurrentPlayerName());
    }

    private static void printBoardGap(int cols)
    {
        System.out.println();
        System.out.print("   ");
        for (int i = 0; i < cols; i++)
        {
            System.out.print("-----");
        }
        System.out.println();
    }

    private static void executeMainMenuChoice(int userChoice){
        switch (userChoice){
            case 1:
                printBoard();
                break;
            case 2:
                chooseMove();
                gameEngine.changeTurn();
                printBoard();
                break;
            case 3:
                showGameStatistics();
                break;
            case 4:
                //resetGame();
                endCurrentGame = true;
                break;
            case 5:
                exitApplication = true;
                break;
        }
    }

    private static void chooseMove() {
        int chosenNumber;
        System.out.println(gameEngine.getCurrentPlayerName() + ", Which Cell would you like to collect?");
        chosenNumber = getUserCellChoice();
        gameEngine.playMove(chosenNumber);
    }

    private static int getUserCellChoice(){
        int userChoice = -1;
        boolean inputIsValid = false;

        do {
            try {
                userChoice = scanner.nextInt();
                userChoice--;
                if (gameEngine.isValidCell(userChoice)){
                    inputIsValid = true;
                }
            } catch (CellNumberOutOfBoundsException e){
                System.out.println(e.getMessage());
            } catch(EmptyCellException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("Input is invalid. Please try again.");
            } finally {
                scanner.nextLine();
            }
        }
        while(!inputIsValid);

        return userChoice;
    }

    private static void showGameStatistics() {
        Player [] players = gameEngine.getPlayers();

        System.out.println("Players' number of moves: " + gameEngine.getMovesCnt());
        System.out.println("Game duration: " + gameEngine.getTimeDuration());
        for(int i = 0 ; i < players.length ; i++) {
            System.out.println(players[i].getName() + " score: " + players[i].getScore());
        }
    }

    private static Player[] createBasicPlayer(){
        Player p1 = new Player("RowPlayer",0,true);
        Player p2 = new Player("ColumnPlayer",0,true);
        Player[] players = {p1,p2};
        return players;
    }
}
