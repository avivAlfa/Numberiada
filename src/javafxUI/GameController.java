package javafxUI;

import game.*;
import game.Cell;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import generated.GameDescriptor;
import javafx.event.ActionEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GameController implements Initializable {
    private GameEngine gameEngine;
    private BoardUI gameBoardUI;
    private CellUI selectedCell;
    private List<Point> possibleCells;
    private List<Point> nextPlayerOpportunities;
    private SimpleBooleanProperty gameIsRunning;
    private SimpleBooleanProperty gameUploaded;
    private SimpleBooleanProperty gameEndView;
    private ObservableList<String> playerStatistics;
    private List<GamePosition> gamePositions;
    private int gamePositionIndex;
    private SimpleBooleanProperty isNextDisabled;
    private SimpleBooleanProperty isPrevDisabled;
    private StringProperty styleCssProperty;
    private ObservableList<String> styleList;

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
    @FXML
    private CheckBox animationCheckBox;
    @FXML
    private CheckBox opportunitiesCheckBox;
    @FXML
    private ChoiceBox<String> skinSelectChoiceBox;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        gameIsRunning = new SimpleBooleanProperty(false);
        gameUploaded = new SimpleBooleanProperty(false);
        gameEndView = new SimpleBooleanProperty(false);
        isNextDisabled = new SimpleBooleanProperty(true);
        isPrevDisabled = new SimpleBooleanProperty(true);

        loadFileButton.disableProperty().bind(gameIsRunning);
        startButton.disableProperty().bind(Bindings.or(gameIsRunning, Bindings.not(gameUploaded)));
        playMoveButton.disableProperty().bind(Bindings.not(gameIsRunning));
        pathTxt.disableProperty().bind(gameIsRunning);
        retireButton.disableProperty().bind(Bindings.not(gameIsRunning));
        nextButton.visibleProperty().bind(gameEndView);
        prevButton.visibleProperty().bind(gameEndView);

        nextButton.disableProperty().bind(isNextDisabled);
        prevButton.disableProperty().bind(isPrevDisabled);

        playerStatistics = FXCollections.observableArrayList();
        playersListView.setItems(playerStatistics);

        selectedCell = new CellUI();
        gamePositions = new ArrayList<>();

        opportunitiesCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                opportunitiesCheckBox.setSelected(newValue);
                if(!opportunitiesCheckBox.isSelected())
                    hideNextPlayerOpportunities();
            }
        });


        styleCssProperty = new SimpleStringProperty("default");
        styleList = FXCollections.observableArrayList("default","Aviv","Nofar");
        skinSelectChoiceBox.setValue("default");
        skinSelectChoiceBox.setItems(styleList);
        styleCssProperty.bind(skinSelectChoiceBox.getSelectionModel().selectedItemProperty());
    }
    public StringProperty getStyleCssProperty(){
        return styleCssProperty;
    }

    @FXML
    void loadFileButton_OnClick(ActionEvent event) {
        resetGame();
        loadFile();
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
        } else
            gameUploaded.setValue(false);
    }

    @FXML
    void playMoveButton_OnClick(ActionEvent event) {

        if(selectedCell.getBorder() != null) {
            CellUI cursorCellUI = gameBoardUI.getCell(gameEngine.getCursorRow(), gameEngine.getCursorCol()); //get cursor before gameEngine.playMove
            int row = GridPane.getRowIndex(selectedCell);
            int col = GridPane.getColumnIndex(selectedCell);


            if(animationCheckBox.isSelected())
                playAnimation(cursorCellUI, selectedCell);

            Cell savedCell = selectedCell.getContent().cloneCell(); //save Cell content before playMove
            gameEngine.playMove(row, col);
            addCurrentPosition(selectedCell, savedCell);

            cursorCellUI.updateValues();//values are updated due to gameEngine.playMove changes!
            selectedCell.updateValues();//values are updated due to gameEngine.playMove changes!
            clearSelected();
            hideNextPlayerOpportunities();
            gameEngine.changeTurn();
            handleTurn();
        }
        else{
            Utils.popupMessage("Please choose a cell first", "Error", -1);
        }
    }

    private void clearSelected(){
        selectedCell.setBorder(null);

    }

    private void addCurrentPosition(CellUI cell, Cell cellContent) {
        GamePosition gamePosition;
        int row = GridPane.getRowIndex(cell);
        int col = GridPane.getColumnIndex(cell);

        if(cell.getContent() != null)
            gamePosition = new GamePosition(gameEngine.getPlayerByIndex(gameEngine.getNextPlayerIndex()).clonePlayer(),
                    gameEngine.cloneCurrPlayerList(), new Point(row, col), cellContent, gameEngine.getMovesCnt());
        else
            gamePosition = new GamePosition(gameEngine.getPlayerByIndex(gameEngine.getNextPlayerIndex()).clonePlayer(),
                    gameEngine.cloneCurrPlayerList(), new Point(row, col), null, gameEngine.getMovesCnt());


        gamePositions.add(gamePosition);
    }

    @FXML
    void startButton_OnClick(ActionEvent event) {
        if(gameEndView.getValue()) {
            restartGame();
        }
        gameIsRunning.setValue(true);
        gamePositions.add(gameEngine.getCurrentGamePos());
        handleTurn();

    }

    @FXML
    private void cellUI_OnClick(CellUI cell) {
        if (selectedCell.getContent() == null)
            selectedCell = cell;

        clearSelected();

        if(opportunitiesCheckBox.isSelected())
            hideNextPlayerOpportunities();
        selectedCell = cell;
        Border border = new Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2)));
        selectedCell.setBorder(border);

        if(opportunitiesCheckBox.isSelected())
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

            if(gamePositionIndex == gamePositions.size() - 1 || gamePositionIndex == 0) {
                isNextDisabled.setValue(true);
                isPrevDisabled.setValue(false);
            } else {
                isPrevDisabled.setValue(false);
                isNextDisabled.setValue(false);
            }
            //showGamePosition();

            GamePosition position = gamePositions.get(gamePositionIndex - 1);

            if(gamePositions.get(gamePositionIndex).getResinedPoints() != null) {

                for(Point point : gamePositions.get(gamePositionIndex).getResinedPoints()) {
                    gameEngine.setCellValue(point, new Cell(-999, 0, true, false));
                    gameBoardUI.getCell((int) point.getX(), (int) point.getY()).updateValues();
                }


            } else {

                gameEngine.setCellValue(position.getSelectedPoint(), new Cell(-999, 0, true, false));
                gameBoardUI.getCell((int) position.getSelectedPoint().getX(), (int) position.getSelectedPoint().getY()).updateValues();

                gameEngine.setCellValue(gamePositions.get(gamePositionIndex).getSelectedPoint(),
                        new Cell(999, 0, false, true));
                gameBoardUI.getCell((int) gamePositions.get(gamePositionIndex).getSelectedPoint().getX(),
                        (int) gamePositions.get(gamePositionIndex).getSelectedPoint().getY()).updateValues();
            }
            showGamePosition(gamePositions.get(gamePositionIndex));
        }
    }

    @FXML
    void prevButton_OnClick(ActionEvent event) {
        if(gamePositionIndex != 0) {
            gamePositionIndex--;

            if(gamePositionIndex == gamePositions.size() - 1 || gamePositionIndex == 0) {
                isPrevDisabled.setValue(true);
                isNextDisabled.setValue(false);

            } else {
                isPrevDisabled.setValue(false);
                isNextDisabled.setValue(false);
            }



            GamePosition position = gamePositions.get(gamePositionIndex + 1);

            if(position.getResinedPoints() != null) {

                int resinedIndex = 0;
                for(Point point : position.getResinedPoints()) {

                    gameEngine.setCellValue(point, new Cell
                            (position.getResinedCells().get(resinedIndex).getValue(),
                                    position.getResinedCells().get(resinedIndex).getColor(), true, false));
                    gameBoardUI.getCell((int) point.getX(), (int) point.getY()).updateValues();
                    resinedIndex++;
                }


            } else {

                gameEngine.setCellValue(position.getSelectedPoint(), position.getSelectedCell());
                gameBoardUI.getCell((int) position.getSelectedPoint().getX(), (int) position.getSelectedPoint().getY()).updateValues();

                gameEngine.setCellValue(gamePositions.get(gamePositionIndex).getSelectedPoint(),
                        new Cell(999, 0, false, true));
                gameBoardUI.getCell((int) gamePositions.get(gamePositionIndex).getSelectedPoint().getX(),
                        (int) gamePositions.get(gamePositionIndex).getSelectedPoint().getY()).updateValues();
            }
            showGamePosition(gamePositions.get(gamePositionIndex));
        }
    }

    private void showGamePosition(GamePosition gamePosition) {
        //GamePosition position = gamePositions.get(gamePositionIndex);
        totalMovesLabel.setText(String.valueOf(gamePosition.getTotalMoves()));
        currentPlayerLabel.setText(gamePosition.getCurrPlayer().getName());
        playerIdLabel.setText(String.valueOf(gamePosition.getCurrPlayer().getId()));
        updateStatistics(gamePosition);

    }


    private void removeCurrentPlayerFromBoard() {
        playerStatistics.remove(gameEngine.getPlayerTurnIndex());

        List<Point> allPossibleCells = gameEngine.getAllPossibleCells();

       addCellsPosition(allPossibleCells);

        gameEngine.setPossibleAsEmpty(allPossibleCells);

        for (Point p : allPossibleCells) {
            gameBoardUI.getCell((int) p.getX(), (int) p.getY()).updateValues();
        }
    }

    private void addCellsPosition(List<Point> cells) {
        List<Point> newPoints = new ArrayList<>();
        List<Cell> newCells = new ArrayList<>();

        for(Point p : cells) {
            newPoints.add((Point)p.clone());
            newCells.add(gameEngine.getChosenCell((int)p.getX(), (int)p.getY()).cloneCell());
        }

        if(gamePositions.size() != 0)
            gamePositions.add(new GamePosition(gameEngine.getPlayerByIndex(gameEngine.getNextPlayerIndex()).clonePlayer(),
                                                gameEngine.cloneCurrPlayerList(),
                                            newCells,
                                             newPoints,
                                            (Point)gamePositions.get(gamePositions.size() - 1).getSelectedPoint().clone(),
                                            gamePositions.get(gamePositions.size() - 1).getSelectedCell().cloneCell(),
                                            gameEngine.getMovesCnt()));
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
            Scene sc = new Scene(progressBar, 300,30);
            s.titleProperty().bind(task.messageProperty());
            s.setScene(sc);
            s.initModality(Modality.APPLICATION_MODAL);
            s.show();

            task.setOnSucceeded(event -> {
                s.close();
                Point computerChoice = task.getValue();
                CellUI selectedCellByComputer = null;
                selectedCellByComputer = gameBoardUI.getCell((int) computerChoice.getX(), (int) computerChoice.getY());

                if(animationCheckBox.isSelected())
                    playAnimation(cursorCellUI,selectedCellByComputer);

                Cell savedCell = selectedCellByComputer.getContent().cloneCell(); //save Cell content before playMove
                gameEngine.playMove((int)computerChoice.getX(),(int)computerChoice.getY());
                addCurrentPosition(selectedCellByComputer,savedCell);
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
    }

    private void updateStatistics(GamePosition gamePosition) {
        currentPlayerLabel.setText(gamePosition.getCurrPlayer().getName());
        playerIdLabel.setText(Integer.toString(gamePosition.getCurrPlayer().getId()));
        totalMovesLabel.setText(String.valueOf(gamePosition.getTotalMoves()));
        int retinedSize = gamePosition.getAllPlayers().size() - playerStatistics.size();

        if(retinedSize >= 0)
            for (int i = 0; i< retinedSize; i++) {
                playerStatistics.add("");
            }
        else {
            playerStatistics.remove(playerStatistics.size() - 1);
        }

        for (int i = 0; i < gamePosition.getAllPlayers().size(); i++) {
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
        gameEndView.setValue(true);
        gameIsRunning.setValue(false);

        gamePositionIndex = gamePositions.size() - 1;
        gameBoardUI.disableAllCells();

        isNextDisabled.setValue(true);
        isPrevDisabled.setValue(false);
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


    public String getPathFromDialog() {
        String selectedPath = null;
        FileChooser fc = new FileChooser();
        File file = fc.showOpenDialog(new Stage());
        if (file != null) {
            selectedPath = file.getPath();
        }
        return selectedPath;
    }

    private void loadFile(){
        String xml_path = getPathFromDialog();
        loadXmlFileTask task = new loadXmlFileTask(xml_path);
        Thread t = new Thread(task);
        t.start();

        Stage loadStage = new Stage();
        StackPane loadPane = new StackPane();
        loadPane.setPadding(new Insets(10,10,10,10));
        Label status = new Label();
        status.textProperty().bind(task.messageProperty());
        ProgressBar progressBar = new ProgressBar();
        progressBar.setPrefSize(380, 30);
        progressBar.progressProperty().bind(task.progressProperty());
        loadPane.getChildren().addAll(status,progressBar);
        StackPane.setAlignment(status, Pos.TOP_LEFT);
        StackPane.setAlignment(progressBar, Pos.CENTER_LEFT);
        Scene loadScene = new Scene(loadPane, 400, 90);
        loadStage.setTitle("Loading game");
        loadStage.setScene(loadScene);
        loadStage.initModality(Modality.APPLICATION_MODAL);
        loadStage.show();

        task.setOnSucceeded(event -> {
            if(task.getValue()!=null){
                loadStage.close();
                GameDescriptor gameDescriptor = task.getValue();
                pathTxt.setText(xml_path);
                loadComponents(gameDescriptor);
            }
            else{
                loadPane.getChildren().remove(progressBar);
                Button closeButton = new Button("Ok");
                closeButton.setOnAction(e -> loadStage.close());
                loadPane.getChildren().add(closeButton);
                StackPane.setAlignment(closeButton, Pos.BOTTOM_CENTER);
            }
        });
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


        markerImage.setLayoutX(cursorCell.getLayoutX() + 12);
        markerImage.setLayoutY(cursorCell.getLayoutY() + topPane.getHeight() + 18);
        mainPane.getChildren().add(markerImage);

        Timeline timeline = new Timeline();
        Duration time = new Duration(300);
        KeyValue keyValue;
        if (selectedRow == cursorRow) {
            keyValue = new KeyValue(markerImage.translateXProperty(), selectedCell.getLayoutX() - cursorCell.getLayoutX());
        } else {
            keyValue = new KeyValue(markerImage.translateYProperty(), selectedCell.getLayoutY() - cursorCell.getLayoutY());
        }

        EventHandler onFinished = e->{mainPane.getChildren().remove(markerImage);};


        KeyFrame keyFrame = new KeyFrame(time,onFinished, keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }
}
