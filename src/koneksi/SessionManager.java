/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package koneksi;

/**
 *
 * @author user
 */
public class SessionManager {
    private static String namaPengguna;
   

    public static void setNamaPengguna(String nama) {
        namaPengguna = nama;
    }

    public static String getNamaPengguna() {
        return namaPengguna;
    }
   
}
