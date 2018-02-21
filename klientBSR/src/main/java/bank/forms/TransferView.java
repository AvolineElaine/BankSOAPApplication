package bank.forms;

import bank.commons.BankUtil;
import bank.commons.Dialogs;
import bank.services.AuthorizationException_Exception;
import bank.services.OperationException_Exception;
import bank.services.ValidationException_Exception;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class responsible for transfer parameters input window
 */

public class TransferView extends JFrame{
    private JPanel transferPanel;
    private JTextField destination;
    private JTextField amount;
    private JTextField transferTitle;
    private JButton OKButton;
    private JButton cancelButton;


    public TransferView(String source){
        super("Make transfer");
        setContentPane(transferPanel);
        pack();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    if(amount.getText() != null && (amount.getText().split("[.]")[1].length() == 2 || amount.getText().split("[,]")[1].length() == 2)) {
                        try {
                            //System.out.println(amount.getText().replaceAll("[.]", ""));
                            BankUtil.getInstance().getBankOperationSvc().makeTransfer(transferTitle.getText(), amount.getText().replaceAll("[.]", ""), source, destination.getText());
                            Dialogs.showSuccess("Transfer made successfully", transferPanel);
                            dispose();
                        } catch (AuthorizationException_Exception | OperationException_Exception | ValidationException_Exception e1) {
                            Dialogs.showException(e1.getMessage(), transferPanel);
                        }
                    }
                }catch(ArrayIndexOutOfBoundsException exception){
                        Dialogs.showException("Improper value",transferPanel);
                }

            }
        });
    }
}
