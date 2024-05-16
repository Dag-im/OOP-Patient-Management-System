import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ModifyAppointmentWindow extends JFrame implements ActionListener {
    private DefaultTableModel appointmentModel;
    private int selectedRow;
    private int appointmentID;
    private JTextField dateField;
    private JTextField timeField;
    private JTextField patientIDField;
    private JTextField doctorIDField;

    public ModifyAppointmentWindow(int appointmentID, DefaultTableModel appointmentModel, int selectedRow) {
        super("Modify Appointment");
        this.appointmentModel = appointmentModel;
        this.selectedRow = selectedRow;
        this.appointmentID = appointmentID;
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_END;
        add(new JLabel("Date:"), c);
        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_START;
        dateField = new JTextField(10);
        add(dateField, c);
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.LINE_END;
        add(new JLabel("Time:"), c);
        c.gridx = 1;
        c.gridy = 1;
        c.anchor = GridBagConstraints.LINE_START;
        timeField = new JTextField(10);
        add(timeField, c);
        c.gridx = 0;
        c.gridy = 2;
        c.anchor = GridBagConstraints.LINE_END;
        add(new JLabel("Patient ID:"), c);
        c.gridx = 1;
        c.gridy = 2;
        c.anchor = GridBagConstraints.LINE_START;
        patientIDField = new JTextField(10);
        add(patientIDField, c);
        c.gridx = 0;
        c.gridy = 3;
        c.anchor = GridBagConstraints.LINE_END;
        add(new JLabel("Doctor ID:"), c);
        c.gridx = 1;
        c.gridy = 3;
        c.anchor = GridBagConstraints.LINE_START;
        doctorIDField = new JTextField(10);
        add(doctorIDField, c);
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;
        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(this);
        add(updateButton, c);

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        loadAppointmentData();
    }

    private void loadAppointmentData() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/patient_management", "root", "abcd1234")) {
            String selectQuery = "SELECT appointment_id, appointment_date, appointment_time, patient_id, doctor_id FROM Appointment WHERE appointment_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(selectQuery)) {
                stmt.setInt(1, appointmentID);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        patientIDField.setText(String.valueOf(rs.getInt("patient_id")));
                        doctorIDField.setText(String.valueOf(rs.getInt("doctor_id")));
                        dateField.setText(String.valueOf(rs.getString("appointment_date")));
                        timeField.setText(String.valueOf(rs.getString("appointment_time")));
                    }
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading medical history data.");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Get values from text fields
        int patientId = Integer.parseInt(patientIDField.getText());
        int doctorId = Integer.parseInt(doctorIDField.getText());
        String date = dateField.getText();
        String time = timeField.getText();

        // Update appointment in database
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/patient_management", "root", "abcd1234")) {
            String updateQuery = "UPDATE Appointment SET patient_id = ?, doctor_id = ?, appointment_date = ?, appointment_time = ? WHERE appointment_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
                stmt.setInt(1, patientId);
                stmt.setInt(2, doctorId);
                stmt.setString(3, date);
                stmt.setString(4, time);
                stmt.setInt(5, appointmentID);
                stmt.executeUpdate();
            }
            // Update values in appointment table model
            appointmentModel.setValueAt(patientId, selectedRow, 1);
            appointmentModel.setValueAt(doctorId, selectedRow, 2);
            appointmentModel.setValueAt(date, selectedRow, 3);
            appointmentModel.setValueAt(time, selectedRow, 4);
            JOptionPane.showMessageDialog(this, "Appointment updated successfully!");
            dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating appointment.");
        }
    }
}
