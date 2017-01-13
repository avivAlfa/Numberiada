package javafxUI;

import game.GameEngine;
import javafx.concurrent.Task;

import java.awt.*;

public class ComputerMoveTask extends Task<Point> {
    private GameEngine gameEngine;

    public ComputerMoveTask(GameEngine _gameEngine) {
        this.gameEngine = _gameEngine;
    }

    protected Point call() throws Exception {
        this.updateMessage("Making computer move");
        Thread.sleep(1000);

        try {
            Point computerChoice = gameEngine.getComputerChosenCellIndexes();
            gameEngine.playMove((int)computerChoice.getX(),(int)computerChoice.getY());

            return computerChoice;
        } catch (Exception e) {
            Utils.popupMessage("Error by making computer move in task", "Error", -1);
            return null;
        }
    }
}
