package model;

public class Anggota {
    private int id_anggota;
    private String nama;
    private String kelas;
    private String alamat;
    private String no_hp;

    // Constructor
    public Anggota() {
    }

    public Anggota(int id_anggota, String nama, String kelas, String alamat, String no_hp) {
        this.id_anggota = id_anggota;
        this.nama = nama;
        this.kelas = kelas;
        this.alamat = alamat;
        this.no_hp = no_hp;
    }

    // Constructor untuk insert baru (tanpa ID)
    public Anggota(String nama, String kelas, String alamat, String no_hp) {
        this.nama = nama;
        this.kelas = kelas;
        this.alamat = alamat;
        this.no_hp = no_hp;
    }

    // Getters and Setters
    public int getId_anggota() {
        return id_anggota;
    }

    public void setId_anggota(int id_anggota) {
        this.id_anggota = id_anggota;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNo_hp() {
        return no_hp;
    }

    public void setNo_hp(String no_hp) {
        this.no_hp = no_hp;
    }
}