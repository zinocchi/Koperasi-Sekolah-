package model;

public class Barang {
    private int id_barang;
    private String nama_barang;
    private int harga;
    private int stok;
    private String satuan;

    public Barang(int idBarang, String nama_barang, int harga, int stok, String satuan) {
        this.id_barang = idBarang;
        this.nama_barang = nama_barang;
        this.harga = harga;
        this.stok = stok;
        this.satuan = satuan;
    }

    // getter
    public int getId_barang() { return id_barang; }
    public String getNama_barang() { return nama_barang; }
    public int getHarga() { return harga; }
    public int getStok() { return stok; }
    public String getSatuan() { return satuan; }
}
