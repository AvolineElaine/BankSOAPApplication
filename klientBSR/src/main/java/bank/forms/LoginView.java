package bank.forms;

import bank.commons.BankUtil;
import bank.commons.Dialogs;
import bank.services.AuthorizationException_Exception;
import bank.services.ValidationException_Exception;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Logging screen
 */

public class LoginView extends JFrame{
    private JPanel mainPanel;
    private JButton OKButton;
    private JTextField login;
    private JPasswordField password;
    private JButton registerButton;

    public LoginView(){
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

                String token = null;
                try {
                    token = BankUtil.getInstance().getAuthorizationSvc().login(log, pass);
                    BankUtil.getInstance().setToken(token);
                    Dialogs.showSuccess("Logged in successfully", mainPanel);
                    dispose();
                    new AccountView();
                } catch (AuthorizationException_Exception | ValidationException_Exception ex) {
                    Dialogs.showException(ex.getMessage(), mainPanel);
                }

            }
        });
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new RegisterView();
            }
        });
    }
}
