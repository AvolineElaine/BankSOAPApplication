package bank.commons;

import javax.swing.*;
import java.awt.*;

public abstract class Dialogs {

    /**
     * Shows confirmation dialog
     * @param message dialog message
     * @param component root component
     */
    public static void showSuccess(String message, Component component) {
        showAlert("Success", message, component, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Shows warning
     * @param message dialog message
     * @param component root component
     */
    public static void showException(String message, Component component) {
        showAlert("Warning", message, component, JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Shows dialog
     * @param message dialog message
     * @param component root component
     * @param title title of the dialog
     * @param messageType type of dialog's message
     */
    private static void showAlert(String message, String title, Component component, int messageType) {
        JOptionPane.showMessageDialog(component, title, message, messageType);
    }
}
