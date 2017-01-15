package javafxUI;


import Exceptions.CellOutOfBoundsException;
import generated.GameDescriptor;
import javafx.concurrent.Task;
import javafx.scene.control.Label;

import javax.rmi.CORBA.Util;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

public class loadXmlFileTask extends Task<GameDescriptor> {
    private String xml_path;

    public loadXmlFileTask(String path) {
        this.xml_path = path;
    }

    protected GameDescriptor call() throws Exception {
        GameDescriptor gameDescriptor = null;
        boolean xmlContentValid = false;

        try {
            gameDescriptor = null;
            this.updateMessage("Opening File");
            Thread.sleep(1000);
            gameDescriptor = game.XML_Handler.getGameDescriptorFromXml(xml_path);
            this.updateMessage("Validating");
            Thread.sleep(1000);
            game.XML_Handler.validate(gameDescriptor);
            this.updateMessage("Starting");
            Thread.sleep(1000);
            xmlContentValid = true;

        } catch (CellOutOfBoundsException e) {
            this.updateMessage(e.getMessage());
        } catch (Exceptions.CursorCellException e) {
            this.updateMessage(e.getMessage());
        } catch (Exceptions.DuplicateCellException e) {
            this.updateMessage(e.getMessage());
        } catch (Exceptions.InvalidBoardSizeException e) {
            this.updateMessage(e.getMessage());
        } catch (Exceptions.InvalidRangeException e) {
            this.updateMessage(e.getMessage());
        } catch (Exceptions.InvalidRangeCompareToBoardSizeException e) {
            this.updateMessage(e.getMessage());
        } catch (Exceptions.InvalidRangeValuesException e) {
            this.updateMessage(e.getMessage());
        } catch (Exceptions.InvalidNumberOfColorsException e) {
            this.updateMessage(e.getMessage());
        } catch (Exceptions.InvalidNumberOfIDsException e) {
            this.updateMessage(e.getMessage());
        } catch (Exceptions.InvalidPlayerTypeException e) {
            this.updateMessage(e.getMessage());
        } catch (FileNotFoundException e) {
            this.updateMessage("File not found!");
        } catch (JAXBException e) {
            if (!xml_path.endsWith(".xml"))
                this.updateMessage("The file you asked for isn't a xml file!");
            else
                this.updateMessage("Error trying to retrieve data from XML file");
        } catch (Exception e) {
            this.updateMessage("An unhandled error occured");
        } finally {
            if(xmlContentValid)
                return gameDescriptor;
            else{
                return null;
            }
        }
    }
}
