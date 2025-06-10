package kasir.manajemenakun;
import custom.*;
import custom.scrollBarCustom01;
import custom.RoundedPanelButton;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.BeanProperty;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import kasir.manajemenakun.koneksi;
import java.awt.*;
import java.sql.PreparedStatement;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;

public class ManajemenAkun extends JPanel {
    private koneksi k = new koneksi();
    private DefaultTableModel model;
    private ResultSet rs;
    private PreparedStatement stat;
    private TableRowSorter<DefaultTableModel> sorter;

    public ManajemenAkun() {
        setPreferredSize(new Dimension(1219, 710));
        initComponents();
        // sambungkan ke database
        k.connect();
        // gaya tabel
        setupTableStyle();
        // muat data
        
        model = new DefaultTableModel(
            new Object[]{"Username","Nama", "Password","Role","RFID","Tanggal",""}, 0
        ) { @Override public boolean isCellEditable(int row,int col){return col==6;} };
        sorter = new TableRowSorter<>(model);
        tabel.setModel(model);
//      tabel.setModel(new DefaultTableModel());

        // Hide ID column
//        if (tabel.getColumnModel().getColumnCount() > 0) {
//            tabel.removeColumn(tabel.getColumnModel().getColumn(0));
//        }
        
        tabel.setRowSorter(sorter);       
        searchPanel.setSearchListener(text -> {
            if (text == null || text.trim().isEmpty()) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(
                    RowFilter.regexFilter("(?i)" + Pattern.quote(text), 0)
                );
            }
        });
        refreshTable();
        // custom scrollbar
        initCustomScroll();
        // tombol tambah akun
        initTambahAkunButton();
        
