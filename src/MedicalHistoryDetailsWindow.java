import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class MedicalHistoryDetailsWindow extends JFrame {
    private JLabel medicalConditionLabel;
    private JLabel medicalConditionDescLabel;
    private JLabel patientLabel;

    public MedicalHistoryDetailsWindow(int appointmentId) {
        // Set the title and layout of the window
        super("Medical History Details");
        setLayout(new GridLayout(7, 2));

        // Create labels for the patient information
        add(new JLabel("Patient:"));
        patientLabel = new JLabel();
        add(patientLabel);
        add(new JLabel("Medical Condition :"));
        medicalConditionLabel = new JLabel();
        add(medicalConditionLabel);
        add(new JLabel("Medical Condition Description:"));
        medicalConditionDescLabel = new JLabel();
        add(medicalConditionDescLabel);
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/patient_management", "root", "abcd1234")) {
            try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Medical_history WHERE medical_history_id = ?")) {
                stmt.setInt(1, appointmentId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        // Set the text of the labels to display the patient information
                        medicalConditionLabel.setText(String.valueOf(rs.getString("medical_condition_name")));
                        medicalConditionDescLabel.setText(String.valueOf(rs.getString("medical_condition_description")));
                    }
                }
            }
            try (PreparedStatement stmt = conn.prepareStatement("SELECT name FROM Patient WHERE patient_id = (SELECT patient_id FROM Medical_history WHERE medical_history_id = ?);")) {
                stmt.setInt(1, appointmentId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        // Set the text of the labels to display the patient information
                        patientLabel.setText(String.valueOf(rs.getString("name")));
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
