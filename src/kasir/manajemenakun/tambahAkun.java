/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package kasir.manajemenakun;
import custom.*;
import kasir.manajemenakun.ManajemenAkun;
import javax.swing.JPasswordField;
import custom.RequirementsPanel; 
import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import kasir.manajemenakun.koneksi;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
import javax.swing.text.DocumentFilter.FilterBypass;
import javax.swing.text.BadLocationException;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.awt.Frame;

public class tambahAkun extends javax.swing.JDialog {
    private ManajemenAkun manajemenPanel;
    private Connection con;
    private PreparedStatement stat;
    private koneksi k = new koneksi();
    private RequirementsPanel pwdReqPanel;

    public tambahAkun(Frame parent, boolean modal, ManajemenAkun panel) {
        super(parent, modal);
        initComponents();
        this.manajemenPanel = panel;
        k.connect();
        this.con = k.getCon();
           
        btnKonfirmasi.setEnabled(false);
       
        combo_role.addItem("Owner");
        combo_role.addItem("Kasir");

        combo_role.setSelectedIndex(-1); 
        
        ketentuanUsername.setVisible(true);
        ketentuanPassword.setVisible(true);
//        labelPasswordError.setVisible(false);
        labelUsernameError.setVisible(false);
        
        PlainDocument doc = (PlainDocument) text_password.getPasswordField().getDocument();
        PlainDocument userDoc = (PlainDocument) text_username.getTextField().getDocument();
        PlainDocument namaDoc = (PlainDocument) text_nama.getTextField().getDocument();
        
        doc.setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if ((fb.getDocument().getLength() + string.length()) <= 20) {
                    super.insertString(fb, offset, string, attr);
                }
                // Jika sudah 20, input tidak ditambahkan
            }

