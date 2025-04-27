package inventoryPupuk;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import koneksi.koneksiDB;
import java.util.Date;
import java.text.SimpleDateFormat;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class PupukDAO {

    // Ambil semua data pupuk
private Connection conn;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public PupukDAO(Connection conn) {
        this.conn = conn;
    }

    public static List<Pupuk> getAllPupuk() {
        
        List<Pupuk> pupukList = new ArrayList<>();
        String sql = "SELECT id_pupuk, nama_pupuk, harga_jual, stock, tgl_expired, id_gambar FROM pupuk";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_kasirpupuk", "root", "");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
            Date tglExp = rs.getDate("tgl_expired");
            String formattedDate = (tglExp != null) ? dateFormat.format(tglExp) : "Tidak ada tanggal";

            String idPupuk = rs.getString("id_pupuk");
            String namaPupuk = rs.getString("nama_pupuk");
            int hargaJual = rs.getInt("harga_jual");
            int stock = rs.getInt("stock");
            byte[] idGambar = rs.getBytes("id_gambar"); // Ambil gambar sebagai byte[]

            

            // Tambahkan ke daftar objek pupuk
            pupukList.add(new Pupuk(idPupuk, namaPupuk, hargaJual, stock, formattedDate, idGambar));

        }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pupukList;
    }

    private static ImageIcon getProductImage(byte[] idGambar) {
    if (idGambar == null || idGambar.length == 0) {
        return null; // Jika tidak ada gambar, kembalikan null
    }

    try {
        ByteArrayInputStream bis = new ByteArrayInputStream(idGambar);
        BufferedImage bufferedImage = ImageIO.read(bis);
        return new ImageIcon(bufferedImage);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null; // Kembalikan null jika terjadi error
}


    // Update data pupuk termasuk tgl_expired


public static void updatePupuk(Pupuk pupuk) {
    Connection conn = koneksiDB.BukaKoneksi();
    String query = "UPDATE pupuk SET nama_pupuk = ?, harga_jual = ?, stock = ?, tgl_expired = ? WHERE id_pupuk = ?";

    try {
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, pupuk.getNama());
        ps.setDouble(2, pupuk.getHarga());
        ps.setInt(3, pupuk.getStock());
        

        // Konversi String tgl_expired ke java.sql.Date
        java.sql.Date sqlDate = null;
        if (pupuk.getTglExpired() != null && !pupuk.getTglExpired().isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); // Sesuai format yang digunakan
                java.util.Date parsedDate = sdf.parse(pupuk.getTglExpired());
                sqlDate = new java.sql.Date(parsedDate.getTime());
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Format tanggal tidak valid!", "Error", JOptionPane.ERROR_MESSAGE);
                return; // Keluar dari metode jika parsing gagal
            }
        }

        ps.setDate(4, sqlDate); // Masukkan hasil konversi
        ps.setString(5, pupuk.getId());

        ps.executeUpdate();
        JOptionPane.showMessageDialog(null, "Data berhasil diperbarui!", "Sukses", JOptionPane.INFORMATION_MESSAGE);

        ps.close();
        conn.close();
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Gagal memperbarui data!", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    // Hapus pupuk berdasarkan ID
    public static void deletePupuk(String id) {
        Connection conn = koneksiDB.BukaKoneksi();
        String query = "DELETE FROM pupuk WHERE id_pupuk = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, id); 

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Data berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Data tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
            }

            ps.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal menghapus data!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Ambil data pupuk berdasarkan ID
    public static Pupuk getPupukById(String idPupuk) {
        Connection conn = koneksiDB.BukaKoneksi();
        String query = "SELECT id_pupuk, nama_pupuk, harga_jual, stock, tgl_expired, id_gambar FROM pupuk WHERE id_pupuk = ?";
        Pupuk pupuk = null;

        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, idPupuk);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                pupuk = new Pupuk(
                    rs.getString("id_pupuk"),
                    rs.getString("nama_pupuk"),
                    rs.getInt("harga_jual"),
                    rs.getInt("stock"),
                    rs.getString("tgl_expired"), // Ambil tanggal expired
                    rs.getBytes("id_gambar")
                 
                );
            }

            rs.close();
            ps.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pupuk;
    }
}
