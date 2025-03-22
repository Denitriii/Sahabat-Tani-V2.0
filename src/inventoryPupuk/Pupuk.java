/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package inventoryPupuk;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
/**
 *
 * @author user
 */import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Pupuk {
    private String idPupuk;
    private String nama;
    private int beratSak;
    private int hargaJual;
    private double hargaEceran;
    private int stock;
    private double stockEceran;
    private String idSuplier;
    private int tipePembelian;
    private String tglExpired; // Tambahkan atribut tanggal expired
    private String barcode;
    private byte[] idGambar;

    // Constructor
    public Pupuk(String idPupuk, String nama, int hargaJual, int stock, String tglExpired, byte[] idGambar) {
    this.idPupuk = idPupuk;
    this.nama = nama;
    this.hargaJual = hargaJual;
    this.stock = stock;
    this.tglExpired = tglExpired;
    this.idGambar = idGambar;
}

    // Getter dan Setter untuk tglExpired
    public String getTglExpired() {
        return tglExpired;
    }

    public void setTglExpired(String tglExpired) {
        this.tglExpired = tglExpired;
    }

    public byte[] getIdGambar() {
    return idGambar; // Simpan dalam bentuk byte[]
}


    public void setIdGambar(byte[] idGambar) {
    this.idGambar = idGambar;
}
     public ImageIcon getImageIcon() {
        if (idGambar == null || idGambar.length == 0) {
            return null;
        }

        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(idGambar);
            BufferedImage bufferedImage = ImageIO.read(bis);
            return new ImageIcon(bufferedImage);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getHarga() {
        return hargaJual;
    }

    public void setHarga(int hargaJual) {
        this.hargaJual = hargaJual;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
    public String getId(){
        return idPupuk;
    }
}
