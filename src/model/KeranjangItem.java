package model;

public class KeranjangItem {
    private String idBarang;
    private String namaBarang;
    private double harga;
    private int jumlah;
    private double subtotal;
    private int stok;

    public KeranjangItem(String idBarang, String namaBarang, double harga, int jumlah, int stok) {
        this.idBarang = idBarang;
        this.namaBarang = namaBarang;
        this.harga = harga;
        this.jumlah = jumlah;
        this.stok = stok;
        this.subtotal = harga * jumlah;
    }

    // Getter dan Setter
    public String getIdBarang() { return idBarang; }
    public void setIdBarang(String idBarang) { this.idBarang = idBarang; }

    public String getNamaBarang() { return namaBarang; }
    public void setNamaBarang(String namaBarang) { this.namaBarang = namaBarang; }

    public double getHarga() { return harga; }
    public void setHarga(double harga) { this.harga = harga; }

    public int getJumlah() { return jumlah; }
    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
        this.subtotal = this.harga * jumlah;
    }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    public int getStok() { return stok; }
    public void setStok(int stok) { this.stok = stok; }
}