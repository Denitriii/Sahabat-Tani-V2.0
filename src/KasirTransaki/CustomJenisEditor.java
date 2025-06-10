/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package KasirTransaki;

/**
 *
 * @author user
 */
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;


public class CustomJenisEditor extends DefaultCellEditor {
    private JComboBox<String> comboBox;
    private Connection conn;
    private JTable table;
    private Map<String, List<String>> jenisTerkunciPerPupuk;

    public CustomJenisEditor(Connection conn, JTable table, Map<String, List<String>> jenisTerkunciPerPupuk) {
        super(new JComboBox<>());
        this.conn = conn;
        this.table = table;
        this.comboBox = (JComboBox<String>) getComponent();
        this.jenisTerkunciPerPupuk = jenisTerkunciPerPupuk;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        comboBox.removeAllItems();

        String namaPupuk = (String) table.getValueAt(row, 0);
        if (namaPupuk == null || namaPupuk.trim().isEmpty()) {
            comboBox.addItem("Pilih Nama Dulu");
            return comboBox;
        }

        List<String> terkunci = jenisTerkunciPerPupuk.getOrDefault(namaPupuk, new ArrayList<>());

        try {
            String sql = "SELECT tipe_pembelian FROM pupuk WHERE nama_pupuk = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, namaPupuk);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int tipe = rs.getInt("tipe_pembelian");
                if (tipe == 1) {
                    comboBox.addItem("Satuan");
                    comboBox.addItem("Eceran");
                } else {
                    comboBox.addItem("Satuan");
                }
            } else {
                comboBox.addItem("Tidak ditemukan");
            }

            rs.close();
            pst.close();
        } catch (SQLException ex) {
            comboBox.addItem("Error");
            ex.printStackTrace();
        }

        if (value != null) {
            comboBox.setSelectedItem(value.toString());
        }

        return comboBox;
    }
}
