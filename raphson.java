import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.function.Function;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class raphson extends JFrame implements ActionListener {

    private JLabel equationLabel, initialGuessLabel, errorLabel;
    private JTextField equationField, initialGuessField, errorField;
    private JButton solveButton, clearButton;
    private JTable resultTable;
    private JScrollPane resultPane;

    public raphson() {
        super("NEWTON-RAPHSON METHOD");
        setSize(1000, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize components
        equationLabel = new JLabel("Equation:");
        initialGuessLabel = new JLabel("Initial Guess:");
        errorLabel = new JLabel("Margin of Error:");
        equationField = new JTextField(20);
        initialGuessField = new JTextField(10);
        errorField = new JTextField(10);
        solveButton = new JButton("Calculate");
        clearButton = new JButton("Clear");
        resultTable = new JTable();
        resultPane = new JScrollPane(resultTable);

        // color
        equationField.setBackground(Color.LIGHT_GRAY);
        initialGuessField.setBackground(Color.LIGHT_GRAY);
        solveButton.setBackground(Color.GREEN);
        clearButton.setBackground(Color.MAGENTA);
        errorField.setBackground(Color.LIGHT_GRAY);

        // Create the table model
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Iteration");
        tableModel.addColumn("Xn");
        tableModel.addColumn("F(Xn)");
        tableModel.addColumn("F'(Xn)");
        tableModel.addColumn("Error");
        resultTable.setModel(tableModel);

        // Set column widths
        TableColumnModel columnModel = resultTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(80);
        columnModel.getColumn(1).setPreferredWidth(80);
        columnModel.getColumn(2).setPreferredWidth(80);
        columnModel.getColumn(3).setPreferredWidth(80);
        columnModel.getColumn(4).setPreferredWidth(80);

        // Add components to the content pane
        JPanel inputPanel = new JPanel();
        inputPanel.add(equationLabel);
        inputPanel.add(equationField);
        inputPanel.add(initialGuessLabel);
        inputPanel.add(initialGuessField);
        inputPanel.add(errorLabel);
        inputPanel.add(errorField);
        inputPanel.add(solveButton);
        inputPanel.add(clearButton);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(resultPane, BorderLayout.CENTER);

        add(mainPanel);

        // Add action listeners
        solveButton.addActionListener(this);
        clearButton.addActionListener(this);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == solveButton) {
            // Retrieve user inputs
            String equation = equationField.getText();
            double initialGuess = Double.parseDouble(initialGuessField.getText());
            double error = Double.parseDouble(errorField.getText());

            // Clear the result table
            DefaultTableModel tableModel = (DefaultTableModel) resultTable.getModel();
            tableModel.setRowCount(0);

            // Solve using the Newton-Raphson method
            solve(equation, initialGuess, error);
        } else if (e.getSource() == clearButton) {
            // Clear the input fields
            equationField.setText("");
            initialGuessField.setText("");
            errorField.setText("");

            // Clear the result table
            DefaultTableModel tableModel = (DefaultTableModel) resultTable.getModel();
            tableModel.setRowCount(0);
            new raphson();
            dispose();
        }
    }

    private void solve(String equation, double xn, double error) {
        DecimalFormat df = new DecimalFormat("0.0000000"); // set decimal format to #.0000
        // Initialize variables
        double fxn = 0.0;
        double fpxn = 0.0;
        double nextXn = xn;
        int iterations = 1;

        // Create function objects to evaluate the equation and its derivative
        Function<Double, Double> f = x -> evaluate(equation, x);
        Function<Double, Double> fp = x -> evaluateDerivative(equation, x);

        // Iterate until the error is within the tolerance level
        while (true) {
            // Calculate the function values and the derivative value for xn
            fxn = f.apply(xn);
            fpxn = fp.apply(xn);

            // Calculate the next approximation using the Newton-Raphson method
            nextXn = xn - (fxn / fpxn);

            // Calculate the error
            double errorValue = Math.abs(nextXn - xn);

            // Add the row to the result table
            DefaultTableModel tableModel = (DefaultTableModel) resultTable.getModel();
            tableModel.addRow(new Object[] {
                    iterations,
                    df.format(xn),
                    df.format(fxn),
                    df.format(fpxn),
                    df.format(errorValue)
            });

            // Check if the error is within the tolerance level
            if (errorValue <= error) {
                break;
            }

            // Update xn with the next approximation
            xn = nextXn;
            iterations++;
        }

        // Get the root value
        double root = xn;

        // Append the root value to the JTextArea
        JTextArea rootTextArea = new JTextArea();
        rootTextArea.append("Root: " + df.format(root) + "\n");

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

    // Helper method to evaluate the polynomial equation
    public static double evaluate(String equation, double x) {
        double result = 0.0;
        String[] terms = equation.split("(?=[-+])"); // split the equation by + or -
        for (String term : terms) {
            if (term.contains("x")) {
                String[] parts = term.split("x");
                double coeff = (parts[0].equals("-")) ? 1.0
                        : (parts[0].equals("+") || parts[0].isEmpty()) ? 1.0 : Double.parseDouble(parts[0]);
                double power = (parts.length == 1) ? 1.0 : Double.parseDouble(parts[1].substring(1));
                result += coeff * Math.pow(x, power);
            } else {
                result += Double.parseDouble(term);
            }
        }
        return result;
    }

    // Helper method to evaluate the derivative of the polynomial equation
    public static double evaluateDerivative(String equation, double x) {
        double result = 0.0;
        String[] terms = equation.split("(?=[-+])"); // split the equation by + or -
        for (String term : terms) {
            if (term.contains("x")) {
                String[] parts = term.split("x");
                double coeff = (parts[0].equals("-")) ? -1.0
                        : (parts[0].equals("+") || parts[0].isEmpty()) ? 1.0 : Double.parseDouble(parts[0]);
                double power = (parts.length == 1) ? 1.0 : Double.parseDouble(parts[1].substring(1));
                if (power != 0) {
                    result += coeff * power * Math.pow(x, power - 1);
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        new raphson();
    }
}
