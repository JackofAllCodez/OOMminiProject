import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

class Question {
    String question;
    List<String> options;
    int correctOption;
    int difficulty;
    int save=0;

    public Question(String question, List<String> options, int correctOption,int difficulty) {
        this.question = question;
        this.options = options;
        this.correctOption = correctOption;
        this.difficulty = difficulty;
    }
}

class ExamSystem {
    int Marks = 0, Good = 0, Tough = 0, Complex = 0;
    List<Question> questions;
    int currentQuestionIndex;
    List<Integer> bookmarkedQuestions;

    public ExamSystem() {
        questions = new ArrayList<>();
        bookmarkedQuestions = new ArrayList<>();
        initializeQuestions();
        currentQuestionIndex = 0;
    }

    private void initializeQuestions() {
        //Good
        List<String> options = List.of("Inheritance", "Encapsulation", "Polymorphism", "Compilation");
        Question question = new Question("Q1) Which of the following is not OOPS concept in Java?", options, 3, 0);
        questions.add(question);

        options = List.of("Compile time polymorphism", "Execution time polymorphism", "Multiple polymorphism", "Multilevel polymorphism");
        question = new Question("Q2) Which of the following is a type of polymorphism in Java?", options, 0,0);
        questions.add(question);

        options = List.of("More than one method with same name but different method signature and different number or type of parameters", "More than one method with same name, same signature but different number of signature", "More than one method with same name, same signature, same number of parameters but different type", "More than one method with same name, same number of parameters and type but different signature");
        question = new Question("Q3) When does overloading not occur?", options, 3,0);
        questions.add(question);

        options = List.of("Polymorphism", "Encapsulation", "Abstraction", "Inheritance");
        question = new Question("Q4) Which concept of Java is a way of converting real world objects in terms of class?", options, 0,0);
        questions.add(question);

        //Tough
        options = List.of("Aggregation", "Composition", "Encapsulation", "Association");
        question = new Question("Q5) What is it called if an object has its own lifecycle and there is no owner?", options, 3,1);
        questions.add(question);

        options = List.of("Aggregation", "Composition", "Encapsulation", "Association");
        question = new Question("Q6) What is it called if a child object gets killed when parent object is killed?", options, 1,1);
        questions.add(question);

        options = List.of("At run time", "At compile time", "At coding time", "At execution time");
        question = new Question("Q7) When is method overloading determined?", options, 1,1);
        questions.add(question);

        //Complex
        options = List.of("Structure only holds the data, classes hold the data and functions", "The structure holds the data and functions, classes only hold the data", "The structure is the instance of the class, classes are a set of objects", "Members of structure and class can be both public and private");
        question = new Question("Q8) Which one of the following defines correct differences between structure and class?", options, 0,2);
        questions.add(question);
    
        options = List.of("Constructors cannot be referred by their address", "Constructors cannot be inherited", "Constructors can be virtual", "Constructors are called automatically");
        question = new Question("Q9) Which one of the following is not the characteristic of a constructor?", options, 2,2);
        questions.add(question);
    
        options = List.of("Member Access or Dot Operator (.)", "Scope Resolution Operator (::)", "Ternary Operator (? :)", "All of the above");
        question = new Question("Q10) Which operator is used to access the static variable and static function of a class?", options, 1,2);
        questions.add(question); 
    }

    public Question getCurrentQuestion() {
        return questions.get(currentQuestionIndex);
    }

    public void setCurrentQuestionIndex(int newIndex) {
        if (newIndex >= 0 && newIndex < questions.size()) {
            currentQuestionIndex = newIndex;
        }
    }

    public boolean isLastQuestion() {
        return currentQuestionIndex == questions.size() - 1;
    }

    public List<Integer> getSelectedOptions() {
        List<Integer> selectedOptions = new ArrayList<>();
        for (Question question : questions) {
            selectedOptions.add(question.save == 1 ? question.correctOption : -1);
        }
        return selectedOptions;
    }

    public void moveToNextQuestion() {
        if (!isLastQuestion()) {
            currentQuestionIndex++;
        }
    }

