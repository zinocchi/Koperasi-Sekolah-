package model;

import java.util.Date;
import java.util.ArrayList;

public class Transaksi {
    private int id_transaksi;
    private int id_anggota;
    private int id_user;
    private Date tanggal;
    private int total_harga;
    private String nama_anggota; // Untuk join dengan tabel anggota
    private String nama_user;    // Untuk join dengan tabel user
    private ArrayList<DetailTransaksi> detailTransaksi; // List detail transaksi

    // Constructor
    public Transaksi() {
        this.detailTransaksi = new ArrayList<>();
    }

    public Transaksi(int id_transaksi, int id_anggota, int id_user, Date tanggal, int total_harga) {
        this.id_transaksi = id_transaksi;
        this.id_anggota = id_anggota;
        this.id_user = id_user;
        this.tanggal = tanggal;
        this.total_harga = total_harga;
        this.detailTransaksi = new ArrayList<>();
    }

    // Constructor untuk insert baru
    public Transaksi(int id_anggota, int id_user, Date tanggal, int total_harga) {
        this.id_anggota = id_anggota;
        this.id_user = id_user;
        this.tanggal = tanggal;
        this.total_harga = total_harga;
        this.detailTransaksi = new ArrayList<>();
    }

    // Getters and Setters
    public int getId_transaksi() {
        return id_transaksi;
    }

    public void setId_transaksi(int id_transaksi) {
        this.id_transaksi = id_transaksi;
    }

    public int getId_anggota() {
        return id_anggota;
    }

    public void setId_anggota(int id_anggota) {
        this.id_anggota = id_anggota;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }

    public int getTotal_harga() {
        return total_harga;
    }

    public void setTotal_harga(int total_harga) {
        this.total_harga = total_harga;
    }

    public String getNama_anggota() {
        return nama_anggota;
    }

    public void setNama_anggota(String nama_anggota) {
        this.nama_anggota = nama_anggota;
    }

    public String getNama_user() {
        return nama_user;
    }

    public void setNama_user(String nama_user) {
        this.nama_user = nama_user;
    }

    public ArrayList<DetailTransaksi> getDetailTransaksi() {
        return detailTransaksi;
    }

    public void setDetailTransaksi(ArrayList<DetailTransaksi> detailTransaksi) {
        this.detailTransaksi = detailTransaksi;
    }

    public void addDetailTransaksi(DetailTransaksi detail) {
        this.detailTransaksi.add(detail);
    }
}