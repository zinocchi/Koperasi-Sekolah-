package dao;

import model.Transaksi;
import model.DetailTransaksi;
import java.sql.*;
import java.util.ArrayList;
import util. DatabaseConnection;

import static util.DatabaseConnection.getConnection;

public class TransaksiDAO {
    private static Connection connection;

    static {
        connection = getConnection();
    }

    public static ArrayList<Transaksi> getAll() {
        ArrayList<Transaksi> transaksiList = new ArrayList<>();
        String sql = "SELECT t.*, a.nama as nama_anggota, u.nama_lengkap as nama_user " +
                "FROM transaksi t " +
                "LEFT JOIN anggota a ON t.id_anggota = a.id_anggota " +
                "LEFT JOIN user u ON t.id_user = u.id_user " +
                "ORDER BY t.tanggal DESC, t.id_transaksi DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Transaksi transaksi = new Transaksi(
                        rs.getInt("id_transaksi"),
                        rs.getInt("id_anggota"),
                        rs.getInt("id_user"),
                        rs.getDate("tanggal"),
                        rs.getInt("total_harga")
                );
                transaksi.setNama_anggota(rs.getString("nama_anggota"));
                transaksi.setNama_user(rs.getString("nama_user"));

                // Load detail transaksi
                transaksi.setDetailTransaksi(getDetailTransaksi(transaksi.getId_transaksi()));

                transaksiList.add(transaksi);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transaksiList;
    }

    // INSERT TRANSAKSI (Transaction)
    public static int insert(Transaksi transaksi) {
        String sql = "INSERT INTO transaksi (id_anggota, id_user, tanggal, total_harga) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Insert transaksi
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, transaksi.getId_anggota());
            stmt.setInt(2, transaksi.getId_user());
            stmt.setDate(3, new java.sql.Date(transaksi.getTanggal().getTime()));
            stmt.setInt(4, transaksi.getTotal_harga());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Creating transaksi failed, no rows affected.");
            }

            // Get generated ID
            rs = stmt.getGeneratedKeys();
            int generatedId = 0;
            if (rs.next()) {
                generatedId = rs.getInt(1);
            } else {
                throw new SQLException("Creating transaksi failed, no ID obtained.");
            }

            // Insert detail transaksi
            for (DetailTransaksi detail : transaksi.getDetailTransaksi()) {
                if (!insertDetailTransaksi(conn, generatedId, detail)) {
                    throw new SQLException("Failed to insert detail transaksi");
                }

                // Update stok barang
                if (!updateStokBarang(conn, detail.getId_barang(), detail.getJumlah())) {
                    throw new SQLException("Failed to update stok barang");
                }
            }

            conn.commit(); // Commit transaction
            return generatedId;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback if error
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return -1;
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // GET DETAIL TRANSAKSI
    private static ArrayList<DetailTransaksi> getDetailTransaksi(int id_transaksi) {
        ArrayList<DetailTransaksi> detailList = new ArrayList<>();
        String sql = "SELECT dt.*, b.nama_barang, b.harga " +
                "FROM detail_transaksi dt " +
                "JOIN barang b ON dt.id_barang = b.id_barang " +
                "WHERE dt.id_transaksi = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id_transaksi);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    DetailTransaksi detail = new DetailTransaksi(
                            rs.getInt("id_detail"),
                            rs.getInt("id_transaksi"),
                            rs.getInt("id_barang"),
                            rs.getInt("jumlah"),
                            rs.getInt("subtotal")
                    );
                    detail.setNama_barang(rs.getString("nama_barang"));
                    detail.setHarga_barang(rs.getInt("harga"));
                    detailList.add(detail);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return detailList;
    }

    // INSERT DETAIL TRANSAKSI
    private static boolean insertDetailTransaksi(Connection conn, int id_transaksi, DetailTransaksi detail) throws SQLException {
        String sql = "INSERT INTO detail_transaksi (id_transaksi, id_barang, jumlah, subtotal) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id_transaksi);
            stmt.setInt(2, detail.getId_barang());
            stmt.setInt(3, detail.getJumlah());
            stmt.setInt(4, detail.getSubtotal());

            return stmt.executeUpdate() > 0;
        }
    }

    // UPDATE STOK BARANG
    private static boolean updateStokBarang(Connection conn, int id_barang, int jumlah) throws SQLException {
        String sql = "UPDATE barang SET stok = stok - ? WHERE id_barang = ? AND stok >= ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, jumlah);
            stmt.setInt(2, id_barang);
            stmt.setInt(3, jumlah);

            return stmt.executeUpdate() > 0;
        }
    }

    // GET TRANSAKSI BY ID
    public static Transaksi getById(int id_transaksi) {
        String sql = "SELECT t.*, a.nama as nama_anggota, u.nama_lengkap as nama_user " +
                "FROM transaksi t " +
                "LEFT JOIN anggota a ON t.id_anggota = a.id_anggota " +
                "LEFT JOIN user u ON t.id_user = u.id_user " +
                "WHERE t.id_transaksi = ?";
        Transaksi transaksi = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id_transaksi);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    transaksi = new Transaksi(
                            rs.getInt("id_transaksi"),
                            rs.getInt("id_anggota"),
                            rs.getInt("id_user"),
                            rs.getDate("tanggal"),
                            rs.getInt("total_harga")
                    );
                    transaksi.setNama_anggota(rs.getString("nama_anggota"));
                    transaksi.setNama_user(rs.getString("nama_user"));
                    transaksi.setDetailTransaksi(getDetailTransaksi(id_transaksi));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transaksi;
    }

    // DELETE TRANSAKSI (Transaction)
    public static boolean delete(int id_transaksi) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            // Get detail transaksi untuk restore stok
            ArrayList<DetailTransaksi> detailList = getDetailTransaksi(id_transaksi);

            // Restore stok barang
            for (DetailTransaksi detail : detailList) {
                if (!restoreStokBarang(conn, detail.getId_barang(), detail.getJumlah())) {
                    throw new SQLException("Failed to restore stok barang");
                }
            }

            // Delete detail transaksi
            String sqlDetail = "DELETE FROM detail_transaksi WHERE id_transaksi = ?";
            stmt = conn.prepareStatement(sqlDetail);
            stmt.setInt(1, id_transaksi);
            stmt.executeUpdate();
            stmt.close();

            // Delete transaksi
            String sqlTransaksi = "DELETE FROM transaksi WHERE id_transaksi = ?";
            stmt = conn.prepareStatement(sqlTransaksi);
            stmt.setInt(1, id_transaksi);
            int rowsAffected = stmt.executeUpdate();

            conn.commit();
            return rowsAffected > 0;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // RESTORE STOK BARANG
    private static boolean restoreStokBarang(Connection conn, int id_barang, int jumlah) throws SQLException {
        String sql = "UPDATE barang SET stok = stok + ? WHERE id_barang = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, jumlah);
            stmt.setInt(2, id_barang);

            return stmt.executeUpdate() > 0;
        }
    }
}