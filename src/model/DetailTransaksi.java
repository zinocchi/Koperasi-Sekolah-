package model;

public class DetailTransaksi {
    private int id_detail;
    private int id_transaksi;
    private int id_barang;
    private int jumlah;
    private int subtotal;
    private String nama_barang; // Untuk join dengan tabel barang
    private int harga_barang;   // Untuk join dengan tabel barang

    // Constructor
    public DetailTransaksi() {
    }

    public DetailTransaksi(int id_detail, int id_transaksi, int id_barang, int jumlah, int subtotal) {
        this.id_detail = id_detail;
        this.id_transaksi = id_transaksi;
        this.id_barang = id_barang;
        this.jumlah = jumlah;
        this.subtotal = subtotal;
    }

    // Constructor untuk insert baru
    public DetailTransaksi(int id_transaksi, int id_barang, int jumlah, int subtotal) {
        this.id_transaksi = id_transaksi;
        this.id_barang = id_barang;
        this.jumlah = jumlah;
        this.subtotal = subtotal;
    }

    // Getters and Setters
    public int getId_detail() {
        return id_detail;
    }

    public void setId_detail(int id_detail) {
        this.id_detail = id_detail;
    }

    public int getId_transaksi() {
        return id_transaksi;
    }

    public void setId_transaksi(int id_transaksi) {
        this.id_transaksi = id_transaksi;
    }

    public int getId_barang() {
        return id_barang;
    }

    public void setId_barang(int id_barang) {
        this.id_barang = id_barang;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public int getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(int subtotal) {
        this.subtotal = subtotal;
    }

    public String getNama_barang() {
        return nama_barang;
    }

    public void setNama_barang(String nama_barang) {
        this.nama_barang = nama_barang;
    }

    public int getHarga_barang() {
        return harga_barang;
    }

    public void setHarga_barang(int harga_barang) {
        this.harga_barang = harga_barang;
    }
}