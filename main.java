import javax.swing.*;
import java.awt.*;

public class main {
    private static final int MIN_LENGTH = 8;
    private static final int MIN_UPPERCASE = 1;
    private static final int MIN_LOWERCASE = 1;
    private static final int MIN_DIGITS = 1;
    private static final int MIN_SPECIAL_CHARS = 1;
    private static final int AVERAGE_PASSWORD_GUESS_PER_SECOND = 10_000_000;

    public static void main(String[] args) {
        // Setup the GUI window
        JFrame frame = new JFrame("Password Strength Checker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());

        // Input field
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Enter password:"));
        JTextField passwordField = new JTextField(20);
        inputPanel.add(passwordField);

        // Output area
        JTextArea outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        // Button
        // Button
        JButton checkButton = new JButton("Check Strength");
        checkButton.setPreferredSize(new Dimension(30, 40)); // 👈 Make it smaller

        checkButton.addActionListener(e -> {
            String password = passwordField.getText();
            String result = checkPasswordStrength(password);
            outputArea.setText(result);
        });


        // Add components to frame
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(checkButton, BorderLayout.CENTER);
        frame.add(scrollPane, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    public static String checkPasswordStrength(String password) {
        StringBuilder strength = new StringBuilder();

        if (password.length() < MIN_LENGTH) {
            strength.append("Weak: Password should have at least ")
                    .append(MIN_LENGTH)
                    .append(" characters.\n");
        }

        int uppercaseCount = 0, lowercaseCount = 0, digitCount = 0, specialCharCount = 0;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) uppercaseCount++;
            else if (Character.isLowerCase(c)) lowercaseCount++;
            else if (Character.isDigit(c)) digitCount++;
            else specialCharCount++;
        }

        if (uppercaseCount < MIN_UPPERCASE)
            strength.append("Weak: Should contain at least one uppercase letter.\n");
        if (lowercaseCount < MIN_LOWERCASE)
            strength.append("Weak: Should contain at least one lowercase letter.\n");
        if (digitCount < MIN_DIGITS)
            strength.append("Weak: Should contain at least one digit.\n");
        if (specialCharCount < MIN_SPECIAL_CHARS)
            strength.append("Weak: Should contain at least one special character.\n");

        if (strength.length() == 0) {
            strength.append("Strong: Password meets all the strength criteria.\n");
        }

        int consecutive = countConsecutiveCharacters(password);
        if (consecutive > 0) {
            strength.append("Weak: Password has ")
                    .append(consecutive)
                    .append(" or more consecutive characters.\n");
        }

        if (checkPalindrome(password)) {
            strength.append("Weak: Password is a palindrome.\n");
        }

        String estimatedTime = estimateTimeToBreak(password);
        strength.append("Estimated time to break: ").append(estimatedTime).append("\n");

        return strength.toString();
    }

    private static int countConsecutiveCharacters(String password) {
        int maxConsecutive = 2;
        int count = 1;
        for (int i = 1; i < password.length(); i++) {
            if (password.charAt(i) == password.charAt(i - 1)) {
                count++;
                if (count >= maxConsecutive) return count;
            } else {
                count = 1;
            }
        }
        return 0;
    }

    private static boolean checkPalindrome(String password) {
        int n = password.length();
        for (int i = 0; i < n / 2; i++) {
            if (password.charAt(i) != password.charAt(n - i - 1)) {
                return false;
            }
        }
        return true;
    }

    private static String estimateTimeToBreak(String password) {
        long combinations = (long) Math.pow(94, password.length());
        long seconds = combinations / AVERAGE_PASSWORD_GUESS_PER_SECOND;
        long days = seconds / (24 * 3600);
        return days + " days";
    }
}
