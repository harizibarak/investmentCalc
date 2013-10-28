import com.sun.deploy.panel.NumberDocument;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: Barak
 * Date: 28/10/13
 * Time: 21:24
 * To change this template use File | Settings | File Templates.
 */
public class PatternDialog extends JDialog
{
    public PatternDialog(Frame owner, final JTable entryPointsTable)
    {
        super(owner, true);
        this.setTitle("תבנית תשלום");
        this.setSize(350, 125);
        this.setLocationRelativeTo(owner);

        JLabel everyLabel = new JLabel("כל ");
        JLabel monthAmountLabel = new JLabel(" חודשים, סכום של ");
        JLabel untilLabel = new JLabel("עד חודש");

        final JTextField monthField = new JTextField();
        monthField.setDocument(new NumberDocument());
        final JTextField untilField = new JTextField();
        untilField.setDocument(new NumberDocument());
        final JTextField amountField = new JTextField();
        amountField.setDocument(new NumberDocument());

        monthField.setMinimumSize(new Dimension(25, 25));
        monthField.setPreferredSize(new Dimension(25, 25));
        monthField.setMaximumSize(new Dimension(25, 25));
        amountField.setMinimumSize(new Dimension(80, 25));
        amountField.setPreferredSize(new Dimension(80, 25));
        amountField.setMaximumSize(new Dimension(80, 25));
        untilField.setMinimumSize(new Dimension(25, 25));
        untilField.setPreferredSize(new Dimension(25, 25));
        untilField.setMaximumSize(new Dimension(25, 25));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.LINE_AXIS));
        mainPanel.add(everyLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        mainPanel.add(monthField);
        mainPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        mainPanel.add(monthAmountLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        mainPanel.add(amountField);
        mainPanel.add(Box.createRigidArea(new Dimension(10 , 10)));
        mainPanel.add(untilLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(10 , 10)));
        mainPanel.add(untilField);
        mainPanel.add(Box.createRigidArea(new Dimension(10, 10)));

        JButton approveButton = new JButton("אשר");
        approveButton.requestFocus();
        approveButton.setPreferredSize(new Dimension(80, 25));
        approveButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // Clears the table data
                ((DefaultTableModel)entryPointsTable.getModel()).setRowCount(0);

                if (monthField.getText().trim().isEmpty() || untilField.getText().trim().isEmpty())
                {
                    return;
                }

                Integer month = Integer.parseInt(monthField.getText());
                Integer until = Integer.parseInt(untilField.getText());

                for (int i = month; i <= until; i += month)
                {
                    ((DefaultTableModel)entryPointsTable.getModel()).addRow(new Object[]{String.valueOf(i), amountField.getText()});
                }

                PatternDialog.this.dispose();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(approveButton);

        this.getRootPane().setDefaultButton(approveButton);
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(mainPanel, BorderLayout.CENTER);
        this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        this.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
    }
}
