import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AddTreatmentWindow extends JFrame implements ActionListener {
    private JTextField patientIdField;
    private JTextField doctorIdField;
    private JTextField treatmentNameField;
    private JTextField treatmentDescField;
    private DefaultTableModel treatmentModel;

    public AddTreatmentWindow(DefaultTableModel treatmentModel) {
        // Set window title
        super("Add Medical History");
        this.treatmentModel = treatmentModel;

        // Set layout
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        // Create and add labels and text fields
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_END;
        add(new JLabel("Patient ID:"), c);
        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_START;
        patientIdField = new JTextField(10);
        add(patientIdField, c);

        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.LINE_END;
        add(new JLabel("Doctor ID:"), c);
        c.gridx = 1;
        c.gridy = 1;
        c.anchor = GridBagConstraints.LINE_START;
        doctorIdField = new JTextField(10);
        add(doctorIdField, c);

        c.gridx = 0;
        c.gridy = 2;
        c.anchor = GridBagConstraints.LINE_END;
        add(new JLabel("Treatment Name:"), c);
        c.gridx = 1;
        c.gridy = 2;
        c.anchor = GridBagConstraints.LINE_START;
        treatmentNameField = new JTextField(10);
        add(treatmentNameField, c);

        c.gridx = 0;
        c.gridy = 3;
        c.anchor = GridBagConstraints.LINE_END;
        add(new JLabel("Treatment Desc:"), c);
        c.gridx = 1;
        c.gridy = 3;
        c.anchor = GridBagConstraints.LINE_START;
        treatmentDescField = new JTextField(10);
        add(treatmentDescField, c);

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
        int patientId = Integer.parseInt(patientIdField.getText());
        int doctorId = Integer.parseInt(doctorIdField.getText());
        String treatmentName = treatmentNameField.getText();
        String treatmentDesc = treatmentDescField.getText();

        // Insert new medical history into database
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/patient_management", "root", "abcd1234")) {
            String insertQuery = "INSERT INTO  Treatment (patient_id,doctor_id,treatment_name,treatment_description) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
                stmt.setInt(1, patientId);
                stmt.setInt(2, doctorId);
                stmt.setString(3, treatmentName);
                stmt.setString(4, treatmentDesc);
                stmt.executeUpdate();
            }
            int treatmentId;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID()")) {
                rs.next();
                treatmentId = rs.getInt(1);
            }

            // Add new row to medical history table model
            Object[] newRow = {treatmentId, doctorId, treatmentName, treatmentDesc};
            treatmentModel.addRow(newRow);

            JOptionPane.showMessageDialog(this, "Medical history added successfully!");
            dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding medical history.");
        }
    }
}