            @Override
            public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if ((fb.getDocument().getLength() - length + (text != null ? text.length() : 0)) <= 20) {
                    super.replace(fb, offset, length, text, attrs);
                }
                // Jika sudah 20, input tidak diganti
            }
        });
        
        userDoc.setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (fb.getDocument().getLength() + string.length() <= 20) {
                    super.insertString(fb, offset, string, attr);
                }
            }
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (fb.getDocument().getLength() - length + (text != null ? text.length() : 0) <= 20) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
        
        namaDoc.setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (fb.getDocument().getLength() + string.length() <= 30) {
                    super.insertString(fb, offset, string, attr);
                }
            }
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (fb.getDocument().getLength() - length + (text != null ? text.length() : 0) <= 30) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
        
        text_username.getTextField().getDocument().addDocumentListener(new InputListener());
        text_nama.getTextField().getDocument().addDocumentListener(new InputListener());
        text_password.getPasswordField().getDocument().addDocumentListener(new InputListener());
        text_rfid.getTextField().getDocument().addDocumentListener(new InputListener());
        combo_role.addActionListener(e -> cekInput());
        
            text_username.getTextField().addFocusListener(new FocusAdapter() {
            @Override public void focusLost(FocusEvent e) { cekInput(); }
        });
        JPasswordField pwdField = text_password.getPasswordField();
        requirementsPanel.bindTo(pwdField);
        
        cekInput();
        
    }
    
    
    private boolean validPassword(String password) {
        // Minimal 1 huruf kapital dan minimal 1 angka, minimal 8 karakter
        return password.length() >= 8
        && password.matches("^(?=.*[A-Z])(?=.*\\d).+$");
    }

    private boolean usernameExists(String username) {
        if (username == null || username.isEmpty()) return false;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM pengguna WHERE id_user = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private boolean usernameMin() {
        String user = text_username.getTextField().getText().trim();
        return user.length() > 4;
    }   

    private void cekInput() {
        String username = text_username.getTextField().getText().trim();
        String nama = text_nama.getTextField().getText().trim();
        String password = new String(text_password.getPasswordField().getPassword()).trim();
        String rfidText = text_rfid.getTextField().getText().trim();
        Object selectedRole = combo_role.getSelectedItem();
        
        if (!usernameMin()) {
            ketentuanUsername.setVisible(true);
        } else {
            ketentuanUsername.setVisible(false);
        }

        if (password.isEmpty()) {
            ketentuanPassword.setVisible(true);
        } else {
            ketentuanPassword.setVisible(false);
        }
        
        boolean semuaTerisi = !username.isEmpty() &&
                              !nama.isEmpty() &&
                              !password.isEmpty() &&
                              selectedRole != null;
//                              !rfidText.isEmpty();
        
        boolean usernameValid = !username.isEmpty() && !usernameExists(username) && usernameMin();
        boolean passwordValid = validPassword(password);

        // Tampilkan error username jika username sudah terdaftar
        if (!username.isEmpty()) {
            if (usernameExists(username)) {
                labelUsernameError.setText("Username telah terdaftar!");
                labelUsernameError.setVisible(true);
            } else {
                labelUsernameError.setVisible(false);
            }
        } else {
            labelUsernameError.setVisible(false);
        }

        String selected = (String) combo_role.getSelectedItem();
        // valid kalau bukan placeholder:
        boolean roleValid = selected != null && !selected.equals("Pilih role");
        // btnSave aktif jika semua terisi, password valid, username tidak ada di DB, dan RFID valid

        boolean formValid = semuaTerisi && passwordValid && usernameValid && roleValid;
        btnKonfirmasi.setEnabled(formValid);
        if (formValid) {
            btnKonfirmasi.setBackground(new Color(70,155,126)); // atau warna default
        } else {
            btnKonfirmasi.setBackground(new Color(150, 150, 150));
        }
    }
    
        private class InputListener implements DocumentListener {
        @Override public void insertUpdate(DocumentEvent e) { cekInput(); }
        @Override public void removeUpdate(DocumentEvent e) { cekInput(); }
        @Override public void changedUpdate(DocumentEvent e) { cekInput(); }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PanelMain = new custom.RoundedPanel01();
        roundedPanelCustom2 = new custom.RoundedPanelCustom();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        ketentuanPassword = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        text_rfid = new custom.PanelInput();
        text_username = new custom.PanelInput();
        combo_role = new custom.ComboBox();
        text_password = new custom.PanelPasswordInput();
        btnKonfirmasi = new custom.Button();
        ketentuanUsername = new javax.swing.JLabel();
        labelUsernameError = new javax.swing.JLabel();
        text_nama = new custom.PanelInput();
        jLabel9 = new javax.swing.JLabel();
        requirementsPanel = new custom.RequirementsPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        PanelMain.setBackground(new java.awt.Color(255, 255, 255));
        PanelMain.setCornerRadius(35);
        PanelMain.setLayout(null);

        roundedPanelCustom2.setBackground(new java.awt.Color(70, 155, 126));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Tambah Akun Baru");

        javax.swing.GroupLayout roundedPanelCustom2Layout = new javax.swing.GroupLayout(roundedPanelCustom2);
        roundedPanelCustom2.setLayout(roundedPanelCustom2Layout);
        roundedPanelCustom2Layout.setHorizontalGroup(
            roundedPanelCustom2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundedPanelCustom2Layout.createSequentialGroup()
                .addContainerGap(115, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(113, 113, 113))
        );
        roundedPanelCustom2Layout.setVerticalGroup(
            roundedPanelCustom2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedPanelCustom2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        PanelMain.add(roundedPanelCustom2);
        roundedPanelCustom2.setBounds(0, 0, 459, 40);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("RFID");
        PanelMain.add(jLabel2);
        jLabel2.setBounds(10, 290, 80, 16);

        ketentuanPassword.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        ketentuanPassword.setForeground(new java.awt.Color(204, 204, 204));
        ketentuanPassword.setText("<html><div style='text-align: justify; width:200px;'>Password minimal <b>8 karakter</b><br>dan maksimal <b>20 karakter</b><br>dengan kombinasi <b>huruf kapital.</b><br>dan <b>angka</b></div></html> ");
        PanelMain.add(ketentuanPassword);
        ketentuanPassword.setBounds(10, 210, 150, 50);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Username");
        PanelMain.add(jLabel4);
        jLabel4.setBounds(10, 50, 80, 16);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("Name");
        PanelMain.add(jLabel6);
        jLabel6.setBounds(10, 140, 80, 16);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setText("Role ");
        PanelMain.add(jLabel7);
        jLabel7.setBounds(10, 340, 80, 16);

        text_rfid.setBorderColor(new java.awt.Color(200, 200, 200));
        text_rfid.setCornerRadius(15);
        PanelMain.add(text_rfid);
        text_rfid.setBounds(230, 290, 220, 30);

        text_username.setBorderColor(new java.awt.Color(200, 200, 200));
        text_username.setCornerRadius(15);
        PanelMain.add(text_username);
        text_username.setBounds(230, 50, 220, 30);

        combo_role.setArrowButtonColor(new java.awt.Color(0, 0, 0));
        combo_role.setBorderColor(new java.awt.Color(200, 200, 200));
        combo_role.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combo_roleActionPerformed(evt);
            }
        });
        PanelMain.add(combo_role);
        combo_role.setBounds(230, 340, 220, 29);

        text_password.setBorderColor(new java.awt.Color(200, 200, 200));
        text_password.setCornerRadius(15);
        PanelMain.add(text_password);
        text_password.setBounds(230, 190, 220, 30);

        btnKonfirmasi.setBackground(new java.awt.Color(70, 155, 126));
        btnKonfirmasi.setText("Konfirmasi");
        btnKonfirmasi.setTextColor(new java.awt.Color(255, 255, 255));
        btnKonfirmasi.setTextFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnKonfirmasi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKonfirmasiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout btnKonfirmasiLayout = new javax.swing.GroupLayout(btnKonfirmasi);
        btnKonfirmasi.setLayout(btnKonfirmasiLayout);
        btnKonfirmasiLayout.setHorizontalGroup(
            btnKonfirmasiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        btnKonfirmasiLayout.setVerticalGroup(
            btnKonfirmasiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        PanelMain.add(btnKonfirmasi);
        btnKonfirmasi.setBounds(350, 390, 100, 30);

        ketentuanUsername.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        ketentuanUsername.setForeground(new java.awt.Color(204, 204, 204));
        ketentuanUsername.setText("<html><div style='text-align: justify; width:200px;'>Username harus <b>unik</b> dan<br>terdiri dari <b>4 karakter</b>.</div></html> ");
        PanelMain.add(ketentuanUsername);
        ketentuanUsername.setBounds(10, 70, 140, 30);

        labelUsernameError.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        labelUsernameError.setForeground(new java.awt.Color(255, 102, 102));
        labelUsernameError.setText("Username telah terdaftar!");
        PanelMain.add(labelUsernameError);
        labelUsernameError.setBounds(230, 80, 140, 20);

        text_nama.setBorderColor(new java.awt.Color(200, 200, 200));
        text_nama.setCornerRadius(15);
        PanelMain.add(text_nama);
        text_nama.setBounds(230, 140, 220, 30);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setText("Password");
        PanelMain.add(jLabel9);
        jLabel9.setBounds(10, 190, 80, 16);

        requirementsPanel.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        PanelMain.add(requirementsPanel);
        requirementsPanel.setBounds(230, 220, 162, 48);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(PanelMain, javax.swing.GroupLayout.PREFERRED_SIZE, 459, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PanelMain, javax.swing.GroupLayout.DEFAULT_SIZE, 441, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void combo_roleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combo_roleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_combo_roleActionPerformed

    private void btnKonfirmasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKonfirmasiActionPerformed
            try {
        String username = text_username.getTextField().getText().trim();
        String nama = text_nama.getTextField().getText().trim();
        String password = new String(text_password.getPasswordField().getPassword()).trim();
        String rfid = text_rfid.getTextField().getText().trim();
        String role = ((String)combo_role.getSelectedItem()).trim();
        
//        if (!password.matches(".*[A-Z].*") || !password.matches(".*\\d.*")) {
//            labelPasswordError.setText("Input Password yang valid!");
//            labelPasswordError.setVisible(true); // tampilkan pesan error
//            return; // hentikan proses simpan
//        } else {
//            labelPasswordError.setVisible(false); // sembunyikan jika valid
//        }
        
        stat = con.prepareStatement("INSERT INTO pengguna (id_user, nama, password, kode_rfid, role) VALUES (?, ?, ?, ?, ?)");
        stat.setString(1, username);
        stat.setString(2, nama);
        stat.setString(3, password);
        stat.setString(4, rfid);
        stat.setString(5, role);
        stat.executeUpdate();

        JOptionPane.showMessageDialog(this, "Data berhasil disimpan");

        if (manajemenPanel != null) {
            manajemenPanel.tampilkanJumlahAkun();
            manajemenPanel.refreshTable();
        }
        dispose(); // Tutup form setelah simpan
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage());
    }
    }//GEN-LAST:event_btnKonfirmasiActionPerformed

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(tambahAkun.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(tambahAkun.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(tambahAkun.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(tambahAkun.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the dialog */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                tambahAkun dialog = new tambahAkun((java.awt.Frame) null, true, null);
//                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
//                    @Override
//                    public void windowClosing(java.awt.event.WindowEvent e) {
//                        System.exit(0);
//                    }
//                });
//                dialog.setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private custom.RoundedPanel01 PanelMain;
    private custom.Button btnKonfirmasi;
    private custom.ComboBox combo_role;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel ketentuanPassword;
    private javax.swing.JLabel ketentuanUsername;
    private javax.swing.JLabel labelUsernameError;
    private custom.RequirementsPanel requirementsPanel;
    private custom.RoundedPanelCustom roundedPanelCustom2;
    private custom.PanelInput text_nama;
    private custom.PanelPasswordInput text_password;
    private custom.PanelInput text_rfid;
    private custom.PanelInput text_username;
    // End of variables declaration//GEN-END:variables
}
