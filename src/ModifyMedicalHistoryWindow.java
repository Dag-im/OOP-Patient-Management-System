import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ModifyMedicalHistoryWindow extends JFrame implements ActionListener {
    private DefaultTableModel medicalHistoryModel;
    private int selectedRow;
    private int medicalHistoryId;
    private JTextField patientIdField;
    private JTextField conditionField;
    private JTextArea conditionDescField;

    public ModifyMedicalHistoryWindow(int medicalHistoryId, DefaultTableModel medicalHistoryModel, int selectedRow) {
        super("Modify Medical History");
        this.medicalHistoryModel = medicalHistoryModel;
        this.selectedRow = selectedRow;
        this.medicalHistoryId = medicalHistoryId;
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_END;
        add(new JLabel("Medical Condition Name:"), c);
        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_START;
        conditionField = new JTextField(10);
        add(conditionField, c);
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.LINE_END;
        add(new JLabel("Medical Condition Description:"), c);
        c.gridx = 1;
        c.gridy = 1;
        c.anchor = GridBagConstraints.LINE_START;
        conditionDescField = new JTextArea(5, 10);
        add(conditionDescField, c);
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
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;
        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(this);
        add(updateButton, c);

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        loadMedicalHistoryData();
    }

    private void loadMedicalHistoryData() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/patient_management", "root", "abcd1234")) {
            String selectQuery = "SELECT patient_id, medical_condition_name, medical_condition_description FROM Medical_history WHERE medical_history_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(selectQuery)) {
                stmt.setInt(1, medicalHistoryId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        patientIdField.setText(String.valueOf(rs.getInt("patient_id")));
                        conditionField.setText(String.valueOf(rs.getString("medical_condition_name")));
                        conditionDescField.setText(String.valueOf(rs.getString("medical_condition_description")));
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
        String condition = conditionField.getText();
        String conditionDesc = conditionDescField.getText();

        // Update medical history in database
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/patient_management", "root", "abcd1234")) {
            String updateQuery = "UPDATE Medical_history SET patient_id = ?, medical_condition_name = ?, medical_condition_description = ? WHERE medical_history_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
                stmt.setInt(1, patientId);
                stmt.setString(2, condition);
                stmt.setString(3, conditionDesc);
                stmt.setInt(4, medicalHistoryId);
                stmt.executeUpdate();
            }
            // Update values in medical history table model
            medicalHistoryModel.setValueAt(patientId, selectedRow, 1);
            medicalHistoryModel.setValueAt(condition, selectedRow, 2);
            medicalHistoryModel.setValueAt(conditionDesc, selectedRow, 3);
            JOptionPane.showMessageDialog(this, "Medical history updated successfully!");
            dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating medical history.");
        }
    }
}
