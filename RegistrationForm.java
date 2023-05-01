import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class RegistrationForm extends JDialog {
    // GUI components
    private JTextField tfName; // text field for name input
    private JTextField tfPhone; // text field for phone input
    private JTextField tfId; // text field for ID input
    private JButton btnRegister; // button to register user
    private JButton btnCancel; // button to cancel registration
    private JPanel registerPanel; // main panel for registration form

    public RegistrationForm(JFrame parent) {
        super(parent);
        setTitle("Create a new account");
        registerPanel = new JPanel();
        tfName = new JTextField(20);
        tfPhone = new JTextField(20);
        tfId = new JTextField(20);
        btnRegister = new JButton("Register");
        btnCancel = new JButton("Cancel");
//verified
        // add components to panel
        registerPanel.add(tfName);
        registerPanel.add(tfPhone);
        registerPanel.add(tfId);
        registerPanel.add(btnRegister);
        registerPanel.add(btnCancel);

        setContentPane(registerPanel); // sets the main panel as the content pane
        setMinimumSize(new Dimension(450, 474));
        setModal(true); // makes the dialog modal (blocks input to parent frame)
        setLocationRelativeTo(parent); // centers the dialog on the parent frame
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
//Verified

        // add action listeners for the register and cancel buttons
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser(); // register the user when the register button is clicked
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // close the dialog when the cancel button is clicked
            }
        });
        setVisible(true); // make the dialog visible
    }

    private void registerUser() {
        // get user inputs from text fields
        String name = tfName.getText();
        String phone = tfPhone.getText();
        String id = tfId.getText();

        // check if all fields are filled
        if (name.isEmpty() || phone.isEmpty() || id.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter all fields",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        /* check if ID number is valid */
        if (isValidNamibianID(id)) {
            // add user to database and get generated student number
            User user = addUserToDatabase(name, phone, id);
            if (user != null) {
                // registration successful, show student number to user and close dialog
                JOptionPane.showMessageDialog(this,
                        "Your student number is: " + user.studentNumber,
                        "Registration successful",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                // registration failed
                JOptionPane.showMessageDialog(this,
                        "Failed to register new user",
                        "Try again",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog("Please enter a valid Namibian ID",
                    this,
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    private boolean isValidNamibianID(String id) {

        private User addUserToDatabase (String FirstName, String LastName, String phone, Interger id){
            return null;

            // private boolean isValidPhoneNumber(String phone) {
            // checks if phone number matches Namibian phone number format
            //   return phone.matches("08[15]\\d{7}");

            // validate phone number
            if (!isValidPhoneNumber(phone)) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid phone number",
                        "Try again",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    }

// validate name
        if (!isValidName(name)) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid name",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
//valid id
        //   private boolean isValidNamibianID (String id){
        //        // checks if ID number is 11 digits long
        //        return id.matches("\\d{11}");
    }

    private User addUserToDatabase(String firstName, String lastName, String phone, String id) {
        User user = null;
        // Set the database URL, username, and password
        final String DB_URL = "jdbc:mysql://localhost/MyStore?serverTimezone=UTC";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             PreparedStatement checkStatement = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE student_number=?");
             PreparedStatement insertStatement = conn.prepareStatement("INSERT INTO users (first_name, last_name, phone, id, student_number) VALUES (?, ?, ?, ?, ?)")) {

            // Check if student number already exists in the database
            String studentNumber = generateStudentNumber();
            checkStatement.setString(1, studentNumber);
            ResultSet resultSet = checkStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            while (count > 0) {
                studentNumber = generateStudentNumber();
                checkStatement.setString(1, studentNumber);
                resultSet = checkStatement.executeQuery();
                resultSet.next();
                count = resultSet.getInt(1);
            }

            // Insert new user into the database
            insertStatement.setString(1, firstName);
            insertStatement.setString(2, lastName);
            insertStatement.setString(3, phone);
            insertStatement.setString(4, id);
            insertStatement.setString(5, studentNumber);
            int addedRows = insertStatement.executeUpdate();

            // Create User object with the inserted values
            if (addedRows > 0) {
                user = new User(firstName, lastName, phone, id, studentNumber  );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }
}
/**
 * aded this to complete code and stuff
 *
 * private User addUserToDatabase(String firstName, String lastName, String cellphone, String id, String password) {
 *         User user = null;
 *         final String DB_URL = "jdbc:mysql://localhost/MyStore?serverTimezone=UTC";
 *         final String USERNAME = "root";
 *         final String PASSWORD = "";
 *
 *         try {
 *             Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
 *
 *             String insertQuery = "INSERT INTO users (first_name, last_name, cellphone, id, password) " +
 *                     "VALUES (?, ?, ?, ?, ?)";
 *
 *             PreparedStatement preparedStatement = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
 *             preparedStatement.setString(1, firstName);
 *             preparedStatement.setString(2, lastName);
 *             preparedStatement.setString(3, cellphone);
 *             preparedStatement.setString(4, id);
 *             preparedStatement.setString(5, password);
 *
 *             int addedRows = preparedStatement.executeUpdate();
 *             if (addedRows > 0) {
 *                 user = new User();
 *                 user.firstName = firstName;
 *                 user.lastName = lastName;
 *                 user.cellphone = cellphone;
 *                 user.id = id;
 *                 user.studentNumber = generateStudentNumber(conn, preparedStatement.getGeneratedKeys().getInt(1));
 *             }
 *
 *             preparedStatement.close();
 *             conn.close();
 *
 *         } catch (SQLException e) {
 *             e.printStackTrace();
 *         }
 *         return user;
 *     }
 */
