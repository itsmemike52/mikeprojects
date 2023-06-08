import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.function.Function;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class Bisection extends JFrame implements ActionListener {

    private JLabel equationLabel, lowerLimitLabel, upperLimitLabel, errorLabel;
    private JTextField equationField, lowerLimitField, upperLimitField, errorField;
    private JButton solveButton, clearButton;
    private JTable resultTable;
    private JScrollPane resultPane;

    public Bisection() {
        super("BISECTION METHOD");
        setSize(1300, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize components
        equationLabel = new JLabel("Equation:");
        lowerLimitLabel = new JLabel("Lower Limit:");
        upperLimitLabel = new JLabel("Upper Limit:");
        errorLabel = new JLabel("Margin of Error:");
        equationField = new JTextField(20);
        lowerLimitField = new JTextField(8);
        upperLimitField = new JTextField(8);
        errorField = new JTextField(8);
        solveButton = new JButton("Calculate");
        clearButton = new JButton("Clear");
        resultTable = new JTable();
        resultPane = new JScrollPane(resultTable);

        // Set the background color of the "Calculate" and "Clear" button
        solveButton.setBackground(Color.GREEN);
        clearButton.setBackground(Color.MAGENTA);
        equationField.setBackground(Color.LIGHT_GRAY);
        lowerLimitField.setBackground(Color.LIGHT_GRAY);
        upperLimitField.setBackground(Color.LIGHT_GRAY);
        errorField.setBackground(Color.LIGHT_GRAY);

        // Create the table model
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Iteration");
        tableModel.addColumn("Xl");
        tableModel.addColumn("Xu");
        tableModel.addColumn("Xm");
        tableModel.addColumn("F(Xl)");
        tableModel.addColumn("F(Xu)");
        tableModel.addColumn("F(Xm)");
        tableModel.addColumn("Error");
        tableModel.addColumn("New Intervals");
        resultTable.setModel(tableModel);

        // Set column widths
        TableColumnModel columnModel = resultTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(80);
        columnModel.getColumn(1).setPreferredWidth(80);
        columnModel.getColumn(2).setPreferredWidth(80);
        columnModel.getColumn(3).setPreferredWidth(80);
        columnModel.getColumn(4).setPreferredWidth(80);
        columnModel.getColumn(5).setPreferredWidth(80);
        columnModel.getColumn(6).setPreferredWidth(80);
        columnModel.getColumn(7).setPreferredWidth(80);
        columnModel.getColumn(8).setPreferredWidth(180);

        // Add components to the content pane
        JPanel inputPanel = new JPanel();
        inputPanel.add(equationLabel);
        inputPanel.add(equationField);
        inputPanel.add(lowerLimitLabel);
        inputPanel.add(lowerLimitField);
        inputPanel.add(upperLimitLabel);
        inputPanel.add(upperLimitField);
        inputPanel.add(errorLabel);
        inputPanel.add(errorField);
        inputPanel.add(solveButton);
        inputPanel.add(clearButton);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(resultPane, BorderLayout.CENTER);

        add(mainPanel);

        // Add action listeners to the buttons
        solveButton.addActionListener(this);
        clearButton.addActionListener(this);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == solveButton) {
            solve();
        } else if (e.getSource() == clearButton) {
            clearFields();
        }
    }

    private void solve() {
        // Retrieve user inputs
        String equation = equationField.getText();
        double error = Double.parseDouble(errorField.getText());

        // Clear the result table
        DefaultTableModel tableModel = (DefaultTableModel) resultTable.getModel();
        tableModel.setRowCount(0);

        // Solve using the bisection method
        solveBisection(equation, error);
    }

    private void solveBisection(String equation, double error) {
        DecimalFormat df = new DecimalFormat("0.000000000");

        // Find the lower and upper limits using bisection method
        double lowerLimit, upperLimit;

        if (!lowerLimitField.getText().isEmpty() && !upperLimitField.getText().isEmpty()) {
            lowerLimit = Double.parseDouble(lowerLimitField.getText());
            upperLimit = Double.parseDouble(upperLimitField.getText());
        } else {
            lowerLimit = findLowerLimit(equation);
            upperLimit = findUpperLimit(equation);
        }

        // Initialize variables
        double xl = lowerLimit;
        double xu = upperLimit;
        double xm = 0.0;
        double fxl = 0.0;
        double fxu = 0.0;
        double fxm = 0.0;
        int iterations = 1;

        // Create a function object to evaluate the equation
        Function<Double, Double> f = x -> evaluate(equation, x);

        // Iterate until the error is within the tolerance level
        while (true) {
            // Calculate the function values for xl and xu
            fxl = f.apply(xl);
            fxu = f.apply(xu);
            // Calculate xm using the bisection method
            xm = (xl + xu) / 2;

            // Calculate the function value for xm
            fxm = f.apply(xm);

            // Add the row to the result table
            DefaultTableModel tableModel = (DefaultTableModel) resultTable.getModel();
            tableModel.addRow(new Object[] {
                    iterations,
                    df.format(xl),
                    df.format(xu),
                    df.format(xm),
                    df.format(fxl),
                    df.format(fxu),
                    df.format(fxm),
                    df.format(Math.abs(xu - xl)),
                    (fxm * fxl < 0) ? "[" + df.format(xl) + ", " + df.format(xm) + "]"
                            : "[" + df.format(xm) + ", " + df.format(xu) + "]"
            });

            // Check if the error is within the tolerance level
            if (Math.abs(fxm) <= error) {
                break;
            }

            // Update xl or xu based on the sign of fxm
            if (fxm * fxl < 0) {
                xu = xm;
            } else {
                xl = xm;
            }
            iterations++;
        }

        // Get the root value
        double root = xm;

        // Append the root value to the JTextArea
        JTextArea rootTextArea = new JTextArea();
        rootTextArea.append("Root: " + df.format(root) + "\n");
        rootTextArea.setBackground(Color.LIGHT_GRAY);

        // Add the JTextArea to the result panel
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.add(resultPane, BorderLayout.CENTER);
        resultPanel.add(rootTextArea, BorderLayout.SOUTH);

        // Replace the result panel in the main panel
        JPanel mainPanel = (JPanel) getContentPane().getComponent(0);
        mainPanel.remove(resultPane);
        mainPanel.add(resultPanel, BorderLayout.CENTER);

        // Refresh the main panel to reflect the changes
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private double findLowerLimit(String equation) {
        double x = 0.0;
        double f = evaluate(equation, x);

        while (f >= 0) {
            x--;
            f = evaluate(equation, x);
        }

        return x;
    }

    private double findUpperLimit(String equation) {
        double x = 0.0;
        double f = evaluate(equation, x);

        while (f <= 0) {
            x++;
            f = evaluate(equation, x);
        }

        return x;
    }

    private double evaluate(String equation, double x) {
        double result = 0.0;
        String[] terms = equation.split("(?=[-+])");
        for (String term : terms) {
            if (term.contains("x")) {
                String[] parts = term.split("x");
                double coeff = (parts[0].equals("+")) ? -1.0
                        : (parts[0].equals("-") || parts[0].isEmpty()) ? 1.0 : Double.parseDouble(parts[0]);
                double power = (parts.length == 1) ? 1.0 : Double.parseDouble(parts[1].substring(1));
                result += coeff * Math.pow(x, power);
            } else {
                result += Double.parseDouble(term);
            }
        }
        return result;
    }

    private void clearFields() {
        equationField.setText("");
        lowerLimitField.setText("");
        upperLimitField.setText("");
        errorField.setText("");

        DefaultTableModel tableModel = (DefaultTableModel) resultTable.getModel();
        tableModel.setRowCount(0);

        // Remove the JTextArea with the root value
        JPanel resultPanel = (JPanel) getContentPane().getComponent(0);
        resultPanel.remove(resultPanel.getComponentCount() - 1);

        // Replace the result panel in the main panel
        JPanel mainPanel = (JPanel) getContentPane().getComponent(0);
        mainPanel.remove(resultPanel);
        mainPanel.add(resultPane, BorderLayout.CENTER);

        // Refresh the main panel to reflect the changes
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public static void main(String[] args) {
        new Bisection();
    }
}

// 3x^4+7x^3-15x^2+5x-17
// 3x^3+3x-5