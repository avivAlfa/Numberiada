//package javafxUI;
//
//
//import generated.GameDescriptor;
//import javafx.concurrent.Task;
//
//public class loadGameTask extends Task<String> {
//
//
//    public loadGameTask() {
//    }
//
//    protected String call() throws Exception {
//        try {
//            this.updateMessage("Opening File");
//            Thread.sleep(1000);
//            GameDescriptor gameDescriptor = getGameDescriptor();
//
//            this.updateMessage("Validating");
//            Thread.sleep(1000);
//
//
//            return computerChoice;
//        } catch (Exception e) {
//            Utils.popupMessage("Error by making computer move in task", "Error", -1);
//            return null;
//        }
//    }
//}
