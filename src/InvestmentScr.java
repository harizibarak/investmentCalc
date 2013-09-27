import com.sun.deploy.panel.NumberDocument;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: Barak
 * Date: 19/09/13
 * Time: 11:55
 * To change this template use File | Settings | File Templates.
 */

public class InvestmentScr extends JFrame
{
    private static int DEFAULT_TEXTFIELD_WIDTH = 150;
    private static int DEFAULT_TEXTFIELD_HEIGTH = 25;
    private static int DEFAULT_LABEL_WIDTH = 120;
    private static int DEFAULT_SLIDER_WIDTH = 250;
    private static Dimension DEFAULT_BUTTON_SIZE = new Dimension(100, 25);
    private static Font DEFAULT_TEXTFIELD_FONT = new Font("Arial", Font.PLAIN, 12);
    private static Font DEFAULT_LABEL_FONT = new Font("Arial", Font.BOLD, 12);
    private static DecimalFormat yearsFormat = new DecimalFormat("##.#");

    private JButton computeButton = new JButton("חשב החזר");
    private JButton exitButton = new JButton("יציאה");
    private JButton patternButton = new JButton("הוסף תבנית");
    private JButton changingRentButton = new JButton("שכירות משתנה");
    private JSlider monthSlider = new JSlider(1, 200, 1);
    private JLabel monthNumLabel = new JLabel("1");
    private JLabel monthLabel = new JLabel("מס' חודשים שעברו:");
    private JLabel propertyLabel = new JLabel("מחיר הנכס:");
    private JLabel investmentLabel = new JLabel("גודל ההשקעה התחלתי:");
    private JLabel rentLabel = new JLabel("גודל השכירות התחלתי:");
    private JFormattedTextField propertyValueTextfield = new JFormattedTextField(NumberFormat.getCurrencyInstance());
    private JFormattedTextField investmentTextfield = new JFormattedTextField(NumberFormat.getCurrencyInstance());
    private JFormattedTextField rentTextfield = new JFormattedTextField(NumberFormat.getCurrencyInstance());
    private JTable entryPointsTable = new JTable(new DefaultTableModel(new Object[]{"בחודש", "סכום הכסף שנכנס"}, 0));
    private Map<Integer, Double> monthToRentMap = new HashMap<Integer, Double>();