    public void bookmarkCurrentQuestion() {
        bookmarkedQuestions.add(currentQuestionIndex);
    }

    public List<Integer> getBookmarkedQuestions(){
        return bookmarkedQuestions;
    }

    public void calculateQuestionMarks(Question question) {
        if (question.difficulty == 0) { // Good Question
            Marks += 1;
            Good += 1;
        } else if (question.difficulty == 1) { // Tough Question
            Marks += 2;
            Tough += 1;
        } else { // Complex Question
            Marks += 3; 
            Complex += 1;
        }
    }
}

class ExamUI extends JFrame implements ActionListener {
    ExamSystem examSystem;

    JLabel questionLabel;
    JRadioButton[] optionButtons;
    List<JButton> buttonArray = new ArrayList<>();
    ButtonGroup buttonGroup;
    JButton nextButton;
    JButton bookmarkButton;
    JButton clearButton;
    JButton submitButton;
    JPanel submitPanel;
    JPanel optionsPanel;
    JPanel categoryPanel;
    int correct=0;
    int index=0;

    public ExamUI(ExamSystem examSystem) {
        this.examSystem = examSystem;

        setTitle("Online Examination System");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());

        initializeComponents();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(createQuestionPanel());
        splitPane.getLeftComponent().setPreferredSize(new Dimension(600, 600));
        splitPane.setRightComponent(createNavigationPanel());
        splitPane.getRightComponent().setPreferredSize(new Dimension(400, 600));
        splitPane.setDividerLocation(750);
        add(splitPane);

        displayCurrentQuestion();

