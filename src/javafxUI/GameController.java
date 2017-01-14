package javafxUI;

import game.*;
import game.Cell;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import Exceptions.*;
import generated.GameDescriptor;
import javafx.event.ActionEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import javax.xml.bind.JAXBException;
import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GameController implements Initializable {
    private GameEngine gameEngine;
    private BoardUI gameBoardUI;
    private CellUI selectedCell = new CellUI();
    private List<Point> possibleCells;
    private List<Point> nextPlayerOpportunities;
    private SimpleBooleanProperty gameIsRunning;
    private SimpleBooleanProperty gameUploaded;
    private SimpleBooleanProperty gameEndView;
    private ObservableList<String> playerStatistics;
    private List<GamePosition> gamePositions;
    private int gamePositionIndex;

    @FXML
    private Pane mainPane;
    @FXML
    private Pane topPane;
    @FXML
    private Label filePathLabel;
    @FXML
    private TextArea pathTxt;
    @FXML
    private Button loadFileButton;
    @FXML
    private Button startButton;
    @FXML
    private Label messageLabel;
    @FXML
    private ScrollPane gameBoardPane;
    @FXML
    private GridPane gameGrid;
    @FXML
    private Pane statisticsPane;
    @FXML
    private Label statisticsLabel;
    @FXML
    private Pane buttomPane;
    @FXML
    private Button playMoveButton;
    @FXML
    private Button retireButton;
    @FXML
    private Label totalMovesLabel;
    @FXML
    private Label currentPlayerLabel;
    @FXML
    private Label playerIdLabel;
    @FXML
    private Button nextButton;
    @FXML
    private Button prevButton;
    @FXML
    private ListView<String> playersListView;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //  mainPane.styleProperty("resources/nofar.css");
        //    mainPane.getStylesheets().add("resources/nofar.css");
        gameIsRunning = new SimpleBooleanProperty(false);
        gameUploaded = new SimpleBooleanProperty(false);
        gameEndView = new SimpleBooleanProperty(false);

        loadFileButton.disableProperty().bind(gameIsRunning);
        startButton.disableProperty().bind(Bindings.or(gameIsRunning, Bindings.not(gameUploaded)));
        playMoveButton.disableProperty().bind(Bindings.not(gameIsRunning));
        pathTxt.disableProperty().bind(gameIsRunning);
        retireButton.disableProperty().bind(Bindings.not(gameIsRunning));
        nextButton.visibleProperty().bind(gameEndView);
        prevButton.visibleProperty().bind(gameEndView);

        playerStatistics = FXCollections.observableArrayList();
        playersListView.setItems(playerStatistics);

        gamePositions = new ArrayList<>();
    }

    @FXML
    void loadFileButton_OnClick(ActionEvent event) {
        resetGame();
        GameDescriptor gameDescriptor = getGameDescriptor();
        loadComponents(gameDescriptor);
    }

    private void restartGame(){
        resetGame();
        try {
            GameDescriptor gameDescriptor = game.XML_Handler.getGameDescriptorFromXml(pathTxt.getText());
            loadComponents(gameDescriptor);
        }catch(Exception e){
            Utils.popupMessage("Error", "File removed or changed",-1);
        }

    }

    private void loadComponents(GameDescriptor gameDescriptor){
        if (gameDescriptor != null) {
            loadGameEngine(gameDescriptor);
            loadGameGrid();
            loadGamePlayersList();
            updateStatistics();
            messageLabel.setText("");
            gameUploaded.setValue(true);

            //the cursor initial place
            gamePositions.add(gameEngine.getCurrentGamePos());
           // gamePositionIndex++;
        } else
            gameUploaded.setValue(false);
    }

    @FXML
    void playMoveButton_OnClick(ActionEvent event) {
        //updatePrevPossibleCells();
        CellUI cursorCellUI = gameBoardUI.getCell(gameEngine.getCursorRow(), gameEngine.getCursorCol()); //get cursor before gameEngine.playMove
        int row = GridPane.getRowIndex(selectedCell);
        int col = GridPane.getColumnIndex(selectedCell);
//        GamePosition gamePosition;
//
//        if(selectedCell.getContent() != null)
//            gamePosition = new GamePosition(gameEngine.getPlayerByIndex(gameEngine.getPlayerTurnIndex()).clonePlayer(),
//                gameEngine.cloneCurrPlayerList(), new Point(row, col), selectedCell.getContent().cloneCell(), gameEngine.getMovesCnt());
//        else
//            gamePosition = new GamePosition(gameEngine.getPlayerByIndex(gameEngine.getPlayerTurnIndex()).clonePlayer(),
//                    gameEngine.cloneCurrPlayerList(), new Point(row, col), null, gameEngine.getMovesCnt());
//
//
//        gamePositions.add(gamePosition);
        addCurrentPosition(selectedCell);

        //playAnimation(cursorCellUI, selectedCell);
        gameEngine.playMove(row, col);



        cursorCellUI.updateValues();//values are updated due to gameEngine.playMove changes!
        selectedCell.updateValues();//values are updated due to gameEngine.playMove changes!
        //selectedCell.setStyle("-fx-base: #ececec ");
        selectedCell.setStyle("-fx-text-fill: " + Colors.getColor(selectedCell.getContent().getColor()) + ";-fx-font-size: 14;font-weight: bold;-fx-base: #ececec; ");
        hideNextPlayerOpportunities();
        gameEngine.changeTurn();
        handleTurn();

    }

    private void addCurrentPosition(CellUI cell) {
        GamePosition gamePosition;
        int row = GridPane.getRowIndex(cell);
        int col = GridPane.getColumnIndex(cell);

        if(cell.getContent() != null)
            gamePosition = new GamePosition(gameEngine.getPlayerByIndex(gameEngine.getPlayerTurnIndex()).clonePlayer(),
                    gameEngine.cloneCurrPlayerList(), new Point(row, col), cell.getContent().cloneCell(), gameEngine.getMovesCnt());
        else
            gamePosition = new GamePosition(gameEngine.getPlayerByIndex(gameEngine.getPlayerTurnIndex()).clonePlayer(),
                    gameEngine.cloneCurrPlayerList(), new Point(row, col), null, gameEngine.getMovesCnt());


        gamePositions.add(gamePosition);
    }

    @FXML
    void startButton_OnClick(ActionEvent event) {
        if(!pathTxt.getText().isEmpty()) {
            restartGame();
        }
        gameIsRunning.setValue(true);
        handleTurn();

    }

    @FXML
    private void cellUI_OnClick(CellUI cell) {
        if (selectedCell.getContent() == null)
            selectedCell = cell;


        selectedCell.setStyle("-fx-text-fill: " + Colors.getColor(selectedCell.getContent().getColor()) + ";-fx-font-size: 14;font-weight: bold;-fx-base: #ececec; ");
        hideNextPlayerOpportunities();
        selectedCell = cell;
        selectedCell.setStyle("-fx-text-fill: " + Colors.getColor(selectedCell.getContent().getColor()) + ";-fx-font-size: 14;font-weight: bold;-fx-base: #add8e6; ");
        showNextPlayerOppotunities();

    }

    @FXML
    void retireButton_OnClick(ActionEvent event) {
        updatePrevPossibleCells();
        removeCurrentPlayerFromBoard();
        gameEngine.removeCurrentPlayerFromGame();
        updateStatistics();
        handleTurn();
    }

    @FXML
    void nextButton_OnClick(ActionEvent event) {
        if(gamePositionIndex < gamePositions.size() - 1) {
            gamePositionIndex++;
            showGamePosition();

            GamePosition position = gamePositions.get(gamePositionIndex - 1);
            gameEngine.setCellValue(position.getSelectedPoint(), new Cell(-999, 0, true, false));
            gameBoardUI.getCell((int)position.getSelectedPoint().getX(), (int)position.getSelectedPoint().getY()).updateValues();

            gameEngine.setCellValue(gamePositions.get(gamePositionIndex).getSelectedPoint(),
                    new Cell(999, 0,false,true));
            gameBoardUI.getCell((int) gamePositions.get(gamePositionIndex).getSelectedPoint().getX(),
                    (int) gamePositions.get(gamePositionIndex).getSelectedPoint().getY()).updateValues();
        }
    }

    @FXML
    void prevButton_OnClick(ActionEvent event) {
        if(gamePositionIndex != 0) {
            gamePositionIndex--;
            showGamePosition();

            GamePosition position = gamePositions.get(gamePositionIndex + 1);
            gameEngine.setCellValue(position.getSelectedPoint(), position.getSelectedCell());
            gameBoardUI.getCell((int)position.getSelectedPoint().getX(), (int)position.getSelectedPoint().getY()).updateValues();

            gameEngine.setCellValue(gamePositions.get(gamePositionIndex).getSelectedPoint(),
                    new Cell(999, 0, false, true));
            gameBoardUI.getCell((int) gamePositions.get(gamePositionIndex).getSelectedPoint().getX(),
                    (int) gamePositions.get(gamePositionIndex).getSelectedPoint().getY()).updateValues();
        }
    }

    private void showGamePosition() {
        GamePosition position = gamePositions.get(gamePositionIndex);
        totalMovesLabel.setText(String.valueOf(position.getTotalMoves()));
        currentPlayerLabel.setText(position.getCurrPlayer().getName());
        playerIdLabel.setText(String.valueOf(position.getCurrPlayer().getId()));
        updateStatistics(position);

    }


    private void removeCurrentPlayerFromBoard() {
        playerStatistics.remove(gameEngine.getPlayerTurnIndex());

        List<Point> allPossibleCells = gameEngine.getAllPossibleCells();

        for (Point p : allPossibleCells) {
        //     gameBoardUI.getCell((int)p.getX(), (int)p.getY()).setText("");
            gameBoardUI.getCell((int) p.getX(), (int) p.getY()).updateValues();
            //  gameBoardUI.getCell((int)p.getX(), (int)p.getY()).disableProperty().setValue(true);
        }
    }

        private void handleTurn() {
            updateStatistics();
            if (gameEngine.endGame()) {
                handleEndGame();
            }
            else{
                skipUnavailableTurns();
                if (gameEngine.isCurrentPlayerComputer()) {
                    makeComputerMove();
                }
            }

        }

    private void skipUnavailableTurns(){
        while (!gameEngine.endGame() && !possibleCellsUpdate()) {
            Utils.popupMessage("I am sorry " + gameEngine.getCurrentPlayerName() + " Unavailable numbers for you\nMy condolences\nSkip to the next player!", "Stop!", 1);
            gameEngine.changeTurn();
            updateStatistics();
        }
        if(gameEngine.endGame()){
            handleEndGame();
        }
    }


    private void makeComputerMove() {
        CellUI cursorCellUI = gameBoardUI.getCell(gameEngine.getCursorRow(), gameEngine.getCursorCol()); //get cursor before gameEngine.playMove
        ComputerMoveTask task = new ComputerMoveTask(gameEngine);
        try {
            Thread t = new Thread(task);
            t.start();

            Stage s = new Stage();
            ProgressBar progressBar = new ProgressBar();
            progressBar.progressProperty().bind(task.progressProperty());
            Scene sc = new Scene(progressBar, 500,20);
            s.titleProperty().bind(task.messageProperty());
            s.setScene(sc);
            s.initModality(Modality.APPLICATION_MODAL);
            s.show();

            task.setOnSucceeded(event -> {

                s.close();
                Point computerChoice = task.getValue();
                CellUI selectedCellByComputer = null;
                selectedCellByComputer = gameBoardUI.getCell((int) computerChoice.getX(), (int) computerChoice.getY());
                addCurrentPosition(selectedCellByComputer);
                gameEngine.playMove((int)computerChoice.getX(),(int)computerChoice.getY());


                cursorCellUI.updateValues();
                selectedCellByComputer.updateValues();
                gameEngine.changeTurn();
                handleTurn();

            });
        } catch (Exception e) {
            Utils.popupMessage("Error on making computer move", "Error", -1);
        }
    }

    private void updateStatistics() {
        currentPlayerLabel.setText(gameEngine.getCurrentPlayerName());
        playerIdLabel.setText(Integer.toString(gameEngine.getCurrentPlayerID()));
        totalMovesLabel.setText(String.valueOf(gameEngine.getMovesCnt()));
        int prevPlayerIndex = gameEngine.getPreviousPlayerIndex();
        for (int i = 0; i < playerStatistics.size(); i++) {
            playerStatistics.set(i, gameEngine.getPlayerInfo(i));
        }
        // playerStatistics.set(prevPlayerIndex, gameEngine.getPlayerInfo(prevPlayerIndex));
    }

    private void updateStatistics(GamePosition gamePosition) {
        currentPlayerLabel.setText(gamePosition.getCurrPlayer().getName());
        playerIdLabel.setText(Integer.toString(gamePosition.getCurrPlayer().getId()));
        totalMovesLabel.setText(String.valueOf(gamePosition.getTotalMoves()));
        //int prevPlayerIndex = gameEngine.getPreviousPlayerIndex();
        for (int i = 0; i < playerStatistics.size(); i++) {
            playerStatistics.set(i, gameEngine.getPlayerInfo(gamePosition.getAllPlayers().get(i)));
        }
        // playerStatistics.set(prevPlayerIndex, gameEngine.getPlayerInfo(prevPlayerIndex));
    }

    private void updatePrevPossibleCells() {
        List<Point> prevPossibleCells = gameEngine.getPossibleCells();

        for (Point p : prevPossibleCells) {
            gameBoardUI.getCell((int) p.getX(), (int) p.getY()).disableProperty().setValue(true);
        }
    }

    private boolean possibleCellsUpdate() {
        if(possibleCells != null){
            //disable previous cells
            for (Point p : possibleCells) {
                gameBoardUI.getCell((int) p.getX(), (int) p.getY()).disableProperty().setValue(true);
            }
        }

        possibleCells = gameEngine.getPossibleCells();
        //enable new possible cells
        for (Point p : possibleCells) {
            gameBoardUI.getCell((int) p.getX(), (int) p.getY()).disableProperty().setValue(false);
        }

        return possibleCells.size() != 0;
    }

    private void showNextPlayerOppotunities() {
        nextPlayerOpportunities = gameEngine.getNextPlayerOpportunities(GridPane.getRowIndex(selectedCell), GridPane.getColumnIndex(selectedCell));
        Border border = new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2)));
        for (Point p : nextPlayerOpportunities) {
            gameBoardUI.getCell((int) p.getX(), (int) p.getY()).setBorder(border);
        }
    }

    private void hideNextPlayerOpportunities() {
        if (nextPlayerOpportunities != null) {
            for (Point p : nextPlayerOpportunities) {
                gameBoardUI.getCell((int) p.getX(), (int) p.getY()).setBorder(null);
            }
        }
    }


    private void handleEndGame() {
        String endGameMessage = gameEngine.createEndGameMessage();
        Utils.popupMessage(endGameMessage, "Game Over", -1);
        //TODO: Prev Next situation
        gameEndView.setValue(true);
        gameIsRunning.setValue(false);

        selectedCell.disableProperty().setValue(true);
        gamePositionIndex = gamePositions.size() - 1;
    }

    private void resetGame() {
        gameEngine = null;
        gameBoardUI = null;
        selectedCell = new CellUI();
        possibleCells = null;
        nextPlayerOpportunities = null;
        gameIsRunning.setValue(false);
        gameUploaded.setValue(false);
        gameEndView.setValue(false);
        playerStatistics.clear();
        gamePositions = new ArrayList<>();
        gamePositionIndex = 0;

        gameGrid.getChildren().clear();
        playersListView.getItems().clear();


    }


    private GameDescriptor getGameDescriptor() {
        boolean xmlPathValid = false;
        boolean xmlContentValid = false;
        String xml_path = null;
        GameDescriptor gameDescriptor = null;

//        do{
        try {
            xml_path = getPathFromDialog();
            pathTxt.setText(xml_path);
            gameDescriptor = game.XML_Handler.getGameDescriptorFromXml(xml_path);
            game.XML_Handler.validate(gameDescriptor);
            xmlPathValid = true;
            xmlContentValid = true;

        } catch (CellOutOfBoundsException e) {
            messageLabel.setText(e.getMessage());
            // JOptionPane.showMessageDialog(null, e.getMessage());
        } catch (CursorCellException e) {
            messageLabel.setText(e.getMessage());
            // JOptionPane.showMessageDialog(null, e.getMessage());
        } catch (DuplicateCellException e) {
            messageLabel.setText(e.getMessage());
            // JOptionPane.showMessageDialog(null, e.getMessage());
        } catch (InvalidBoardSizeException e) {
            messageLabel.setText(e.getMessage());
            //JOptionPane.showMessageDialog(null, e.getMessage());
        } catch (InvalidRangeException e) {
            messageLabel.setText(e.getMessage());
            //JOptionPane.showMessageDialog(null, e.getMessage());
        } catch (InvalidRangeCompareToBoardSizeException e) {
            messageLabel.setText(e.getMessage());
            //JOptionPane.showMessageDialog(null, e.getMessage());
        } catch (InvalidRangeValuesException e) {
            messageLabel.setText(e.getMessage());
            //JOptionPane.showMessageDialog(null, e.getMessage());
        } catch (InvalidNumberOfColorsException e) {
            messageLabel.setText(e.getMessage());
        } catch (InvalidNumberOfIDsException e) {
            messageLabel.setText(e.getMessage());
        } catch (InvalidPlayerTypeException e) {
            messageLabel.setText(e.getMessage());
            //JOptionPane.showMessageDialog(null, e.getMessage());
        } catch (FileNotFoundException e) {
            messageLabel.setText("File not found!");
            //JOptionPane.showMessageDialog(null, "File not found!");
        } catch (JAXBException e) {
            if (!xml_path.endsWith(".xml"))
                messageLabel.setText("The file you asked for isn't a xml file!");
                // JOptionPane.showMessageDialog(null, "The file you asked for isn't a xml file!");
            else
                messageLabel.setText("Error trying to retrieve data from XML file");
            //JOptionPane.showMessageDialog(null, "Error trying to retrieve data from XML file");
        } catch (Exception e) {
            messageLabel.setText("An unhandled error occured");
            //JOptionPane.showMessageDialog(null, "An unhandled error occured");

        }
            /*finally {
                if(!xmlPathValid){
                    JOptionPane.showMessageDialog(null, " Please try again:");
                }
            }*/


        //}while(!xmlPathValid && !xmlContentValid);

        // loadedXmlFilePath = xml_path;
        if (xmlContentValid)
            return gameDescriptor;
        else
            return null;
    }


    public String getPathFromDialog() {
        String selectedPath = null;
        FileChooser fc = new FileChooser();
        int returnVal;
        File file = fc.showOpenDialog(new Stage());
        if (file != null) {
            selectedPath = file.getPath();
        }
        //   if (returnVal == JFileChooser.APPROVE_OPTION) {
        //File file = fc.getSelectedFile();
        //This is where a real application would open the file.
//            JOptionPane.showMessageDialog(null, "Opening: " + file.getName() + ".");
//            //log.append("Opening: " + file.getName() + "." + newline);
//        } else {
//            JOptionPane.showMessageDialog(null, "Open command cancelled by user.");
//           // log.append("Open command cancelled by user." + newline);
//        }

        return selectedPath;
    }

    private void loadGameEngine(GameDescriptor gameDescriptor) {
        switch (gameDescriptor.getGameType()) {
            case "Basic":
                gameEngine = new BasicGameEngine(); //new BasicEngine
                break;
            case "Advance":
                gameEngine = new AdvancedGameEngine();
                break;
            default:
                gameEngine = null;
        }
        gameEngine.loadGameParams();
        gameEngine.loadGameParamsFromDescriptor(gameDescriptor);
    }

    private void loadGameGrid() {
        Board gameBoard = gameEngine.getGameBoard();
        gameBoardUI = new BoardUI(new CellUI[gameBoard.getSize()][gameBoard.getSize()]);
        for (int i = 0; i < gameBoard.getSize(); i++) {
            for (int j = 0; j < gameBoard.getSize(); j++) {
                CellUI currCell = new CellUI(gameBoard.getCell(i, j));
                currCell.disableProperty().setValue(true);
                currCell.setOnAction(e -> cellUI_OnClick((CellUI) e.getSource()));

                gameBoardUI.setCellUI(currCell, i, j);
                gameGrid.add(currCell, j, i, 1, 1);
            }
            //  gameGrid.getRowConstraints().get(i).s
        }
    }

    private void loadGamePlayersList() {
        List<Player> playersList = gameEngine.getPlayers();
        for (int i = 0; i < playersList.size(); i++) {
            playerStatistics.add(i, gameEngine.getPlayerInfo(i));
        }
    }


    private void playAnimation(CellUI cursorCell, CellUI selectedCell) {
        ImageView markerImage = new ImageView(new Image(getClass().getResourceAsStream("/resources/marker.png")));
        int selectedRow = GridPane.getRowIndex(selectedCell);
        int selectedCol = GridPane.getColumnIndex(selectedCell);
        int cursorRow = GridPane.getRowIndex(cursorCell);
        int cursorCol = GridPane.getColumnIndex(cursorCell);


        //gameGrid.getChildren().add(markerImage);
        markerImage.setLayoutX(cursorCell.getLayoutX() + 12);
        markerImage.setLayoutY(cursorCell.getLayoutY() + topPane.getHeight() + 18);
        mainPane.getChildren().add(markerImage);


        //markerImage.relocate(selectedCell.translateXProperty().getValue(), selectedCell.translateYProperty().getValue());


        Timeline timeline = new Timeline();
        Duration time = new Duration(1 * 1000);
        KeyValue keyValue;
        if (selectedRow == cursorRow) {
            keyValue = new KeyValue(markerImage.translateXProperty(), selectedCell.getLayoutX() - cursorCell.getLayoutX());
        } else {
            keyValue = new KeyValue(markerImage.translateYProperty(), selectedCell.getLayoutY() - cursorCell.getLayoutY());
        }

        KeyFrame keyFrame = new KeyFrame(time, keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();

    }
}
