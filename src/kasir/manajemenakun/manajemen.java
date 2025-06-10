
package kasir.manajemenakun;

import java.awt.Dimension;
import javax.swing.JViewport;

public class manajemen {

    public static void main(String[] args) {
        koneksi k = new koneksi(); 
        k.connect();
        System.out.println("Hello World!");
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Tampilkan panel dalam frame
        javax.swing.SwingUtilities.invokeLater(() -> {
            javax.swing.JFrame frame = new javax.swing.JFrame("Tes Manajemen Akun Panel");
            frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
            
            ManajemenAkun panel = new ManajemenAkun();
            frame.setContentPane(panel);
            
//            frame.setContentPane(new ManajemenAkun()); // Ganti sesuai nama JPanel kamu
            

 javax.swing.JScrollPane scroll = panel.getScrollPaneAkun(); // Pastikan getter benar!
    javax.swing.JViewport vp = scroll.getColumnHeader();
    if (vp != null) {
        vp.setOpaque(false);
        vp.setBackground(panel.getTableHeader().getBackground());
    }
            
            frame.pack(); // Menyesuaikan ukuran
            frame.setLocationRelativeTo(null); // Tampil di tengah
            frame.setVisible(true); // Tampilkan GUI
        });
    }
}
