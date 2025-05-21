/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package formMenu;

import com.toedter.calendar.JDateChooser;
import inventoryPupuk.Diskon;
import inventoryPupuk.DiskonDAO;
import inventoryPupuk.Pupuk;
import inventoryPupuk.PupukDAO;
import static inventoryPupuk.PupukDAO.deletePupuk;
import java.awt.Color;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import popUp.tambahProduk;
import java.sql.Connection;
import javax.imageio.ImageIO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import koneksi.SessionManager;
import koneksi.koneksiDB;
import java.sql.*;
import java.util.UUID;
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
        nonaktifkanDiskonKadaluarsa();
        setLayout(new BorderLayout());
        //panel atas untuk tombol tambah produk dan searching textfield
        JPanel panelAtas = new JPanel(new BorderLayout());
        panelAtas.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        // panel untuk searching field
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        JTextField searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        leftPanel.add(new JLabel("Cari: "));
        leftPanel.add(searchField);
        // panel untuk tombol
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0)); 
        JButton btnTambah = new JButton("Tambah Produk Baru");
        

        btnTambah.addActionListener(e -> {
            tambahProduk popup = new tambahProduk(null, true);
            popup.setLocationRelativeTo(null);
            popup.setVisible(true);
        });

        
        
        searchField.addKeyListener(new KeyAdapter() {
        @Override
        public void keyReleased(KeyEvent e) {
            String keyword = searchField.getText().trim().toLowerCase();
            filterProduk(keyword);
            }
            });
        buttonPanel.add(btnTambah);
        
        panelAtas.add(buttonPanel, BorderLayout.EAST);
        panelAtas.add(leftPanel, BorderLayout.WEST);
     

        // **Panel utama untuk produk**
        contentPanel = new JPanel(new GridLayout(0, 3, 20, 20)); // 3 kolom
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(panelAtas, BorderLayout.NORTH);
        
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

    // Membuat kartu produk
    private RoundedPanel createCard(Pupuk pupuk) {
        RoundedPanel card = new RoundedPanel();
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(200, 250));  
        card.setBackground(Color.WHITE);
        card.setCornerRadius(35);
        // Label untuk gambar produk
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setPreferredSize(new Dimension(142, 174));

        // Ambil gambar dari database
        
        ImageIcon icon = pupuk.getImageIcon();
        if (icon != null) {
            Image img = icon.getImage().getScaledInstance(142, 172, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(img));
        } else {
            imageLabel.setText("No Image");
        }

        // Panel informasi produk
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
        stockLabel.setForeground(pupuk.getStock() > 20 ? Color.GREEN : Color.RED);

        JLabel expiredLabel = new JLabel("Exp: " + pupuk.getTglExpired());
        expiredLabel.setFont(new Font("Arial", Font.ITALIC, 12));

        infoPanel.add(nameLabel);
        infoPanel.add(priceLabel);
        infoPanel.add(stockLabel);
        infoPanel.add(expiredLabel);
        // menampilkan diskon label
        DiskonDAO diskonDAO = new DiskonDAO(conn);
        Diskon diskon = diskonDAO.getDiskonAktifByPupukId(pupuk.getId());

