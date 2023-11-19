import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Solution extends JFrame {
    private ExamSystem examSystem;
    private int currentIndex;

    private JLabel questionLabel;
    private JButton prevButton;
    private JButton nextButton;

    public Solution(ExamSystem examSystem) {
        this.examSystem = examSystem;
        currentIndex = 0;

        setTitle("Solutions");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initializeComponents();

        add(createQuestionPanel(), BorderLayout.CENTER);
        add(createNavigationPanel(), BorderLayout.SOUTH);

        displayCurrentQuestion();

        setSize(600, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeComponents() {
        questionLabel = new JLabel();

        prevButton = new JButton("Previous");
        nextButton = new JButton("Next");

        prevButton.addActionListener(this::goToPreviousQuestion);
        nextButton.addActionListener(this::goToNextQuestion);
    }

    private JPanel createQuestionPanel() {
        JPanel questionPanel = new JPanel(new BorderLayout());
        questionPanel.add(questionLabel, BorderLayout.NORTH);
        return questionPanel;
    }

    private JPanel createNavigationPanel() {
        JPanel navigationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        navigationPanel.add(prevButton);
        navigationPanel.add(nextButton);
        return navigationPanel;
    }

    private void displayCurrentQuestion() {
        if (currentIndex < examSystem.questions.size()) {
            Question currentQuestion = examSystem.questions.get(currentIndex);
            questionLabel.setText(currentQuestion.question);

            JPanel questionPanel = createQuestionPanel();
            JPanel ansPanel = new JPanel(new GridLayout(12, 1));
            JLabel ansLabel = new JLabel("Correct Answer: ");
            int correctOptionIndex = currentQuestion.correctOption;
            ansLabel.setText("Correct Answer: " + currentQuestion.options.get(correctOptionIndex));
            ansPanel.add(ansLabel);
            questionPanel.add(ansPanel);
            getContentPane().removeAll();
            getContentPane().add(questionPanel, BorderLayout.CENTER);
            getContentPane().add(createNavigationPanel(), BorderLayout.SOUTH);

            revalidate();
            repaint();
        } else {
            questionLabel.setText("No more questions.");
        }
    }

    private void goToPreviousQuestion(ActionEvent e) {
        if (currentIndex > 0) {
            currentIndex--;
            displayCurrentQuestion();
        }
    }

    private void goToNextQuestion(ActionEvent e) {
        if (currentIndex < examSystem.questions.size() - 1) {
            currentIndex++;
            displayCurrentQuestion();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ExamSystem examSystem = new ExamSystem();
            new Solution(examSystem);
        });
    }
}
