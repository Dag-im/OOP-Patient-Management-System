import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AddMedicalHistoryWindow extends JFrame implements ActionListener {
    private JTextField patientIdField;
    private JTextField conditionField;
    private JTextField conditionDescField;
    private DefaultTableModel medicalHistoryModel;

    public AddMedicalHistoryWindow(DefaultTableModel medicalHistoryModel) {
        // Set window title
        super("Add Medical History");
        this.medicalHistoryModel = medicalHistoryModel;

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
        add(new JLabel("Condition:"), c);
        c.gridx = 1;
        c.gridy = 1;
        c.anchor = GridBagConstraints.LINE_START;
        conditionField = new JTextField(10);
        add(conditionField, c);

        c.gridx = 0;
        c.gridy = 2;
        c.anchor = GridBagConstraints.LINE_END;
        add(new JLabel("Condition Desc:"), c);
        c.gridx = 1;
        c.gridy = 2;
        c.anchor = GridBagConstraints.LINE_START;
        conditionDescField = new JTextField(10);
        add(conditionDescField, c);

        // Create and add Add button
        JButton addButton = new JButton("Add");
        addButton.addActionListener(this);
        c.gridx = 0;
        c.gridy = 3;
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
        String condition = conditionField.getText();
        String conditionDesc = conditionDescField.getText();

        // Insert new medical history into database
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/patient_management", "root", "abcd1234")) {
            String insertQuery = "INSERT INTO Medical_history (patient_id, medical_condition_name, medical_condition_description) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
                stmt.setInt(1, patientId);
                stmt.setString(2, condition);
                stmt.setString(3, conditionDesc);
                stmt.executeUpdate();
            }
            // Get ID of newly inserted medical history record
            int medicalHistoryId;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID()")) {
                rs.next();
                medicalHistoryId = rs.getInt(1);
            }

            // Add new row to medical history table model
            Object[] newRow = {medicalHistoryId, patientId, condition, conditionDesc};
            medicalHistoryModel.addRow(newRow);

            JOptionPane.showMessageDialog(this, "Medical history added successfully!");
            dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding medical history.");
        }
    }
}
