package javafxUI;

import game.Board;
import game.GameEngine;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import Exceptions.*;
import generated.GameDescriptor;
import javafx.event.ActionEvent;
import javafxUI.CellUI;

import java.io.File;
import java.io.FileNotFoundException;
import javax.xml.bind.JAXBException;
import javax.swing.*;

public class GameController {
    private GameEngine gameEngine;
    private BoardUI gameBoardUI;

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
    @FXML
    void loadFileButton_OnClick(ActionEvent event) {
        GameDescriptor gameDescriptor = getGameDescriptor();
        if(gameDescriptor != null) {
            loadGameEngine(gameDescriptor);
            loadGameGrid();
            bindComponents();
            messageLabel.setText("");
            startButton.setDisable(false);

        } else
            startButton.setDisable(true);
    }

    @FXML
    void playMoveButton_OnClick(ActionEvent event) {

    }

    @FXML
    void startButton_OnClick(ActionEvent event) {

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

    private void loadGameGrid(){
        Board gameBoard = gameEngine.getGameBoard();
        gameBoardUI = new BoardUI(new CellUI[gameBoard.getSize()][gameBoard.getSize()]);
        for(int i = 0; i <gameBoard.getSize(); i++){
            for(int j = 0; j < gameBoard.getSize(); j++){
                CellUI currCell = new CellUI(gameBoard.getCell(i,j));
                //currCell.setOnAction(event -> gameEngine.playMove(i,j));
                gameBoardUI.setCellUI(currCell,i,j);
                gameGrid.add(currCell,i,j,1,1);
            }
          //  gameGrid.getRowConstraints().get(i).s
        }
    }

    private void bindComponents() {
        totalMovesLabel.textProperty().bind(new SimpleIntegerProperty(gameEngine.getMovesCnt()).asString());
        currentPlayerLabel.textProperty().bind(new SimpleStringProperty(gameEngine.getCurrentPlayerName()));
        //TODO: view and bind player score table

    }

}
