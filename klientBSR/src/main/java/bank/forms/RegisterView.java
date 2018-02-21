package bank.forms;

import bank.commons.BankUtil;
import bank.commons.Dialogs;
import bank.services.AuthorizationException_Exception;
import bank.services.ValidationException_Exception;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterView extends JFrame{
    private JTextField firstname;
    private JTextField lastname;
    private JTextField login;
    private JPasswordField password;
    private JPanel mainPanel;
    private JButton OKButton;

    /**
     * Registration screen
     */

    public RegisterView() {
        super("The Bank");

        setContentPane(mainPanel);
        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String log = login.getText();
                String pass = password.getText();
                String firstName = firstname.getText();
                String lastName = lastname.getText();

                try {
                    BankUtil.getInstance().getAuthorizationSvc().register(log, pass, firstName, lastName);
                } catch (AuthorizationException_Exception | ValidationException_Exception ex) {
                    Dialogs.showException(ex.getMessage(), mainPanel);
                }
                Dialogs.showSuccess("Logged in successfully", mainPanel);
                dispose();
                new LoginView();
            }
        });
    }
}
