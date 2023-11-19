import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public Login() {
        setTitle("Login Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        mainPanel.add(usernameLabel);
        mainPanel.add(usernameField);
        mainPanel.add(passwordLabel);
        mainPanel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!usernameField.getText().isEmpty()) {
                    dispose();
                    SwingUtilities.invokeLater(() -> {
                        ExamSystem examSystem = new ExamSystem();
                        new ExamUI(examSystem);
                    });
                }
            }
        });

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(mainPanel, BorderLayout.CENTER);
        centerPanel.add(loginButton, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(centerPanel, BorderLayout.CENTER);

        setSize(350, 170);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Login();
        });
    }
}
