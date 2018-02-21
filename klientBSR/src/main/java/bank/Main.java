package bank;

import bank.commons.BankUtil;
import bank.forms.LoginView;

import java.net.MalformedURLException;

/**
 * Main class of the application
 */

public class Main {
    public static void main(String[] args) throws MalformedURLException {
        BankUtil.getInstance().initialize();

        LoginView loginDialog = new LoginView();
    }
}
