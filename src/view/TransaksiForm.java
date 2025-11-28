package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

public class TransaksiForm extends JFrame {
    // Components untuk bagian atas
    private JLabel lblTanggal;
    private JLabel lblTotal;
    private JComboBox<String> cmbAnggota;
    private JButton btnBack;

    // Components untuk daftar barang
    private JTable tblBarang;
    private DefaultTableModel tableModelBarang;
    private JTextField txtCariBarang;
    private JTextField txtJumlah;
    private JButton btnTambahKeranjang;

    // Components untuk keranjang
    private JTable tblKeranjang;
    private DefaultTableModel tableModelKeranjang;
    private JButton btnHapusKeranjang;

    // Components untuk riwayat transaksi
    private JTable tblTransaksi;
    private DefaultTableModel tableModelTransaksi;

    // Action buttons
    private JButton btnBayar;
    private JButton btnBatal;
    private JButton btnKembali;

    public TransaksiForm() {
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Transaksi - Koperasi Sikma");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1400, 800);

        // Main panel dengan gradient background
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(138, 186, 214),
                        0, getHeight(), new Color(108, 156, 184)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Back button
        btnBack = createStyledButton("← Dashboard", new Color(100, 100, 100), 130, 40);

        JPanel leftHeader = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftHeader.setOpaque(false);
        leftHeader.add(btnBack);

        // Title
        JLabel lblTitle = new JLabel("💰 Transaksi Penjualan", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);

        headerPanel.add(leftHeader, BorderLayout.WEST);
        headerPanel.add(lblTitle, BorderLayout.CENTER);

        // Content Panel - menggunakan GridBagLayout untuk layout yang lebih fleksibel
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();

        // ===== PANEL INFORMASI TRANSAKSI =====
        JPanel infoPanel = createInfoPanel();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 15, 0);
        contentPanel.add(infoPanel, gbc);

