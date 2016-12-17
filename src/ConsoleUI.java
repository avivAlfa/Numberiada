import Exceptions.*;
import generated.GameDescriptor;
import resources.RunMode;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.util.*;
import java.util.List;

public class ConsoleUI {

    private static GameEngine gameEngine;
    private static boolean endCurrentGame = false;
    private static boolean exitApplication = false;
    private static boolean gameLoaded = false;
    private static String loadedXmlFilePath = null;
    private static Scanner scanner = new Scanner(System.in);

    private static RunMode runMode;
    private static List<String> movesList;


    public static void runGame()
    {

        setRunMode();
        while(!exitApplication){
            runMenu((MenuType.START_MENU));
        }
    }

    private static void setRunMode()
    {
        int input;
        Boolean inputIsValid = false;

        System.out.println("Please choose run mode of the game:");
        System.out.println("(1) User");
        System.out.println("(2) File");

        do {
            try{
                //userChoice = scanner.nextInt();
                input = scanner.nextInt();
                if(input == 1){
                    runMode = RunMode.USER;
                    inputIsValid = true;
                }else if(input == 2){
                    runMode = RunMode.FILE;
                    initListFromUserFile();
                    inputIsValid = true;
                }
                else {
                    System.out.println("Input is out of range, Please try again.");
                }

            } catch (Exception e) {
                System.out.println("Input is invalid. Please try again.");
            }finally {
                cleanBuffer();
            }
        }
        while(!inputIsValid);
    }

    private static void initListFromUserFile(){
        movesList = new ArrayList<>();
        System.out.println("Please enter the path of the steps file");
        boolean fileLoaded = false;

        scanner.nextLine();
        while(!fileLoaded) {

            String filePath = scanner.nextLine();
            try {
                FileReader fr = new FileReader(filePath);
                BufferedReader textReader = new BufferedReader(fr);

                String line;
                while ((line = textReader.readLine()) != null) {
                    movesList.add(line);
                }
                textReader.close();
                fileLoaded = true;
            } catch (FileNotFoundException e) {
                System.out.println("The path you've entered isn't correct! Please try again");
            } catch (IOException e) {
                System.out.println("Error reading file, please provide a path to another file.");
            }
        }

    }

    public static int getIntInput() throws Exception{
        int input = 0;
        switch (runMode) {
            case USER:
                input = scanner.nextInt();
                break;
            case FILE:
                input = Integer.parseInt(movesList.get(0));
                movesList.remove(0);
                break;
        }
        return input;
    }
    public static String getStringInput(){
        String input = null;
        switch (runMode) {
            case USER:
                input = scanner.nextLine();
                break;
            case FILE:
                input = (movesList.get(0));
                movesList.remove(0);
                break;
        }
        return input;
    }

    private static void cleanBuffer(){
        if(runMode == RunMode.USER){
            scanner.nextLine();
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
                System.out.println("(3) Exit");
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
                //userChoice = scanner.nextInt();
                userChoice = getIntInput();
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
                cleanBuffer();
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
                validInput = userChoice == 1 || userChoice == 2 || userChoice == 3;
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
                gameLoaded = true;
                endCurrentGame = false;
                break;
            case 2:
                if(gameLoaded){
                    endCurrentGame = false;
                    runGameMainLoop();
                }else{
                    System.out.println("Please load a game first.");
                    System.out.println();
                }
                break;
            case 3:
                exitApplication = true;
        }
    }

    private static void loadLevel() {
        gameEngine = new GameEngine();

        GameDescriptor gameDescriptor = getGameDescriptor();
        gameEngine.loadGameParams();
        gameEngine.loadGameParamsFromDescriptor(gameDescriptor);
        if(gameEngine.getPlayers() == null){
            gameEngine.buildBasicPlayers();
        }
    }

    private static GameDescriptor getGameDescriptor(){
        boolean xmlPathValid = false;
        boolean xmlContentValid = false;
        String xml_path = null;
        System.out.println("Please enter xml path:");
        GameDescriptor gameDescriptor = null;

        do{
            try{
               // xml_path = scanner.nextLine();
                xml_path = getStringInput();
                gameDescriptor = XML_Handler.getGameDescriptorFromXml(xml_path);
                XML_Handler.validate(gameDescriptor);
                xmlPathValid = true;
                xmlContentValid = true;

            } catch (CellOutOfBoundsException e) {
                System.out.print(e.getMessage());
            } catch (CursorCellException e) {
                System.out.print(e.getMessage());
            } catch (DuplicateCellException e) {
                System.out.print(e.getMessage());
            } catch (InvalidBoardSizeException e) {
                System.out.print(e.getMessage());
            } catch (InvalidRangeException e) {
                System.out.print(e.getMessage());
            } catch (InvalidRangeCompareToBoardSizeException e) {
                System.out.print(e.getMessage());
            } catch (InvalidRangeValuesException e){
                System.out.print(e.getMessage());
            } catch (InvalidPlayerTypeException e){
                System.out.print(e.getMessage());
            } catch(FileNotFoundException e) {
                System.out.print("File not found!");
            } catch(JAXBException e) {
                if(!xml_path.endsWith(".xml"))
                    System.out.print("The file you asked for isn't a xml file!");
                else
                    System.out.print("Error trying to retrieve data from XML file");
            } catch (Exception e) {
                System.out.print("An unhandled error occured");

            }
            finally {
                if(!xmlPathValid){
                    System.out.println(" Please try again:");
                }
            }


        }while(!xmlPathValid && !xmlContentValid);

        loadedXmlFilePath = xml_path;
        return gameDescriptor;
    }


