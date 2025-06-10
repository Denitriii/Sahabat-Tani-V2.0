

package Menu;

import KasirTransaki.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import koneksi.koneksiDB;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import koneksi.SessionManager;
import koneksi.sessionRole;
import popUp.Struck;
import popUp.tambahProduk;


/**
 *
 * @author user
 */
public class Transaksi extends javax.swing.JFrame {

    /**
     * Creates new form Transaksi
     */
    private final Map<String, List<String>> jenisTerkunciPerPupuk = new HashMap<>();
    private Connection conn;
    private Timer timer;
    


    public Transaksi() { 
        conn = koneksiDB.BukaKoneksi();
        initComponents();
      
        txtNamaOwn.setText(SessionManager.getNamaPengguna());
        txtRole.setText(sessionRole.getRolePengguna());
       


        btnCash.addActionListener(e -> {
        if (btnCash.isSelected()) {
        btnQris.setSelected(false);
        btnTransfer.setSelected(false);
        } else {
                // Kalau mau, kalau btnCash di unselect, bisa lakukan sesuatu
        }
});
        btnQris.addActionListener(e -> {
        if (btnQris.isSelected()) {
        btnCash.setSelected(false);
        btnTransfer.setSelected(false);
        }
    });
    btnTransfer.addActionListener(e -> {
    if (btnTransfer.isSelected()) {
        btnCash.setSelected(false);
        btnQris.setSelected(false);
    }
});
 txtBarcode.getDocument().addDocumentListener(new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            restartTimer();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            // Tidak perlu aksi
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            // Tidak perlu aksi
        }
    });



    String[] kolom = {"Item", "Kemasan", "Quantitas", "Diskon", "Harga","Harga Diskon","Subtotal"};
        int[] kolomEditable = {1, 2}; 
        NonEditableColumnTableModel model = new NonEditableColumnTableModel(kolom, kolomEditable);
        tblKeranjang.setModel(model);
        
        TableColumn kolomJenis = tblKeranjang.getColumnModel().getColumn(1);
        kolomJenis.setCellEditor(new CustomJenisEditor(conn, tblKeranjang,jenisTerkunciPerPupuk));
        
        ((DefaultTableModel) tblKeranjang.getModel()).addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int col = e.getColumn();

                if (row >= 0 && (col == 1 || col == 2)) {
                    hitungDanUpdateHarga(row);
                    Total();
                }
            }
        });
    }

    private void hitungDanUpdateHarga(int row) {
        String namaPupuk = (String) tblKeranjang.getValueAt(row, 0);
        String jenis = (String) tblKeranjang.getValueAt(row, 1);

        if (namaPupuk == null || jenis == null) return;

        try {
            String sql = "SELECT id_pupuk, harga_jual, harga_eceran FROM pupuk WHERE nama_pupuk = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, namaPupuk);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String idPupuk = rs.getString("id_pupuk");
                double hargaAwal = jenis.equals("Satuan") ? rs.getDouble("harga_jual") : rs.getDouble("harga_eceran");
                double hargaAkhir = hargaAwal;

                if (jenis.equals("Satuan")) {
                    String sqlDiskon = "SELECT persen_diskon FROM diskon WHERE id_pupuk = ? AND aktif = 1";
                    PreparedStatement pstDiskon = conn.prepareStatement(sqlDiskon);
                    pstDiskon.setString(1, idPupuk);
                    ResultSet rsDiskon = pstDiskon.executeQuery();

                    if (rsDiskon.next()) {
                        double persenDiskon = rsDiskon.getDouble("persen_diskon");
                        double potongan = hargaAwal * persenDiskon / 100.0;
                        hargaAkhir = hargaAwal - potongan;

                        tblKeranjang.setValueAt(persenDiskon + "%", row, 3);
                        tblKeranjang.setValueAt(formatRupiah(hargaAwal), row, 4);
                        tblKeranjang.setValueAt(formatRupiah(hargaAkhir), row, 5);
                    } else {
                        tblKeranjang.setValueAt("0%", row, 3);
                        tblKeranjang.setValueAt(formatRupiah(hargaAwal), row, 4);
                        tblKeranjang.setValueAt(formatRupiah(0), row, 5);
                    }

                    rsDiskon.close();
                    pstDiskon.close();
                } else {
                    tblKeranjang.setValueAt("0%", row, 3);
                    tblKeranjang.setValueAt(formatRupiah(hargaAwal), row, 4);
                    tblKeranjang.setValueAt(formatRupiah(0), row, 5);
                }

                // Tambahkan jenis yang telah dipilih agar tidak muncul di entri lain
                List<String> terkunci = jenisTerkunciPerPupuk.getOrDefault(namaPupuk, new ArrayList<>());
                if (!terkunci.contains(jenis)) {
                    terkunci.add(jenis);
                    jenisTerkunciPerPupuk.put(namaPupuk, terkunci);
                }
            }

            rs.close();
            pst.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal menghitung harga: " + ex.getMessage());
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        roundedPanel8 = new customComponen.RoundedPanel();
        btnRiset = new javax.swing.JLabel();
        roundedPanel10 = new Menu.RoundedPanel();
        jLabel6 = new javax.swing.JLabel();
        roundedPanel1 = new Menu.RoundedPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        roundedPanel11 = new Menu.RoundedPanel();
        roundedPanel14 = new Menu.RoundedPanel();
        txtRole = new javax.swing.JLabel();
        txtNamaOwn = new javax.swing.JLabel();
        absensi = new Menu.RoundedPanel();
        roundedPanel2 = new Menu.RoundedPanel();
        txtNamaOwn1 = new javax.swing.JLabel();
        btnLogOut = new javax.swing.JLabel();
        roundedPanel3 = new Menu.RoundedPanel();
        konfirmasi = new javax.swing.JLabel();
        roundedPanel13 = new Menu.RoundedPanel();
        roundedPanel12 = new Menu.RoundedPanel();
        txtKembalian = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        txtBayar = new javax.swing.JTextField();
        txtxSubtotal = new javax.swing.JLabel();
        txtBarcode = new javax.swing.JTextField();
        btnTransfer = new customToggle(" transfer");
        btnCash = new customToggle("CASH");
        btnQris = new customToggle("QRIS");
        roundedScrollPane1 = new customComponen.RoundedScrollPane();
        tblKeranjang = new Custom.Jtablecustom();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(238, 238, 238));

        roundedPanel8.setBackground(new java.awt.Color(70, 155, 126));
        roundedPanel8.setPreferredSize(new java.awt.Dimension(103, 52));

        btnRiset.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        btnRiset.setForeground(new java.awt.Color(255, 255, 255));
        btnRiset.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btnRiset.setText("CLEAR");
        btnRiset.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRisetMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout roundedPanel8Layout = new javax.swing.GroupLayout(roundedPanel8);
        roundedPanel8.setLayout(roundedPanel8Layout);
        roundedPanel8Layout.setHorizontalGroup(
            roundedPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundedPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnRiset, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                .addContainerGap())
        );
        roundedPanel8Layout.setVerticalGroup(
            roundedPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundedPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnRiset, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                .addContainerGap())
        );

        roundedPanel10.setBackground(new java.awt.Color(70, 155, 126));
        roundedPanel10.setCornerRadius(25);

        jLabel6.setBackground(new java.awt.Color(70, 155, 126));
        jLabel6.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("HAPUS ");
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout roundedPanel10Layout = new javax.swing.GroupLayout(roundedPanel10);
        roundedPanel10.setLayout(roundedPanel10Layout);
        roundedPanel10Layout.setHorizontalGroup(
            roundedPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                .addContainerGap())
        );
        roundedPanel10Layout.setVerticalGroup(
            roundedPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundedPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                .addContainerGap())
        );

        roundedPanel1.setBackground(new java.awt.Color(255, 255, 255));
        roundedPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel19.setBackground(new java.awt.Color(105, 220, 109));
        jLabel19.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(105, 220, 109));
        jLabel19.setText("Sahabat ");
        roundedPanel1.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, -1, -1));

        jLabel20.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(105, 220, 109));
        jLabel20.setText("Tani");
        roundedPanel1.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        roundedPanel11.setBackground(new java.awt.Color(70, 155, 126));

        roundedPanel14.setBackground(new java.awt.Color(255, 255, 255));

        txtRole.setBackground(new java.awt.Color(153, 255, 102));
        txtRole.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtRole.setForeground(new java.awt.Color(70, 155, 126));
        txtRole.setText("role");

        txtNamaOwn.setBackground(new java.awt.Color(153, 255, 102));
        txtNamaOwn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtNamaOwn.setForeground(new java.awt.Color(70, 155, 126));
        txtNamaOwn.setText("nama");

        javax.swing.GroupLayout roundedPanel14Layout = new javax.swing.GroupLayout(roundedPanel14);
        roundedPanel14.setLayout(roundedPanel14Layout);
        roundedPanel14Layout.setHorizontalGroup(
            roundedPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(roundedPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNamaOwn)
                    .addComponent(txtRole))
                .addContainerGap(83, Short.MAX_VALUE))
        );
        roundedPanel14Layout.setVerticalGroup(
            roundedPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel14Layout.createSequentialGroup()
                .addComponent(txtNamaOwn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtRole, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout roundedPanel11Layout = new javax.swing.GroupLayout(roundedPanel11);
        roundedPanel11.setLayout(roundedPanel11Layout);
        roundedPanel11Layout.setHorizontalGroup(
            roundedPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundedPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(roundedPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        roundedPanel11Layout.setVerticalGroup(
            roundedPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(roundedPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        roundedPanel1.add(roundedPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(1130, 20, -1, 70));

        absensi.setBackground(new java.awt.Color(70, 155, 126));
        absensi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                absensiMouseClicked(evt);
            }
        });

        roundedPanel2.setBackground(new java.awt.Color(255, 255, 255));
        roundedPanel2.setCornerRadius(10);

        txtNamaOwn1.setBackground(new java.awt.Color(153, 255, 102));
        txtNamaOwn1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtNamaOwn1.setForeground(new java.awt.Color(70, 155, 126));
        txtNamaOwn1.setText("informasi absensi");

        javax.swing.GroupLayout roundedPanel2Layout = new javax.swing.GroupLayout(roundedPanel2);
        roundedPanel2.setLayout(roundedPanel2Layout);
        roundedPanel2Layout.setHorizontalGroup(
            roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundedPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtNamaOwn1)
                .addGap(44, 44, 44))
        );
        roundedPanel2Layout.setVerticalGroup(
            roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundedPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtNamaOwn1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout absensiLayout = new javax.swing.GroupLayout(absensi);
        absensi.setLayout(absensiLayout);
        absensiLayout.setHorizontalGroup(
            absensiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, absensiLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(roundedPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(98, 98, 98))
        );
        absensiLayout.setVerticalGroup(
            absensiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(absensiLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(roundedPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        roundedPanel1.add(absensi, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 20, 140, -1));

        btnLogOut.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnLogOut.setForeground(new java.awt.Color(70, 155, 126));
        btnLogOut.setText("Log Out");
        btnLogOut.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLogOutMouseClicked(evt);
            }
        });
        roundedPanel1.add(btnLogOut, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 70, 20));

        roundedPanel3.setBackground(new java.awt.Color(242, 150, 2));
        roundedPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        konfirmasi.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        konfirmasi.setForeground(new java.awt.Color(255, 255, 255));
        konfirmasi.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        konfirmasi.setText("PRINT STRUK");
        konfirmasi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                konfirmasiMouseClicked(evt);
            }
        });
        roundedPanel3.add(konfirmasi, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 0, 450, 65));

        roundedPanel13.setBackground(new java.awt.Color(70, 155, 126));
        roundedPanel13.setCornerRadius(30);
        roundedPanel13.setPreferredSize(new java.awt.Dimension(261, 60));

        roundedPanel12.setBackground(new java.awt.Color(241, 242, 242));
        roundedPanel12.setCornerRadius(30);
        roundedPanel12.setPreferredSize(new java.awt.Dimension(261, 60));

        txtKembalian.setFont(new java.awt.Font("SansSerif", 1, 48)); // NOI18N
        txtKembalian.setForeground(new java.awt.Color(70, 155, 126));
        txtKembalian.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        javax.swing.GroupLayout roundedPanel12Layout = new javax.swing.GroupLayout(roundedPanel12);
        roundedPanel12.setLayout(roundedPanel12Layout);
        roundedPanel12Layout.setHorizontalGroup(
            roundedPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel12Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(txtKembalian, javax.swing.GroupLayout.PREFERRED_SIZE, 392, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );
        roundedPanel12Layout.setVerticalGroup(
            roundedPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel12Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(txtKembalian, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jLabel21.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Total ");

        jLabel22.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Bayar");

        txtBayar.setBorder(null);
        txtBayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBayarActionPerformed(evt);
            }
        });
        txtBayar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBayarKeyPressed(evt);
            }
        });

        txtxSubtotal.setBackground(new java.awt.Color(241, 242, 242));
        txtxSubtotal.setFont(new java.awt.Font("SansSerif", 1, 48)); // NOI18N
        txtxSubtotal.setForeground(new java.awt.Color(241, 242, 242));
        txtxSubtotal.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        javax.swing.GroupLayout roundedPanel13Layout = new javax.swing.GroupLayout(roundedPanel13);
        roundedPanel13.setLayout(roundedPanel13Layout);
        roundedPanel13Layout.setHorizontalGroup(
            roundedPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel13Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(roundedPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(roundedPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(roundedPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(roundedPanel13Layout.createSequentialGroup()
                            .addComponent(jLabel21)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtxSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 367, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, roundedPanel13Layout.createSequentialGroup()
                            .addComponent(jLabel22)
                            .addGap(18, 18, 18)
                            .addComponent(txtBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(20, 20, 20))
        );
        roundedPanel13Layout.setVerticalGroup(
            roundedPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel13Layout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addGroup(roundedPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundedPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addGap(21, 21, 21))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundedPanel13Layout.createSequentialGroup()
                        .addComponent(txtxSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(roundedPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(txtBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(roundedPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        txtBarcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBarcodeActionPerformed(evt);
            }
        });

        btnTransfer.setBackground(new java.awt.Color(70, 155, 126));
        btnTransfer.setFont(new java.awt.Font("SansSerif", 1, 20)); // NOI18N
        btnTransfer.setForeground(new java.awt.Color(255, 255, 255));
        btnTransfer.setText("TRANFER");
        btnTransfer.setPreferredSize(new java.awt.Dimension(129, 61));
        btnTransfer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTransferActionPerformed(evt);
            }
        });

        btnCash.setBackground(new java.awt.Color(70, 155, 126));
        btnCash.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        btnCash.setForeground(new java.awt.Color(255, 255, 255));
        btnCash.setText("CASH");
        btnCash.setPreferredSize(new java.awt.Dimension(129, 61));

        btnQris.setBackground(new java.awt.Color(70, 155, 126));
        btnQris.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        btnQris.setForeground(new java.awt.Color(255, 255, 255));
        btnQris.setText("QRIS");
        btnQris.setPreferredSize(new java.awt.Dimension(129, 61));

        tblKeranjang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Item", "Jenis", "Quantitas", "Diskon", "Harga"
            }
        ));
        roundedScrollPane1.setViewportView(tblKeranjang);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(roundedScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 882, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btnQris, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(34, 34, 34)
                                .addComponent(btnCash, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(roundedPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, 462, Short.MAX_VALUE))
                        .addGap(90, 90, 90))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(roundedPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 465, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnTransfer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(roundedPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1441, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(roundedPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(roundedPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(txtBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(roundedPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(roundedPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnTransfer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnQris, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCash, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(29, 29, 29)
                        .addComponent(roundedPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(roundedScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 546, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(roundedPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(roundedPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(txtBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(90, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1460, 840));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        hapus();
    }//GEN-LAST:event_jLabel6MouseClicked

    private void txtBarcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBarcodeActionPerformed

    }//GEN-LAST:event_txtBarcodeActionPerformed

    private void btnRisetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRisetMouseClicked
      int confirm = JOptionPane.showConfirmDialog(
        null,
        "Apakah Anda yakin untuk mereset?",
        "Konfirmasi Reset",
        JOptionPane.YES_NO_OPTION
    );  
        if (confirm == JOptionPane.YES_OPTION) {
        clear();
         }

    }//GEN-LAST:event_btnRisetMouseClicked

    private void txtBayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBayarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBayarActionPerformed

    private void txtBayarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBayarKeyPressed
if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        String input = txtBayar.getText().replaceAll("[^\\d]", "");
        if (!input.isEmpty()) {
            try {
                int angka = Integer.parseInt(input);
                txtBayar.setText(formatRupiah(angka));
                txtBayar.setCaretPosition(txtBayar.getText().length());
            } catch (NumberFormatException ex) {
                // Jika terjadi error parsing, tidak melakukan apapun
            }
        }
        // Setelah menekan Enter, hitung kembalian
        kembalian();
//        txtBayar.setText("");
    }
    

    }//GEN-LAST:event_txtBayarKeyPressed

    private void konfirmasiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_konfirmasiMouseClicked
        SimpanTransaksi();
    }//GEN-LAST:event_konfirmasiMouseClicked

    private void btnTransferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransferActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTransferActionPerformed

    private void absensiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_absensiMouseClicked
       detailAbsensi popup = new detailAbsensi(null, true);
            popup.setLocationRelativeTo(null);
            popup.setVisible(true);
    }//GEN-LAST:event_absensiMouseClicked

    private void btnLogOutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogOutMouseClicked
         dispose();
            //kembali ke halaman login
            formLoginManual logout = new formLoginManual();
            logout.setVisible(true);
    }//GEN-LAST:event_btnLogOutMouseClicked

      private void restartTimer() {
        if (timer != null && timer.isRunning()) {
            timer.restart(); // Reset timer jika masih berjalan
        } else {
            timer = new Timer(800, new ActionListener() { 
                @Override
                public void actionPerformed(ActionEvent evt) {
                    scanBarcode();
                }
      
            });
            timer.setRepeats(false);
            timer.start();
        }
    }
        
    
     class NonEditableColumnTableModel extends DefaultTableModel {
    // index kolom yang bisa diedit (misalnya hanya kolom 1, 2, dan 3)
    private final int[] editableColumns;
    public NonEditableColumnTableModel(String[] columnNames, int[] editableColumns) {
        super(columnNames, 0); // 0 = baris awal
        this.editableColumns = editableColumns;
    }
    @Override
    public boolean isCellEditable(int row, int column) {
        for (int editableColumn : editableColumns) {
            if (column == editableColumn) return true;
        }
        return false;
    }
}
   private void SimpanTransaksi() {
    // Cek apakah keranjang kosong
    if (tblKeranjang.getRowCount() == 0) {
        JOptionPane.showMessageDialog(null, "Keranjang masih kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }

    String idTransaksi = "TR-" + UUID.randomUUID().toString().substring(0, 7);
    String teksTotal = txtxSubtotal.getText();
    String kembalian = txtKembalian.getText();
    int uangBayar = parseRupiah(txtBayar.getText());
    int total = parseRupiah(teksTotal);
    int uangKembalian = parseRupiah(kembalian);
    List<String[]> listStruk = new ArrayList<>();


    try {
        String sqlInsertTransaksi = "INSERT INTO transaksi (id_transaksi, total_harga, tanggal_transaksi, id_user) VALUES (?, ?, NOW(), ?)";
       String sqlInsertDetail = "INSERT INTO detail_transaksi (qty, id_transaksi, id_pupuk, tipe_transaksi) VALUES (?, ?, ?, ?)";
        String CheckStok = "SELECT stock_eceran, stock, berat_sak FROM pupuk WHERE id_pupuk = ?";
        String UpdateStok = "UPDATE pupuk SET stock_eceran = ?, stock = ? WHERE id_pupuk = ?";

        PreparedStatement pstTransaksi = conn.prepareStatement(sqlInsertTransaksi);
        PreparedStatement stmtCheckStok = conn.prepareStatement(CheckStok);
        PreparedStatement stmtUpdateStok = conn.prepareStatement(UpdateStok);
        PreparedStatement pstDetailTransaksi = conn.prepareStatement(sqlInsertDetail);

        // Simpan transaksi terlebih dahulu (sekali saja)
        pstTransaksi.setString(1, idTransaksi);
        pstTransaksi.setInt(2, total);
        pstTransaksi.setString(3, SessionManager.getIdUser());
        pstTransaksi.executeUpdate();
       

        for (int i = 0; i < tblKeranjang.getRowCount(); i++) {
            try {
                 
        String qtyStr = tblKeranjang.getValueAt(i, 2).toString().trim();
        String hargaPupuk = tblKeranjang.getValueAt(i,4).toString();
        int hargaFix = parseRupiah(hargaPupuk);
        
        
        int qty;

        if (qtyStr.isEmpty()) {
            qty = 1; // Default qty jika kosong
        } else {
            try {
                qty = Integer.parseInt(qtyStr);
             } catch (NumberFormatException nfe) {
                qty = 1; 
            }
            }
                String namaPupuk = tblKeranjang.getValueAt(i, 0).toString();
                String jenis = tblKeranjang.getValueAt(i, 1).toString();
                String idPupuk = getIdPupuk(namaPupuk);

                if (idPupuk == null) {
                    JOptionPane.showMessageDialog(null, "ID Pupuk tidak ditemukan untuk: " + namaPupuk);
                    continue;
                }
                if (qty<=0) {
                    JOptionPane.showMessageDialog(this,"Masukan quantits terlebih dahulu","ERROR",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                stmtCheckStok.setString(1, idPupuk);
                ResultSet rs = stmtCheckStok.executeQuery();

                if (rs.next()) {
                    int stokEceran = rs.getInt("stock_eceran");
                    int stokSak = rs.getInt("stock");
                    int beratSak = rs.getInt("berat_sak");

                    if (jenis.equalsIgnoreCase("Eceran")) {
                        while (stokEceran < qty && stokSak > 0) {
                            stokEceran += beratSak;
                            stokSak--;
                        }
                        if (stokEceran < qty) {
                            JOptionPane.showMessageDialog(null, "Stok tidak cukup untuk pupuk eceran: " + namaPupuk);
                            continue;
                        }
                        stokEceran -= qty;
                    } else if (jenis.equalsIgnoreCase("Satuan")) {
                        if (stokSak < qty) {
                            JOptionPane.showMessageDialog(null, "Stok tidak cukup untuk pupuk satuan: " + namaPupuk);
                            continue;
                        }
                        stokSak -= qty;
                    }

                    // Update stok
                    stmtUpdateStok.setInt(1, stokEceran);
                    stmtUpdateStok.setInt(2, stokSak);
                    stmtUpdateStok.setString(3, idPupuk);
                    stmtUpdateStok.executeUpdate();

                    // Simpan ke detail_transaksi
                    pstDetailTransaksi.setInt(1, qty);
                    pstDetailTransaksi.setString(2, idTransaksi);
                    pstDetailTransaksi.setString(3, idPupuk);
                    pstDetailTransaksi.setString(4, jenis.equalsIgnoreCase("Eceran") ? "eceran" : "satuan");
                    pstDetailTransaksi.executeUpdate();
                    
                   listStruk.add(new String[]{namaPupuk, String.valueOf(qty), String.valueOf(hargaFix)});
                } else {
                    JOptionPane.showMessageDialog(null, "Pupuk tidak ditemukan di database: " + namaPupuk);
                }

            } catch (Exception e) {
                
            }
        }

        // Tutup statement
        stmtCheckStok.close();
        stmtUpdateStok.close();
        pstDetailTransaksi.close();
        pstTransaksi.close();

        String metode = "";

       if (btnCash.isSelected()) metode = "Cash";
else if (btnQris.isSelected()) metode = "QRIS";
else if (btnTransfer.isSelected()) metode = "Transfer";

switch (metode) {
    case "Cash":
        
        break;
    case "QRIS":
        // tindakan khusus jika ada
        break;
    case "Transfer":
        // tindakan khusus jika ada
        break;
    default:
        JOptionPane.showMessageDialog(null, "Silakan pilih metode pembayaran!");
        return;
}

      
            Struck dialogStruk = new Struck(null, true);
            dialogStruk.displayStruk(idTransaksi, uangKembalian, uangBayar,total,metode, listStruk); 
            dialogStruk.setVisible(true);
        

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Gagal menyimpan transaksi: " + ex.getMessage(), "Kesalahan", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
     clear();
}
     private void Total() {
    double totalBelanja = 0; 
    for (int i = 0; i < tblKeranjang.getRowCount(); i++) {
        Object hargaDiskon = tblKeranjang.getValueAt(i, 5);
        Object hargaBiasa = tblKeranjang.getValueAt(i, 4);
       
         Object qtyObj = tblKeranjang.getValueAt(i, 2); 
         Object namaPupukObj = tblKeranjang.getValueAt(i, 0); 
        Object jenisObj = tblKeranjang.getValueAt(i, 1);
       int qty = 1;
       String jenis = jenisObj.toString();
        if (qtyObj != null && !qtyObj.toString().trim().isEmpty()) {
            try {
                qty = Integer.parseInt(qtyObj.toString());
                if (jenis.equalsIgnoreCase("Eceran") && qty > 10) {
        JOptionPane.showMessageDialog(null, "Batas pembelian eceran adalah 20 kg!");
        return; // Hentikan proses jika melebihi batas
    }
                if (qty <= 0) {
                    
                    JOptionPane.showMessageDialog(null, "Kuantitas tidak bisa 0 dan juga minus");
                     tblKeranjang.setValueAt("", i, 2);        
}
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "isi kuantitas yang bener sayangg ^_^ ");
                tblKeranjang.setValueAt("", i, 2);
                return;
            }
        }
        try {
            String namaPupuk = namaPupukObj.toString();
            String idPupuk = getIdPupuk(namaPupuk);

            String sqlStok = "SELECT stock, stock_eceran, berat_sak FROM pupuk WHERE id_pupuk = ?";
            PreparedStatement pst = conn.prepareStatement(sqlStok);
            pst.setString(1, idPupuk);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int stokSak = rs.getInt("stock");
                int stokEceran = rs.getInt("stock_eceran");
                int beratSak = rs.getInt("berat_sak");

                if (jenis.equalsIgnoreCase("Satuan") && qty > stokSak) {
                    JOptionPane.showMessageDialog(null, "Stok satuan tidak mencukupi untuk " + namaPupuk);
                     return;
                } else if (jenis.equalsIgnoreCase("Eceran")) {
                    int totalStokEceran = stokEceran;
                    int sisaStokSak = stokSak;
                    while (totalStokEceran < qty && sisaStokSak > 0) {
                        totalStokEceran += beratSak;
                        sisaStokSak--;
                    }
                    if (totalStokEceran < qty) {
                        JOptionPane.showMessageDialog(null, "Stok eceran tidak mencukupi untuk " + namaPupuk);
                        return;
                    }
                }
            }
            rs.close();
            pst.close();
        } catch (Exception e) {
            e.printStackTrace();
            
            return;
        }
        double harga = 0;
        if (hargaDiskon != null) {
            int diskonParsed = parseRupiah(hargaDiskon.toString());
            if (diskonParsed > 0) {
                harga = diskonParsed;
            } else if (hargaBiasa != null) {
                harga = parseRupiah(hargaBiasa.toString());
            }
        } else if (hargaBiasa != null) {
            harga = parseRupiah(hargaBiasa.toString());
        }
        double subtotal = harga * qty;
        totalBelanja += subtotal;
        tblKeranjang.setValueAt(formatRupiah(subtotal), i, 6);

    }

    txtxSubtotal.setText(formatRupiah(totalBelanja));
}
private void scanBarcode() {
    try {
        String sql = "SELECT nama_pupuk, tipe_pembelian FROM pupuk WHERE barcode=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, txtBarcode.getText());
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            String namaPupuk = rs.getString("nama_pupuk");
            int tipePembelian = rs.getInt("tipe_pembelian");
            DefaultTableModel model = (DefaultTableModel) tblKeranjang.getModel();

            int barisKosong = -1;
            int count = 0;

            // Cek baris yang belum dipilih jenisnya
            for (int i = 0; i < model.getRowCount(); i++) {
                String nama = (String) model.getValueAt(i, 0);
                String jenis = (String) model.getValueAt(i, 1);

                if (namaPupuk.equals(nama)) {
                    if (jenis == null || jenis.trim().isEmpty() || jenis.equalsIgnoreCase("click for select the type")) {
                        barisKosong = i;
                        break;
                    }
                }
            }

            if (barisKosong != -1) {
                JOptionPane.showMessageDialog(this, "Pilih jenis kemasan terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                txtBarcode.setText("");
                txtBarcode.requestFocus();
                rs.close();
                pst.close();
                return;
            }

            // Hitung jumlah baris dengan nama pupuk yang sama
            for (int i = 0; i < model.getRowCount(); i++) {
                if (namaPupuk.equals(model.getValueAt(i, 0))) {
                    count++;
                }
            }

            int batas = (tipePembelian == 1) ? 2 : 1;

            if (count >= batas) {
                JOptionPane.showMessageDialog(this,
                    "Pembelian untuk pupuk ini dibatasi maksimal " + batas + " kali.",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            } else {
                model.addRow(new Object[]{
                    namaPupuk,
                    "click for select the type",
                    "",
                    "",
                    ""
                });
            }

        } else {
            JOptionPane.showMessageDialog(this, "Barcode tidak cocok!", "ERROR", JOptionPane.ERROR_MESSAGE);
        }

        txtBarcode.setText("");
        txtBarcode.requestFocus();
        rs.close();
        pst.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    }
}


private void kembalian() {
    try {
        int bayar = parseRupiah(txtBayar.getText());
        int total = parseRupiah(txtxSubtotal.getText());
       
        if (total!= 0 ){
            
        if (bayar >= total) {
            int kembali = bayar - total;
            txtKembalian.setText(formatRupiah(kembali));
        } else {
            JOptionPane.showMessageDialog(null, "Uang Anda kurang");
            txtBayar.setText("");
        }
        
       
        }
        else {JOptionPane.showMessageDialog(null, "Belum ada barang yang diinput");
            txtBayar.setText("");}
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Isi nominal yang benar ya sayang ^_^ (gunakan angka saja)");
        txtBayar.setText("");
    }  
    
}

private String getIdPupuk(String nama){
    String idPupuk="";
    
    try {
        String sql ="SELECT id_pupuk FROM pupuk WHERE nama_pupuk = ?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, nama);
        
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            idPupuk = rs.getString("id_pupuk");
            
        }
        
    } catch (Exception e) {
    }
    return idPupuk;
}

 private void clear() {
    DefaultTableModel model = (DefaultTableModel) tblKeranjang.getModel();
    model.setRowCount(0); // Menghapus semua baris di tabel

    // Kosongkan field input dan label total
    txtBayar.setText("");
    txtxSubtotal.setText("");
    txtKembalian.setText("");

    // Bersihkan map jenis terkunci per pupuk
    jenisTerkunciPerPupuk.clear();

    // Perbarui total (jaga-jaga, meski tabel kosong)
    Total();
}

 private void hapus() {
    int terpilih = tblKeranjang.getSelectedRow();
    if (terpilih >= 0) {
        String namaPupuk = (String) tblKeranjang.getValueAt(terpilih, 0);
        String jenis = (String) tblKeranjang.getValueAt(terpilih, 1);

        // Hapus jenis dari list terkunci
        List<String> listJenis = jenisTerkunciPerPupuk.get(namaPupuk);
        if (listJenis != null) {
            listJenis.remove(jenis);
            if (listJenis.isEmpty()) {
                jenisTerkunciPerPupuk.remove(namaPupuk);
            }
        }

        // Hapus baris dari tabel
        DefaultTableModel model = (DefaultTableModel) tblKeranjang.getModel();
        model.removeRow(terpilih);

        // Perbarui total
        Total();
    }
}

 public static String formatRupiah(double value) {
        NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return rupiahFormat.format(value);
        }
 
 public static int parseRupiah(String text) {
    try {
        NumberFormat format = NumberFormat.getInstance(new Locale("id", "ID"));
        Number number = format.parse(text.replace("Rp", "").trim());
        return number.intValue(); // otomatis buang ,00
    } catch (ParseException e) {
        e.printStackTrace();
        return 0;
    }
} 
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        try {
    UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
    } catch (Exception ex) {
        ex.printStackTrace();
    }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Transaksi().setVisible(true);
            }
            
        });
    }
    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private Menu.RoundedPanel absensi;
    private javax.swing.JToggleButton btnCash;
    private javax.swing.JLabel btnLogOut;
    private javax.swing.JToggleButton btnQris;
    private javax.swing.JLabel btnRiset;
    private javax.swing.JToggleButton btnTransfer;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel konfirmasi;
    private Menu.RoundedPanel roundedPanel1;
    private Menu.RoundedPanel roundedPanel10;
    private Menu.RoundedPanel roundedPanel11;
    private Menu.RoundedPanel roundedPanel12;
    private Menu.RoundedPanel roundedPanel13;
    private Menu.RoundedPanel roundedPanel14;
    private Menu.RoundedPanel roundedPanel2;
    private Menu.RoundedPanel roundedPanel3;
    private customComponen.RoundedPanel roundedPanel8;
    private customComponen.RoundedScrollPane roundedScrollPane1;
    private Custom.Jtablecustom tblKeranjang;
    private javax.swing.JTextField txtBarcode;
    private javax.swing.JTextField txtBayar;
    private javax.swing.JLabel txtKembalian;
    private javax.swing.JLabel txtNamaOwn;
    private javax.swing.JLabel txtNamaOwn1;
    private javax.swing.JLabel txtRole;
    private javax.swing.JLabel txtxSubtotal;
    // End of variables declaration//GEN-END:variables
public void nonaktifkanDiskonKadaluarsa() {
    String sql = "UPDATE diskon SET aktif = 0 WHERE tanggal_selesai < CURDATE() AND aktif = 1";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        int updated = ps.executeUpdate();
        System.out.println("Jumlah diskon yang dinonaktifkan: " + updated);
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
private void updateHargaDiskon(int row) {
    // Ambil nama pupuk dan jenis kemasan dari baris yang diupdate
    String namaPupuk = (String) tblKeranjang.getValueAt(row, 0);
    String jenisKemasan = (String) tblKeranjang.getValueAt(row, 1);

    if (namaPupuk == null || namaPupuk.trim().isEmpty() || jenisKemasan == null || jenisKemasan.trim().isEmpty()) {
        return; // Kalau data belum lengkap, stop
    }

    // Ambil list terkunci untuk pupuk ini (jenis kemasan yang sudah dipakai)
    List<String> terkunci = jenisTerkunciPerPupuk.getOrDefault(namaPupuk, new ArrayList<>());

    // Kalau jenis kemasan belum ada di list, tambahkan
    if (!terkunci.contains(jenisKemasan)) {
        terkunci.add(jenisKemasan);
        jenisTerkunciPerPupuk.put(namaPupuk, terkunci);
    }

    try {
        // Query untuk ambil data harga pupuk sesuai nama pupuk
        String sql = "SELECT id_pupuk, harga_jual, harga_eceran FROM pupuk WHERE nama_pupuk = ?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, namaPupuk);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            String idPupuk = rs.getString("id_pupuk");
            double hargaAwal = jenisKemasan.equals("Satuan") ? rs.getDouble("harga_jual") : rs.getDouble("harga_eceran");
            double hargaAkhir = hargaAwal;

            if (jenisKemasan.equals("Satuan")) {
                // Cek diskon aktif untuk pupuk ini
                String sqlDiskon = "SELECT persen_diskon FROM diskon WHERE id_pupuk = ? AND aktif = 1";
                PreparedStatement pstDiskon = conn.prepareStatement(sqlDiskon);
                pstDiskon.setString(1, idPupuk);
                ResultSet rsDiskon = pstDiskon.executeQuery();

                if (rsDiskon.next()) {
                    double persenDiskon = rsDiskon.getDouble("persen_diskon");
                    double potongan = hargaAwal * (persenDiskon / 100.0);
                    hargaAkhir = hargaAwal - potongan;

                    tblKeranjang.setValueAt(persenDiskon + "%", row, 3); // Kolom Diskon
                    tblKeranjang.setValueAt(formatRupiah(hargaAwal), row, 4); // Kolom Harga
                    tblKeranjang.setValueAt(formatRupiah(hargaAkhir), row, 5); // Kolom Harga Diskon
                } else {
                    tblKeranjang.setValueAt("0%", row, 3);
                    tblKeranjang.setValueAt(formatRupiah(hargaAwal), row, 4);
                    tblKeranjang.setValueAt(formatRupiah(hargaAwal), row, 5);
                }

                rsDiskon.close();
                pstDiskon.close();
            } else {
                // Kalau eceran, tidak ada diskon
                tblKeranjang.setValueAt("0%", row, 3);
                tblKeranjang.setValueAt(formatRupiah(hargaAwal), row, 4);
                tblKeranjang.setValueAt(formatRupiah(hargaAwal), row, 5);
            }
        }

        rs.close();
        pst.close();

        // Update subtotal: subtotal = harga diskon * quantitas (kolom 2)
        Object qtyObj = tblKeranjang.getValueAt(row, 2);
        int qty = 0;
        if (qtyObj instanceof Integer) {
            qty = (Integer) qtyObj;
        } else if (qtyObj instanceof String) {
            try {
                qty = Integer.parseInt((String) qtyObj);
            } catch (NumberFormatException ex) {
                qty = 0;
            }
        }
        double hargaDiskon = parseRupiah((String) tblKeranjang.getValueAt(row, 5));
        double subtotal = hargaDiskon * qty;
        tblKeranjang.setValueAt(formatRupiah(subtotal), row, 6);

        Total(); // Panggil fungsi hitung total keseluruhan

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Gagal update harga dan diskon: " + ex.getMessage());
    }
}

}
