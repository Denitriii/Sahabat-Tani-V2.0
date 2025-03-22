/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package koneksi;

/**
 *
 * @author user
 */
public class sessionRole {
    private static String rolePengguna;
    
     public static void setRolePengguna(String role) {
        rolePengguna = role;
    }

    public static String getNamaPengguna() {
        return rolePengguna;
    }
}