        // ===== PANEL DAFTAR BARANG =====
        JPanel barangPanel = createBarangPanel();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.6;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 15, 10);
        contentPanel.add(barangPanel, gbc);

        // ===== PANEL KERANJANG =====
        JPanel keranjangPanel = createKeranjangPanel();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.4;
        gbc.insets = new Insets(0, 10, 15, 0);
        contentPanel.add(keranjangPanel, gbc);

        // ===== PANEL RIWAYAT TRANSAKSI =====
        JPanel riwayatPanel = createRiwayatPanel();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.4;
        gbc.insets = new Insets(0, 0, 0, 0);
        contentPanel.add(riwayatPanel, gbc);

        // ===== PANEL ACTION BUTTONS =====
        JPanel actionPanel = createActionPanel();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 0, 0, 0);
        contentPanel.add(actionPanel, gbc);

        // Add to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 4, 15, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Tanggal
        JLabel lblTanggalTitle = new JLabel("📅 Tanggal:");
        lblTanggalTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTanggal = new JLabel("20/11/2024 08:17:18"); // FIX: Set default tanggal
        lblTanggal.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTanggal.setForeground(new Color(70, 70, 70));

        // Anggota
        JLabel lblAnggotaTitle = new JLabel("👥 Anggota:");
        lblAnggotaTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        cmbAnggota = new JComboBox<>();
        cmbAnggota.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbAnggota.addItem("Pilih Anggota"); // FIX: Default item

        // Total
        JLabel lblTotalTitle = new JLabel("💰 Total:");
        lblTotalTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTotal = new JLabel("Rp 0");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTotal.setForeground(Color.RED);

        // Empty space
        JLabel empty = new JLabel();

        panel.add(lblTanggalTitle);
        panel.add(lblTanggal);
        panel.add(lblAnggotaTitle);
        panel.add(cmbAnggota);
        panel.add(lblTotalTitle);
        panel.add(lblTotal);
        panel.add(empty);
        panel.add(empty);

        return panel;
    }

    private JPanel createBarangPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Search Panel
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setOpaque(false);

        JLabel lblCari = new JLabel("🔍 Cari Barang:");
        lblCari.setFont(new Font("Segoe UI", Font.BOLD, 13));

        txtCariBarang = new JTextField();
        txtCariBarang.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtCariBarang.setPreferredSize(new Dimension(200, 35));
        txtCariBarang.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // FIX: Placeholder yang benar
        txtCariBarang.setText("");
        txtCariBarang.setForeground(Color.BLACK);

        JPanel searchLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        searchLeft.setOpaque(false);
        searchLeft.add(lblCari);
        searchLeft.add(txtCariBarang);

        searchPanel.add(searchLeft, BorderLayout.WEST);

        // Control Panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        controlPanel.setOpaque(false);
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JLabel lblJumlah = new JLabel("Jumlah:");
        lblJumlah.setFont(new Font("Segoe UI", Font.BOLD, 13));

        txtJumlah = new JTextField("1", 5);
        txtJumlah.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtJumlah.setPreferredSize(new Dimension(60, 35));
        txtJumlah.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // FIX: Validasi input jumlah hanya angka
        txtJumlah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!Character.isDigit(c) && c != '\b') {
                    evt.consume();
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        });

        btnTambahKeranjang = createStyledButton("➕ Tambah ke Keranjang", new Color(129, 199, 132), 0, 35);

        controlPanel.add(lblJumlah);
        controlPanel.add(txtJumlah);
        controlPanel.add(btnTambahKeranjang);

        // Table Panel - FIX: Table model dengan data contoh
        String[] columnNamesBarang = {"Nama Barang", "Harga", "Stok", "Satuan"};
        tableModelBarang = new DefaultTableModel(columnNamesBarang, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // FIX: Tambahkan data contoh untuk testing
        tableModelBarang.addRow(new Object[]{"Pensil 2B", "Rp 2.500", 50, "pcs"});
        tableModelBarang.addRow(new Object[]{"Buku Tulis", "Rp 6.000", 30, "pcs"});
        tableModelBarang.addRow(new Object[]{"Bolpoin", "Rp 4.000", 40, "pcs"});
        tableModelBarang.addRow(new Object[]{"Penghapus", "Rp 2.000", 25, "pcs"});
        tableModelBarang.addRow(new Object[]{"Penggaris", "Rp 3.000", 20, "pcs"});

        tblBarang = new JTable(tableModelBarang);
        styleTable(tblBarang);

        // FIX: Selection mode untuk tabel barang
        tblBarang.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Set column widths
        tblBarang.getColumnModel().getColumn(0).setPreferredWidth(200);
        tblBarang.getColumnModel().getColumn(1).setPreferredWidth(100);
        tblBarang.getColumnModel().getColumn(2).setPreferredWidth(80);
        tblBarang.getColumnModel().getColumn(3).setPreferredWidth(80);

        JScrollPane scrollPane = new JScrollPane(tblBarang);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setPreferredSize(new Dimension(500, 200));

        // Add to panel
        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(controlPanel, BorderLayout.CENTER);
        panel.add(scrollPane, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createKeranjangPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Header
        JLabel lblTitle = new JLabel("🛒 Keranjang Belanja");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Delete button
        btnHapusKeranjang = createStyledButton("🗑 Hapus dari Keranjang", new Color(239, 83, 80), 0, 35);
        btnHapusKeranjang.setEnabled(false); // FIX: Disable awal

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(btnHapusKeranjang, BorderLayout.EAST);

        // Table Panel - FIX: Keranjang table model
        String[] columnNamesKeranjang = {"Nama Barang", "Harga", "Jumlah", "Subtotal"};
        tableModelKeranjang = new DefaultTableModel(columnNamesKeranjang, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblKeranjang = new JTable(tableModelKeranjang);
        styleTable(tblKeranjang);

        // FIX: Selection mode untuk tabel keranjang
        tblKeranjang.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // FIX: Listener untuk enable/disable tombol hapus
        tblKeranjang.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                btnHapusKeranjang.setEnabled(tblKeranjang.getSelectedRow() != -1);
            }
        });

        // Set column widths
        tblKeranjang.getColumnModel().getColumn(0).setPreferredWidth(150);
        tblKeranjang.getColumnModel().getColumn(1).setPreferredWidth(100);
        tblKeranjang.getColumnModel().getColumn(2).setPreferredWidth(80);
        tblKeranjang.getColumnModel().getColumn(3).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(tblKeranjang);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setPreferredSize(new Dimension(400, 200));

        // FIX: Handle empty keranjang
        if (tableModelKeranjang.getRowCount() == 0) {
            // Kosong, tidak perlu label khusus karena table sudah kosong
        }

        // Add to panel
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createRiwayatPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Header
        JLabel lblTitle = new JLabel("📊 Riwayat Transaksi Terbaru");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Table Panel - FIX: Riwayat table model dengan data contoh
        String[] columnNamesTransaksi = {"ID Transaksi", "Nama Anggota", "Tanggal", "Total"};
        tableModelTransaksi = new DefaultTableModel(columnNamesTransaksi, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // FIX: Tambahkan data contoh riwayat
        tableModelTransaksi.addRow(new Object[]{"TRX001", "Budi Santoso", "20/11/2024", "Rp 45.000"});
        tableModelTransaksi.addRow(new Object[]{"TRX002", "Siti Aminah", "19/11/2024", "Rp 12.500"});
        tableModelTransaksi.addRow(new Object[]{"TRX003", "Ahmad Rizki", "18/11/2024", "Rp 28.000"});

        tblTransaksi = new JTable(tableModelTransaksi);
        styleTable(tblTransaksi);

        // Set column widths
        tblTransaksi.getColumnModel().getColumn(0).setPreferredWidth(100);
        tblTransaksi.getColumnModel().getColumn(1).setPreferredWidth(150);
        tblTransaksi.getColumnModel().getColumn(2).setPreferredWidth(120);
        tblTransaksi.getColumnModel().getColumn(3).setPreferredWidth(120);

        JScrollPane scrollPane = new JScrollPane(tblTransaksi);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setPreferredSize(new Dimension(800, 150));

        // Add to panel
        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setOpaque(false);

        btnBayar = createStyledButton("💳 Bayar & Simpan", new Color(129, 199, 132), 180, 45);
        btnBayar.setEnabled(false); // FIX: Disable awal

        btnBatal = createStyledButton("✖ Batal Transaksi", new Color(189, 189, 189), 150, 45);
        btnKembali = createStyledButton("← Kembali", new Color(149, 117, 205), 120, 45);

        panel.add(btnBayar);
        panel.add(btnBatal);
        panel.add(btnKembali);

        return panel;
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(35);
        table.setSelectionBackground(new Color(108, 156, 184, 100));
        table.setSelectionForeground(Color.BLACK);
        table.setShowVerticalLines(true);
        table.setGridColor(new Color(240, 240, 240));
        table.setIntercellSpacing(new Dimension(1, 1));

        // Table Header Styling
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(108, 156, 184));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));

        // Center align header
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        headerRenderer.setBackground(new Color(108, 156, 184));
        headerRenderer.setForeground(Color.WHITE);
        headerRenderer.setFont(new Font("Segoe UI", Font.BOLD, 13));

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        // Cell alignment
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(JLabel.LEFT);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);

        // Apply alignment based on column content
        for (int i = 0; i < table.getColumnCount(); i++) {
            String columnName = table.getColumnName(i);
            if (columnName.contains("Harga") || columnName.contains("Total") || columnName.contains("Subtotal") || columnName.contains("Jumlah")) {
                table.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
            } else if (columnName.contains("ID") || columnName.contains("Stok") || columnName.contains("Satuan")) {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            } else {
                table.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
            }
        }
    }

    private JButton createStyledButton(String text, Color color, int width, int height) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color baseColor = color;
                if (getModel().isPressed()) {
                    baseColor = color.darker();
                } else if (getModel().isRollover()) {
                    baseColor = color.brighter();
                }

                g2d.setColor(baseColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                g2d.setColor(getForeground());
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), x, y);
            }
        };

        if (width > 0) {
            button.setPreferredSize(new Dimension(width, height));
        }
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    public void setBackButtonListener(java.awt.event.ActionListener listener) {
        btnBack.addActionListener(listener);
        btnKembali.addActionListener(listener);
    }

    // FIX: Method untuk update total
    public void updateTotal(String total) {
        lblTotal.setText(total);
    }

    // FIX: Method untuk update tanggal
    public void updateTanggal(String tanggal) {
        lblTanggal.setText(tanggal);
    }

    // FIX: Method untuk enable/disable tombol berdasarkan kondisi
    public void updateButtonStates(boolean hasItemsInCart) {
        btnBayar.setEnabled(hasItemsInCart);
        // Tombol hapus hanya enable jika ada item yang dipilih
        btnHapusKeranjang.setEnabled(hasItemsInCart && tblKeranjang.getSelectedRow() != -1);
    }

    // FIX: Method untuk clear keranjang
    public void clearKeranjang() {
        tableModelKeranjang.setRowCount(0);
        updateButtonStates(false);
    }

    // FIX: Method untuk tambah item ke keranjang
    public void addToKeranjang(String namaBarang, String harga, int jumlah, String subtotal) {
        tableModelKeranjang.addRow(new Object[]{namaBarang, harga, jumlah, subtotal});
        updateButtonStates(true);
    }

    // Getter methods
    public JLabel getLblTanggal() { return lblTanggal; }
    public JLabel getLblTotal() { return lblTotal; }
    public JComboBox<String> getCmbAnggota() { return cmbAnggota; }
    public JTable getTblBarang() { return tblBarang; }
    public DefaultTableModel getTableModelBarang() { return tableModelBarang; }
    public JTextField getTxtCariBarang() { return txtCariBarang; }
    public JTextField getTxtJumlah() { return txtJumlah; }
    public JButton getBtnTambahKeranjang() { return btnTambahKeranjang; }
    public JTable getTblKeranjang() { return tblKeranjang; }
    public DefaultTableModel getTableModelKeranjang() { return tableModelKeranjang; }
    public JButton getBtnHapusKeranjang() { return btnHapusKeranjang; }
    public JTable getTblTransaksi() { return tblTransaksi; }
    public DefaultTableModel getTableModelTransaksi() { return tableModelTransaksi; }
    public JButton getBtnBayar() { return btnBayar; }
    public JButton getBtnBatal() { return btnBatal; }
    public JButton getBtnKembali() { return btnKembali; }
    public JButton getBtnBack() { return btnBack; }
}