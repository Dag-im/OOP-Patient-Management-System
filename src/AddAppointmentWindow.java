import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AddAppointmentWindow extends JFrame implements ActionListener {
    private JTextField appointmentDateField;
    private JTextField appointmentTimeField;
    private JTextField patientIdField;
    private JTextField doctorIdField;
    private DefaultTableModel appointmentModel;

    public AddAppointmentWindow(DefaultTableModel appointmentModel) {
        // Set window title
        super("Add Appointment");
        this.appointmentModel = appointmentModel;

        // Set layout
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        // Create and add labels and text fields
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_END;
        add(new JLabel("Appointment Date (YYYY-MM-DD):"), c);
        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_START;
        appointmentDateField = new JTextField(10);
        add(appointmentDateField, c);

        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.LINE_END;
        add(new JLabel("Appointment Time (HH:MM:SS):"), c);
        c.gridx = 1;
        c.gridy = 1;
        c.anchor = GridBagConstraints.LINE_START;
        appointmentTimeField = new JTextField(10);
        add(appointmentTimeField, c);

        // Add patient ID field
        c.gridx = 0;
        c.gridy = 2;
        c.anchor = GridBagConstraints.LINE_END;
        add(new JLabel("Patient ID:"), c);
        c.gridx = 1;
        c.gridy = 2;
        c.anchor = GridBagConstraints.LINE_START;
        patientIdField = new JTextField(10);
        add(patientIdField, c);

        // Add doctor ID field
        c.gridx = 0;
        c.gridy = 3;
        c.anchor = GridBagConstraints.LINE_END;
        add(new JLabel("Doctor ID:"), c);
        c.gridx = 1;
        c.gridy = 3;
        c.anchor = GridBagConstraints.LINE_START;
        doctorIdField = new JTextField(10);
        add(doctorIdField, c);

        // Create and add Add button
        JButton addButton = new JButton("Add");
        addButton.addActionListener(this);
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 2;
        add(addButton, c);

        // Set window size and location
        pack();
        setLocationRelativeTo(null);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        // Get values from text fields
        String appointmentDate = appointmentDateField.getText();
        String appointmentTime = appointmentTimeField.getText();
        int patientId = Integer.parseInt(patientIdField.getText());
        int doctorId = Integer.parseInt(doctorIdField.getText());

        // Insert new appointment into database
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/patient_management", "root", "abcd1234")) {
            String insertQuery = "INSERT INTO Appointment (appointment_date, appointment_time, patient_id, doctor_id) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
                stmt.setString(1, appointmentDate);
                stmt.setString(2, appointmentTime);
                stmt.setInt(3, patientId);
                stmt.setInt(4, doctorId);
                stmt.executeUpdate();
            }
            // Get ID of newly inserted medical history record
            int appointmentId;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID()")) {
                rs.next();
                appointmentId = rs.getInt(1);
            }
            // Add new row to medical history table model
            Object[] newRow = {appointmentId, appointmentDate, appointmentTime, patientId, doctorId};
            appointmentModel.addRow(newRow);
            JOptionPane.showMessageDialog(this, "Appointment added successfully!");
            dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding appointment.");
        }
    }
}
