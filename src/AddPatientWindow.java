import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

//ADD BUTTON
public class AddPatientWindow extends JFrame implements ActionListener {
    private JTextField nameField;
    private JTextField genderField;
    private JTextField dateOfBirthField;
    private JTextField addressField;
    private JTextField phoneNumberField;
    private JTextField emailAddressField;
    private DefaultTableModel patientModel;

    public AddPatientWindow(DefaultTableModel patientModel) {
        // Set window title
        super("Add Patient");
        this.patientModel = patientModel;

        // Set layout
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        // Create and add labels and text fields
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_END;
        add(new JLabel("Name:"), c);
        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_START;
        nameField = new JTextField(10);
        add(nameField, c);

        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.LINE_END;
        add(new JLabel("Gender:"), c);
        c.gridx = 1;
        c.gridy = 1;
        c.anchor = GridBagConstraints.LINE_START;
        genderField = new JTextField(10);
        add(genderField, c);

        c.gridx = 0;
        c.gridy = 2;
        c.anchor = GridBagConstraints.LINE_END;
        add(new JLabel("Date of Birth (YYYY-MM-DD):"), c);
        c.gridx = 1;
        c.gridy = 2;
        c.anchor = GridBagConstraints.LINE_START;
        dateOfBirthField = new JTextField(10);
        add(dateOfBirthField, c);

        // Add address field
        c.gridx = 0;
        c.gridy = 3;
        c.anchor = GridBagConstraints.LINE_END;
        add(new JLabel("Address:"), c);
        c.gridx = 1;
        c.gridy = 3;
        c.anchor = GridBagConstraints.LINE_START;
        addressField = new JTextField(10);
        add(addressField, c);

        // Add phone number field
        c.gridx = 0;
        c.gridy = 4;
        c.anchor = GridBagConstraints.LINE_END;
        add(new JLabel("Phone Number:"), c);
        c.gridx = 1;
        c.gridy = 4;
        c.anchor = GridBagConstraints.LINE_START;
        phoneNumberField = new JTextField(10);
        add(phoneNumberField, c);

        // Add email address field
        c.gridx = 0;
        c.gridy = 5;
        c.anchor = GridBagConstraints.LINE_END;
        add(new JLabel("Email Address:"), c);
        c.gridx = 1;
        c.gridy = 5;
        c.anchor = GridBagConstraints.LINE_START;
        emailAddressField = new JTextField(10);
        add(emailAddressField, c);

        // Create and add Add button
        JButton addButton = new JButton("Add");
        addButton.addActionListener(this);
        c.gridx = 0;
        c.gridy = 6;
        c.gridwidth = 2;
        add(addButton, c);

        // Set window size and location
        pack();
        setLocationRelativeTo(null);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        // Get values from text fields
        String name = nameField.getText();
        String gender = genderField.getText();
        String dob = dateOfBirthField.getText();
        String address = addressField.getText();
        String phoneNumber = phoneNumberField.getText();
        String emailAddress = emailAddressField.getText();

        // Insert new patient into database
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/patient_management", "root", "abcd1234")) {
            String insertQuery = "INSERT INTO Patient (name, dob, gender, address, phone_number, email_address) VALUES (?, ?, ?,?,?,?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
                stmt.setString(1, name);
                stmt.setString(2, dob);
                stmt.setString(3, gender);
                stmt.setString(4, address);
                stmt.setString(5, phoneNumber);
                stmt.setString(6, emailAddress);
                stmt.executeUpdate();
            }
            // Get ID of newly inserted medical history record
            int patientId;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID()")) {
                rs.next();
                patientId = rs.getInt(1);
            }
            // Add new row to medical history table model
            Object[] newRow = {patientId, name, dob, gender, address, phoneNumber, emailAddress};
            patientModel.addRow(newRow);
            JOptionPane.showMessageDialog(this, "Patient added successfully!");
            dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding patient.");
        }
    }
}
