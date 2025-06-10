package kasir.manajemenakun;
import custom.*;
import java.awt.Frame;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

import kasir.manajemenakun.ManajemenAkun;
import java.awt.Color;
import java.awt.Component;
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
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
public class editAkunn extends javax.swing.JDialog {
    private ManajemenAkun manajemenPanel;
    private Connection con;
    private PreparedStatement stat;
    private koneksi k = new koneksi();
    private String Username;
    private String initialNama;
    private String initialPassword;
    private String initialRfid;
    public editAkunn(Frame parent, Connection con) {
        super(parent, true);
        this.con = con;
        initComponents();
        
        JPasswordField pwdField = text_password.getPasswordField();
        requirementsPanel.bindTo(pwdField);
        
        btnKonfirmasi.setEnabled(false);
        
        text_username.setEnabled(false);
        text_username.getTextField().setEditable(false);
        combo_role.setEnabled(false);
        
        ketentuanPassword.setVisible(false);
      
        combo_role.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                javax.swing.JList<?> list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus
            ) {
                JLabel label = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus
                );
                // Jika ini baris tampilan (bukan drop‐down), set warna font
                label.setForeground(new Color(151, 151, 151));
                return label;
            }
        });
        combo_role.addItem("Admin");
        combo_role.addItem("Kasir");
        combo_role.setForeground(new Color(151,151,151));
        
        labelPasswordError.setVisible(false);

        koneksi db = new koneksi();
        db.connect();
        this.con = db.getCon();
        
        PlainDocument doc = (PlainDocument) text_password.getPasswordField().getDocument();
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
        text_nama.getTextField().setForeground(Color.BLACK);
        text_nama.getTextField().getDocument().addDocumentListener(new InputListener());
        
        text_password.getPasswordField().setForeground(Color.BLACK);
        text_password.getPasswordField().getDocument().addDocumentListener(new InputListener());
        
        text_rfid.getTextField().setForeground(Color.BLACK);
        text_rfid.getTextField().getDocument().addDocumentListener(new InputListener());
        
        text_password.getPasswordField().addFocusListener(new FocusAdapter() {
        @Override public void focusLost(FocusEvent e) { cekInput(); }
        });
        text_rfid.getTextField().addFocusListener(new FocusAdapter() {
            @Override public void focusLost(FocusEvent e) { cekInput(); }
        });

        cekInput();
        
    }
    
    public void setUsername(String user) {
        this.Username = user;
    }

    private boolean validPassword(String password) {
        // Minimal 1 huruf kapital dan minimal 1 angka, minimal 8 karakter
        return password.length() >= 8
        && password.matches("^(?=.*[A-Z])(?=.*\\d).+$");
    }
    
    private void cekInput(){
        String nama = text_nama.getTextField().getText().trim();
        String password = new String(text_password.getPasswordField().getPassword()).trim();
        String rfidText = text_rfid.getTextField().getText().trim();
        
        if (password.isEmpty()) {
            requirementsPanel.setVisible(false);
            ketentuanPassword.setVisible(true);
        } else {
            ketentuanPassword.setVisible(false);
            requirementsPanel.setVisible(false);
            if(!validPassword(password)){
                requirementsPanel.setVisible(true);
            }
        }
        
        boolean passwordValid = validPassword(password); 
//        if (!passwordValid && !password.isEmpty()) {
//            labelPasswordError.setText("Input password sesuai ketentuan!");
//            labelPasswordError.setVisible(true);
//        } else {
//            labelPasswordError.setText("");
//            labelPasswordError.setVisible(false);
//        }
        boolean semuaTerisi = !nama.isEmpty() &&
                              !password.isEmpty();
        boolean formValid = passwordValid && semuaTerisi;
        btnKonfirmasi.setEnabled(formValid);
        if (formValid) {
            btnKonfirmasi.setBackground(new Color(70,155,126));
        } else {
            btnKonfirmasi.setBackground(new Color(150, 150, 150));
        }
    }
    
    public void setData(String user, String nama, String pass, String rfid, String role) {
        this.initialNama = (nama != null ? nama : "");
        this.initialPassword = (pass != null ? pass : "");
        this.initialRfid     = (rfid != null ? rfid : "");
        

        text_username.setText(user);
        text_nama.setText(nama);
        text_password.setText(pass);
        text_rfid.setText(String.valueOf(rfid));
        combo_role.setSelectedItem(String.valueOf(role));
        combo_role.repaint();
        cekInput();
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
        btnKonfirmasi = new custom.Button();
        labelPasswordError = new javax.swing.JLabel();
        text_password = new custom.PanelPasswordInput();
        text_nama = new custom.PanelInput();
        jLabel5 = new javax.swing.JLabel();
        requirementsPanel = new custom.RequirementsPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        PanelMain.setBackground(new java.awt.Color(255, 255, 255));
        PanelMain.setCornerRadius(35);
        PanelMain.setLayout(null);

        roundedPanelCustom2.setBackground(new java.awt.Color(70, 155, 126));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Edit Akun");

        javax.swing.GroupLayout roundedPanelCustom2Layout = new javax.swing.GroupLayout(roundedPanelCustom2);
        roundedPanelCustom2.setLayout(roundedPanelCustom2Layout);
        roundedPanelCustom2Layout.setHorizontalGroup(
            roundedPanelCustom2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundedPanelCustom2Layout.createSequentialGroup()
                .addContainerGap(172, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(170, 170, 170))
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
        jLabel2.setBounds(10, 250, 80, 16);

        ketentuanPassword.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        ketentuanPassword.setForeground(new java.awt.Color(204, 204, 204));
        ketentuanPassword.setText("<html><div style='text-align: justify; width:200px;'>Password minimal <b>8 karakter</b><br>dan maksimal <b>20 karakter</b><br>dengan kombinasi <b>huruf kapital.</b><br>dan <b>angka</b></div></html> ");
        PanelMain.add(ketentuanPassword);
        ketentuanPassword.setBounds(10, 170, 150, 50);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Nama");
        PanelMain.add(jLabel4);
        jLabel4.setBounds(10, 100, 80, 16);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("Password");
        PanelMain.add(jLabel6);
        jLabel6.setBounds(10, 150, 80, 16);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setText("Role ");
        PanelMain.add(jLabel7);
        jLabel7.setBounds(10, 300, 80, 16);

        text_rfid.setBorderColor(new java.awt.Color(200, 200, 200));
        text_rfid.setCornerRadius(15);
        PanelMain.add(text_rfid);
        text_rfid.setBounds(230, 250, 220, 30);

        text_username.setBorderColor(new java.awt.Color(200, 200, 200));
        text_username.setCornerRadius(15);
        text_username.setEnabled(false);
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
        combo_role.setBounds(230, 300, 220, 29);

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
        btnKonfirmasi.setBounds(350, 360, 100, 30);

        labelPasswordError.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        labelPasswordError.setForeground(new java.awt.Color(255, 102, 102));
        labelPasswordError.setText("Input password sesuai ketentuan!");
        PanelMain.add(labelPasswordError);
        labelPasswordError.setBounds(30, 360, 175, 16);

        text_password.setBorderColor(new java.awt.Color(200, 200, 200));
        PanelMain.add(text_password);
        text_password.setBounds(230, 150, 220, 30);

        text_nama.setBorderColor(new java.awt.Color(200, 200, 200));
        text_nama.setCornerRadius(15);
        text_nama.setEnabled(false);
        PanelMain.add(text_nama);
        text_nama.setBounds(230, 100, 220, 30);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("Username");
        PanelMain.add(jLabel5);
        jLabel5.setBounds(10, 50, 80, 16);

        requirementsPanel.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        PanelMain.add(requirementsPanel);
        requirementsPanel.setBounds(230, 180, 162, 48);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(PanelMain, javax.swing.GroupLayout.PREFERRED_SIZE, 459, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PanelMain, javax.swing.GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void combo_roleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combo_roleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_combo_roleActionPerformed

    private void btnKonfirmasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKonfirmasiActionPerformed
        try {
            String nama = text_nama.getTextField().getText().trim();
            String pass = new String(text_password.getPasswordField().getPassword()).trim();
            String rfid = text_rfid.getTextField().getText().trim();
            
            boolean namaChanged = !nama.equals(initialNama);
            boolean passChanged = !pass.equals(initialPassword);
            boolean rfidChanged = !rfid.equals(initialRfid);
                if (!passChanged && !rfidChanged && !namaChanged) {
                    JOptionPane.showMessageDialog(this, "Tidak terdapat perbaruan apapun");
                } else {
                    // Contoh query hanya memperbarui password dan/atau rfid
                    // (sesuaikan kolom mana yang mau diupdate)
                    String sql = "UPDATE pengguna SET nama = ?, password = ?, kode_rfid = ? WHERE id_user = ?";
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setString(1, nama);
                    ps.setString(2, pass);
                    ps.setString(3, rfid);
                    ps.setString(4, Username);

                    int updated = ps.executeUpdate();
                    if (updated > 0) {
                        JOptionPane.showMessageDialog(this, "Data berhasil diperbarui!");
                    } else {
                        JOptionPane.showMessageDialog(this, "ID akun tidak ditemukan.");
                    }
                }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saat menyimpan: " + ex.getMessage());
        } finally {
            dispose();
        }
    }//GEN-LAST:event_btnKonfirmasiActionPerformed

    /**
     * @param args the command line arguments
     */
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
            java.util.logging.Logger.getLogger(editAkunn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(editAkunn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(editAkunn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(editAkunn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                koneksi k = new koneksi();
                editAkunn dialog = new editAkunn(new javax.swing.JFrame(), k.getCon());
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private custom.RoundedPanel01 PanelMain;
    private custom.Button btnKonfirmasi;
    private custom.ComboBox combo_role;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel ketentuanPassword;
    private javax.swing.JLabel labelPasswordError;
    private custom.RequirementsPanel requirementsPanel;
    private custom.RoundedPanelCustom roundedPanelCustom2;
    private custom.PanelInput text_nama;
    private custom.PanelPasswordInput text_password;
    private custom.PanelInput text_rfid;
    private custom.PanelInput text_username;
    // End of variables declaration//GEN-END:variables
}
