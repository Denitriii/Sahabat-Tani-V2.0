/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package inventoryPupuk;

/**
 *
 * @author user
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import koneksi.koneksiDB;

public class DiskonDAO {
 private  Connection conn;
public DiskonDAO(Connection conn){
    this.conn = conn;
}
    // ✅ Get diskon aktif untuk 1 produk (yang sudah ada)
    public Diskon getDiskonAktifByPupukId(String idPupuk) {
        Diskon diskon = null;
        String sql = "SELECT * FROM diskon WHERE id_pupuk = ? AND aktif = TRUE AND tanggal_mulai <= CURDATE() AND tanggal_selesai >= CURDATE()";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idPupuk);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                diskon = mapResultSetToDiskon(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return diskon;
    }

    // ✅ Ambil semua diskon
    public List<Diskon> getAllDiskon() {
        List<Diskon> list = new ArrayList<>();
        String sql = "SELECT * FROM diskon";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToDiskon(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ✅ Insert diskon baru
    public boolean insertDiskon(Diskon diskon) {
        String sql = "INSERT INTO diskon (id_diskon, nama_diskon, id_pupuk, persen_diskon, tanggal_mulai, tanggal_selesai, aktif) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, diskon.getIdDiskon());
            stmt.setString(2, diskon.getNamaDiskon());
            stmt.setString(3, diskon.getIdPupuk());
            stmt.setDouble(4, diskon.getPersenDiskon());
            stmt.setDate(5, (Date) diskon.getTanggalMulai());
            stmt.setDate(6, (Date) diskon.getTanggalSelesai());
            stmt.setBoolean(7, diskon.isAktif());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    
    

   
  

    // ✅ (Opsional) Delete diskon
    public boolean deleteDiskon(String idDiskon) {
        String sql = "DELETE FROM diskon WHERE id_diskon = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idDiskon);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔄 Helper: konversi ResultSet jadi objek Diskon
    private Diskon mapResultSetToDiskon(ResultSet rs) throws SQLException {
        return new Diskon(
            rs.getString("id_diskon"),
            rs.getString("nama_diskon"),
            rs.getString("id_pupuk"),
            rs.getDouble("persen_diskon"),
            rs.getDate("tanggal_mulai"),
            rs.getDate("tanggal_selesai"),
            rs.getBoolean("aktif")
        );
    }
}

