package javafxUI;


import javax.swing.*;

public class Utils {

    public static void popupMessage(String msg,String type, int jOptionPane){
        JOptionPane optionPane = new JOptionPane(msg, jOptionPane);
        JDialog dialog = optionPane.createDialog(type);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }
}
