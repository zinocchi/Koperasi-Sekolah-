package dao;

import model.Anggota;
import java.sql.*;
import java.util.ArrayList;
import util.DatabaseConnection;

public class AnggotaDAO {
    private static Connection connection;

    static {
        connection = DatabaseConnection.getConnection();
    }

    // GET ALL ANGGOTA
    public static ArrayList<Anggota> getAll() {
        ArrayList<Anggota> anggotaList = new ArrayList<>();
        String sql = "SELECT * FROM anggota ORDER BY id_anggota DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Anggota anggota = new Anggota(
                        rs.getInt("id_anggota"),
                        rs.getString("nama"),
                        rs.getString("kelas"),
                        rs.getString("alamat"),
                        rs.getString("no_hp")
                );
                anggotaList.add(anggota);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return anggotaList;
    }

    // INSERT ANGGOTA
    public static boolean insert(Anggota anggota) {
        String sql = "INSERT INTO anggota (nama, kelas, alamat, no_hp) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, anggota.getNama());
            stmt.setString(2, anggota.getKelas());
            stmt.setString(3, anggota.getAlamat());
            stmt.setString(4, anggota.getNo_hp());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // UPDATE ANGGOTA
    public static boolean update(Anggota anggota) {
        String sql = "UPDATE anggota SET nama = ?, kelas = ?, alamat = ?, no_hp = ? WHERE id_anggota = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, anggota.getNama());
            stmt.setString(2, anggota.getKelas());
            stmt.setString(3, anggota.getAlamat());
            stmt.setString(4, anggota.getNo_hp());
            stmt.setInt(5, anggota.getId_anggota());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE ANGGOTA
    public static boolean delete(int id_anggota) {
        String sql = "DELETE FROM anggota WHERE id_anggota = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id_anggota);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // SEARCH ANGGOTA BY NAME
    public static ArrayList<Anggota> searchByName(String keyword) {
        ArrayList<Anggota> anggotaList = new ArrayList<>();
        String sql = "SELECT * FROM anggota WHERE nama LIKE ? ORDER BY id_anggota DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Anggota anggota = new Anggota(
                            rs.getInt("id_anggota"),
                            rs.getString("nama"),
                            rs.getString("kelas"),
                            rs.getString("alamat"),
                            rs.getString("no_hp")
                    );
                    anggotaList.add(anggota);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return anggotaList;
    }

    // GET ANGGOTA BY ID
    public static Anggota getById(int id_anggota) {
        String sql = "SELECT * FROM anggota WHERE id_anggota = ?";
        Anggota anggota = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id_anggota);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    anggota = new Anggota(
                            rs.getInt("id_anggota"),
                            rs.getString("nama"),
                            rs.getString("kelas"),
                            rs.getString("alamat"),
                            rs.getString("no_hp")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return anggota;
    }
}