if (diskon != null && diskon.isAktif()) {
    JLabel discountLabel = new JLabel("Diskon: " + diskon.getPersenDiskon() + "%");
    discountLabel.setFont(new Font("Arial", Font.BOLD, 12));
    discountLabel.setForeground(Color.GREEN);
    infoPanel.add(discountLabel);

    double hargaAsli = pupuk.getHarga();
    double hargaDiskon = hargaAsli - (hargaAsli * (diskon.getPersenDiskon() / 100.0));
    JLabel hargaDiskonLabel = new JLabel("Harga Setelah Diskon: Rp " + (int) hargaDiskon);
    hargaDiskonLabel.setFont(new Font("Arial", Font.BOLD, 12));
    hargaDiskonLabel.setForeground(Color.black);
    infoPanel.add(hargaDiskonLabel);
}
        // Panel untuk  tombol option
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        topPanel.setOpaque(false);

        JButton btnOptions = new JButton("...");
        btnOptions.setFont(new Font("Arial", Font.BOLD, 14));
        btnOptions.setBorderPainted(false);
        btnOptions.setFocusPainted(false);
        btnOptions.setContentAreaFilled(false);

        btnOptions.addActionListener(e -> showPopupMenu(btnOptions, pupuk));

        topPanel.add(btnOptions);

        
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
        private void showPopupMenu(JButton button, Pupuk pupuk) {
    JPopupMenu popupMenu = new JPopupMenu();
    
    JMenuItem editItem = new JMenuItem("Edit");
    editItem.addActionListener(e -> openEditDialog(pupuk)); // Metode untuk mengedit

    JMenuItem deleteItem = new JMenuItem("Hapus");
    deleteItem.addActionListener(e -> deletePupuk(pupuk.getId())); // Metode untuk menghapus
    
    JMenuItem restock = new JMenuItem("Restock");
    restock.addActionListener(e -> tampilkanDialogRestok(pupuk));
    
    JMenuItem diskonProduk =  new JMenuItem("Diskon");
    diskonProduk.addActionListener(e -> diskonProduk(pupuk));
    popupMenu.add(editItem);
    popupMenu.add(deleteItem);
    popupMenu.add(restock);
    popupMenu.add(diskonProduk);
    
    popupMenu.show(button, 0, button.getHeight()); // Tampilkan di bawah tombol
}
    private void openEditDialog(Pupuk pupuk) {
    if (pupuk == null) {
        JOptionPane.showMessageDialog(null, "Data tidak valid!", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    JTextField txtNama = new JTextField(pupuk.getNama());
    JTextField txtHarga = new JTextField(String.valueOf(pupuk.getHarga()));
    JTextField txtStock = new JTextField(String.valueOf(pupuk.getStock()));
    


    Object[] fields = {
        "Nama:", txtNama,
        "Harga:", txtHarga,
        "Stock:", txtStock
    };

    int result = JOptionPane.showConfirmDialog(null, fields, "Edit Produk", JOptionPane.OK_CANCEL_OPTION);

    if (result == JOptionPane.OK_OPTION) {
        String nama =txtNama.getText();
        double harga = Double.parseDouble(txtHarga.getText());
        int stok = Integer.parseInt(txtStock.getText());
        String idPupuk = pupuk.getId();
        if (harga<=0||stok<=0) {
            JOptionPane.showMessageDialog(this, "Input angka harus valid !","ERROR !",JOptionPane.ERROR_MESSAGE);
            return;
        }
        simpanEdite(nama,harga,stok,idPupuk);
        JOptionPane.showMessageDialog(this, "Berhasil edit produk");
        loadProduk();
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
    contentPanel.setBackground(Color.GRAY);

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
            
            loadProduk();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal menghapus produk: " , "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
private void tampilkanDialogRestok(Pupuk pupuk) {
    JTextField tfJumlah = new JTextField();
    JTextField tfHargaBeli = new JTextField();
    JDateChooser tglExp = new JDateChooser();
    tglExp.setDateFormatString("yyyy-MM-dd");
    JComboBox<String> cbSuplier = new JComboBox<>();
    Map<String, String> suplierMap = new HashMap<>();

    
     try {
        String sql = "SELECT id_suplier, nama_suplier FROM suplier";
        PreparedStatement st = conn.prepareStatement(sql);
        ResultSet rs = st.executeQuery();

        while (rs.next()) {
            String id = rs.getString("id_suplier");
            String nama = rs.getString("nama_suplier");

            cbSuplier.addItem(nama);
            suplierMap.put(nama, id);
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Gagal memuat data suplier");
        return; // keluar supaya dialog tidak ditampilkan
    }
     
    Object[] message = {
        "Restok untuk: " + pupuk.getNama(),
        "Jumlah:", tfJumlah,
        "Harga Beli (per sak):", tfHargaBeli,
        "Tanggal Expired",tglExp,
        "Suplier",cbSuplier
    };

    int option = JOptionPane.showConfirmDialog(null, message, "Restok Produk", JOptionPane.OK_CANCEL_OPTION);

    if (option == JOptionPane.OK_OPTION) {
        try {
            int jumlah = Integer.parseInt(tfJumlah.getText());
            double hargaBeli = Double.parseDouble(tfHargaBeli.getText());
             if (jumlah <= 0 || hargaBeli <= 0) {
                JOptionPane.showMessageDialog(null, "Jumlah dan harga beli harus lebih dari 0!");
                return;
            }
             Date hariIni = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String tglSekarang = sdf.format(hariIni);
            String tglExpired = sdf.format(tglExp.getDate());
            String namaSuplierDipilih = cbSuplier.getSelectedItem().toString();
            String idSuplier = suplierMap.get(namaSuplierDipilih); 
            if (tglExpired==null) {
                JOptionPane.showMessageDialog(this, "Tanggal expired harus terisi","ERROR",JOptionPane.ERROR_MESSAGE);
                return;
            }
            else if (tglExpired.compareTo(tglSekarang)<0) {
                JOptionPane.showMessageDialog(this, "Tanggal expired sudah terlewat","ERROR",JOptionPane.ERROR_MESSAGE);
                return;
            }
            simpanRestokKeDatabase(pupuk.getId(), jumlah, hargaBeli,tglExpired,idSuplier);
            JOptionPane.showMessageDialog(null, "Restok berhasil!");
            
            loadProduk();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Input tidak valid. Pastikan jumlah dan harga berupa angka.");
        }
    }
}
private void simpanRestokKeDatabase(String idPupuk, int jumlah, double hargaBeli, String tanggalExp,String idSuplier) {
    String idRestock = "RS-" + UUID.randomUUID().toString().substring(0, 8);
    String sql = "INSERT INTO restock (restock_qty, harga_beli, total_harga, id_suplier, id_user, id_pupuk,id_restock,tanggal_restock) " +
                 "VALUES (?, ?, ?, ?, ?, ?,?, NOW())";
    String updateTanggal = "UPDATE pupuk SET tgl_expired= ? WHERE id_pupuk = ?;";
    try (PreparedStatement st = conn.prepareStatement(sql);
         PreparedStatement stTanggalExp = conn.prepareStatement(updateTanggal)) {
        st.setInt(1, jumlah);
        st.setDouble(2, hargaBeli);
        st.setDouble(3, jumlah * hargaBeli);
        st.setString(4, idSuplier);
        st.setString(5, SessionManager.getIdUser());
        st.setString(6, idPupuk);
        st.setString(7, idRestock);

        st.executeUpdate();
        
        stTanggalExp.setString(1, tanggalExp);
        stTanggalExp.setString(2, idPupuk);
        
        stTanggalExp.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Gagal menyimpan restok: " + e.getMessage());
    }
}





    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables

    private void simpanEdite(String nama, double harga, int stok,String idPupuk) {
        String sqlEdit ="UPDATE pupuk SET nama_pupuk =?, harga_jual = ? , stock = ? WHERE id_pupuk =?";
        
        try(PreparedStatement pst = conn.prepareCall(sqlEdit)) {
            pst.setString(1, nama);
            pst.setDouble(2, harga);
            pst.setInt(3, stok);
            pst.setString(4, idPupuk);
            
            pst.executeUpdate();
            
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Edit gagal !"+e.getMessage(),"ERROR !",JOptionPane.ERROR_MESSAGE);
        }
    }

    private void diskonProduk(Pupuk pupuk) {
    JTextField namaDiskon = new JTextField();
    JTextField persenDiskon = new JTextField();
    JDateChooser tglMulai = new JDateChooser();
    tglMulai.setDateFormatString("yyyy-MM-dd");
    JDateChooser tglAkhir = new JDateChooser();
    tglAkhir.setDateFormatString("yyyy-MM-dd");
    
    Object[] message = {
        "Restok untuk: " + pupuk.getNama(),
        "Besar Diskon %", persenDiskon,
        "Periode mulai", tglMulai,
        "Periode berakhir",tglAkhir
    };
        int option = JOptionPane.showConfirmDialog(null, message, "Restok Produk", JOptionPane.OK_CANCEL_OPTION);

    if (option == JOptionPane.OK_OPTION) {
        try {
            double diskon = Integer.parseInt(persenDiskon.getText());
            
             if (diskon <=0) {
                JOptionPane.showMessageDialog(null, "Diskon harus lebih dari 0!");
                return;
            }
             String namaPromo = namaDiskon.getText();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
            String tglPeriodeMulai = sdf.format(tglMulai.getDate());
            String tglPeriodeHabis = sd.format(tglAkhir.getDate());
            Date periodeMulai = tglMulai.getDate();
            Date periodeHabis = tglAkhir.getDate();
            if (tglPeriodeMulai==null||tglPeriodeHabis==null) {
                JOptionPane.showMessageDialog(this, "Field tanggal tidak boleh kosong");
                return;
            }else if(periodeMulai.after(periodeHabis)){
             JOptionPane.showMessageDialog(this, "periode mulai tidak boleh di atas periode habis ","ERROR",JOptionPane.ERROR_MESSAGE);
             return;
            }

            simpanDiskon(pupuk.getId(), namaPromo, diskon, tglPeriodeMulai,tglPeriodeHabis );

            JOptionPane.showMessageDialog(null, "Produk berhasil di diskon");
            loadProduk();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Input tidak valid. Pastikan jumlah dan harga berupa angka.");
        }
    }
    
    }
private void simpanDiskon(String idPupuk,String nama,double diskon, String tglMulai, String tglAkhir) {
        String sqlDiskon ="INSERT INTO diskon (id_diskon, nama_diskon, id_pupuk, persen_diskon, tanggal_mulai, tanggal_selesai, aktif) VALUES (?, ?, ?, ?, ?, ?, TRUE)";
        String idDiskon = "DS-" + UUID.randomUUID().toString().substring(0, 6);
        try(PreparedStatement pst = conn.prepareCall(sqlDiskon)) {
           pst.setString(1, idDiskon);
           pst.setString(2, nama);
           pst.setString(3, idPupuk);
           pst.setDouble(4, diskon);
           pst.setString(5, tglMulai);
           pst.setString(6, tglAkhir);
           
           pst.executeUpdate();
            
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Edit gagal !"+e.getMessage(),"ERROR !",JOptionPane.ERROR_MESSAGE);
        }
    }
public void nonaktifkanDiskonKadaluarsa() {
    String sql = "UPDATE diskon SET aktif = 0 WHERE tanggal_selesai < CURDATE() AND aktif = 1";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        int updated = ps.executeUpdate();
        System.out.println("Jumlah diskon yang dinonaktifkan: " + updated);
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

// method untuk secrhing produk
   private void filterProduk(String keyword) {
     contentPanel.removeAll();

    List<Pupuk> pupukList = PupukDAO.getAllPupuk();
    for (Pupuk pupuk : pupukList) {
        if (pupuk.getNama() != null && pupuk.getNama().toLowerCase().contains(keyword)) {
            contentPanel.add(createCard(pupuk));
        }
    }

    contentPanel.revalidate();
    contentPanel.repaint();   
    }
}