        setSize(1000, 600);
        setLocationRelativeTo(null); 
        setVisible(true);
    }
   
    private void initializeComponents() {
        questionLabel = new JLabel();

        optionButtons = new JRadioButton[4];
        buttonGroup = new ButtonGroup(); //with this only one option will be selected at a time
        optionsPanel = new JPanel(new GridLayout(12, 1));

        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton();
            optionButtons[i].addActionListener(this);
            buttonGroup.add(optionButtons[i]);
            optionsPanel.add(optionButtons[i]);
        }

        nextButton = new JButton("Save and Next");
        bookmarkButton = new JButton("Bookmark");
        clearButton = new JButton("Clear Answer");
        submitButton = new JButton("Submit");

        nextButton.addActionListener(e -> {
            int optionSelected = getSelectedOption();
            if (optionSelected != -1) {
                int confirmResult = JOptionPane.showConfirmDialog(this, "Are you sure you want to save and go to the next question?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirmResult == JOptionPane.YES_OPTION) {
                    examSystem.questions.get(index).save = 1; 
                    if (optionSelected==examSystem.questions.get(index).correctOption) {
                        correct++;
                        examSystem.calculateQuestionMarks(examSystem.questions.get(index));     
                    };
                    buttonArray.get(index).setBackground(Color.GREEN);
                    buttonArray.get(index).setEnabled(false);
                    int c=0;
                    for (JButton bu : buttonArray){
                        c++;
                        if (bu.isEnabled()) {
                            c=0;
                            break;
                        }
                        if (c==buttonArray.size()) showResults();
                    }
                    goToQuestion(index + 1);
                    clearOptions(examSystem.getCurrentQuestion());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select an option.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });
        bookmarkButton.addActionListener(this);
        clearButton.addActionListener(e -> clearOptions(examSystem.getCurrentQuestion()));
        submitButton.addActionListener(e -> showResults());
    }

    private int getSelectedOption() {
        for (int i = 0; i < optionButtons.length; i++) {
            if (optionButtons[i].isSelected()) {
                return i;
            }
        }
        return -1;
    }

    private void clearOptions(Question question) {
        buttonGroup.clearSelection(); 
        for (JRadioButton button1 : optionButtons) button1.setEnabled(true);
    }

    private JPanel createQuestionPanel() {
        JPanel buttonBook=new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonBook.add(questionLabel);
        buttonBook.add(bookmarkButton);
        JPanel questionPanel = new JPanel(new BorderLayout());
        questionPanel.add(buttonBook, BorderLayout.NORTH);
        questionPanel.add(optionsPanel, BorderLayout.CENTER);
        JPanel buttons=new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttons.add(Box.createHorizontalStrut(305));
        buttons.add(clearButton);
        buttons.add(Box.createHorizontalStrut(190));
        buttons.add(nextButton);
        questionPanel.add(buttons,BorderLayout.SOUTH);
        return questionPanel;
    }

    private JPanel createNavigationPanel() {
        JPanel submitPanel = new JPanel();
        submitPanel.setLayout(new BoxLayout(submitPanel, BoxLayout.Y_AXIS));
        JLabel text = new JLabel("Good");
        submitPanel.add(Box.createHorizontalBox());
        submitPanel.add(text,BorderLayout.WEST);
        
        addCategoryPanel(submitPanel, "Good", 1, 4);
        text = new JLabel("Tough");
        submitPanel.add(text, BorderLayout.WEST); 
        addCategoryPanel(submitPanel, "Tough", 5, 7);
        text = new JLabel("Complex");
        submitPanel.add(text, BorderLayout.WEST);
        addCategoryPanel(submitPanel, "Complex", 8, 10);

        JPanel submitButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e->showResults()); 
        submitButtonPanel.add(submitButton, BorderLayout.SOUTH);

        submitPanel.add(submitButtonPanel);
        submitPanel.add(Box.createVerticalStrut(-125));

        return submitPanel;
    }

    private void addCategoryPanel(JPanel parentPanel, String category, int start, int end) {
        categoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        for (int i = start; i <= end; i++) {
            int questionIndex = i-1;
            JButton button = new JButton("" + i);
            button.addActionListener(e -> goToQuestion(questionIndex)); 
            categoryPanel.add(button);
            buttonArray.add(button);
        }
        parentPanel.add(categoryPanel);
    }

    private void goToQuestion(int questionIndex) {
        if (buttonArray != null && questionIndex >= 0 && questionIndex < buttonArray.size() && buttonArray.get(questionIndex).isEnabled()) {
            index=questionIndex;
            examSystem.setCurrentQuestionIndex(questionIndex);
            displayCurrentQuestion();
        } else {
            for (JButton bu : buttonArray) {
                if (bu.isEnabled()) {
                    String st = bu.getText();
                    int num = Integer.parseInt(st);
                    index = num-1;
                    examSystem.setCurrentQuestionIndex(num-1);
                    displayCurrentQuestion();
                    break;
                }
            }
        }
    }

    private void displayCurrentQuestion() {
        Question currentQuestion = examSystem.getCurrentQuestion();
        questionLabel.setText(currentQuestion.question);
        for (int i=0;i<10;i++){
            if (currentQuestion.question==examSystem.questions.get(i).question) index=i;
        }
        List<String> options = currentQuestion.options;
        for (int i = 0; i < 4; i++) {
            optionButtons[i].setText(options.get(i));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bookmarkButton) {
            buttonArray.get(index).setBackground(Color.BLUE);
        }else {
            for (int i = 0; i < 4; i++) {
                if (optionButtons[i].isSelected()) {
                    for (int j = 0; j < 4; j++) {
                        if (j != i) {
                            optionButtons[j].setEnabled(false);
                        }
                    }
                }
            }
        }
    }

    private void showResults() {
        int option = JOptionPane.showConfirmDialog(this,
            "Exam Completed!\n" +
                    "Total Marks: " + examSystem.Marks + "\n" +
                    "Correct: " + correct + "\n" +
                    "Good Correct: " + examSystem.Good + "\n" +
                    "Tough Correct: " + examSystem.Tough + "\n" +
                    "Complex Correct: " + examSystem.Complex +
                    "\n\nDo you want to view the solutions?",
            "Results",
            JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            Solution bookmarkFrame = new Solution(examSystem);
            bookmarkFrame.setVisible(true);
        }
        dispose();
    }

}

public class Home {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ExamSystem examSystem = new ExamSystem();
            new ExamUI(examSystem);
        });
    }
}