    private static void runGameMainLoop() {
        int choice;
        boolean gameOver = false;
        printBoard();
        gameEngine.setStartingTime();

        gameOver = gameEngine.endGame();
        while(!gameOver && !endCurrentGame && !exitApplication){
            if(gameEngine.isCurrentPlayerHuman()) {
                System.out.println();
                runMenu(MenuType.MAIN_MENU);
            }
            else { //current player is computer
                gameEngine.playMove(0);
            }
            gameOver = gameEngine.endGame();
        }

        if(gameOver){
            printGameWinner();
            printGameStatistics();
            endCurrentGame = true;
        }
        if(endCurrentGame){
            restartGame();
        }
    }

    private static void restartGame(){
        try{
            gameEngine.restartGame(loadedXmlFilePath);

        } catch (Exception e){
            System.out.println("An unhandled error occured");
        }
    }

    private static void printBoard(){
        int boardSize = gameEngine.getGameBoard().getSize();
        Cell currCell;

        System.out.print("    ");
        for(int i = 0; i < boardSize; i++) {
            System.out.print(String.format("%1$3s",i + 1) + "  ");
        }
            for(int i = 0; i < boardSize; i++){
            printBoardGap(boardSize);
            System.out.print(String.format("%1$-3s",i+1) + "|");
            for(int j = 0; j < boardSize; j++){
                currCell = gameEngine.getGameBoard().getCell(i,j);
                if(currCell.isCursor()){
                    System.out.print(String.format("%1$3s",'@') + " |");
                }
                else if(currCell.isEmpty()){
                    System.out.print(String.format("%1$3s",' ') + " |");
                }
                else {
                    System.out.print(String.format("%1$3s", currCell.getValue()) + " |");
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
                printBoard();
                break;
            case 3:
                printGameStatistics();
                break;
            case 4:
                gameEngine.removeCurrentPlayerFromGame();
                if(gameEngine.getNumOfPlayingPlayers() == 1){
                    endCurrentGame = true;
                    printAllPlayersResignedMessage();
                }
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
                //userChoice = scanner.nextInt();
                userChoice = getIntInput();
                userChoice--;
                if (gameEngine.isValidCell(userChoice)){
                    inputIsValid = true;
                }
            } catch (CellNumberOutOfBoundsException e){
                System.out.println(e.getMessage());
            } catch(EmptyCellException e) {
                System.out.println(e.getMessage());
            } catch(CursorCellException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("Input is invalid. Please try again.");
            } finally {
                //scanner.nextLine();
                cleanBuffer();
            }
        }
        while(!inputIsValid);

        return userChoice;
    }

    private static void printGameStatistics() {
        List<Player> players = gameEngine.getPlayers();
        List<Player> resignedPlayers = gameEngine.getResignedPlayers();

        System.out.println("Game statistics:");
        System.out.println("Players number of moves: " + gameEngine.getMovesCnt());
        System.out.println("Game duration: " + gameEngine.getTimeDuration());
        for(int i = 0 ; i < players.size() ; i++) {
            System.out.println(players.get(i).getName() + " score: " + players.get(i).getScore());
        }
        if(resignedPlayers != null){
            for(int i = 0 ; i < resignedPlayers.size() ; i++) {
                System.out.println(resignedPlayers.get(i).getName() + " score: " + resignedPlayers.get(i).getScore());
            }
        }
    }

    private static  void printGameWinner(){
        List<Player> gameWinners = gameEngine.getGameWinners();

        System.out.println();
        System.out.println("Game Over");

        if(gameWinners.size() == 1){
            System.out.println("The winner is " + gameWinners.get(0).getName() + " with score of " + gameWinners.get(0).getScore() + "!");
        }
        else {
            System.out.println("It's a Tie!");
            }

        System.out.println();
    }

    private static void printAllPlayersResignedMessage(){
        System.out.println("Game Over");
        System.out.println("The winner is " + gameEngine.getCurrentPlayerName() + " due to all other players resignment");
        printGameStatistics();
        System.out.println();
    }


}
