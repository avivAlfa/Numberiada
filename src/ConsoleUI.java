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

    private static void loadLevel() {
        Cell[][] board = new Cell[5][5];
        for(int i = 0; i < 5; i++)
            for(int j = 0; j < 5; j++){
                Cell c = new Cell((i+1));
                board[i][j] = c;
            }
        Board gameBoard = new Board(board, 5);

        Player p1 = new Player("RowPlayer",0,true);
        Player p2 = new Player("ColumnPlayer",0,true);
        Player[] p = {p1,p2};

        gameEngine = new GameEngine(gameBoard, p, 0, 0);
    }

    private static void runGameMainLoop() {
        int choice;
        printBoard();
        do{
            runMenu(MenuType.MAIN_MENU);

            //choice = getUserMainMenuChoice();
            //executeMainMenuUserChoice
        }//while(!gameEngine.endGame() && !endCurrentGame && !exitApplication);
        while(!endCurrentGame && !exitApplication);

    }

    private static void printBoard(){
        for(int i = 0; i < gameEngine.gameBoard.getSize(); i++){
            for(int j = 0; j < gameEngine.gameBoard.getSize(); j++){
                if(gameEngine.gameBoard.isEmptyCell(i,j)){
                    System.out.print("  ");

                }
                else{
                    System.out.print(gameEngine.gameBoard.getCellValue(i,j)+ " ");
                }
            }
            System.out.println();
        }
       // System.out.println("Current Player:" + gameEngine.getCurrentPlayerName());
    }

    private static void executeMainMenuChoice(int userChoice){
        switch (userChoice){
            case 1:
                printBoard();
                break;
            case 2:
                chooseMove();
                gameEngine.changeTurn();
                break;
            case 3:
                //showGameStatistics();
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
            try{
                userChoice = scanner.nextInt();
                if(gameEngine.isValidCell(userChoice)){
                    inputIsValid = true;
                }
                else {
                    System.out.println("Input is out of range, Please try again.");
                }

            } catch (Exception e) {
                System.out.println("Input is invalid. Please try again.");
                scanner.nextLine();
            }
        }
        while(!inputIsValid);

        return userChoice;
    }





}
