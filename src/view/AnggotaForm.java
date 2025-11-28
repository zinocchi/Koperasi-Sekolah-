package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

public class AnggotaForm extends JFrame {
    private JTable tblAnggota;
    private DefaultTableModel tableModel;
    private JTextField txtCari;
    private JTextField txtNama;
    private JTextField txtKelas;
    private JTextField txtAlamat;
    private JTextField txtNoHp;
    private JButton btnSimpan;
    private JButton btnBatal;
    private JButton btnKembali;
    private JButton btnBack;
    private JButton btnDelete;

    public AnggotaForm() {
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Data Anggota - Koperasi Sikma");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 700);

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
        JLabel lblTitle = new JLabel("👥 Data Anggota", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);

        headerPanel.add(leftHeader, BorderLayout.WEST);
        headerPanel.add(lblTitle, BorderLayout.CENTER);

        // Content Panel
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setOpaque(false);

        // Left Panel - Table Section
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setOpaque(false);

        // Search Panel - DIPERBAIKI LAYOUTNYA
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JPanel searchLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        searchLeft.setOpaque(false);

        JLabel lblCari = new JLabel("🔍 Cari Anggota:");
        lblCari.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblCari.setForeground(new Color(80, 80, 80));

        txtCari = new JTextField(25);
        txtCari.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtCari.setPreferredSize(new Dimension(250, 35));
        txtCari.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // TAMBAH PLACEHOLDER TEXT
        txtCari.setToolTipText("Ketik nama atau kelas anggota...");

        searchLeft.add(lblCari);
        searchLeft.add(txtCari);

        // Delete button di kanan
        JPanel searchRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        searchRight.setOpaque(false);

        btnDelete = createStyledButton("🗑 Hapus", new Color(239, 83, 80), 100, 35);
        btnDelete.setToolTipText("Hapus anggota yang dipilih");
        searchRight.add(btnDelete);

        searchPanel.add(searchLeft, BorderLayout.WEST);
        searchPanel.add(searchRight, BorderLayout.EAST);

        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));

        // 4 KOLOM: Nama, Kelas, Alamat, No HP
        String[] columnNames = {"Nama", "Kelas", "Alamat", "No HP"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return String.class;
            }
        };

        tblAnggota = new JTable(tableModel);
        tblAnggota.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblAnggota.setRowHeight(35);
        tblAnggota.setSelectionBackground(new Color(108, 156, 184, 100));
        tblAnggota.setSelectionForeground(Color.BLACK);
        tblAnggota.setShowVerticalLines(true);
        tblAnggota.setGridColor(new Color(240, 240, 240));
        tblAnggota.setIntercellSpacing(new Dimension(1, 1));
        tblAnggota.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Set column widths yang lebih proporsional
        tblAnggota.getColumnModel().getColumn(0).setPreferredWidth(200); // Nama
        tblAnggota.getColumnModel().getColumn(1).setPreferredWidth(120); // Kelas
        tblAnggota.getColumnModel().getColumn(2).setPreferredWidth(250); // Alamat
        tblAnggota.getColumnModel().getColumn(3).setPreferredWidth(130); // No HP

        // Table Header Styling
        JTableHeader header = tblAnggota.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(108, 156, 184));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));

        // Center align semua header
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        headerRenderer.setBackground(new Color(108, 156, 184));
        headerRenderer.setForeground(Color.WHITE);
        headerRenderer.setFont(new Font("Segoe UI", Font.BOLD, 13));

        for (int i = 0; i < tblAnggota.getColumnCount(); i++) {
            tblAnggota.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        // Cell alignment yang konsisten
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(JLabel.LEFT);

        tblAnggota.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);    // Nama - LEFT
        tblAnggota.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);  // Kelas - CENTER
        tblAnggota.getColumnModel().getColumn(2).setCellRenderer(leftRenderer);    // Alamat - LEFT
        tblAnggota.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);  // No HP - CENTER

        JScrollPane scrollPane = new JScrollPane(tblAnggota);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        leftPanel.add(searchPanel, BorderLayout.NORTH);
        leftPanel.add(tablePanel, BorderLayout.CENTER);

        // Right Panel - Form Section
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(380, 0));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblFormTitle = new JLabel("Form Data Anggota");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblFormTitle.setForeground(new Color(70, 70, 70));
        lblFormTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);

        // Field Nama - TAMBAH VALIDASI VISUAL
        addFormField(formPanel, "Nama *", txtNama = createFormTextField());
        txtNama.setToolTipText("Masukkan nama lengkap anggota");

        // Field Kelas
        addFormField(formPanel, "Kelas", txtKelas = createFormTextField());
        txtKelas.setToolTipText("Contoh: X IPA 1, XI IPS 2, dll.");

        // Field Alamat
        addFormField(formPanel, "Alamat", txtAlamat = createFormTextField());
        txtAlamat.setToolTipText("Alamat lengkap anggota");

        // Field No HP - TAMBAH VALIDASI NUMERIC
        addFormField(formPanel, "No HP", txtNoHp = createFormTextField());
        txtNoHp.setToolTipText("Nomor HP (hanya angka)");

        // TAMBAH DOCUMENT LISTENER UNTUK VALIDASI NO HP
        txtNoHp.getDocument().addDocumentListener(new DocumentListener() {
            private void validateNumber() {
                String text = txtNoHp.getText();
                if (!text.matches("\\d*")) {
                    txtNoHp.setBackground(new Color(255, 230, 230)); // Light red for invalid
                    txtNoHp.setToolTipText("Hanya boleh mengandung angka!");
                } else {
                    txtNoHp.setBackground(Color.WHITE);
                    txtNoHp.setToolTipText("Nomor HP (hanya angka)");
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) { validateNumber(); }
            @Override
            public void removeUpdate(DocumentEvent e) { validateNumber(); }
            @Override
            public void changedUpdate(DocumentEvent e) { validateNumber(); }
        });

        formPanel.add(Box.createVerticalStrut(25));

        // Info panel untuk required field
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setOpaque(false);
        JLabel lblInfo = new JLabel("* Field wajib diisi");
        lblInfo.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblInfo.setForeground(new Color(150, 150, 150));
        infoPanel.add(lblInfo);
        formPanel.add(infoPanel);

        formPanel.add(Box.createVerticalStrut(15));

        // Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        btnSimpan = createStyledButton("💾 Simpan", new Color(108, 156, 184), 0, 40);
        btnSimpan.setToolTipText("Simpan data anggota");

        btnBatal = createStyledButton("✖ Batal", new Color(189, 189, 189), 0, 40);
        btnBatal.setToolTipText("Bersihkan form");

        btnKembali = createStyledButton("← Kembali", new Color(149, 117, 205), 0, 40);
        btnKembali.setToolTipText("Kembali ke dashboard");

        buttonPanel.add(btnSimpan);
        buttonPanel.add(btnBatal);
        buttonPanel.add(btnKembali);
        buttonPanel.add(new JLabel()); // Empty cell

        formPanel.add(buttonPanel);

        JPanel rightContent = new JPanel(new BorderLayout());
        rightContent.setOpaque(false);
        rightContent.add(lblFormTitle, BorderLayout.NORTH);
        rightContent.add(formPanel, BorderLayout.CENTER);

        rightPanel.add(rightContent, BorderLayout.CENTER);

        // Add to content panel
        contentPanel.add(leftPanel, BorderLayout.CENTER);
        contentPanel.add(rightPanel, BorderLayout.EAST);

        // Add to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    // METHOD BARU: Helper untuk buat form field yang konsisten
    private void addFormField(JPanel parent, String label, JTextField textField) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        parent.add(lbl);
        parent.add(Box.createVerticalStrut(5));
        parent.add(textField);
        parent.add(Box.createVerticalStrut(15));
    }

    // METHOD BARU: Helper untuk buat textfield yang konsisten
    private JTextField createFormTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        return textField;
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

    // Getter methods
    public JTable getTblAnggota() { return tblAnggota; }
    public DefaultTableModel getTableModel() { return tableModel; }
    public JTextField getTxtCari() { return txtCari; }
    public JTextField getTxtNama() { return txtNama; }
    public JTextField getTxtKelas() { return txtKelas; }
    public JTextField getTxtAlamat() { return txtAlamat; }
    public JTextField getTxtNoHp() { return txtNoHp; }
    public JButton getBtnSimpan() { return btnSimpan; }
    public JButton getBtnBatal() { return btnBatal; }
    public JButton getBtnKembali() { return btnKembali; }
    public JButton getBtnBack() { return btnBack; }
    public JButton getBtnDelete() { return btnDelete; }
}