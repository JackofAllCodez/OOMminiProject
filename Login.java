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
                    showRulesDialog();
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

    private void showRulesDialog() {
        JTextArea rulesTextArea = new JTextArea("Rules:\n"+
                        "1. Marking Scheme: Good (1 mark), Tough (2 marks), Complex (3 marks).\n" +
                        "2. You can't close the exam unless you submit.\n" +
                        "3. Once you save a question, you cannot revisit it, and it will become green.\n" +
                        "4. If you bookmark a question, it will become blue on the button panel.\n\n" +
                        "Do you want to proceed to the exam?"
        );

        rulesTextArea.setEditable(false);
        rulesTextArea.setLineWrap(true);
        rulesTextArea.setWrapStyleWord(true);
        rulesTextArea.setSize(400,400);

        int option = JOptionPane.showConfirmDialog(
                this,
                new JScrollPane(rulesTextArea),
                "Exam Rules",
                JOptionPane.YES_NO_OPTION
        );

        if (option == JOptionPane.YES_OPTION) {
            dispose();
            SwingUtilities.invokeLater(() -> {
                ExamSystem examSystem = new ExamSystem();
                new ExamUI(examSystem);
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Login();
        });
    }
}
