import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

//Modify Button
public class ModifyPatientWindow extends JFrame implements ActionListener {
    private DefaultTableModel patientModel;
    private int selectedRow;
    private int patientID;
    private JTextField nameField;
    private JTextField dobField;
    private JTextField genderField;
    private JTextField addressField;
    private JTextField phoneNumberField;
    private JTextField emailAddressField;

    public ModifyPatientWindow(int patientID, DefaultTableModel patientModel, int selectedRow) {
        // Set window title
        super("Modify Patient");
        // Set patient model and selected row
        this.patientModel = patientModel;
        this.selectedRow = selectedRow;
        // Set patient ID
        this.patientID = patientID;

        // Set layout
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);

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
        add(new JLabel("Date of Birth:"), c);
        c.gridx = 1;
        c.gridy = 1;
        c.anchor = GridBagConstraints.LINE_START;
        dobField = new JTextField(10);
        add(dobField, c);

        c.gridx = 0;
        c.gridy = 2;
        c.anchor = GridBagConstraints.LINE_END;
        add(new JLabel("Gender:"), c);
        c.gridx = 1;
        c.gridy = 2;
        c.anchor = GridBagConstraints.LINE_START;
        genderField = new JTextField(10);
        add(genderField, c);

        c.gridx = 0;
        c.gridy = 3;
        c.anchor = GridBagConstraints.LINE_END;
        add(new JLabel("Address:"), c);
        c.gridx = 1;
        c.gridy = 3;
        c.anchor = GridBagConstraints.LINE_START;
        addressField = new JTextField(10);
        add(addressField, c);

        c.gridx = 0;
        c.gridy = 4;
        c.anchor = GridBagConstraints.LINE_END;
        add(new JLabel("Phone Number:"), c);
        c.gridx = 1;
        c.gridy = 4;
        c.anchor = GridBagConstraints.LINE_START;
        phoneNumberField = new JTextField(10);
        add(phoneNumberField, c);

        c.gridx = 0;
        c.gridy = 5;
        c.anchor = GridBagConstraints.LINE_END;
        add(new JLabel("Email Address:"), c);
        c.gridx = 1;
        c.gridy = 5;
        c.anchor = GridBagConstraints.LINE_START;
        emailAddressField = new JTextField(10);
        add(emailAddressField, c);
        // Create and add update button
        c.gridx = 0;
        c.gridy = 6;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;
        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(this);
        add(updateButton, c);
        // Set window size and location
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

// Populate text fields with current patient data
        loadPatientData();
    }

    private void loadPatientData() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/patient_management", "root", "abcd1234")) {
            String selectQuery = "SELECT patient_id, name, dob, gender, address, phone_number, email_address FROM Patient WHERE patient_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(selectQuery)) {
                stmt.setInt(1, patientID);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        nameField.setText(rs.getString("name"));
                        dobField.setText(rs.getString("dob"));
                        genderField.setText(rs.getString("gender"));
                        addressField.setText(rs.getString("address"));
                        phoneNumberField.setText(rs.getString("phone_number"));
                        emailAddressField.setText(rs.getString("email_address"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Get values from text fields
        String name = nameField.getText();
        String dob = dobField.getText();
        String gender = genderField.getText();
        String address = addressField.getText();
        String phoneNumber = phoneNumberField.getText();
        String emailAddress = emailAddressField.getText();

        // Update patient in database
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/patient_management", "root", "abcd1234")) {
            String updateQuery = "UPDATE Patient SET name = ?, dob = ?, gender = ?, address = ?, phone_number = ?, email_address = ? WHERE patient_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
                stmt.setString(1, name);
                stmt.setString(2, dob);
                stmt.setString(3, gender);
                stmt.setString(4, address);
                stmt.setString(5, phoneNumber);
                stmt.setString(6, emailAddress);
                stmt.setInt(7, patientID);
                stmt.executeUpdate();
            }
            // Update values in patient table model
            patientModel.setValueAt(name, selectedRow, 1);
            patientModel.setValueAt(dob, selectedRow, 2);
            patientModel.setValueAt(gender, selectedRow, 3);
            patientModel.setValueAt(address, selectedRow, 4);
            patientModel.setValueAt(phoneNumber, selectedRow, 5);
            patientModel.setValueAt(emailAddress, selectedRow, 6);
            JOptionPane.showMessageDialog(this, "Patient updated successfully!");
            dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating patient.");
        }
    }
}