    public InvestmentScr() throws HeadlessException
    {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("home.png")));
        this.setTitle("מחשבון השקעות");
        this.setSize(new Dimension(700, 250));
        this.setLocationRelativeTo(null);

        propertyValueTextfield.setValue(700000);
        investmentTextfield.setValue(175000);
        rentTextfield.setValue(3000);

        setComponentsDefaultSizeAndFont();

        JPanel propertyValuePanel = new JPanel();
        propertyValuePanel.setLayout(new BoxLayout(propertyValuePanel, BoxLayout.LINE_AXIS));
        propertyValuePanel.add(propertyLabel);
        propertyValuePanel.add(Box.createRigidArea(new Dimension(10, 10)));
        propertyValuePanel.add(propertyValueTextfield);

        JPanel investmentPanel = new JPanel();
        investmentPanel.setLayout(new BoxLayout(investmentPanel, BoxLayout.LINE_AXIS));
        investmentPanel.add(investmentLabel);
        investmentPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        investmentPanel.add(investmentTextfield);

        changingRentButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                ChangingRentDialog rentDialog = new ChangingRentDialog(InvestmentScr.this, monthToRentMap);
                monthToRentMap = rentDialog.getMonthToRentMap();
            }
        });

        JPanel rentPanel = new JPanel();
        rentPanel.setLayout(new BoxLayout(rentPanel, BoxLayout.LINE_AXIS));
        rentPanel.add(rentLabel);
        rentPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        rentPanel.add(rentTextfield);
        rentPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        rentPanel.add(changingRentButton);

        JPanel monthPanel = new JPanel();
        monthPanel.setLayout(new BoxLayout(monthPanel, BoxLayout.LINE_AXIS));
        monthPanel.add(monthLabel);
        monthPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        monthPanel.add(monthSlider);
        monthPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        monthPanel.add(monthNumLabel);
        monthSlider.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                monthNumLabel.setText(monthSlider.getValue() + " (" + yearsFormat.format(monthSlider.getValue()/(double)12) + " שנים)");
            }
        });

        computeButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                computeReturnValue();
            }
        });

        exitButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                InvestmentScr.this.dispose();
                System.exit(0);
            }
        });

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));
        rightPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        rightPanel.add(propertyValuePanel);
        rightPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        rightPanel.add(investmentPanel);
        rightPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        rightPanel.add(rentPanel);
        rightPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        rightPanel.add(monthPanel);

        JPanel mainButtonPanel = new JPanel();
        mainButtonPanel.setLayout(new BoxLayout(mainButtonPanel, BoxLayout.LINE_AXIS));
        mainButtonPanel.add(computeButton);
        mainButtonPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        mainButtonPanel.add(exitButton);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.LINE_AXIS));
        mainPanel.add(rightPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        mainPanel.add(initEntryPointsTablePanel(entryPointsTable, true));

        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
        this.getContentPane().add(mainPanel);
        this.getContentPane().add(mainButtonPanel);

        this.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
    }

    private void setComponentsDefaultSizeAndFont()
    {
        propertyValueTextfield.setPreferredSize(new Dimension(DEFAULT_TEXTFIELD_WIDTH, DEFAULT_TEXTFIELD_HEIGTH));
        investmentTextfield.setPreferredSize(new Dimension(DEFAULT_TEXTFIELD_WIDTH, DEFAULT_TEXTFIELD_HEIGTH));
        rentTextfield.setPreferredSize(new Dimension(DEFAULT_TEXTFIELD_WIDTH, DEFAULT_TEXTFIELD_HEIGTH));
        monthSlider.setPreferredSize(new Dimension(DEFAULT_SLIDER_WIDTH, DEFAULT_TEXTFIELD_HEIGTH));
        propertyValueTextfield.setMinimumSize(new Dimension(DEFAULT_TEXTFIELD_WIDTH, DEFAULT_TEXTFIELD_HEIGTH));
        investmentTextfield.setMinimumSize(new Dimension(DEFAULT_TEXTFIELD_WIDTH, DEFAULT_TEXTFIELD_HEIGTH));
        rentTextfield.setMinimumSize(new Dimension(DEFAULT_TEXTFIELD_WIDTH, DEFAULT_TEXTFIELD_HEIGTH));
        monthSlider.setMinimumSize(new Dimension(DEFAULT_SLIDER_WIDTH, DEFAULT_TEXTFIELD_HEIGTH));

        propertyLabel.setPreferredSize(new Dimension(DEFAULT_LABEL_WIDTH, DEFAULT_TEXTFIELD_HEIGTH));
        investmentLabel.setPreferredSize(new Dimension(DEFAULT_LABEL_WIDTH, DEFAULT_TEXTFIELD_HEIGTH));
        rentLabel.setPreferredSize(new Dimension(DEFAULT_LABEL_WIDTH, DEFAULT_TEXTFIELD_HEIGTH));
        monthLabel.setPreferredSize(new Dimension(DEFAULT_LABEL_WIDTH, DEFAULT_TEXTFIELD_HEIGTH));
        propertyLabel.setMinimumSize(new Dimension(DEFAULT_LABEL_WIDTH, DEFAULT_TEXTFIELD_HEIGTH));
        investmentLabel.setMinimumSize(new Dimension(DEFAULT_LABEL_WIDTH, DEFAULT_TEXTFIELD_HEIGTH));
        rentLabel.setMinimumSize(new Dimension(DEFAULT_LABEL_WIDTH, DEFAULT_TEXTFIELD_HEIGTH));
        monthLabel.setMinimumSize(new Dimension(DEFAULT_LABEL_WIDTH, DEFAULT_TEXTFIELD_HEIGTH));
        monthNumLabel.setMinimumSize(new Dimension(90, 25));
        monthNumLabel.setPreferredSize(new Dimension(90, 25));
        computeButton.setMinimumSize(DEFAULT_BUTTON_SIZE);
        computeButton.setMaximumSize(DEFAULT_BUTTON_SIZE);
        exitButton.setMinimumSize(DEFAULT_BUTTON_SIZE);
        exitButton.setMaximumSize(DEFAULT_BUTTON_SIZE);

        propertyValueTextfield.setFont(DEFAULT_TEXTFIELD_FONT);
        investmentTextfield.setFont(DEFAULT_TEXTFIELD_FONT);
        rentTextfield.setFont(DEFAULT_TEXTFIELD_FONT);
        propertyLabel.setFont(DEFAULT_LABEL_FONT);
        investmentLabel.setFont(DEFAULT_LABEL_FONT);
        rentLabel.setFont(DEFAULT_LABEL_FONT);
        monthLabel.setFont(DEFAULT_LABEL_FONT);
        monthNumLabel.setFont(DEFAULT_LABEL_FONT);
        computeButton.setFont(DEFAULT_LABEL_FONT);
        exitButton.setFont(DEFAULT_LABEL_FONT);
        changingRentButton.setFont(DEFAULT_LABEL_FONT);
        patternButton.setFont(DEFAULT_LABEL_FONT);
    }

    private void computeReturnValue()
    {
        double propValue = Double.parseDouble(propertyValueTextfield.getValue().toString());
        double investValue = Double.parseDouble(investmentTextfield.getValue().toString());
        double rentValue = Double.parseDouble(rentTextfield.getValue().toString());

        Map<Integer, Double> monthToAmountMap = getMonthToAmountMap(entryPointsTable);

        for (int i = 0; i < monthSlider.getValue(); i++)
        {
            if (monthToRentMap.containsKey(i + 1))
            {
                rentValue = monthToRentMap.get(i + 1);
            }

            investValue += (investValue/propValue)*rentValue;

            if (monthToAmountMap.containsKey(i + 1))
            {
                investValue += monthToAmountMap.get(i + 1);
            }
        }

        DecimalFormat dfRent = new DecimalFormat("#.##");
        DecimalFormat dfInvest = new DecimalFormat("###,###.##");
        String msg = "סכום השקעתך עומד על: "+ dfInvest.format(investValue) + " ש\"ח אשר מהווה כ-"+dfRent.format(investValue/propValue * 100) +
                "% מסכום הנכס ולכן אתה זכאי ל-" + dfRent.format((investValue / propValue)*rentValue) + " ש\"ח שכירות";
        JLabel msgLabel = new JLabel(msg);
        msgLabel.setFont(DEFAULT_LABEL_FONT);
        JOptionPane.showMessageDialog(InvestmentScr.this, msgLabel,"החזר השקעה" , JOptionPane.INFORMATION_MESSAGE);
    }

    public static Map<Integer, Double> getMonthToAmountMap(JTable entryPointsTable)
    {
        Map<Integer, Double> monthToAmountMap = new HashMap<Integer, Double>();

        for (int i = 0; i < entryPointsTable.getRowCount(); i++)
        {
            monthToAmountMap.put(Integer.valueOf((String)entryPointsTable.getModel().getValueAt(i, 0)),
                    Double.valueOf((String)entryPointsTable.getModel().getValueAt(i, 1)));
        }

        return monthToAmountMap;
    }

    private JPanel initEntryPointsTablePanel(final JTable entryPointsTable, boolean includePatternButton)
    {
        JScrollPane entryPointsTableScrollPane = new JScrollPane(entryPointsTable);
        entryPointsTableScrollPane.setMinimumSize(new Dimension(200, 120));
        entryPointsTableScrollPane.setPreferredSize(new Dimension(200, 120));

        final JTextField monthField = new JTextField();
        monthField.setFont(DEFAULT_TEXTFIELD_FONT);
        monthField.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        monthField.setDocument(new NumberDocument());
        entryPointsTable.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(monthField));
        entryPointsTable.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer());
        entryPointsTable.getColumnModel().getColumn(0).setCellRenderer(new EntryPointsTableCellRenderer());
        monthField.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusLost(FocusEvent e)
            {
                if (entryPointsTable.getCellEditor() != null) entryPointsTable.getCellEditor().stopCellEditing();
            }

            @Override
            public void focusGained(FocusEvent e)
            {
                monthField.selectAll();
            }
        });
        final JTextField amountField = new JTextField();
        amountField.setFont(DEFAULT_TEXTFIELD_FONT);
        amountField.setDocument(new NumberDocument());
        amountField.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        entryPointsTable.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(amountField));
        entryPointsTable.getColumnModel().getColumn(1).setCellRenderer(new EntryPointsTableCellRenderer());
        amountField.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusLost(FocusEvent e)
            {
                if (entryPointsTable.getCellEditor() != null) entryPointsTable.getCellEditor().stopCellEditing();
            }

            @Override
            public void focusGained(FocusEvent e)
            {
                amountField.selectAll();
            }
        });

        JButton plusButton = new JButton("+");
        JButton minusButton = new JButton("-");

        plusButton.setFont(DEFAULT_LABEL_FONT);
        minusButton.setFont(DEFAULT_LABEL_FONT);

        plusButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int maxMonth = 0;

                for (int i = 0; i < entryPointsTable.getRowCount(); i++)
                {
                    Integer month = Integer.valueOf(entryPointsTable.getValueAt(i, 0).toString());
                    if (maxMonth < month)
                    {
                        maxMonth = month;
                    }
                }

                ((DefaultTableModel)entryPointsTable.getModel()).addRow(new Object[]{String.valueOf(maxMonth + 1), "1"});
            }
        });

        minusButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (entryPointsTable.getSelectedRow() != -1)
                {
                    ((DefaultTableModel)entryPointsTable.getModel()).removeRow(entryPointsTable.getSelectedRow());
                }
                else if (entryPointsTable.getRowCount() > 0)
                {
                    ((DefaultTableModel)entryPointsTable.getModel()).removeRow(entryPointsTable.getRowCount() - 1);
                }
            }
        });

        patternButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                new PatternDialog(InvestmentScr.this);
            }
        });

        JPanel tableButtonPanel = new JPanel();
        tableButtonPanel.setLayout(new BoxLayout(tableButtonPanel, BoxLayout.LINE_AXIS));
        tableButtonPanel.add(plusButton);
        tableButtonPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        tableButtonPanel.add(minusButton);

        if (includePatternButton)
        {
            tableButtonPanel.add(Box.createRigidArea(new Dimension(10, 10)));
            tableButtonPanel.add(patternButton);
        }

        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.PAGE_AXIS));
        tablePanel.add(entryPointsTableScrollPane);
        tablePanel.add(Box.createRigidArea(new Dimension(10, 10)));
        tablePanel.add(tableButtonPanel);

        tablePanel.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        return tablePanel;
    }

    private class EntryPointsTableCellRenderer extends DefaultTableCellRenderer
    {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            String valueString = column == 0 ? value.toString() : NumberFormat.getCurrencyInstance().format(Double.parseDouble((String) value));
            Component component = super.getTableCellRendererComponent(table, valueString, isSelected, hasFocus, row, column);
            component.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            return component;
        }
    }

    private class ChangingRentDialog extends JDialog
    {
        private JTable entryPointRentTable = new JTable(new DefaultTableModel(new Object[]{"נכון לחודש", "שכירות בגובה"}, 0));
        private Map<Integer, Double> monthToRentMap = new HashMap<Integer, Double>();

        private ChangingRentDialog(Frame owner, Map<Integer, Double> monthToRentMap)
        {
            super(owner, true);
            this.setTitle("שכירות משתנה");
            this.setSize(400, 300);
            this.setLocationRelativeTo(null);

            JButton approveButton = new JButton("אשר");
            approveButton.setFont(DEFAULT_LABEL_FONT);
            approveButton.setPreferredSize(new Dimension(80, 25));
            approveButton.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    ChangingRentDialog.this.monthToRentMap = InvestmentScr.getMonthToAmountMap(entryPointRentTable);
                    ChangingRentDialog.this.dispose();
                }
            });

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(approveButton);

            this.getContentPane().setLayout(new BorderLayout());
            this.getContentPane().add(initEntryPointsTablePanel(entryPointRentTable, false), BorderLayout.CENTER);
            this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

            for (Integer month : monthToRentMap.keySet())
            {
                ((DefaultTableModel)entryPointRentTable.getModel()).addRow(new Object[]{month.toString(), monthToRentMap.get(month).toString()});
            }

            this.monthToRentMap = monthToRentMap;

            this.setVisible(true);
        }

        public Map<Integer, Double> getMonthToRentMap()
        {
            return monthToRentMap;
        }
    }

    private class PatternDialog extends JDialog
    {
        private PatternDialog(Frame owner)
        {
            super(owner, true);
            this.setTitle("תבנית תשלום");
            this.setSize(350, 125);
            this.setLocationRelativeTo(null);

            JLabel everyLabel = new JLabel("כל ");
            JLabel monthAmountLabel = new JLabel(" חודשים, סכום של ");
            JLabel untilLabel = new JLabel("עד חודש");
            everyLabel.setFont(DEFAULT_LABEL_FONT);
            monthAmountLabel.setFont(DEFAULT_LABEL_FONT);
            untilLabel.setFont(DEFAULT_LABEL_FONT);

            final JTextField monthField = new JTextField();
            monthField.setDocument(new NumberDocument());
            final JTextField untilField = new JTextField();
            untilField.setDocument(new NumberDocument());
            final JTextField amountField = new JTextField();
            amountField.setDocument(new NumberDocument());
            monthField.setFont(DEFAULT_TEXTFIELD_FONT);
            amountField.setFont(DEFAULT_TEXTFIELD_FONT);
            untilField.setFont(DEFAULT_TEXTFIELD_FONT);

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
            approveButton.setFont(DEFAULT_LABEL_FONT);
            approveButton.setPreferredSize(new Dimension(80, 25));
            approveButton.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    ((DefaultTableModel)entryPointsTable.getModel()).setRowCount(0);

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

            this.getContentPane().setLayout(new BorderLayout());
            this.getContentPane().add(mainPanel, BorderLayout.CENTER);
            this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
            this.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            this.setVisible(true);
        }
    }

    public static void main(String[] args)
    {
        new InvestmentScr().setVisible(true);
    }
}
