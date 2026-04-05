import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;


class Question {
    String question;
    String[] options;
    int correctAnswer;
    String imagePath;

    public Question(String question, String[] options, int correctAnswer, String imagePath) {
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.imagePath = imagePath;
    }
}

public class QuizGUI extends JFrame implements ActionListener {
    List<Question> questions;
    int currentQuestionIndex = 0;
    int score = 0;

    JLabel questionLabel, timerLabel, imageLabel;
    JRadioButton[] options = new JRadioButton[4];
    ButtonGroup optionGroup;
    JButton nextButton;

    Timer countdownTimer;
    int timeLeft = 15;

    public QuizGUI() {
        setTitle("Java Quiz App");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        loadQuestions();

        // Top: Question + Timer
        JPanel topPanel = new JPanel(new BorderLayout());
        questionLabel = new JLabel("Question will appear here");
        questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timerLabel = new JLabel("Time left: 15s", SwingConstants.RIGHT);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        topPanel.add(questionLabel, BorderLayout.WEST);
        topPanel.add(timerLabel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Center: Image + Options
        JPanel centerPanel = new JPanel(new BorderLayout());
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        centerPanel.add(imageLabel, BorderLayout.NORTH);

        JPanel optionsPanel = new JPanel(new GridLayout(4, 1));
        optionGroup = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            optionGroup.add(options[i]);
            optionsPanel.add(options[i]);
        }
        centerPanel.add(optionsPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // Bottom: Button
        nextButton = new JButton("Next");
        nextButton.addActionListener(this);
        add(nextButton, BorderLayout.SOUTH);

        displayQuestion();
        setVisible(true);
    }

    private void loadQuestions() {
        questions = new ArrayList<>();
        questions.add(new Question("1. What is the size of int in Java?",
                new String[]{"2 bytes", "4 bytes", "8 bytes", "Depends on the system"}, 1, null));
        questions.add(new Question("2. Which keyword is used to inherit a class in Java?",
                new String[]{"implement", "inherits", "extends", "super"}, 2, null));
        questions.add(new Question("3. Which method is the entry point in Java programs?",
                new String[]{"start()", "main()", "run()", "init()"}, 1, null));
        questions.add(new Question("4. Which of the following is not a Java primitive type?",
                new String[]{"int", "float", "String", "char"}, 2, null));
        questions.add(new Question("5. Identify the Java logo (image question)",
                new String[]{"Python", "Java", "C++", "Ruby"}, 1, "java_logo.png")); // Add a valid image path
    questions.add(new Question("6. Which of these is not an OOP concept in Java?",
        new String[]{"Inheritance", "Encapsulation", "Compilation", "Polymorphism"}, 2, null));

questions.add(new Question("7. What will happen if you divide a number by zero in Java?",
        new String[]{"Returns 0", "Throws ArithmeticException", "Throws NullPointerException", "Compiler error"}, 1, null));

questions.add(new Question("8. Which interface must be implemented to create a thread in Java?",
        new String[]{"Runnable", "Thread", "Callable", "EventListener"}, 0, null));

questions.add(new Question("9. Which loop guarantees the body will execute at least once?",
        new String[]{"for", "while", "do-while", "None"}, 2, null));

questions.add(new Question("10. Which keyword is used to handle exceptions in Java?",
        new String[]{"try", "throw", "catch", "All of the above"}, 3, null));

            }

    private void displayQuestion() {
        if (currentQuestionIndex >= questions.size()) {
            showResult();
            return;
        }

        Question q = questions.get(currentQuestionIndex);
        questionLabel.setText(q.question);

        for (int i = 0; i < 4; i++) {
            options[i].setText(q.options[i]);
            options[i].setSelected(false);
        }

        if (q.imagePath != null) {
            imageLabel.setIcon(new ImageIcon(q.imagePath));
        } else {
            imageLabel.setIcon(null);
        }

        timeLeft = 15;
        timerLabel.setText("Time left: " + timeLeft + "s");

        if (countdownTimer != null) countdownTimer.stop();

        countdownTimer = new Timer(1000, e -> {
            timeLeft--;
            timerLabel.setText("Time left: " + timeLeft + "s");
            if (timeLeft == 0) {
                countdownTimer.stop();
                JOptionPane.showMessageDialog(this, "⏰ Time's up! Correct: " + q.options[q.correctAnswer]);
                currentQuestionIndex++;
                displayQuestion();
            }
        });
        countdownTimer.start();
    }

    private void showResult() {
        if (countdownTimer != null) countdownTimer.stop();
        int response = JOptionPane.showConfirmDialog(this,
                "Your Score: " + score + "/" + questions.size() + "\nPlay Again?",
                "Quiz Finished", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            currentQuestionIndex = 0;
            score = 0;
            displayQuestion();
        } else {
            dispose();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Question current = questions.get(currentQuestionIndex);
        int selected = -1;
        for (int i = 0; i < 4; i++) {
            if (options[i].isSelected()) {
                selected = i;
                break;
            }
        }

        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Please select an option!");
            return;
        }

        countdownTimer.stop();

        if (selected == current.correctAnswer) {
            score++;
        } else {
            JOptionPane.showMessageDialog(this, "❌ Wrong! Correct: " + current.options[current.correctAnswer]);
        }

        currentQuestionIndex++;
        displayQuestion();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new QuizGUI());
    }
}
