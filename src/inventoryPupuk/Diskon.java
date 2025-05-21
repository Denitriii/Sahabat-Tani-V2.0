/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package inventoryPupuk;

import java.util.Date;

/**
 *
 * @author user
 */
public class Diskon {
    private String idDiskon;
    private String namaDiskon;
    private String idPupuk;
    private double persenDiskon;
    private Date tanggalMulai;
    private Date tanggalSelesai;
    private boolean aktif;

    public Diskon(String idDiskon, String namaDiskon, String idPupuk, double persenDiskon, Date tanggalMulai, Date tanggalSelesai, boolean aktif) {
        this.idDiskon = idDiskon;
        this.namaDiskon = namaDiskon;
        this.idPupuk = idPupuk;
        this.persenDiskon = persenDiskon;
        this.tanggalMulai = tanggalMulai;
        this.tanggalSelesai = tanggalSelesai;
        this.aktif = aktif;
    }

    // Getter dan Setter
    public String getIdDiskon() { 
        return idDiskon; }
    public void setIdDiskon(String idDiskon) { 
        this.idDiskon = idDiskon; }

    public String getNamaDiskon() { 
        return namaDiskon; }
    public void setNamaDiskon(String namaDiskon) { 
        this.namaDiskon = namaDiskon; }

    public String getIdPupuk() { 
        return idPupuk; }
    public void setIdPupuk(String idPupuk) { 
        this.idPupuk = idPupuk; }

    public double getPersenDiskon() { 
        return persenDiskon; }
    public void setPersenDiskon(double persenDiskon) { 
        this.persenDiskon = persenDiskon; }

    public Date getTanggalMulai() { 
        return tanggalMulai; }
    public void setTanggalMulai(Date tanggalMulai) { 
        this.tanggalMulai = tanggalMulai; }

    public Date getTanggalSelesai() { 
        return tanggalSelesai; }
    public void setTanggalSelesai(Date tanggalSelesai) { 
        this.tanggalSelesai = tanggalSelesai; }

    public boolean isAktif() { 
        return aktif; }
    public void setAktif(boolean aktif) { 
        this.aktif = aktif; }
}

