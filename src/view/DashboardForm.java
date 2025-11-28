package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class DashboardForm extends JFrame {
    private JButton btnBarangForm;
    private JButton btnAnggotaForm;
    private JButton btnTransaksiForm;
    private JButton btnDetailTransaksiForm;
    private JButton btnLogout;
    private JLabel lblWelcome;

    public DashboardForm() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Dashboard - Koperasi Sikma");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

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

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Left side header
        JPanel leftHeader = new JPanel();
        leftHeader.setOpaque(false);
        leftHeader.setLayout(new BoxLayout(leftHeader, BoxLayout.Y_AXIS));

        lblWelcome = new JLabel("Selamat Datang di Koperasi");
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblWelcome.setForeground(Color.WHITE);

        JLabel lblSubHeader = new JLabel("Sistem Informasi Koperasi Sekolah");
        lblSubHeader.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubHeader.setForeground(new Color(255, 255, 255, 200));

        leftHeader.add(lblWelcome);
        leftHeader.add(Box.createVerticalStrut(5));
        leftHeader.add(lblSubHeader);

        // Logout button
        btnLogout = new JButton("Logout") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(new Color(200, 50, 50));
                } else if (getModel().isRollover()) {
                    g2d.setColor(new Color(230, 70, 70));
                } else {
                    g2d.setColor(new Color(220, 60, 60));
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                g2d.setColor(getForeground());
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), x, y);
            }
        };
        btnLogout.setPreferredSize(new Dimension(120, 45));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogout.setBorderPainted(false);
        btnLogout.setContentAreaFilled(false);
        btnLogout.setFocusPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));

        headerPanel.add(leftHeader, BorderLayout.WEST);
        headerPanel.add(btnLogout, BorderLayout.EAST);

        // Menu Panel dengan card style
        JPanel menuContainer = new JPanel();
        menuContainer.setOpaque(false);
        menuContainer.setLayout(new GridBagLayout());

        JPanel menuPanel = new JPanel(new GridLayout(2, 2, 25, 25));
        menuPanel.setOpaque(false);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        menuPanel.setPreferredSize(new Dimension(900, 550));

        // Create menu cards
        btnBarangForm = createMenuCard("Manajemen Barang", "📦",
                "Kelola data barang dan stok koperasi", new Color(108, 156, 184));
        btnAnggotaForm = createMenuCard("Data Anggota", "👥",
                "Kelola data anggota koperasi", new Color(100, 181, 246));
        btnTransaksiForm = createMenuCard("Transaksi", "💰",
                "Catat transaksi penjualan", new Color(129, 199, 132));
        btnDetailTransaksiForm = createMenuCard("Detail Transaksi", "📊",
                "Lihat riwayat & detail transaksi", new Color(149, 117, 205));

        menuPanel.add(btnBarangForm);
        menuPanel.add(btnAnggotaForm);
        menuPanel.add(btnTransaksiForm);
        menuPanel.add(btnDetailTransaksiForm);

        menuContainer.add(menuPanel);

        JPanel footerPanel = new JPanel();
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel lblFooter = new JLabel("© 2024 Koperasi-Sikma - Sistem Informasi Koperasi Sekolah");
        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblFooter.setForeground(new Color(255, 255, 255, 200));

        footerPanel.add(lblFooter);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(menuContainer, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JButton createMenuCard(String title, String emoji, String description, Color baseColor) {
        JButton card = new JButton() {
            private boolean isHovered = false;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(new Color(0, 0, 0, 40));
                g2d.fillRoundRect(6, 6, getWidth() - 12, getHeight() - 12, 20, 20);

                if (getModel().isPressed()) {
                    g2d.setColor(baseColor.darker());
                } else if (isHovered) {
                    g2d.setColor(baseColor.brighter());
                } else {
                    g2d.setColor(Color.WHITE);
                }
                g2d.fillRoundRect(0, 0, getWidth() - 12, getHeight() - 12, 20, 20);

                g2d.setColor(new Color(230, 230, 230));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, getWidth() - 12, getHeight() - 12, 20, 20);

                // Icon circle background
                int circleSize = 80;
                int circleX = (getWidth() - 12 - circleSize) / 2;
                int circleY = 40;

                g2d.setColor(new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), 30));
                g2d.fillOval(circleX, circleY, circleSize, circleSize);

                // Icon
                g2d.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 42));
                FontMetrics fmEmoji = g2d.getFontMetrics();
                int emojiX = circleX + (circleSize - fmEmoji.stringWidth(emoji)) / 2;
                int emojiY = circleY + (circleSize + fmEmoji.getAscent() - fmEmoji.getDescent()) / 2;
                g2d.drawString(emoji, emojiX, emojiY);

                // Title
                g2d.setColor(new Color(60, 60, 60));
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 18));
                FontMetrics fmTitle = g2d.getFontMetrics();
                int titleX = (getWidth() - 12 - fmTitle.stringWidth(title)) / 2;
                g2d.drawString(title, titleX, 150);

                // Description
                g2d.setColor(new Color(120, 120, 120));
                g2d.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                FontMetrics fmDesc = g2d.getFontMetrics();

                // Wrap text
                String[] words = description.split(" ");
                StringBuilder line = new StringBuilder();
                int y = 180;

                for (String word : words) {
                    String testLine = line + word + " ";
                    if (fmDesc.stringWidth(testLine) > getWidth() - 60) {
                        int descX = (getWidth() - 12 - fmDesc.stringWidth(line.toString().trim())) / 2;
                        g2d.drawString(line.toString().trim(), descX, y);
                        line = new StringBuilder(word + " ");
                        y += 18;
                    } else {
                        line.append(word).append(" ");
                    }
                }
                if (line.length() > 0) {
                    int descX = (getWidth() - 12 - fmDesc.stringWidth(line.toString().trim())) / 2;
                    g2d.drawString(line.toString().trim(), descX, y);
                }
            }
        };

        card.setBorderPainted(false);
        card.setContentAreaFilled(false);
        card.setFocusPainted(false);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setPreferredSize(new Dimension(200, 240));

        // Hover effect
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                ((JButton)e.getSource()).putClientProperty("isHovered", true);
                card.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ((JButton)e.getSource()).putClientProperty("isHovered", false);
                card.repaint();
            }
        });

        return card;
    }

    public void navigateToBarangForm() {
        BarangForm barangForm = new BarangForm();
        barangForm.setVisible(true);
        barangForm.setLocationRelativeTo(null);
        this.dispose();
    }

    public void navigateToAnggotaForm() {
        AnggotaForm anggotaForm = new AnggotaForm();
        anggotaForm.setVisible(true);
        anggotaForm.setLocationRelativeTo(null);
        this.dispose();
    }

    public void navigateToTransaksiForm() {
        TransaksiForm transaksiForm = new TransaksiForm();
        transaksiForm.setVisible(true);
        transaksiForm.setLocationRelativeTo(null);
        this.dispose();
    }

//    public void navigateToDetailTransaksiForm() {
//        DetailTransaksiForm detailForm = new DetailTransaksiForm();
//        detailForm.setVisible(true);
//        detailForm.setLocationRelativeTo(null);
//        this.dispose();
//    }

    public JButton getBtnBarangForm() { return btnBarangForm; }
    public JButton getBtnAnggotaForm() { return btnAnggotaForm; }
    public JButton getBtnTransaksiForm() { return btnTransaksiForm; }
    public JButton getBtnDetailTransaksiForm() { return btnDetailTransaksiForm; }
    public JButton getBtnLogout() { return btnLogout; }
    public JLabel getLblWelcome() { return lblWelcome; }
}