import javax.swing.*;
import view.*;
import controller.BarangController;
import controller.AnggotaController;
import controller.TransaksiController;

public class Main {
    private static DashboardForm currentDashboard; // Variabel global untuk dashboard

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            showLoginForm();
        });
    }

    private static void showLoginForm() {
        LoginForm loginForm = new LoginForm();
        setupLoginController(loginForm);
        loginForm.setVisible(true);
        loginForm.setLocationRelativeTo(null);
    }

    private static void setupLoginController(LoginForm loginForm) {
        loginForm.getBtnLogin().addActionListener(e -> {
            String username = loginForm.getTxtUsername().getText();
            String password = new String(loginForm.getTxtPassword().getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(loginForm,
                        "Username dan password harus diisi!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (loginDummy(username, password)) {
                JOptionPane.showMessageDialog(loginForm,
                        "Login berhasil! Selamat datang di Koperasi Sikma.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                loginForm.dispose();
                showDashboard();
            } else {
                JOptionPane.showMessageDialog(loginForm,
                        "Login gagal! Username atau password salah.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        loginForm.getBtnCancel().addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(loginForm,
                    "Yakin ingin keluar dari aplikasi?",
                    "Konfirmasi Keluar",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        loginForm.getTxtPassword().addActionListener(e -> {
            loginForm.getBtnLogin().doClick();
        });
    }

    private static boolean loginDummy(String username, String password) {
        return "admin".equals(username) && "admin".equals(password);
    }

    private static void showDashboard() {
        currentDashboard = new DashboardForm();
        setupDashboardController(currentDashboard);
        currentDashboard.setVisible(true);
        currentDashboard.setLocationRelativeTo(null);
    }

    private static void setupDashboardController(DashboardForm dashboard) {
        dashboard.getBtnBarangForm().addActionListener(e -> {
            BarangForm barangForm = new BarangForm();

            new BarangController(barangForm);

            barangForm.setBackButtonListener(backEvent -> {
                barangForm.dispose();
                dashboard.setVisible(true);
                refreshDashboardData(); // Refresh data setelah kembali
            });

            barangForm.setVisible(true);
            barangForm.setLocationRelativeTo(null);
            dashboard.setVisible(false);
        });

        dashboard.getBtnAnggotaForm().addActionListener(e -> {
            AnggotaForm anggotaForm = new AnggotaForm();

            new AnggotaController(anggotaForm);

            anggotaForm.setBackButtonListener(backEvent -> {
                anggotaForm.dispose();
                dashboard.setVisible(true);
                refreshDashboardData(); // Refresh data setelah kembali
            });

            anggotaForm.setVisible(true);
            anggotaForm.setLocationRelativeTo(null);
            dashboard.setVisible(false);
        });

        dashboard.getBtnTransaksiForm().addActionListener(e -> {
            TransaksiForm transaksiForm = new TransaksiForm();

            new TransaksiController(transaksiForm);

            transaksiForm.setBackButtonListener(backEvent -> {
                transaksiForm.dispose();
                dashboard.setVisible(true);
                refreshDashboardData(); // Refresh data setelah kembali
            });

            transaksiForm.setVisible(true);
            transaksiForm.setLocationRelativeTo(null);
            dashboard.setVisible(false);
        });

        dashboard.getBtnDetailTransaksiForm().addActionListener(e -> {
            JOptionPane.showMessageDialog(dashboard,
                    "Fitur Detail Transaksi akan segera hadir!\n\n" +
                            "Fitur ini akan menampilkan detail lengkap dari setiap transaksi " +
                            "yang dilakukan, termasuk barang-barang yang dibeli.",
                    "Info Fitur",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        dashboard.getBtnLogout().addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(dashboard,
                    "Yakin ingin logout dari aplikasi?",
                    "Konfirmasi Logout",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                dashboard.dispose();
                showLoginForm();
            }
        });
    }

    private static void refreshDashboardData() {
        System.out.println("Dashboard data refreshed");
    }

    public static DashboardForm getCurrentDashboard() {
        return currentDashboard;
    }
}