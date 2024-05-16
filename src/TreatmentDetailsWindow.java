import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class TreatmentDetailsWindow extends JFrame {
    private JLabel doctorLabel;
    private JLabel treatmentNameLabel;
    private JLabel treatmentDescLabel;
    private JLabel patientLabel;

    public TreatmentDetailsWindow(int appointmentId) {
        // Set the title and layout of the window
        super("Treatment Details");
        setLayout(new GridLayout(7, 2));

        // Create labels for the patient information
        add(new JLabel("Patient:"));
        patientLabel = new JLabel();
        add(patientLabel);
        add(new JLabel("Doctor :"));
        doctorLabel = new JLabel();
        add(doctorLabel);
        add(new JLabel("Treatment :"));
        treatmentNameLabel = new JLabel();
        add(treatmentNameLabel);
        add(new JLabel("Treatment Description :"));
        treatmentDescLabel = new JLabel();
        add(treatmentDescLabel);
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/patient_management", "root", "abcd1234")) {
            try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Treatment WHERE treatment_id = ?")) {
                stmt.setInt(1, appointmentId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        // Set the text of the labels to display the patient information
                        treatmentNameLabel.setText(String.valueOf(rs.getString("treatment_name")));
                        treatmentDescLabel.setText(String.valueOf(rs.getString("treatment_description")));
                    }
                }
            }
            try (PreparedStatement stmt = conn.prepareStatement("SELECT name FROM Patient WHERE patient_id = (SELECT patient_id FROM Treatment WHERE treatment_id = ?);")) {
                stmt.setInt(1, appointmentId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        // Set the text of the labels to display the patient information
                        patientLabel.setText(String.valueOf(rs.getString("name")));
                    }
                }
            }
            try (PreparedStatement stmt = conn.prepareStatement("SELECT name FROM Doctor WHERE doctor_id = (SELECT doctor_id FROM Treatment WHERE treatment_id = ?);")) {
                stmt.setInt(1, appointmentId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        // Set the text of the labels to display the patient information
                        doctorLabel.setText(String.valueOf(rs.getString("name")));
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
