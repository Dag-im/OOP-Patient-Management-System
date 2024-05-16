import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ModifyTreatmentWindow extends JFrame implements ActionListener {
    private DefaultTableModel treatmentModel;
    private int selectedRow;
    private int treatmentID;
    private JTextField patientIdField;
    private JTextField doctorIDField;
    private JTextField treatmentNameField;
    private JTextArea treatmentDescField;

    public ModifyTreatmentWindow(int treatmentID, DefaultTableModel treatmentModel, int selectedRow) {
        super("Modify Treatment");
        this.treatmentModel = treatmentModel;
        this.selectedRow = selectedRow;
        this.treatmentID = treatmentID;
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_END;
        add(new JLabel("Treatment Name:"), c);
        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_START;
        treatmentNameField = new JTextField(10);
        add(treatmentNameField, c);
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.LINE_END;
        add(new JLabel("Treatment Description:"), c);
        c.gridx = 1;
        c.gridy = 1;
        c.anchor = GridBagConstraints.LINE_START;
        treatmentDescField = new JTextArea(5, 10);
        add(treatmentDescField, c);

        c.gridx = 0;
        c.gridy = 2;
        c.anchor = GridBagConstraints.LINE_END;
        add(new JLabel("Patient ID:"), c);
        c.gridx = 1;
        c.gridy = 2;
        c.anchor = GridBagConstraints.LINE_START;
        patientIdField = new JTextField(10);
        add(patientIdField, c);

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
        loadTreatmentData();
    }

    private void loadTreatmentData() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/patient_management", "root", "abcd1234")) {
            String selectQuery = "SELECT patient_id,doctor_id,treatment_name,treatment_description FROM Treatment WHERE treatment_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(selectQuery)) {
                stmt.setInt(1, treatmentID);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        patientIdField.setText(String.valueOf(rs.getInt("patient_id")));
                        doctorIDField.setText(String.valueOf(rs.getInt("doctor_id")));
                        treatmentNameField.setText(String.valueOf(rs.getString("treatment_name")));
                        treatmentDescField.setText(String.valueOf(rs.getString("treatment_description")));
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
        int patientId = Integer.parseInt(patientIdField.getText());
        int doctorId = Integer.parseInt(doctorIDField.getText());
        String treatment = treatmentNameField.getText();
        String treatmentDesc = treatmentDescField.getText();
        // Update medical history in database
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/patient_management", "root", "abcd1234")) {
            String updateQuery = "UPDATE Treatment SET patient_id = ?,doctor_id = ?, treatment_name = ?, treatment_description = ? WHERE medical_history_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
                stmt.setInt(1, patientId);
                stmt.setInt(2, doctorId);
                stmt.setString(3, treatment);
                stmt.setString(4, treatmentDesc);
                stmt.setInt(5, treatmentID);
                stmt.executeUpdate();
            }
            // Update values in medical history table model
            treatmentModel.setValueAt(patientId, selectedRow, 1);
            treatmentModel.setValueAt(doctorId, selectedRow, 2);
            treatmentModel.setValueAt(treatment, selectedRow, 3);
            treatmentModel.setValueAt(treatmentDesc, selectedRow, 3);
            JOptionPane.showMessageDialog(this, "Treatment updated successfully!");
            dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating Treatment.");
        }
    }
}
