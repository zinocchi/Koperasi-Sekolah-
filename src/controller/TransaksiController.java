package controller;

import dao.TransaksiDAO;
import dao.BarangDAO;
import dao.AnggotaDAO;
import model.Transaksi;
import model.DetailTransaksi;
import model.Barang;
import model.Anggota;
import view.TransaksiForm;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

public class TransaksiController {

    private TransaksiForm view;
    private ArrayList<Barang> currentBarangList;
    private ArrayList<DetailTransaksi> keranjang;
    private int totalHarga;
    private Timer refreshTimer;

    public TransaksiController(TransaksiForm view) {
        this.view = view;
        this.currentBarangList = new ArrayList<>();
        this.keranjang = new ArrayList<>();
        this.totalHarga = 0;
        loadInitialData();
        initListeners();
        updateTotal();
        startAutoRefresh();
    }

    private void loadInitialData() {
        loadBarang();
        loadAnggota();
        loadRiwayatTransaksi();

        // Format tanggal
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        view.getLblTanggal().setText(sdf.format(new Date()));
    }

    private void loadBarang() {
        try {
            currentBarangList = BarangDAO.getAll();
            DefaultTableModel model = view.getTableModelBarang(); // ✅ Gunakan getter dari View
            model.setRowCount(0);

            for (Barang b : currentBarangList) {
                if (b.getStok() > 0) {
                    model.addRow(new Object[]{
                            b.getNama_barang(),
                            formatRupiah(b.getHarga()),
                            b.getStok(),
                            b.getSatuan()
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Error loading barang: " + e.getMessage());
        }
    }

    private void loadAnggota() {
        try {
            ArrayList<Anggota> anggotaList = AnggotaDAO.getAll();
            view.getCmbAnggota().removeAllItems();
            view.getCmbAnggota().addItem("Pilih Anggota");

            for (Anggota a : anggotaList) {
                view.getCmbAnggota().addItem(a.getNama() + " - " + a.getKelas());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Error loading anggota: " + e.getMessage());
        }
    }

    private void loadRiwayatTransaksi() {
        try {
            ArrayList<Transaksi> transaksiList = TransaksiDAO.getAll();
            DefaultTableModel model = view.getTableModelTransaksi(); // ✅ Gunakan getter dari View
            model.setRowCount(0);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            for (Transaksi t : transaksiList) {
                model.addRow(new Object[]{
                        t.getId_transaksi(),
                        t.getNama_anggota() != null ? t.getNama_anggota() : "Non-Anggota",
                        sdf.format(t.getTanggal()),
                        formatRupiah(t.getTotal_harga())
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Error loading riwayat: " + e.getMessage());
        }
    }

    private void initListeners() {
        // **FIX: Tambah ke Keranjang**
        view.getBtnTambahKeranjang().addActionListener(e -> tambahKeKeranjang());

        view.getBtnHapusKeranjang().addActionListener(e -> hapusDariKeranjang());
        view.getBtnBayar().addActionListener(e -> prosesBayar());
        view.getBtnBatal().addActionListener(e -> batalTransaksi());

        // **FIX: Double click pada tabel barang untuk tambah cepat**
        view.getTblBarang().addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    tambahKeKeranjang();
                }
            }
        });

        // **FIX: Enter key di field jumlah**
        view.getTxtJumlah().addActionListener(e -> tambahKeKeranjang());

        // **FIX: Pencarian real-time**
        view.getTxtCariBarang().getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { searchBarang(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { searchBarang(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { searchBarang(); }
        });
    }

    // **FIX: Method tambahKeKeranjang yang sesuai dengan View**
    private void tambahKeKeranjang() {
        int selectedRow = view.getTblBarang().getSelectedRow();

        // Validasi: pastikan barang dipilih
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view,
                    "Pilih barang dari daftar terlebih dahulu!",
                    "Barang Belum Dipilih",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // **FIX: Ambil barang dari currentBarangList berdasarkan row yang dipilih**
            Barang selectedBarang = currentBarangList.get(selectedRow);

            // **FIX: Validasi jumlah**
            String jumlahText = view.getTxtJumlah().getText().trim();
            if (jumlahText.isEmpty()) {
                JOptionPane.showMessageDialog(view,
                        "Masukkan jumlah barang!",
                        "Jumlah Kosong",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int jumlah = Integer.parseInt(jumlahText);

            if (jumlah <= 0) {
                JOptionPane.showMessageDialog(view,
                        "Jumlah harus lebih dari 0!",
                        "Jumlah Tidak Valid",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // **FIX: Validasi stok**
            if (jumlah > selectedBarang.getStok()) {
                JOptionPane.showMessageDialog(view,
                        "Stok tidak mencukupi!\n\n" +
                                "Stok tersedia: " + selectedBarang.getStok() + " " + selectedBarang.getSatuan() + "\n" +
                                "Jumlah diminta: " + jumlah,
                        "Stok Tidak Cukup",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // **FIX: Cek apakah barang sudah ada di keranjang**
            boolean barangSudahAda = false;
            for (DetailTransaksi item : keranjang) {
                if (item.getId_barang() == selectedBarang.getId_barang()) {
                    // Barang sudah ada, update jumlah
                    int jumlahBaru = item.getJumlah() + jumlah;

                    // Validasi stok lagi untuk jumlah baru
                    if (jumlahBaru > selectedBarang.getStok()) {
                        JOptionPane.showMessageDialog(view,
                                "Total jumlah melebihi stok!\n\n" +
                                        "Stok tersedia: " + selectedBarang.getStok() + "\n" +
                                        "Jumlah di keranjang: " + item.getJumlah() + "\n" +
                                        "Jumlah tambahan: " + jumlah,
                                "Stok Tidak Cukup",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    item.setJumlah(jumlahBaru);
                    item.setSubtotal(item.getHarga_barang() * jumlahBaru);
                    barangSudahAda = true;
                    break;
                }
            }

            // **FIX: Jika barang belum ada, tambahkan baru**
            if (!barangSudahAda) {
                DetailTransaksi newItem = new DetailTransaksi();
                newItem.setId_barang(selectedBarang.getId_barang());
                newItem.setNama_barang(selectedBarang.getNama_barang());
                newItem.setHarga_barang(selectedBarang.getHarga());
                newItem.setJumlah(jumlah);
                newItem.setSubtotal(selectedBarang.getHarga() * jumlah);

                keranjang.add(newItem);
            }

            // **FIX: Update tampilan keranjang dan total**
            updateKeranjang();
            updateTotal();

            // **FIX: Reset jumlah dan beri feedback**
            view.getTxtJumlah().setText("1");

            JOptionPane.showMessageDialog(view,
                    "✅ Berhasil menambahkan ke keranjang!\n" +
                            "Barang: " + selectedBarang.getNama_barang() + "\n" +
                            "Jumlah: " + jumlah + " " + selectedBarang.getSatuan(),
                    "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view,
                    "Jumlah harus berupa angka!",
                    "Format Tidak Valid",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view,
                    "Error: " + e.getMessage(),
                    "Error Sistem",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // **FIX: Method updateKeranjang yang sesuai dengan View**
    private void updateKeranjang() {
        DefaultTableModel model = view.getTableModelKeranjang(); // ✅ Gunakan getter dari View
        model.setRowCount(0);

        for (DetailTransaksi item : keranjang) {
            model.addRow(new Object[]{
                    item.getNama_barang(),
                    formatRupiah(item.getHarga_barang()),
                    item.getJumlah(),
                    formatRupiah(item.getSubtotal())
            });
        }
    }

    // **FIX: Method updateTotal**
    private void updateTotal() {
        totalHarga = 0;
        for (DetailTransaksi item : keranjang) {
            totalHarga += item.getSubtotal();
        }

        view.getLblTotal().setText(formatRupiah(totalHarga));

        // Enable/disable tombol bayar berdasarkan keranjang
        view.getBtnBayar().setEnabled(!keranjang.isEmpty());
    }

    private void hapusDariKeranjang() {
        int selectedRow = view.getTblKeranjang().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view,
                    "Pilih barang di keranjang yang ingin dihapus!",
                    "Tidak Ada Barang Dipilih",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        DetailTransaksi item = keranjang.get(selectedRow);

        int confirm = JOptionPane.showConfirmDialog(view,
                "Hapus barang dari keranjang?\n\n" +
                        "Barang: " + item.getNama_barang() + "\n" +
                        "Jumlah: " + item.getJumlah() + "\n" +
                        "Subtotal: " + formatRupiah(item.getSubtotal()),
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            keranjang.remove(selectedRow);
            updateKeranjang();
            updateTotal();

            JOptionPane.showMessageDialog(view,
                    "✅ Barang berhasil dihapus dari keranjang!",
                    "Hapus Berhasil",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void searchBarang() {
        String keyword = view.getTxtCariBarang().getText().trim().toLowerCase();

        if (keyword.isEmpty()) {
            loadBarang();
            return;
        }

        DefaultTableModel model = view.getTableModelBarang(); // ✅ Gunakan getter dari View
        model.setRowCount(0);

        currentBarangList.clear();
        ArrayList<Barang> allBarang = BarangDAO.getAll();

        for (Barang b : allBarang) {
            if (b.getNama_barang().toLowerCase().contains(keyword) && b.getStok() > 0) {
                model.addRow(new Object[]{
                        b.getNama_barang(),
                        formatRupiah(b.getHarga()),
                        b.getStok(),
                        b.getSatuan()
                });
                currentBarangList.add(b);
            }
        }
    }

    private void prosesBayar() {
        if (keranjang.isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "Keranjang masih kosong!\nTambahkan barang terlebih dahulu.",
                    "Keranjang Kosong",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validasi pilih anggota
        if (view.getCmbAnggota().getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(view,
                    "Pilih anggota terlebih dahulu!",
                    "Anggota Belum Dipilih",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Dapatkan ID anggota dari combobox
            String selectedAnggota = view.getCmbAnggota().getSelectedItem().toString();
            String namaAnggota = selectedAnggota.split(" - ")[0];
            int idAnggota = getAnggotaIdByName(namaAnggota);

            if (idAnggota == 0) {
                JOptionPane.showMessageDialog(view,
                        "Data anggota tidak ditemukan!",
                        "Error Data",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Konfirmasi transaksi
            StringBuilder detailTransaksi = new StringBuilder();
            detailTransaksi.append("Detail Transaksi:\n\n");
            for (DetailTransaksi detail : keranjang) {
                detailTransaksi.append("• ").append(detail.getNama_barang())
                        .append(" - ").append(detail.getJumlah())
                        .append(" x ").append(formatRupiah(detail.getHarga_barang()))
                        .append(" = ").append(formatRupiah(detail.getSubtotal()))
                        .append("\n");
            }
            detailTransaksi.append("\nTotal: ").append(formatRupiah(totalHarga));

            int confirm = JOptionPane.showConfirmDialog(view,
                    detailTransaksi.toString() + "\n\n" +
                            "Anggota: " + namaAnggota + "\n" +
                            "Lanjutkan proses transaksi?",
                    "Konfirmasi Transaksi",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            // **FIX: Simpan transaksi ke database**
            Transaksi transaksi = new Transaksi();
            transaksi.setId_anggota(idAnggota);
            transaksi.setId_user(1); // User dummy
            transaksi.setTanggal(new Date());
            transaksi.setTotal_harga(totalHarga);

            for (DetailTransaksi detail : keranjang) {
                transaksi.addDetailTransaksi(detail);
            }

            int idTransaksi = TransaksiDAO.insert(transaksi);

            if (idTransaksi > 0) {
                JOptionPane.showMessageDialog(view,
                        "✅ TRANSAKSI BERHASIL!\n\n" +
                                "ID Transaksi: TRX-" + idTransaksi + "\n" +
                                "Anggota: " + namaAnggota + "\n" +
                                "Total: " + formatRupiah(totalHarga) + "\n" +
                                "Tanggal: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "\n\n" +
                                "Terima kasih telah berbelanja!",
                        "Transaksi Sukses",
                        JOptionPane.INFORMATION_MESSAGE);

                // Reset dan refresh data
                batalTransaksi();
                refreshData();

            } else {
                JOptionPane.showMessageDialog(view,
                        "❌ Gagal melakukan transaksi!\n" +
                                "Silakan coba lagi atau hubungi administrator.",
                        "Transaksi Gagal",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(view,
                    "Terjadi kesalahan sistem:\n" + e.getMessage(),
                    "Error Sistem",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private int getAnggotaIdByName(String nama) {
        ArrayList<Anggota> anggotaList = AnggotaDAO.getAll();
        for (Anggota a : anggotaList) {
            if (a.getNama().equals(nama)) {
                return a.getId_anggota();
            }
        }
        return 0;
    }

    private void batalTransaksi() {
        if (!keranjang.isEmpty()) {
            int confirm = JOptionPane.showConfirmDialog(view,
                    "Yakin ingin membatalkan transaksi?\n" +
                            "Semua barang di keranjang akan dihapus.",
                    "Konfirmasi Batal",
                    JOptionPane.YES_NO_OPTION);

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
        }

        keranjang.clear();
        totalHarga = 0;
        updateKeranjang();
        updateTotal();
        view.getTxtJumlah().setText("1");
        view.getCmbAnggota().setSelectedIndex(0);
        view.getTblBarang().clearSelection();
        view.getTxtCariBarang().setText("");
    }

    private void refreshData() {
        loadBarang();
        loadRiwayatTransaksi();
        loadAnggota();
    }

    private void startAutoRefresh() {
        refreshTimer = new Timer(30000, e -> refreshData());
        refreshTimer.start();
    }

    private String formatRupiah(int amount) {
        return "Rp " + String.format("%,d", amount).replace(',', '.');
    }

    public void dispose() {
        if (refreshTimer != null) {
            refreshTimer.stop();
        }
    }
}