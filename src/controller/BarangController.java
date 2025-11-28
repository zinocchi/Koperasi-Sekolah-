package controller;

import dao.BarangDAO;
import model.Barang;
import view.BarangForm;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class BarangController {

    private BarangForm view;
    private ArrayList<Barang> currentBarangList;

    public BarangController(BarangForm view) {
        this.view = view;
        this.currentBarangList = new ArrayList<>();
        loadTable();
        initListeners();
        setFormMode(false);
    }

    private void loadTable() {
        DefaultTableModel model = (DefaultTableModel) view.getTblBarang().getModel();
        model.setRowCount(0);

        currentBarangList = BarangDAO.getAll();

        for (Barang b : currentBarangList) {
            model.addRow(new Object[]{
                    b.getNama_barang(),
                    formatRupiah(b.getHarga()),
                    b.getStok(),
                    b.getSatuan()
            });
        }
    }

    private void initListeners() {
        view.getBtnSimpan().addActionListener(e -> save());

        view.getBtnDelete().addActionListener(e -> delete());

        view.getBtnBatal().addActionListener(e -> {
            clearForm();
            setFormMode(false);
        });

        view.getTblBarang().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillForm();
                setFormMode(true); // Set mode edit ketika ada selection
            }
        });

        view.getTxtCari().getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { search(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { search(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { search(); }
        });

        view.getTxtNama().addActionListener(e -> view.getTxtHarga().requestFocus());
        view.getTxtHarga().addActionListener(e -> view.getTxtStok().requestFocus());
        view.getTxtStok().addActionListener(e -> view.getTxtSatuan().requestFocus());
        view.getTxtSatuan().addActionListener(e -> save());
    }

    private void save() {
        try {
            String nama = view.getTxtNama().getText().trim();
            String hargaText = view.getTxtHarga().getText().trim().replaceAll("[^\\d]", "");
            String stokText = view.getTxtStok().getText().trim();
            String satuan = view.getTxtSatuan().getText().trim();

            if (nama.isEmpty() || hargaText.isEmpty() || stokText.isEmpty() || satuan.isEmpty()) {
                JOptionPane.showMessageDialog(view,
                        "Semua field harus diisi!\n\n" +
                                "• Nama Barang: Wajib diisi\n" +
                                "• Harga: Wajib diisi (angka)\n" +
                                "• Stok: Wajib diisi (angka)\n" +
                                "• Satuan: Wajib diisi",
                        "Data Tidak Lengkap",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int harga, stok;
            try {
                harga = Integer.parseInt(hargaText);
                stok = Integer.parseInt(stokText);

                if (harga <= 0 || stok < 0) {
                    JOptionPane.showMessageDialog(view,
                            "• Harga harus lebih dari 0\n" +
                                    "• Stok tidak boleh negatif",
                            "Input Tidak Valid",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(view,
                        "Harga dan Stok harus berupa angka!",
                        "Format Tidak Valid",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int selectedRow = view.getTblBarang().getSelectedRow();

            if (selectedRow != -1) {
                Barang selectedBarang = currentBarangList.get(selectedRow);
                int id = selectedBarang.getId_barang();

                Barang br = new Barang(id, nama, harga, stok, satuan);

                if (BarangDAO.update(br)) {
                    JOptionPane.showMessageDialog(view,
                            "✅ Berhasil update barang!\n\n" +
                                    "Nama: " + nama + "\n" +
                                    "Harga: " + formatRupiah(harga) + "\n" +
                                    "Stok: " + stok + " " + satuan,
                            "Update Berhasil",
                            JOptionPane.INFORMATION_MESSAGE);
                    loadTable();
                    clearForm();
                    setFormMode(false);
                } else {
                    JOptionPane.showMessageDialog(view,
                            "Gagal update barang!\nMungkin terjadi kesalahan di database.",
                            "Update Gagal",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // INSERT mode - tambah data baru
                Barang br = new Barang(0, nama, harga, stok, satuan);

                if (BarangDAO.insert(br)) {
                    JOptionPane.showMessageDialog(view,
                            "✅ Berhasil menambah barang!\n\n" +
                                    "Nama: " + nama + "\n" +
                                    "Harga: " + formatRupiah(harga) + "\n" +
                                    "Stok: " + stok + " " + satuan,
                            "Tambah Berhasil",
                            JOptionPane.INFORMATION_MESSAGE);
                    loadTable();
                    clearForm();
                    setFormMode(false);
                } else {
                    JOptionPane.showMessageDialog(view,
                            "Gagal menambah barang!\nMungkin terjadi kesalahan di database.",
                            "Tambah Gagal",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view,
                    "Terjadi kesalahan sistem:\n" + ex.getMessage(),
                    "Error Sistem",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void delete() {
        int selectedRow = view.getTblBarang().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view,
                    "Pilih barang yang ingin dihapus dari tabel!",
                    "Tidak Ada Barang Dipilih",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Barang selectedBarang = currentBarangList.get(selectedRow);
        int id = selectedBarang.getId_barang();
        String namaBarang = selectedBarang.getNama_barang();
        int stok = selectedBarang.getStok();

        int confirm = JOptionPane.showConfirmDialog(view,
                "⚠️ Yakin ingin menghapus barang ini?\n\n" +
                        "Nama: " + namaBarang + "\n" +
                        "Stok: " + stok + " " + selectedBarang.getSatuan() + "\n\n" +
                        "Data yang dihapus tidak dapat dikembalikan!",
                "Konfirmasi Hapus Barang",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (BarangDAO.delete(id)) {
                JOptionPane.showMessageDialog(view,
                        "✅ Berhasil menghapus barang: " + namaBarang,
                        "Hapus Berhasil",
                        JOptionPane.INFORMATION_MESSAGE);
                loadTable();
                clearForm();
                setFormMode(false);
            } else {
                JOptionPane.showMessageDialog(view,
                        "Gagal menghapus barang!\n" +
                                "Mungkin barang sedang digunakan dalam transaksi.",
                        "Hapus Gagal",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void fillForm() {
        int row = view.getTblBarang().getSelectedRow();
        if (row == -1) return;

        Barang selectedBarang = currentBarangList.get(row);

        view.getTxtNama().setText(selectedBarang.getNama_barang());
        view.getTxtHarga().setText(String.valueOf(selectedBarang.getHarga()));
        view.getTxtStok().setText(String.valueOf(selectedBarang.getStok()));
        view.getTxtSatuan().setText(selectedBarang.getSatuan());
    }

    private void clearForm() {
        view.getTxtNama().setText("");
        view.getTxtHarga().setText("");
        view.getTxtStok().setText("");
        view.getTxtSatuan().setText("");
        view.getTblBarang().clearSelection();
    }

    private void search() {
        String keyword = view.getTxtCari().getText().trim().toLowerCase();

        if (keyword.isEmpty()) {
            loadTable();
            return;
        }

        DefaultTableModel model = (DefaultTableModel) view.getTblBarang().getModel();
        model.setRowCount(0);

        ArrayList<Barang> barangList = BarangDAO.getAll();
        currentBarangList.clear();

        for (Barang b : barangList) {
            if (b.getNama_barang().toLowerCase().contains(keyword) ||
                    b.getSatuan().toLowerCase().contains(keyword)) {
                model.addRow(new Object[]{
                        b.getNama_barang(),
                        formatRupiah(b.getHarga()),
                        b.getStok(),
                        b.getSatuan()
                });
                currentBarangList.add(b);
            }
        }

        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(view,
                    "Tidak ditemukan barang dengan kata kunci: '" + keyword + "'",
                    "Pencarian Tidak Ditemukan",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void setFormMode(boolean isEditMode) {
        if (!isEditMode) {
            view.getTxtNama().setText("");
            view.getTxtHarga().setText("");
            view.getTxtStok().setText("");
            view.getTxtSatuan().setText("");
        }

        if (isEditMode) {
            view.getTxtNama().requestFocus();
        }
    }

    private String formatRupiah(int amount) {
        return "Rp " + String.format("%,d", amount).replace(',', '.');
    }
}