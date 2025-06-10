package kasir.manajemenakun;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class koneksi {
    private String url = "jdbc:mysql://localhost/db_kasirpupuk";
    private String username_xampp = "root";
    private String password_xampp = "";
    private Connection con;
    public Statement st;         // <-- ditambahkan Statement publik

    // Konstruktor otomatis memanggil connect()
    public koneksi() {
        connect();
    }

    public void connect() {
        
        try {
            con = DriverManager.getConnection(url, username_xampp, password_xampp);
            st = con.createStatement();           // <-- inisialisasi Statement
            System.out.println("Koneksi berhasil");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Koneksi gagal:\n" + e.getMessage());
        }
    }

    public Connection getCon() {
        return con;
    }
}
