import Exceptions.*;
import generated.GameDescriptor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import javax.swing.*;
import javax.swing.JFileChooser;
import java.io.File;
import javax.xml.bind.JAXBException;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;

public class GameController {
    private  GameEngine gameEngine;

    @FXML
    private Pane mainPane;
    @FXML
    private Label filePathLabel;
    @FXML
    private TextArea pathTxt;
    @FXML
    private Button loadFileButton;
    @FXML
    private Button startButton;
    @FXML
    private Button playMoveButton;
    @FXML
    private Label statisticsLabel;
    @FXML
    private Label messageLabel;

    public void setModel(GameEngine model) {
        this.gameEngine = model;
    }

    @FXML
    void loadFileButton_OnClick(ActionEvent event) {
        GameDescriptor gameDescriptor = getGameDescriptor();
        if(gameDescriptor != null) {
            messageLabel.setText("XML loaded!");
            gameEngine.loadGameParams();
            gameEngine.loadGameParamsFromDescriptor(gameDescriptor);
            JOptionPane.showMessageDialog(null, gameDescriptor.getGameType());
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
                gameDescriptor = XML_Handler.getGameDescriptorFromXml(xml_path);
                XML_Handler.validate(gameDescriptor);
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
}
