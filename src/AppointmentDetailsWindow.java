import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class  AppointmentDetailsWindow extends JFrame {
    private JLabel appointmentDateLabel;
    private JLabel appointmentTimeLabel;
    private JLabel patientLabel;
    private JLabel doctorLabel;

    public AppointmentDetailsWindow(int appointmentId) {
        // Set the title and layout of the window
        super("Appointment Details");
        setLayout(new GridLayout(7, 2));

        // Create labels for the patient information
        add(new JLabel("Patient:"));
        patientLabel = new JLabel();
        add(patientLabel);

        add(new JLabel("Doctor:"));
        doctorLabel = new JLabel();
        add(doctorLabel);

        add(new JLabel("Appointment Date:"));
        appointmentDateLabel = new JLabel();
        add(appointmentDateLabel);

        add(new JLabel("Appointment Time:"));
        appointmentTimeLabel = new JLabel();
        add(appointmentTimeLabel);
        // Retrieve patient data from the database
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/patient_management", "root", "abcd1234")) {
            try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Appointment WHERE appointment_id = ?")) {
                stmt.setInt(1, appointmentId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        // Set the text of the labels to display the patient information
                        appointmentDateLabel.setText(String.valueOf(rs.getString("appointment_date")));
                        appointmentTimeLabel.setText(String.valueOf(rs.getString("appointment_time")));
                    }
                }
            }
            try (PreparedStatement stmt = conn.prepareStatement("SELECT name FROM Patient WHERE patient_id = (SELECT patient_id FROM Appointment WHERE appointment_id = ?);")) {
                stmt.setInt(1, appointmentId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        // Set the text of the labels to display the patient information
                        patientLabel.setText(String.valueOf(rs.getString("name")));
                    }
                }
            }
            try (PreparedStatement stmt = conn.prepareStatement("SELECT name FROM Doctor WHERE doctor_id = (SELECT doctor_id FROM Appointment WHERE appointment_id = ?);")) {
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
