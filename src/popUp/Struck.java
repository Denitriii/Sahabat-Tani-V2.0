/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package popUp;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import javax.swing.JOptionPane;
import javax.swing.JEditorPane;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.text.NumberFormat;
import java.util.Locale;
import koneksi.koneksiDB;
import java.awt.Dimension;
import java.awt.print.Paper;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import koneksi.SessionManager;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.List;
import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;


/**
 *
 * @author ASUS
 */

public class Struck extends javax.swing.JDialog {
    
    // Menambahkan objek Connection
    private final Connection conn;
//    private JEditorPane jEditorPane1;    
    
    public Struck(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        // Membuka koneksi database seperti pada contoh yang Anda kirim
        conn = koneksiDB.BukaKoneksi();
    }
    public void printRawStruk(String strukRawText) {
    try {
        // Cari PrintService bernama “POS80 Printer” atau driver ESC/POS
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        PrintService posPrinter = null;
        for (PrintService svc : services) {
            if (svc.getName().equalsIgnoreCase("POS80 Printer") 
                || svc.getName().toLowerCase().contains("pos80")) {
                posPrinter = svc;
                break;
            }
        }
        if (posPrinter == null) {
            JOptionPane.showMessageDialog(this, "Printer POS80 tidak ditemukan!");
            return;
        }

        // Siapkan dokumen “RAW” (flavor: AUTOSENSE / BYTE_ARRAY)
        DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
        DocPrintJob job = posPrinter.createPrintJob();

        // Konversi String (termasuk ESC/POS codes) ke byte[]
        byte[] bytes = strukRawText.getBytes("ISO-8859-1");
        Doc doc = new SimpleDoc(bytes, DocFlavor.BYTE_ARRAY.AUTOSENSE, null);

        // Atribut cetak—bisa kosong
        PrintRequestAttributeSet attrs = new HashPrintRequestAttributeSet();
        attrs.add(new Copies(1));

        // Kirim print job
        job.print(doc, attrs);
    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error saat cetak raw: " + ex.getMessage());
    }
}
public void displayStruk(String idTransaksi, int uangKembalian, int uangBayar, double total, String metode,List<String[]> listStruk) {
try {
        NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

        StringBuilder html = new StringBuilder();
        html.append("<html>")
            .append("<body style='font-family:times new roman; font-size:10pt; width:200px;'>")
            .append("<div style='text-align:center;'>")
            .append("<b>SAHABAT TANI</b><br>")
            .append("<b>Toko Pupuk & Pertanian</b><br>")
            .append("Alamat: Rowo Indah, Kabupaten Jember<br>")
            .append("HP: 0821-3456-7890<br>")
            .append("<hr style='border:none; border-top:1px solid #000;'/>")
            .append("</div>");

        html.append("<div style='text-align:left;'>")
            .append("Kode Transaksi : ").append(idTransaksi).append("<br>")
            .append("Kasir          : ").append(SessionManager.getNamaPengguna()).append("<br>")
            .append("Tanggal        : ").append(LocalDate.now().toString()).append("<br>")
            .append("<hr style='border:none; border-top:1px solid #000;'/>")
            .append("Produk:<br>");
//        NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
//        String query = "SELECT t.id_transaksi, t.tanggal_transaksi, p.nama_pupuk, dt.qty, p.harga_jual, (dt.qty * p.harga_jual) AS subtotal "
//                     + "FROM db_kasirpupuk.transaksi t "
//                     + "JOIN db_kasirpupuk.detail_transaksi dt ON t.id_transaksi = dt.id_transaksi "
//                     + "JOIN db_kasirpupuk.pupuk p ON dt.id_pupuk = p.id_pupuk "
//                     + "WHERE t.id_transaksi = ?";
//
//        PreparedStatement stmt = conn.prepareStatement(query);
//        stmt.setString(1, idTransaksi);
//        ResultSet rs = stmt.executeQuery();
//
//        // Mulai bangun konten HTML dengan font‐size kecil (8pt) dan monospace
//        StringBuilder html = new StringBuilder();
//        html.append("<html>")
//            // Body dengan style global: monospace, ukuran 8pt
//            .append("<body style='font-family:times new roman; font-size:10pt; width:200px;'>")
//            // Header, rata tengah
//            .append("<div style='text-align:center;'>")
//            .append("<b>SAHABAT TANI</b><br>")
//            .append("<b>Toko Pupuk & Pertanian</b><br>")
//            .append("Alamat: Rowo Indah, Kabupaten Jember<br>")
//            .append("HP: 0821-3456-7890<br>")
//            .append("<hr style='border:none; border-top:1px solid #000;'/>")
//            .append("</div>");
//
//        // Header transaksi, rata kiri
//        html.append("<div style='text-align:left;'>")
//            .append("Kode Transaksi : ").append(idTransaksi).append("<br>")
//            .append("Kasir          : ").append(SessionManager.getNamaPengguna()).append("<br>");
//
//        if (rs.next()) {
//            html.append("Tanggal        : ")
//                .append(rs.getDate("tanggal_transaksi").toString())
//                .append("<br>")
//                .append("<hr style='border:none; border-top:1px solid #000;'/>");

            // Daftar produk
            for (String[] item : listStruk) {
            String nama = item[0];
            int qty = Integer.parseInt(item[1]);
            double harga = Double.parseDouble(item[2]);
            double subtotal = qty * harga;

            html.append(nama).append("<br>");
            html.append("&nbsp;&nbsp;")
                .append(qty).append(" x ")
                .append(rupiahFormat.format(harga))
                .append(" = ")
                .append(rupiahFormat.format(subtotal))
                .append("<br>");
            } 

            // Total, Bayar, Kembalian, Metode
            html.append("<br>");
            html.append("TOTAL: ").append(rupiahFormat.format(total)).append("<br>");
            html.append("Bayar: ").append(rupiahFormat.format(uangBayar)).append("<br>");
            html.append("Kembalian: ").append(rupiahFormat.format(uangKembalian)).append("<br>");
            html.append("Metode Pembayaran: ").append(metode).append("<br>");
            html.append("<hr style='border:none; border-top:1px solid #000;'/>");
        
        html.append("</div>"); // tutup div rata kiri

        // Footer, rata tengah
        html.append("<div style='text-align:center;'>")
            .append("<b>TERIMAKASIH TELAH BERBELANJA</b><br>")
            .append("<b>BARAKALLAHU FIIKUM</b><br>")
            .append("</div>");

        html.append("</body>")
            .append("</html>");

        // Tampilkan HTML di jEditorPane1 (yang sudah diset sebagai JTextPane)
        jEditorPane1.setContentType("text/html");
        jEditorPane1.setText(html.toString());
        jEditorPane1.setCaretPosition(0);

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error saat mengambil data transaksi: " + e.getMessage());
    }
}


//public void displayStruk(String idTransaksi, int uangKembalian, int uangBayar, double total, String metode) {
//        try {
//            NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
//            String query = "SELECT t.id_transaksi, t.tanggal_transaksi, p.nama_pupuk, dt.qty, p.harga_jual, (dt.qty * p.harga_jual) AS subtotal "
//                         + "FROM db_kasirpupuk.transaksi t "
//                         + "JOIN db_kasirpupuk.detail_transaksi dt ON t.id_transaksi = dt.id_transaksi "
//                         + "JOIN db_kasirpupuk.pupuk p ON dt.id_pupuk = p.id_pupuk "
//                         + "WHERE t.id_transaksi = ?";
//
//            PreparedStatement stmt = conn.prepareStatement(query);
//            stmt.setString(1, idTransaksi);
//            ResultSet rs = stmt.executeQuery();
//
//            // Bangun konten HTML
//            StringBuilder html = new StringBuilder();
//            html.append("<html>")
//                .append("<div style='font-family: Monospaced; width:200px; text-align: center;'>")
//                .append("<b>SAHABAT TANI</b><br>")
//                .append("Toko Pupuk & Pertanian<br>")
//                .append("Alamat: Rowo Indah, Kabupaten Jember<br>")
//                .append("HP: 0821-3456-7890<br>")
//                .append("<hr style='border: none; border-top: 1px solid #000;'/>");
//
//            // Header Transaksi
//            html.append("<div style='text-align: left;'>");
//            html.append("<b>Kode Transaksi : </b>").append(idTransaksi).append("<br>");
//            html.append("<b>Kasir          : </b>").append(SessionManager.getNamaPengguna()).append("<br>");
//
//            if (rs.next()) {
//                html.append("<b>Tanggal        : </b>")
//                    .append(rs.getDate("tanggal_transaksi").toString())
//                    .append("<br>");
//                html.append("<hr style='border: none; border-top: 1px solid #000;'/>");
//
//                // Daftar Produk
//                html.append("<b>Produk:</b><br>");
//                do {
//                    String namaPupuk   = rs.getString("nama_pupuk");
//                    int qty            = rs.getInt("qty");
//                    double hargaSatuan = rs.getDouble("harga_jual");
//                    double subtotal    = rs.getDouble("subtotal");
//
//                    html.append(namaPupuk).append("<br>");
//                    html.append("&nbsp;&nbsp;")
//                        .append(qty)
//                        .append(" x ")
//                        .append(rupiahFormat.format(hargaSatuan))
//                        .append(" = ")
//                        .append(rupiahFormat.format(subtotal))
//                        .append("<br>");
//                } while (rs.next());
//
//                // Total, Bayar, Kembalian, Metode
//                html.append("<br>");
//                html.append("<b>TOTAL: </b>").append(rupiahFormat.format(total)).append("<br>");
//                html.append("<b>Bayar: </b>").append(rupiahFormat.format(uangBayar)).append("<br>");
//                html.append("<b>Kembalian: </b>").append(rupiahFormat.format(uangKembalian)).append("<br>");
//                html.append("<b>Metode Pembayaran: </b>").append(metode).append("<br>");
//                html.append("<hr style='border: none; border-top: 1px solid #000;'/>");
//            }
//            html.append("</div>"); // tutup div rata kiri
//
//            // Footer (bold dan rata tengah)
//            html.append("<div style='text-align: center;'>")
//                .append("<b>TERIMAKASIH TELAH BERBELANJA</b><br>")
//                .append("<b>BARAKALLAHU FIIKUM</b><br>")
//                .append("</div>");
//
//            html.append("</div>")   // tutup div utama
//                .append("</html>");
//
//            // Tampilkan HTML di jEditorPane
//            jEditorPane1.setContentType("text/html");
//            jEditorPane1.setText(html.toString());
//            jEditorPane1.setCaretPosition(0);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(this, "Error saat mengambil data transaksi: " + e.getMessage());
//        }
    
    
    
//        StringBuilder struk = new StringBuilder();
//        struk.append("         SAHABAT TANI   \u001B    \n");
//      
//        struk.append("    Toko Pupuk & Pertanian    \n");
//        struk.append("Alamat : Rowo Indah Kabupaten Jember \n");
//        struk.append("HP     : 0821-3456-7890       \n");
//        struk.append("==============================\n");
//        struk.append(String.format("Kode Transaksi : %s\n", idTransaksi));
//        struk.append(String.format("Kasir          : %s\n", SessionManager.getNamaPengguna()));
//
//        if (rs.next()) {
//            struk.append(String.format("Tanggal        : %s\n", rs.getDate("tanggal_transaksi")));
//            
//            struk.append("------------------------------\n");
//            struk.append("\nProduk:\n");
//
//            
//            do {
//                String namaPupuk = rs.getString("nama_pupuk");
//                int qty = rs.getInt("qty");
//                double hargaSatuan = rs.getDouble("harga_jual");
//                double subtotal = rs.getDouble("subtotal");
//
//                // Tambahkan nama pupuk di baris pertama
//                struk.append(String.format("%-35s\n", namaPupuk));
//        struk.append(String.format("%2d x %-10s = %-10s\n", qty, rupiahFormat.format(hargaSatuan), rupiahFormat.format(subtotal)));
//                // Tambahkan Qty x Harga di baris kedua
//                
//
//            } while (rs.next());
//
//            struk.append("\nTOTAL:  " + rupiahFormat.format(total));
//            struk.append(String.format("\nBayar: %s ", rupiahFormat.format(uangBayar)));
//            struk.append(String.format("\nKembalian: %s ", rupiahFormat.format(uangKembalian)));
//            struk.append(String.format("\nmetode pembayaran: %s ", metode));
//            struk.append("\n==============================\n");
//            struk.append(" TERIMAKASIH TELAH BERBELANJA \n");
//            struk.append("      BARAKALLAHU FIIKUM      \n");
//        }
//        // Tampilkan di jTextArea1 dan atur font
//        jTextArea1.setText(struk.toString());
//        
//        // Mengatur font agar lebih kecil
//        jTextArea1.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN,8));  // Ukuran font 10 dan font monospaced
//
//    } catch (Exception e) {
//        e.printStackTrace();
//        JOptionPane.showMessageDialog(this, "Error saat mengambil data transaksi: " + e.getMessage());
//    }

//      private String repeatChar(char c, int count) {
//    if (count <= 0) return "";
//    StringBuilder sb = new StringBuilder(count);
//    for (int i = 0; i < count; i++) {
//        sb.append(c);
//    }
//    return sb.toString();
//}
//
//      // Helper: membuat teks rata kiri/padding kanan hingga total length karakter
//    private String padRight(String text, int totalWidth) {
//        if (text.length() >= totalWidth) {
//            return text.substring(0, totalWidth);
//        }
//        StringBuilder sb = new StringBuilder(text);
//        while (sb.length() < totalWidth) {
//            sb.append(' ');
//        }
//        return sb.toString();
//    }
//
//    // Helper: membuat teks rata kanan/padding kiri hingga total length karakter
//    private String padLeft(String text, int totalWidth) {
//        if (text.length() >= totalWidth) {
//            return text.substring(0, totalWidth);
//        }
//        StringBuilder sb = new StringBuilder();
//        while (sb.length() + text.length() < totalWidth) {
//            sb.append(' ');
//        }
//        sb.append(text);
//        return sb.toString();
//    }
//
//    // Helper: membuat teks rata tengah dalam lebar tertentu
//    private String centerText(String text, int totalWidth) {
//        if (text.length() >= totalWidth) {
//            return text.substring(0, totalWidth);
//        }
//        int padding = (totalWidth - text.length()) / 2;
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < padding; i++) sb.append(' ');
//        sb.append(text);
//        // sisanya pad kanan
//        while (sb.length() < totalWidth) sb.append(' ');
//        return sb.toString();
//    }
//
//    // Helper: jika teks lebih panjang dari lebar, potong agar tidak melewati batas
//    private String truncateText(String text, int maxWidth) {
//        if (text.length() <= maxWidth) {
//            return text;
//        }
//        return text.substring(0, maxWidth);
//    }







    /**
     * Creates new form Struck
     */
   
    // Inisialisasi komponen GUI (tetap seperti semula)
    

   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jButton1.setText("Print");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jScrollPane1.setViewportView(jEditorPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(124, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
PrinterJob printerJob = PrinterJob.getPrinterJob();

// Konversi ukuran 80 mm ke point (1 point = 1/72 inch, 1 inch = 25.4 mm)
double mmToPoint  = 72.0 / 25.4;
double paperWidth = 80 * mmToPoint; // lebar kertas = 80 mm
double margin     = 0;              // tanpa margin

// 1) Set terlebih dahulu Paper dengan tinggi besar agar JEditorPane bisa menghitung PreferredSize
Paper paper = new Paper();
double tempHeight = 2000; // nilai sementara yang cukup besar
paper.setSize(paperWidth, tempHeight);
paper.setImageableArea(0, 0, paperWidth, tempHeight);

PageFormat pageFormat = printerJob.defaultPage();
pageFormat.setPaper(paper);

// 2) Pastikan JEditorPane sudah di‐layout ulang agar PreferredSize benar
jEditorPane1.revalidate();
jEditorPane1.repaint();

// 3) Ambil tinggi konten HTML‐nya
double contentHeight = jEditorPane1.getPreferredSize().height;

// 4) Hitung tinggi kertas yang diperlukan (konten + margin)
double paperHeight = contentHeight + (2 * margin);

// 5) Update ukuran Paper menjadi tinggi sebenarnya
paper.setSize(paperWidth, paperHeight);
paper.setImageableArea(0, 0, paperWidth, paperHeight);
pageFormat.setPaper(paper);

// 6) Set Printable dan cetak seluruh isi JEditorPane
printerJob.setPrintable((graphics, pf, pageIndex) -> {
    if (pageIndex > 0) {
        return Printable.NO_SUCH_PAGE;
    }
    Graphics2D g2d = (Graphics2D) graphics;
    g2d.translate(pf.getImageableX(), pf.getImageableY());
    jEditorPane1.printAll(g2d);
    return Printable.PAGE_EXISTS;
}, pageFormat);

// 7) Tampilkan dialog Print dan cetak jika dikonfirmasi
if (printerJob.printDialog()) {
    try {
        printerJob.print();
        JOptionPane.showMessageDialog(null, "Struk berhasil dicetak!");
    } catch (PrinterException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat mencetak: " + e.getMessage());
    }
} else {
    JOptionPane.showMessageDialog(null, "Cetak dibatalkan!");
}

    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(Struck.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Struck.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Struck.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Struck.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Struck dialog = new Struck(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButton1;
    private javax.swing.JTextPane jEditorPane1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
