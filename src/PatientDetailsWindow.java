import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class PatientDetailsWindow extends JFrame {
    private JLabel patientIdLabel;
    private JLabel nameLabel;
    private JLabel dobLabel;
    private JLabel genderLabel;
    private JLabel addressLabel;
    private JLabel phoneNumberLabel;
    private JLabel emailAddressLabel;

    public PatientDetailsWindow(int patientId) {
        // Set the title and layout of the window
        super("Patient Details");
        setLayout(new GridLayout(7, 2));

        // Create labels for the patient information
        add(new JLabel("Patient ID:"));
        patientIdLabel = new JLabel();
        add(patientIdLabel);

        add(new JLabel("Name:"));
        nameLabel = new JLabel();
        add(nameLabel);

        add(new JLabel("DOB:"));
        dobLabel = new JLabel();
        add(dobLabel);

        add(new JLabel("Gender:"));
        genderLabel = new JLabel();
        add(genderLabel);

        add(new JLabel("Address:"));
        addressLabel = new JLabel();
        add(addressLabel);

        add(new JLabel("Phone Number:"));
        phoneNumberLabel = new JLabel();
        add(phoneNumberLabel);

        add(new JLabel("Email Address:"));
        emailAddressLabel = new JLabel();
        add(emailAddressLabel);

        // Retrieve patient data from the database
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/patient_management", "root", "abcd1234")) {
            try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Patient WHERE patient_id = ?")) {
                stmt.setInt(1, patientId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        // Set the text of the labels to display the patient information
                        patientIdLabel.setText(String.valueOf(rs.getInt("patient_id")));
                        nameLabel.setText(rs.getString("name"));
                        dobLabel.setText(String.valueOf(rs.getDate("dob")));
                        genderLabel.setText(rs.getString("gender"));
                        addressLabel.setText(rs.getString("address"));
                        phoneNumberLabel.setText(rs.getString("phone_number"));
                        emailAddressLabel.setText(rs.getString("email_address"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Set the size and location of the window
        pack();
        setLocationRelativeTo(null);
    }
}
