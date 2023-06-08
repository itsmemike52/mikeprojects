import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MAIN extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private JButton button1, button2;

    public MAIN() {
        setTitle("Main GUI");
        setSize(300, 150);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false); // Disable maximize button
        setBackground(new java.awt.Color(102, 255, 51));
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        setFont(new java.awt.Font("MS UI Gothic", 0, 10)); // NOI18N
        setLocation(new java.awt.Point(620, 200));

        JPanel panel = new JPanel();
        button1 = new JButton("BISECTION");
        button1.setPreferredSize(new Dimension(200, 50)); // Set button size
        button1.addActionListener(this);
        panel.add(button1);

        button2 = new JButton("NEWTON");
        button2.setPreferredSize(new Dimension(200, 50)); // Set button size
        button2.addActionListener(this);
        panel.add(button2);

        button1.setBackground(Color.GREEN);
        button2.setBackground(Color.BLUE);
        panel.setBackground(Color.GRAY);

        getContentPane().add(panel, BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button1) {
            Bisection bisection = new Bisection();
            bisection.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            bisection.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    // Handle window closed event if needed
                }
            });
            bisection.setVisible(true);
        } else if (e.getSource() == button2) {
            raphson Raphson = new raphson();
            Raphson.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            Raphson.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    // Handle window closed event if needed
                }
            });
            Raphson.setVisible(true);
        }
    }

    public static void main(String[] args) {
        MAIN main = new MAIN();
        main.setVisible(true);
    }
}
