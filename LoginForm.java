import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginForm extends JDialog {
    // Declare components
    private JTextField tfStudentNumber;
    private JPasswordField pfPassword;
    private JButton btnOK;
    private JButton btnCancel;
    private JPanel loginPanel;

    // Constructor
    public LoginForm(JFrame parent) {
        super(parent);
        setTitle("Login");
        setContentPane(loginPanel);
        setMinimumSize(new Dimension(450, 474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Add action listener to OK button
        btnOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // Get student number and password from text fields
                String studentNumber = tfStudentNumber.getText();
                String password = String.valueOf(pfPassword.getPassword());

                // Authenticate user with student number and password
                user = getAuthenticatedUser(studentNumber, password);

                if (user != null) {
                    // If authentication is successful, close login form
                    dispose();
                }
                else {
                    // If authentication fails, show error message
                    JOptionPane.showMessageDialog(LoginForm.this,
                            "Student Number or Password Invalid",
                            "Try again",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Add action listener to Cancel button
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close login form
                dispose();
            }
        });

        setVisible(true);
    }

    public User user;
    // Method to authenticate user with student number and password
    private User getAuthenticatedUser(String studentNumber, String password) {
        User user = null;

        // Connect to database
        final String DB_URL = "jdbc:mysql://localhost/MyStore?serverTimezone=UTC";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try{
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            // Prepare SQL statement to select user with given student number and password
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM users WHERE student_number=? AND password=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, studentNumber);
            preparedStatement.setString(2, password);

            // Execute SQL statement and get result set
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // If result set is not empty, create new user object with data from result set
                user = new User();
                user.name = resultSet.getString("name");
                user.studentNumber = resultSet.getString("student_number");
                user.phone = resultSet.getString("phone");
                user.address = resultSet.getString("address");
                user.password = resultSet.getString("password");
            }

            // Close statement and connection
            stmt.close();
            conn.close();

        }catch(Exception e){
            e.printStackTrace();
        }

        return user;
    }

    public static void main(String[] args) {
        // Create login form and get authenticated user object
        LoginForm loginForm = new LoginForm(null);
        User user = loginForm.user;
        if (user != null) {
            // If authentication is successful, print user data
            System.out.println("Successful Authentication of: " + user.name);
            System.out.println("  Student Number: " + user.studentNumber);
            System.out.println("          Phone: " + user.phone);
            System.out.println("         Address: " + user.address);
        }
        else {
            // If authentication fails, print error message
            System.out.println("Authentication canceled");
        }
    }
}
