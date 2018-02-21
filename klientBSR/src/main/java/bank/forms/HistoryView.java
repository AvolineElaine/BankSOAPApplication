package bank.forms;

import bank.services.Account;
import bank.services.Operation;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Class responsible for a screen presenting history of operations
 */

public class HistoryView extends JFrame{
    private JList list1;
    private JPanel historyPanel;

    public HistoryView(Account.History operations){
        super("History");
        setContentPane(historyPanel);
        pack();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

        ArrayList<String> operationsList = new ArrayList<>();

        for (Operation o : operations.getOperation()){
            operationsList.add(o.getType().value()+"  "+((double)o.getAmount())/100 + "  " +o.getDate().toString());
        }
        list1.setListData(operationsList.toArray());
    }
}
