/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package KasirTransaksi;

/**
 *
 * @author user
 */
public class produk {
    private String barcode, nama, jenis;
    private int stok;
    private double harga;

    public produk (String barcode, String nama, String jenis, int stok, double harga) {
        this.barcode = barcode;
        this.nama = nama;
        this.jenis = jenis;
        this.stok = stok;
        this.harga = harga;
    }

    public String getBarcode() { return barcode; }
    public String getNama() { return nama; }
    public String getJenis() { return jenis; }
    public int getStok() { return stok; }
    public double getHarga() { return harga; }
}
