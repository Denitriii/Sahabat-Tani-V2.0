/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package formMenu;

import com.toedter.calendar.JDateChooser;
import inventoryPupuk.Pupuk;
import inventoryPupuk.PupukDAO;
import static inventoryPupuk.PupukDAO.deletePupuk;
import java.awt.Color;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import popUp.tambahProduk;
import java.sql.Connection;
import javax.imageio.ImageIO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import koneksi.koneksiDB;
/**
 *
 * @author user
 */
public class formBarang extends RoundedPanel {
    private Connection conn; 
    private JPanel contentPanel; 
    /**
     * Creates new form formBarang
     */
    public formBarang() {
        initComponents();
        conn = koneksiDB.BukaKoneksi();
        setLayout(new BorderLayout());

        // **PANEL UNTUK TOMBOL**
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10)); 
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnTambah = new JButton("Tambah Produk Baru");
        JButton btnRestock = new JButton("Restock");

        btnTambah.addActionListener(e -> {
            tambahProduk popup = new tambahProduk(null, true);
            popup.setLocationRelativeTo(null);
            popup.setVisible(true);
        });

        btnRestock.addActionListener(e -> 
            JOptionPane.showMessageDialog(null, "Form Restock akan muncul!")
        );

        buttonPanel.add(btnTambah);
        buttonPanel.add(btnRestock);

        // **Panel utama untuk produk**
        contentPanel = new JPanel(new GridLayout(0, 3, 20, 20)); // 3 kolom
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        loadProduk();
    }

    private void loadProduk() {
        contentPanel.removeAll();

        List<Pupuk> pupukList = PupukDAO.getAllPupuk();
        for (Pupuk pupuk : pupukList) {
            contentPanel.add(createCard(pupuk));
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // **Membuat kartu produk**
    private RoundedPanel createCard(Pupuk pupuk) {
        RoundedPanel card = new RoundedPanel();
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(200, 250));  
        card.setBackground(Color.WHITE);
        card.setCornerRadius(35);
        // **Label untuk gambar produk**
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setPreferredSize(new Dimension(142, 174));

        // **Ambil gambar dari database**
        
        ImageIcon icon = pupuk.getImageIcon();
        if (icon != null) {
            Image img = icon.getImage().getScaledInstance(142, 172, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(img));
        } else {
            imageLabel.setText("No Image");
        }

        // **Panel informasi produk**
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        infoPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(pupuk.getNama());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel priceLabel = new JLabel("Harga: Rp " + pupuk.getHarga());
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        JLabel stockLabel = new JLabel("Stok: " + pupuk.getStock());
        stockLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        stockLabel.setForeground(pupuk.getStock() > 0 ? Color.GREEN : Color.RED);

        JLabel expiredLabel = new JLabel("Exp: " + pupuk.getTglExpired());
        expiredLabel.setFont(new Font("Arial", Font.ITALIC, 12));

        infoPanel.add(nameLabel);
        infoPanel.add(priceLabel);
        infoPanel.add(stockLabel);
        infoPanel.add(expiredLabel);

        // **Panel atas untuk tombol opsi**
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        topPanel.setOpaque(false);

        JButton btnOptions = new JButton("...");
        btnOptions.setFont(new Font("Arial", Font.BOLD, 14));
        btnOptions.setBorderPainted(false);
        btnOptions.setFocusPainted(false);
        btnOptions.setContentAreaFilled(false);

        btnOptions.addActionListener(e -> showPopupMenu(btnOptions, pupuk));

        topPanel.add(btnOptions);

        // **Tata letak elemen dalam kartu**
        card.add(topPanel, BorderLayout.NORTH);
        card.add(imageLabel, BorderLayout.CENTER);
        card.add(infoPanel, BorderLayout.SOUTH);

        return card;
    }                                 
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();

        setBackground(new java.awt.Color(235, 235, 235));
        setPreferredSize(new java.awt.Dimension(1219, 708));
        setLayout(new java.awt.CardLayout());

        jPanel1.setBackground(new java.awt.Color(235, 235, 235));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 988, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 783, Short.MAX_VALUE)
        );

        add(jPanel1, "card2");
    }// </editor-fold>//GEN-END:initComponents
    private void openEditDialog(Pupuk pupuk) {
    if (pupuk == null) {
        JOptionPane.showMessageDialog(null, "Data tidak valid!", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    JTextField txtNama = new JTextField(pupuk.getNama());
    JTextField txtHarga = new JTextField(String.valueOf(pupuk.getHarga()));
    JTextField txtStock = new JTextField(String.valueOf(pupuk.getStock()));
    JDateChooser tglExp = new JDateChooser();
    tglExp.setDateFormatString("dd-MM-yyyy"); // Format Indonesia


    Object[] fields = {
        "Nama:", txtNama,
        "Harga:", txtHarga,
        "Stock:", txtStock,
        "Tanggal Exp",tglExp
    };

    int result = JOptionPane.showConfirmDialog(null, fields, "Edit Produk", JOptionPane.OK_CANCEL_OPTION);

    if (result == JOptionPane.OK_OPTION) {
        pupuk.setNama(txtNama.getText());
        pupuk.setHarga(Integer.parseInt(txtHarga.getText()));
        pupuk.setStock(Integer.parseInt(txtStock.getText()));
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String tglExpiredFormatted = sdf.format(tglExp.getDate());
        
        pupuk.setTglExpired(tglExpiredFormatted); 
        PupukDAO.updatePupuk(pupuk); // Simpan ke database
        refreshData(); // Refresh tampilan
    }
}
    private void refreshData() {
    removeAll(); // Hapus semua komponen lama
    revalidate(); // Validasi ulang
    repaint(); // Gambar ulang UI

    // Tambahkan ulang daftar produk
    List<Pupuk> pupukList = PupukDAO.getAllPupuk();
    
    RoundedPanel contentPanel = new RoundedPanel();
    contentPanel.setLayout(new GridLayout(0, 3, 10, 10));
    contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

    for (Pupuk pupuk : pupukList) {
        contentPanel.add(createCard(pupuk));
    }

    JScrollPane scrollPane = new JScrollPane(contentPanel);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    add(scrollPane, BorderLayout.CENTER);
    revalidate();
    repaint();
}
    private void showPopupMenu(JButton button, Pupuk pupuk) {
    JPopupMenu popupMenu = new JPopupMenu();
    
    JMenuItem editItem = new JMenuItem("Edit");
    editItem.addActionListener(e -> openEditDialog(pupuk)); // Metode untuk mengedit

    JMenuItem deleteItem = new JMenuItem("Hapus");
    deleteItem.addActionListener(e -> deletePupuk(pupuk.getId())); // Metode untuk menghapus

    popupMenu.add(editItem);
    popupMenu.add(deleteItem);
    
    popupMenu.show(button, 0, button.getHeight()); // Tampilkan di bawah tombol
}
private void deletePupuk(String idPupuk) {
    int confirm = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menghapus produk ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
        try (Connection conn = koneksiDB.BukaKoneksi(); ) {
            String sqlPupuk = "DELETE FROM pupuk WHERE id_pupuk = ?";
            String sqlRestock= "DELETE FROM restock WHERE id_pupuk = ?";
            try(PreparedStatement ps = conn.prepareStatement(sqlRestock)) {
                ps.setString(1, idPupuk);
                ps.executeUpdate();
            }
            try (PreparedStatement pst = conn.prepareStatement(sqlPupuk)) {
                pst.setString(1, idPupuk);
                pst.executeUpdate();
            }
            JOptionPane.showMessageDialog(this, "Produk berhasil dihapus!");
            
            refreshData();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal menghapus produk: " , "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}



    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
