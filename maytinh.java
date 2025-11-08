
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class maytinh extends JFrame implements ActionListener {

    private final JTextField displayField;
    private StringBuilder currentInput;
    private double num1 = 0;
    private double num2 = 0;
    private char operator = ' ';
    private boolean justCalculated = false;

    public maytinh() {
        super("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        currentInput = new StringBuilder();

        // Main panel sử dụng BorderLayout để chia màn hình hiển thị và nút
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Display panel (JPanel chứa JTextField)
        JPanel displayPanel = new JPanel(new BorderLayout());
        displayField = new JTextField();
        displayField.setHorizontalAlignment(JTextField.RIGHT);
        displayField.setEditable(false);
        displayField.setFont(new Font("Arial", Font.BOLD, 28));
        displayField.setPreferredSize(new Dimension(260, 60));
        displayPanel.add(displayField, BorderLayout.CENTER);
        mainPanel.add(displayPanel, BorderLayout.NORTH);

        // Buttons panel (JPanel với GridLayout)
        JPanel buttonPanel = new JPanel(new GridLayout(5, 4, 8, 8));

        
       

        String[] buttons = {
            "7", "8", "9", "/",
            "4", "5", "6", "x",
            "1", "2", "3", "-",
            "0", ".", "+", "="
        };

        for (String text : buttons) {
            if (text.isEmpty()) {
                buttonPanel.add(new JLabel());
                continue;
            }
            JButton btn = new JButton(text);
            btn.setFont(new Font("Arial", Font.BOLD, 20));
            btn.addActionListener(this);
            buttonPanel.add(btn);
        }

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
        pack();
        setResizable(false);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        // Clear
        if (cmd.equals("C")) {
            currentInput.setLength(0);
            num1 = num2 = 0;
            operator = ' ';
            justCalculated = false;
            displayField.setText("");
            return;
        }

        // Backspace
        if (cmd.equals("⌫")) {
            if (currentInput.length() > 0) {
                currentInput.deleteCharAt(currentInput.length() - 1);
                displayField.setText(currentInput.toString());
            }
            return;
        }

        // Digit or dot
        if ("0123456789.".contains(cmd)) {
            if (justCalculated) { // nếu vừa tính xong và nhập số thì bắt đầu số mới
                currentInput.setLength(0);
                justCalculated = false;
                operator = ' ';
                num1 = 0;
                num2 = 0;
            }
            // ngăn nhập nhiều dấu '.' 
            if (cmd.equals(".") && currentInput.toString().contains(".")) return;
            currentInput.append(cmd);
            displayField.setText(currentInput.toString());
            return;
        }

        // Operator (+ - x /)
        if ("+-x/".contains(cmd)) {
            // Nếu chưa có input hiện tại, cho phép đổi operator (vd: 5 + - -> cập nhật operator)
            if (currentInput.length() == 0) {
                operator = cmd.charAt(0);
                return;
            }

            try {
                num1 = Double.parseDouble(currentInput.toString());
            } catch (NumberFormatException ex) {
                displayField.setText("Error");
                currentInput.setLength(0);
                return;
            }

            operator = cmd.charAt(0);
            currentInput.setLength(0);
            justCalculated = false;
            return;
        }

        // Equals
        if (cmd.equals("=")) {
            // nếu chưa có operator hoặc chưa có input ở num2 thì không làm gì
            if (operator == ' ') return;

            if (currentInput.length() == 0) {
                // nếu người dùng nhấn '=' mà không nhập num2, dùng num2 = num1 (ví dụ 5 + = -> 10)
                num2 = num1;
            } else {
                try {
                    num2 = Double.parseDouble(currentInput.toString());
                } catch (NumberFormatException ex) {
                    displayField.setText("Error");
                    currentInput.setLength(0);
                    return;
                }
            }

            double result;
            switch (operator) {
                case '+': result = num1 + num2; break;
                case '-': result = num1 - num2; break;
                case 'x': result = num1 * num2; break;
                case '/':
                    if (num2 == 0) {
                        displayField.setText("Cannot divide by 0");
                        currentInput.setLength(0);
                        return;
                    }
                    result = num1 / num2; break;
                default:
                    return;
            }

            // Hiển thị: nếu là số nguyên (ví dụ 5.0) thì bỏ .0
            if (result == (long) result) {
                displayField.setText(String.valueOf((long) result));
            } else {
                displayField.setText(String.valueOf(result));
            }

            // chuẩn bị cho phép tiếp tục phép toán với kết quả
            num1 = result;
            currentInput.setLength(0);
            justCalculated = true;
            return;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new maytinh());
    }
}