        // Pindahkan fokus awal ke tabel
        SwingUtilities.invokeLater(() -> tabel.requestFocusInWindow());
    }

    private void setupTableStyle() {
        tabel.setShowGrid(false);
        tabel.setIntercellSpacing(new Dimension(0, 0));
        tabel.setRowHeight(40);
        tabel.getTableHeader().setReorderingAllowed(false);
        tabel.getTableHeader().setOpaque(false);
    }

    private void initTambahAkunButton() {
        RoundedPanelButton btn = new RoundedPanelButton();
        btn.setText("Tambah Akun");
        btn.setTextColor(Color.WHITE);
        btn.setPanelColor(Color.BLUE);
        btn.setCornerRadius(20);
        btn.addActionListener(e -> {
            Window w = SwingUtilities.getWindowAncestor(this);
            tambahAkun dialog = new tambahAkun((Frame) w, true, this);
            dialog.setLocationRelativeTo(w);
            dialog.setVisible(true);
            // refresh setelah dialog tutup
            refreshTable();
        });
//        panelKontrol.add(btn);
    }

    public void refreshTable() {
        model.setRowCount(0);
        try (PreparedStatement stat = k.getCon().prepareStatement(
                "SELECT * FROM pengguna")) {
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id_user"),
                    rs.getString("nama"),
                    rs.getString("password"),
                    rs.getString("role"),
                    rs.getString("kode_rfid"),
                    rs.getDate("tanggal"),
                    ""
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Gagal memuat data akun:\n" + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE
            );
        }

        setupButtonColumn();
        tampilkanTanggal();
        tampilkanJumlahAkun();
    }
    
    private void tampilkanTanggal() {
        try {
            // Kita gunakan query MIN dan MAX pada kolom 'tanggal'
            String sql = "SELECT MIN(tanggal) AS tgl_min, MAX(tanggal) AS tgl_max FROM pengguna";
            PreparedStatement ps = k.getCon().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                java.sql.Date minDate = rs.getDate("tgl_min");
                java.sql.Date maxDate = rs.getDate("tgl_max");

                // Tampilkan di JLabel—jika null, tampilkan tanda "-" atau teks lain sesuai keinginan
                if (minDate != null) {
                    tgl_terlama.setText(minDate.toString());
                } else {
                    tgl_terlama.setText("-");
                }

                if (maxDate != null) {
                    tgl_terbaru.setText(maxDate.toString());
                } else {
                    tgl_terbaru.setText("-");
                }
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Jika ingin menampilkan pesan ke user, bisa:
            // JOptionPane.showMessageDialog(this, "Gagal mengambil tanggal: " + e.getMessage());
        }
    }

    private void setupButtonColumn() {
        // view-index 5: kolom tombol
        if (tabel.getColumnCount() <= 6) return;
        TableColumn col = tabel.getColumnModel().getColumn(6);

        ActionPanel renderer = new ActionPanel(
            viewRow -> showEditDialog(viewRow),
            viewRow -> showDeleteConfirmation(viewRow)
        );
        ActionPanel editor = new ActionPanel(
            viewRow -> showEditDialog(viewRow),
            viewRow -> showDeleteConfirmation(viewRow)
        );

        col.setCellRenderer(renderer);
        col.setCellEditor(editor);
    }

    private void showEditDialog(int viewRow) {
        int modelRow = tabel.convertRowIndexToModel(viewRow);
        
        String user  = (String) model.getValueAt(modelRow, 0);
        String nama  = (String) model.getValueAt(modelRow, 1);
        String pass  = (String) model.getValueAt(modelRow, 2);
        String role  = (String) model.getValueAt(modelRow, 3);
        String rfid  = (String) model.getValueAt(modelRow, 4);

        Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
        editAkunn dialog = new editAkunn(parent, k.getCon());
        dialog.setUsername(user);
        
        dialog.setData(user, nama, pass, rfid, role);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
        refreshTable();
    }

    private void showDeleteConfirmation(int viewRow) {
        int modelRow = tabel.convertRowIndexToModel(viewRow);
        String user = (String) model.getValueAt(modelRow, 0);

        int ok = JOptionPane.showConfirmDialog(
            this,
            "Yakin hapus akun: " + user + "?",
            "Konfirmasi",
            JOptionPane.YES_NO_OPTION
        );
        if (ok == JOptionPane.YES_OPTION) {
            try {
                PreparedStatement d = k.getCon()
                    .prepareStatement("DELETE FROM pengguna WHERE id_user = ?");
                d.setString(1, user);
                d.executeUpdate();
                JOptionPane.showMessageDialog(this, "Akun terhapus!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
            refreshTable();
        }
    }

    public void tampilkanJumlahAkun() {
        try (Statement s = k.getCon().createStatement()){
            ResultSet t = s.executeQuery("SELECT COUNT(*) FROM pengguna");
            if (t.next()) label_totalAkun.setText("" + t.getInt(1));
            t = s.executeQuery("SELECT COUNT(*) FROM pengguna WHERE role='Owner'");
            if (t.next()) label_akunOwner.setText("" + t.getInt(1));
            t = s.executeQuery("SELECT COUNT(*) FROM pengguna WHERE role='Kasir'");
            if (t.next()) label_akunKasir.setText("" + t.getInt(1));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void initCustomScroll() {
        scrollBarCustom01 scroll = new scrollBarCustom01();
        jScrollPane1.setVerticalScrollBar(scroll);
    }

    // Inner class ActionPanel untuk render dan edit tombol aksi
    private class ActionPanel extends AbstractCellEditor
            implements TableCellRenderer, TableCellEditor {

        private final JPanel panel = new JPanel(new GridBagLayout());
        private final JButton btnEdit   = new JButton("✏");
        private final JButton btnDelete = new JButton("🗑");
        private int editingRow;
        private final Consumer<Integer> onEdit;
        private final Consumer<Integer> onDelete;

        public ActionPanel(Consumer<Integer> onEdit, Consumer<Integer> onDelete) {
            this.onEdit   = onEdit;
            this.onDelete = onDelete;
            panel.add(btnEdit);
            panel.add(btnDelete);

            btnEdit.addActionListener(e -> {
                fireEditingStopped();
                onEdit.accept(editingRow);
            });
            btnDelete.addActionListener(e -> {
                fireEditingStopped();
                onDelete.accept(editingRow);
            });
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            return panel;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            this.editingRow = row;
            return panel;
        }

        @Override
        public Object getCellEditorValue() { return null; }
    }

    // getters for scroll pane and header if needed
    public JScrollPane getScrollPaneAkun() { return jScrollPane1; }
    public JTableHeader getTableHeader() { return tabel.getTableHeader(); }



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        roundedPanel1 = new custom.RoundedPanel01();
        roundedPanel2 = new custom.RoundedPanel01();
        btn_tambahAkun = new custom.RoundedPanelButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        roundedPanel5 = new custom.RoundedPanel01();
        roundedPanel6 = new custom.RoundedPanel01();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabel = new custom.Jtablecustom01();
        label_totalAkun = new javax.swing.JLabel();
        label_akunOwner = new javax.swing.JLabel();
        label_akunKasir = new javax.swing.JLabel();
        tgl_terlama = new javax.swing.JLabel();
        tgl_terbaru = new javax.swing.JLabel();
        roundedPanel4 = new custom.RoundedPanel01();
        text_nama = new javax.swing.JLabel();
        text_profilerole = new javax.swing.JLabel();
        btn_Profile = new custom.ButtonPhotos();
        searchPanel = new custom.RoundedSearchPanel();
        jLabel1 = new javax.swing.JLabel();

        setLayout(new java.awt.CardLayout());

        roundedPanel1.setBackground(new java.awt.Color(238, 238, 238));
        roundedPanel1.setPreferredSize(new java.awt.Dimension(1219, 710));

        roundedPanel2.setBackground(new java.awt.Color(255, 255, 255));

        btn_tambahAkun.setBackground(new java.awt.Color(70, 155, 126));
        btn_tambahAkun.setForeground(new java.awt.Color(255, 255, 255));
        btn_tambahAkun.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_tambahAkun.setName("btnTambahAkun"); // NOI18N
        btn_tambahAkun.setText("Tambah Akun");
        btn_tambahAkun.setTextColor(null);
        btn_tambahAkun.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_tambahAkunMouseClicked(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(200, 200, 200));
        jLabel2.setText("<html><div style='text-align: justify; width:30px;'>Username&nbsp;&nbsp;:</div></html> ");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(200, 200, 200));
        jLabel3.setText("<html><div style='text-align: justify; width:30px;'>Owner&nbsp;&nbsp;:</div></html> ");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(200, 200, 200));
        jLabel4.setText("<html><div style='text-align: justify; width:30px;'>Kasir&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:</div></html> ");

        roundedPanel5.setBackground(new java.awt.Color(200, 200, 200));

        javax.swing.GroupLayout roundedPanel5Layout = new javax.swing.GroupLayout(roundedPanel5);
        roundedPanel5.setLayout(roundedPanel5Layout);
        roundedPanel5Layout.setHorizontalGroup(
            roundedPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1, Short.MAX_VALUE)
        );
        roundedPanel5Layout.setVerticalGroup(
            roundedPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        roundedPanel6.setBackground(new java.awt.Color(200, 200, 200));

        javax.swing.GroupLayout roundedPanel6Layout = new javax.swing.GroupLayout(roundedPanel6);
        roundedPanel6.setLayout(roundedPanel6Layout);
        roundedPanel6Layout.setHorizontalGroup(
            roundedPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1, Short.MAX_VALUE)
        );
        roundedPanel6Layout.setVerticalGroup(
            roundedPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(200, 200, 200));
        jLabel5.setText("<html><div style='text-align: justify; width:200px;'>Tanggal Terlama&nbsp;&nbsp;:</div></html> ");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(200, 200, 200));
        jLabel6.setText("<html><div style='text-align: justify; width:200px;'>Tanggal Terbaru&nbsp;&nbsp;&nbsp;:</div></html> ");

        tabel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jScrollPane1.setViewportView(tabel);

        label_totalAkun.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        label_totalAkun.setText("jLabel7");

        label_akunOwner.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        label_akunOwner.setText("jLabel8");

        label_akunKasir.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        label_akunKasir.setText("jLabel9");

        tgl_terlama.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        tgl_terlama.setText("jLabel9");

        tgl_terbaru.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        tgl_terbaru.setText("jLabel9");

        javax.swing.GroupLayout roundedPanel2Layout = new javax.swing.GroupLayout(roundedPanel2);
        roundedPanel2.setLayout(roundedPanel2Layout);
        roundedPanel2Layout.setHorizontalGroup(
            roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(label_totalAkun)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(roundedPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addGroup(roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(roundedPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(label_akunOwner))
                    .addGroup(roundedPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(label_akunKasir)))
                .addGap(218, 218, 218)
                .addComponent(roundedPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addGroup(roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(roundedPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(tgl_terbaru))
                    .addGroup(roundedPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(tgl_terlama)))
                .addGap(175, 175, 175))
            .addGroup(roundedPanel2Layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addGroup(roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btn_tambahAkun, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1116, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 25, Short.MAX_VALUE))
        );
        roundedPanel2Layout.setVerticalGroup(
            roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(roundedPanel2Layout.createSequentialGroup()
                            .addGroup(roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(tgl_terlama))
                            .addGap(18, 18, 18)
                            .addGroup(roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(tgl_terbaru)))
                        .addComponent(roundedPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundedPanel2Layout.createSequentialGroup()
                            .addGroup(roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(label_akunOwner))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(label_akunKasir)))
                        .addComponent(roundedPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(roundedPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(label_totalAkun)))
                .addGap(32, 32, 32)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_tambahAkun, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        roundedPanel4.setBackground(new java.awt.Color(255, 255, 255));
        roundedPanel4.setLayout(null);

        text_nama.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        text_nama.setForeground(new java.awt.Color(151, 151, 151));
        text_nama.setText("Mas Sugeng");
        roundedPanel4.add(text_nama);
        text_nama.setBounds(40, 0, 70, 20);

        text_profilerole.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        text_profilerole.setText("Owner");
        roundedPanel4.add(text_profilerole);
        text_profilerole.setBounds(40, 20, 50, 10);

        btn_Profile.setPanelColor(new java.awt.Color(255, 255, 255));
        roundedPanel4.add(btn_Profile);
        btn_Profile.setBounds(0, 0, 35, 35);

        searchPanel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        searchPanel.setBackground(new java.awt.Color(255, 255, 255));
        searchPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                searchPanelMousePressed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("MANAJEMEN AKUN");

        javax.swing.GroupLayout roundedPanel1Layout = new javax.swing.GroupLayout(roundedPanel1);
        roundedPanel1.setLayout(roundedPanel1Layout);
        roundedPanel1Layout.setHorizontalGroup(
            roundedPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(roundedPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(roundedPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(roundedPanel1Layout.createSequentialGroup()
                            .addComponent(searchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 514, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(roundedPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(roundedPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        roundedPanel1Layout.setVerticalGroup(
            roundedPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundedPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(roundedPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(searchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(roundedPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(roundedPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(89, Short.MAX_VALUE))
        );

        add(roundedPanel1, "card2");
    }// </editor-fold>//GEN-END:initComponents

    private void btn_tambahAkunMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_tambahAkunMouseClicked
        Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
        tambahAkun dialog = new tambahAkun(parent, true, this);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);

        refreshTable();
    }//GEN-LAST:event_btn_tambahAkunMouseClicked

    private void searchPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchPanelMousePressed
    

    }//GEN-LAST:event_searchPanelMousePressed
    public static void main(String[] args) {
         javax.swing.SwingUtilities.invokeLater(new Runnable() {
        public void run() {
            new ManajemenAkun().setVisible(true); // Tampilkan GUI
        }
    });
        System.out.println("Aplikasi Manajemen Akun dimulai...");
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private custom.ButtonPhotos btn_Profile;
    private custom.RoundedPanelButton btn_tambahAkun;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel label_akunKasir;
    private javax.swing.JLabel label_akunOwner;
    private javax.swing.JLabel label_totalAkun;
    private custom.RoundedPanel01 roundedPanel1;
    private custom.RoundedPanel01 roundedPanel2;
    private custom.RoundedPanel01 roundedPanel4;
    private custom.RoundedPanel01 roundedPanel5;
    private custom.RoundedPanel01 roundedPanel6;
    private custom.RoundedSearchPanel searchPanel;
    private custom.Jtablecustom01 tabel;
    private javax.swing.JLabel text_nama;
    private javax.swing.JLabel text_profilerole;
    private javax.swing.JLabel tgl_terbaru;
    private javax.swing.JLabel tgl_terlama;
    // End of variables declaration//GEN-END:variables
}


