package controller;

import dao.AnggotaDAO;
import model.Anggota;
import view.AnggotaForm;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class AnggotaController {

    private AnggotaForm view;
    private ArrayList<Anggota> currentAnggotaList;

    public AnggotaController(AnggotaForm view) {
        this.view = view;
        this.currentAnggotaList = new ArrayList<>();
        loadTable();
        initListeners();
    }

    private void loadTable() {
        DefaultTableModel model = (DefaultTableModel) view.getTblAnggota().getModel();
        model.setRowCount(0);

        currentAnggotaList = AnggotaDAO.getAll();

        for (Anggota a : currentAnggotaList) {
            model.addRow(new Object[]{
                    a.getNama(),
                    a.getKelas(),
                    a.getAlamat(),
                    a.getNo_hp()
            });
        }
    }

    private void initListeners() {
        view.getBtnSimpan().addActionListener(e -> save());
        view.getBtnDelete().addActionListener(e -> delete());
        view.getBtnBatal().addActionListener(e -> clearForm());

        view.getTblAnggota().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillForm();
            }
        });

        view.getTxtCari().addActionListener(e -> search());
    }

    private void save() {
        try {
            String nama = view.getTxtNama().getText().trim();
            String kelas = view.getTxtKelas().getText().trim();
            String alamat = view.getTxtAlamat().getText().trim();
            String noHp = view.getTxtNoHp().getText().trim();

            // Validasi input
            if (nama.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Nama harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int selectedRow = view.getTblAnggota().getSelectedRow();

            if (selectedRow != -1) {
                // EDIT mode
                Anggota selectedAnggota = currentAnggotaList.get(selectedRow);
                int id = selectedAnggota.getId_anggota();
                Anggota anggota = new Anggota(id, nama, kelas, alamat, noHp);

                if (AnggotaDAO.update(anggota)) {
                    JOptionPane.showMessageDialog(view, "Berhasil update anggota!");
                    loadTable();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(view, "Gagal update anggota!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // INSERT mode
                Anggota anggota = new Anggota(nama, kelas, alamat, noHp);

                if (AnggotaDAO.insert(anggota)) {
                    JOptionPane.showMessageDialog(view, "Berhasil menambah anggota!");
                    loadTable();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(view, "Gagal menambah anggota!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Terjadi kesalahan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void delete() {
        int selectedRow = view.getTblAnggota().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Pilih anggota yang ingin dihapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Anggota selectedAnggota = currentAnggotaList.get(selectedRow);
        int id = selectedAnggota.getId_anggota();
        String namaAnggota = selectedAnggota.getNama();

        int confirm = JOptionPane.showConfirmDialog(view,
                "Yakin ingin menghapus anggota: " + namaAnggota + "?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (AnggotaDAO.delete(id)) {
                JOptionPane.showMessageDialog(view, "Berhasil menghapus anggota!");
                loadTable();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(view, "Gagal menghapus anggota!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void fillForm() {
        int row = view.getTblAnggota().getSelectedRow();
        if (row == -1) return;

        Anggota selectedAnggota = currentAnggotaList.get(row);

        view.getTxtNama().setText(selectedAnggota.getNama());
        view.getTxtKelas().setText(selectedAnggota.getKelas());
        view.getTxtAlamat().setText(selectedAnggota.getAlamat());
        view.getTxtNoHp().setText(selectedAnggota.getNo_hp());
    }

    private void clearForm() {
        view.getTxtNama().setText("");
        view.getTxtKelas().setText("");
        view.getTxtAlamat().setText("");
        view.getTxtNoHp().setText("");
        view.getTblAnggota().clearSelection();
    }

    private void search() {
        String keyword = view.getTxtCari().getText().trim();

        if (keyword.isEmpty()) {
            loadTable();
            return;
        }

        DefaultTableModel model = (DefaultTableModel) view.getTblAnggota().getModel();
        model.setRowCount(0);

        currentAnggotaList = AnggotaDAO.searchByName(keyword);

        for (Anggota a : currentAnggotaList) {
            model.addRow(new Object[]{
                    a.getNama(),
                    a.getKelas(),
                    a.getAlamat(),
                    a.getNo_hp()
            });
        }
    }
}