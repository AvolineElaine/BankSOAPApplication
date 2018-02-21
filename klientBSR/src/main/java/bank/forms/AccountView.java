package bank.forms;

import bank.commons.BankUtil;
import bank.commons.Dialogs;
import bank.services.Account;
import bank.services.AuthorizationException_Exception;
import bank.services.OperationException_Exception;
import bank.services.User;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Main screen of application - overview of account
 */

public class AccountView extends JFrame{
    private User currentUser;
    private ArrayList<Account> bankAccounts;
    private JButton addAccountButton;
    private JButton deleteAccountButton;
    private JButton makeTransfer;
    private JButton withdraw;
    private JButton deposit;
    private JList list1;
    private JButton history;
    private JButton logout;
    private JPanel mainPanel;
    private JLabel userLabel;
    private JTextField balance;
    private JButton refresh;



    public AccountView() {
        super("The Bank");

        setContentPane(mainPanel);
        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        try {
            User user = BankUtil.getInstance().getAuthorizationSvc().getCurrentUser();
            userLabel.setText(user.getUsername());
        } catch (AuthorizationException_Exception e) {
            Dialogs.showException(e.getMessage(), mainPanel);
        }
        ;

        updateAccounts();

        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    BankUtil.getInstance().getAuthorizationSvc().logout();
                } catch (AuthorizationException_Exception e1) {
                    Dialogs.showException(e1.getMessage(), mainPanel);
                }
                dispose();
                new LoginView();
            }
        });
        addAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    BankUtil.getInstance().getAddAccountSvc().addBankAccount();
                } catch (AuthorizationException_Exception e1) {
                    Dialogs.showException(e1.getMessage(), mainPanel);
                }
                updateAccounts();
            }
        });
        deleteAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    System.out.println(list1.getSelectedValue().toString());
                    //for(Account a: BankUtil.getInstance().getAuthorizationSvc().getCurrentUser().getAccounts()){
                    //    System.out.println(a.getNumber());
                    //}
                    BankUtil.getInstance().getDeleteAccountSvc().deleteBankAccount(list1.getSelectedValue().toString());
                } catch (AuthorizationException_Exception e1) {
                    Dialogs.showException(e1.getMessage(), mainPanel);
                }
                ;
                updateAccounts();
            }
        });
        deposit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String number = list1.getSelectedValue().toString();
                String s = (String) JOptionPane.showInputDialog(mainPanel, "Amount:", "Deposit",
                        JOptionPane.PLAIN_MESSAGE,
                        null, null, "");
                try {
                    if (s != null && (s.split("[.]")[1].length() == 2 || s.split("[,]")[1].length() == 2)) {
                        try {
                            System.out.println(s.split("[.]")[1]);
                            BankUtil.getInstance().getBankOperationSvc().makeDeposit((int) (Double.parseDouble(s) * 100), number);
                        } catch (AuthorizationException_Exception | OperationException_Exception e1) {
                            Dialogs.showException(e1.getMessage(), mainPanel);
                        }

                        updateAccounts();
                    }
                }catch(ArrayIndexOutOfBoundsException exception){
                    Dialogs.showException("Improper value", mainPanel);
                }

            }
        });
        withdraw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String number = list1.getSelectedValue().toString();
                String s = (String) JOptionPane.showInputDialog(mainPanel, "Amount:", "Withdraw",
                        JOptionPane.PLAIN_MESSAGE,
                        null, null, "");
                try {
                    if (s != null && (s.split("[.]")[1].length() == 2 || s.split("[,]")[1].length() == 2)){
                        try {
                            BankUtil.getInstance().getBankOperationSvc().makeWithdraw((int) (Double.parseDouble(s) * 100), number);
                        } catch (AuthorizationException_Exception | OperationException_Exception e1) {
                            Dialogs.showException(e1.getMessage(), mainPanel);
                        }
                        updateAccounts();
                    }
                }
                catch(ArrayIndexOutOfBoundsException exception){
                    Dialogs.showException("Improper value", mainPanel);

                }
            }
        });
        makeTransfer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ListSelectionModel lsm = list1.getSelectionModel();
                if (!lsm.isSelectionEmpty()) {
                    //Account account = bankAccounts.stream().filter(bankAccount -> bankAccount.getNumber().equals(list1.getSelectedValue())).findFirst().get();
                    new TransferView(list1.getSelectedValue().toString());
                    //updateAccounts();
                }

            }

        });
        history.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ListSelectionModel lsm = list1.getSelectionModel();
                if (!lsm.isSelectionEmpty()) {
                    Account account = bankAccounts.stream().filter(bankAccount -> bankAccount.getNumber().equals(list1.getSelectedValue())).findFirst().get();
                    new HistoryView(account.getHistory());
                }
                list1.clearSelection();
            }
        });
        list1.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                //updateAccounts();
                ListSelectionModel lsm = list1.getSelectionModel();
                if (!lsm.isSelectionEmpty()) {
                    Account account = bankAccounts.stream().filter(bankAccount -> bankAccount.getNumber().equals(list1.getSelectedValue())).findFirst().get();
                    balance.setText(String.valueOf((double)account.getBalance()/100));
                }
            }
        });
        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateAccounts();
            }
        });
    }


    private void updateAccounts(){
        list1.clearSelection();
        ArrayList<String> accountsList = new ArrayList<>();
        try {
            bankAccounts = (ArrayList<Account>) BankUtil.getInstance().getAuthorizationSvc().getCurrentUser().getAccounts();
        } catch (AuthorizationException_Exception e) {
            Dialogs.showException(e.getMessage(), mainPanel);
        }
        for (Account a : bankAccounts){
            accountsList.add(a.getNumber());
        }
        list1.setListData(accountsList.toArray());

    }

    private void getCurrentUser() {
        try {
            currentUser = BankUtil.getInstance().getAuthorizationSvc().getCurrentUser();
        } catch (AuthorizationException_Exception e) {
            Dialogs.showException(e.getMessage(), this);
        }
    }
}
