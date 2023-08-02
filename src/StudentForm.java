import javax.swing.*;
import javax.swing.plaf.nimbus.State;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;


class Department {
    private String id;
    private String name;

    public Department(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + " (" + id + ")";
    }
}

public class StudentForm extends JFrame {
    // GUI Components
    private JTextField studentNumberField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField phoneNumberField;
    private JTextField birthdateField;
    private JTextField streetField;
    private JTextField zipcodeField;
    private JComboBox<Department> departmentComboBox;
    private JButton btnSubmit;
    private JButton btnView;

    public StudentForm() {
        // Set up the frame
        setTitle("Student Information Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 300);
        setLocationRelativeTo(null);

        // Create a panel to hold the form components
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(9, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add labels and text fields for each student information
        panel.add(new JLabel("Student Number:"));
        studentNumberField = new JTextField(20);
        panel.add(studentNumberField);

        panel.add(new JLabel("First Name:"));
        firstNameField = new JTextField(20);
        panel.add(firstNameField);

        panel.add(new JLabel("Last Name:"));
        lastNameField = new JTextField(20);
        panel.add(lastNameField);

        panel.add(new JLabel("Phone Number:"));
        phoneNumberField = new JTextField(20);
        panel.add(phoneNumberField);

        panel.add(new JLabel("Birthdate:"));
        birthdateField = new JTextField(20);
        panel.add(birthdateField);

        panel.add(new JLabel("Street:"));
        streetField = new JTextField(20);
        panel.add(streetField);

        panel.add(new JLabel("Zipcode:"));
        zipcodeField = new JTextField(20);
        panel.add(zipcodeField);

        panel.add(new JLabel("Department:"));
        Department[] departments = {
                new Department("DCS", "Department of Computer Science"),
                new Department("DEE", "Department of Electrical Engineering")
                // Add more departments if needed
        };
        departmentComboBox = new JComboBox<>(departments);
        panel.add(departmentComboBox);

        // Create a button to submit the form
        btnSubmit = new JButton("Submit");
        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitInfo();
            }
        });

        // Add the submit button to the panel
        panel.add(btnSubmit);

        // Add the panel to the frame
        add(panel);

        // Make the frame visible
        setVisible(true);
    }

    public void submitInfo() {

        // Get the student information from the text fields
        String studentNumber = studentNumberField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String phoneNumber = phoneNumberField.getText();
        String birthdate = birthdateField.getText();
        String street = streetField.getText();
        String zipcode = zipcodeField.getText();
        Department selectedDepartment = (Department) departmentComboBox.getSelectedItem();
        String department = selectedDepartment.getId();

        // TODO: You can process the extracted information here, such as saving it to a database or displaying it.
        // For this example, we'll simply print the information to the console.
        System.out.println("Student Number: " + studentNumber);
        System.out.println("First Name: " + firstName);
        System.out.println("Last Name: " + lastName);
        System.out.println("Phone Number: " + phoneNumber);
        System.out.println("Birthdate: " + birthdate);
        System.out.println("Street: " + street);
        System.out.println("Zipcode: " + zipcode);
        System.out.println("Department: " + department);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/students", "root", "r00tpa55word");

            Statement statement = connection.createStatement();

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS students (" +
                    "ssn VARCHAR(15) PRIMARY KEY, " +
                    "firstname VARCHAR(15), " +
                    "lastname VARCHAR(15), " +
                    "phone VARCHAR(15), " +
                    "birthdate DATE, " +
                    "street VARCHAR(250), " +
                    "zipcode VARCHAR(5), " +
                    "deptid VARCHAR(3), " +
                    "CONSTRAINT fk_departments FOREIGN KEY (deptid) REFERENCES departments (deptid));");

//            statement.executeUpdate("INSERT INTO students (ssn, firstname, lastname, deptid) VALUES ('"+studentNumber+"','"+firstName+"','"+lastName+"','"+department+"');");

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO students (ssn, firstname, lastname, deptid) VALUES (?, ?, ?, ?);");

            preparedStatement.setString(1,studentNumber);
            preparedStatement.setString(2, firstName);
            preparedStatement.setString(3, lastName);
            preparedStatement.setString(4, department);
            preparedStatement.executeUpdate();

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }

    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new StudentForm();
            }
        });
    }
}