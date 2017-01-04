package javafxUI;

import game.Board;
import game.GameEngine;
import game.Player;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import Exceptions.*;
import generated.GameDescriptor;
import javafx.event.ActionEvent;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import javax.xml.bind.JAXBException;
import javax.swing.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GameController implements Initializable{
    private GameEngine gameEngine;
    private BoardUI gameBoardUI;
    private CellUI selectedCell = new CellUI();
    private SimpleBooleanProperty gameIsRunning;
    private SimpleBooleanProperty gameUploaded;

    @FXML
    private Pane mainPane;
    @FXML
    private AnchorPane topPane;
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
    private Label totalMovesLabel;
    @FXML
    private Label currentPlayerLabel;
    @FXML
    private TableView<?> playerScoreTable;
    @FXML
    private TableColumn<?, ?> playerColumn;
    @FXML
    private TableColumn<?, ?> scoreColumn;

/*
    public void setModel(GameEngine model) {
        this.gameEngine = model;
    }
*/

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gameIsRunning = new SimpleBooleanProperty(false);
        gameUploaded = new SimpleBooleanProperty(false);

        loadFileButton.disableProperty().bind(gameIsRunning);
        startButton.disableProperty().bind(Bindings.or(gameIsRunning, Bindings.not(gameUploaded)));
        playMoveButton.disableProperty().bind(Bindings.not(gameIsRunning));

    }

    @FXML
    void loadFileButton_OnClick(ActionEvent event) {
        GameDescriptor gameDescriptor = getGameDescriptor();
        if(gameDescriptor != null) {
            loadGameEngine(gameDescriptor);
            loadGameGrid();
            handleTurn();
            messageLabel.setText("");
            gameUploaded.setValue(true);
        } else
            gameUploaded.setValue(false);
    }

    @FXML
    void playMoveButton_OnClick(ActionEvent event) {
        CellUI cursorCellUI = gameBoardUI.getCell(gameEngine.getCursorRow(),gameEngine.getCursorCol()); //get cursor before gameEngine.playMove
        int row = GridPane.getRowIndex(selectedCell);
        int col = GridPane.getColumnIndex(selectedCell);
        gameEngine.playMove(row,col);

        cursorCellUI.updateValues();//values are updated due to gameEngine.playMove changes!
        selectedCell.updateValues();//values are updated due to gameEngine.playMove changes!
        selectedCell.setStyle("-fx-base: #ececec ");
        handleTurn();

    }

    @FXML
    void startButton_OnClick(ActionEvent event) {
        gameIsRunning.setValue(true);
    }

    @FXML
    private void cellUI_OnClick(CellUI cell){
        selectedCell.setStyle("-fx-base: #ececec ");
        selectedCell = cell;
        selectedCell.setStyle("-fx-base: #b6e7c9;");

    }

    private void handleTurn(){
        currentPlayerLabel.setText(gameEngine.getCurrentPlayerName());
        totalMovesLabel.setText(String.valueOf(gameEngine.getMovesCnt()));
        //TODO:scoretable update
        if(gameEngine.endGame()){
            handleEndGame();
        }
    }

    private void handleEndGame(){
        String endGameMessage = createEndGameMessage();
        popupMessage(endGameMessage);
        gameIsRunning.setValue(false);
        gameUploaded.setValue(false);

    }

    private String createEndGameMessage(){
        List<Player> gameWinners = gameEngine.getGameWinners();
        StringBuilder endGameMessage = new StringBuilder();
        endGameMessage.append("Game Over\n");
        if(gameWinners.size() == 1){
            endGameMessage.append("The winner is " + gameWinners.get(0).getName() + " with score of " + gameWinners.get(0).getScore() + "!\n");
        }else{
            endGameMessage.append("It's a Tie!\n");
        }

        List<Player> players = gameEngine.getPlayers();
        List<Player> resignedPlayers = gameEngine.getResignedPlayers();

        endGameMessage.append("\n");
        endGameMessage.append("Players total moves: " + gameEngine.getMovesCnt() + "\n");
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

    private void popupMessage(String msg){
        JOptionPane.showMessageDialog(null, msg);
    }

    private GameDescriptor getGameDescriptor(){
        boolean xmlPathValid = false;
        boolean xmlContentValid = false;
        String xml_path = null;
        GameDescriptor gameDescriptor = null;

//        do{
        try{
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
        } catch (InvalidRangeValuesException e){
            messageLabel.setText(e.getMessage());
            //JOptionPane.showMessageDialog(null, e.getMessage());
        } catch (InvalidPlayerTypeException e){
            messageLabel.setText(e.getMessage());
            //JOptionPane.showMessageDialog(null, e.getMessage());
        } catch(FileNotFoundException e) {
            messageLabel.setText("File not found!");
            //JOptionPane.showMessageDialog(null, "File not found!");
        } catch(JAXBException e) {
            if(!xml_path.endsWith(".xml"))
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
        return gameDescriptor;
    }


    public String getPathFromDialog() {

        final JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(null);
        String selectedPath;
        //   if (returnVal == JFileChooser.APPROVE_OPTION) {
        File file = fc.getSelectedFile();
        //This is where a real application would open the file.
//            JOptionPane.showMessageDialog(null, "Opening: " + file.getName() + ".");
//            //log.append("Opening: " + file.getName() + "." + newline);
//        } else {
//            JOptionPane.showMessageDialog(null, "Open command cancelled by user.");
//           // log.append("Open command cancelled by user." + newline);
//        }
        selectedPath = (returnVal == JFileChooser.APPROVE_OPTION) ? file.getPath() : null;
        return selectedPath;
    }

    private void loadGameEngine(GameDescriptor gameDescriptor){
        switch (gameDescriptor.getGameType()){
            case "Basic":
                gameEngine = new GameEngine(); //new BasicEngine
                break;
            case "Advance":
                //new AdvanceEngine
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
                currCell.setOnAction(e -> cellUI_OnClick((CellUI) e.getSource()));
                gameBoardUI.setCellUI(currCell, i, j);
                gameGrid.add(currCell, j, i, 1, 1);
            }
            //  gameGrid.getRowConstraints().get(i).s
        }
    }
}
