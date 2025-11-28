package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class BarangForm extends JFrame {
    private JTable tblBarang;
    private DefaultTableModel tableModel;
    private JTextField txtCari;
    private JTextField txtNama;
    private JTextField txtHarga;
    private JTextField txtStok;
    private JTextField txtSatuan;
    private JButton btnSimpan;
    private JButton btnBatal;
    private JButton btnKembali;
    private JButton btnBack;
    private JButton btnDelete;

    public BarangForm() {
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Manajemen Barang - Koperasi Sikma");
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
        JLabel lblTitle = new JLabel("📦 Manajemen Barang", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);

        headerPanel.add(leftHeader, BorderLayout.WEST);
        headerPanel.add(lblTitle, BorderLayout.CENTER);

        // Content Panel
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setOpaque(false);

        // Left Panel - Table Section
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setOpaque(false);

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JLabel lblCari = new JLabel("🔍 Cari Barang:");
        lblCari.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblCari.setForeground(new Color(80, 80, 80));

        txtCari = new JTextField(30);
        txtCari.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtCari.setPreferredSize(new Dimension(300, 35));
        txtCari.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // Delete button di sebelah search
        btnDelete = createStyledButton("🗑 Delete", new Color(239, 83, 80), 100, 35);

        searchPanel.add(lblCari);
        searchPanel.add(txtCari);
        searchPanel.add(Box.createHorizontalStrut(10)); // Spacer
        searchPanel.add(btnDelete);

        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // HANYA 4 KOLOM: Nama Barang, Harga, Stok, Satuan
        String[] columnNames = {"Nama Barang", "Harga", "Stok", "Satuan"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblBarang = new JTable(tableModel);
        tblBarang.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblBarang.setRowHeight(35);
        tblBarang.setSelectionBackground(new Color(108, 156, 184, 100));
        tblBarang.setSelectionForeground(Color.BLACK);
        tblBarang.setShowVerticalLines(false);
        tblBarang.setIntercellSpacing(new Dimension(0, 5));

        // Set column widths
        tblBarang.getColumnModel().getColumn(0).setPreferredWidth(250); // Nama Barang
        tblBarang.getColumnModel().getColumn(1).setPreferredWidth(150); // Harga
        tblBarang.getColumnModel().getColumn(2).setPreferredWidth(100); // Stok
        tblBarang.getColumnModel().getColumn(3).setPreferredWidth(100); // Satuan

        // Table Header Styling
        JTableHeader header = tblBarang.getTableHeader();
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

        for (int i = 0; i < tblBarang.getColumnCount(); i++) {
            tblBarang.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        // Center align cells untuk Harga, Stok, Satuan
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        // Left align untuk Nama Barang
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(JLabel.LEFT);

        tblBarang.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);  // Nama Barang - LEFT
        tblBarang.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // Harga - CENTER
        tblBarang.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); // Stok - CENTER
        tblBarang.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // Satuan - CENTER

        JScrollPane scrollPane = new JScrollPane(tblBarang);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        leftPanel.add(searchPanel, BorderLayout.NORTH);
        leftPanel.add(tablePanel, BorderLayout.CENTER);

        // Right Panel - Form Section
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(350, 0));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblFormTitle = new JLabel("Form Data Barang");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblFormTitle.setForeground(new Color(70, 70, 70));
        lblFormTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);

        // Nama Barang
        JLabel lblNama = new JLabel("Nama Barang");
        lblNama.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblNama.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtNama = new JTextField();
        txtNama.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtNama.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txtNama.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        // Harga
        JLabel lblHarga = new JLabel("Harga");
        lblHarga.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblHarga.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtHarga = new JTextField();
        txtHarga.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtHarga.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txtHarga.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        // Stok
        JLabel lblStok = new JLabel("Stok");
        lblStok.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblStok.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtStok = new JTextField();
        txtStok.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtStok.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txtStok.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        // Satuan
        JLabel lblSatuan = new JLabel("Satuan");
        lblSatuan.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSatuan.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtSatuan = new JTextField();
        txtSatuan.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtSatuan.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txtSatuan.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        formPanel.add(lblNama);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(txtNama);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(lblHarga);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(txtHarga);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(lblStok);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(txtStok);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(lblSatuan);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(txtSatuan);
        formPanel.add(Box.createVerticalStrut(25));

        // Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        btnSimpan = createStyledButton("💾 Simpan", new Color(108, 156, 184), 0, 40);
        btnBatal = createStyledButton("✖ Batal", new Color(189, 189, 189), 0, 40);
        btnKembali = createStyledButton("← Kembali", new Color(149, 117, 205), 0, 40);

        buttonPanel.add(btnSimpan);
        buttonPanel.add(btnBatal);
        buttonPanel.add(btnKembali);
        buttonPanel.add(new JLabel()); // Empty cell untuk alignment

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

    // Method untuk set back button listener
    public void setBackButtonListener(java.awt.event.ActionListener listener) {
        btnBack.addActionListener(listener);
        btnKembali.addActionListener(listener);
    }

    // Getter methods
    public JTable getTblBarang() { return tblBarang; }
    public DefaultTableModel getTableModel() { return tableModel; }
    public JTextField getTxtCari() { return txtCari; }
    public JTextField getTxtNama() { return txtNama; }
    public JTextField getTxtHarga() { return txtHarga; }
    public JTextField getTxtStok() { return txtStok; }
    public JTextField getTxtSatuan() { return txtSatuan; }
    public JButton getBtnSimpan() { return btnSimpan; }
    public JButton getBtnBatal() { return btnBatal; }
    public JButton getBtnKembali() { return btnKembali; }
    public JButton getBtnBack() { return btnBack; }
    public JButton getBtnDelete() { return btnDelete; }
}