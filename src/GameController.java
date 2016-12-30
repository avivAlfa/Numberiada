import Exceptions.*;
import generated.GameDescriptor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
    private Button loadFileButton;
    @FXML
    private Button startButton;
    @FXML
    private Button playMoveButton;
    @FXML
    private Label statisticsLabel;

    public void setModel(GameEngine model) {
        this.gameEngine = model;
    }

    @FXML
    void loadFileButton_OnClick(ActionEvent event) {
        GameDescriptor gameDescriptor = getGameDescriptor();
        gameEngine.loadGameParams();
        gameEngine.loadGameParamsFromDescriptor(gameDescriptor);
        JOptionPane.showMessageDialog(null, gameDescriptor.getGameType());
    }

    public static void test(){

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

        do{
            try{
                xml_path = getPathFromDialog();
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

       // loadedXmlFilePath = xml_path;
        return gameDescriptor;
    }


    public String getPathFromDialog()
    {
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
