package dao;

import util.DatabaseConnection;
import model.Barang;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;

public class BarangDAO {

    public static ArrayList<Barang> getAll() {
        ArrayList<Barang> list = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM barang")) {

            while (rs.next()) {
                list.add(new Barang(
                        rs.getInt("id_barang"),
                        rs.getString("nama_barang"),
                        rs.getInt("harga"),
                        rs.getInt("stok"),
                        rs.getString("satuan")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static boolean insert(Barang barang) {
        String sql = "INSERT INTO barang(nama_barang, harga, stok, satuan) VALUES(?,?,?,?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, barang.getNama_barang());
            ps.setInt(2, barang.getHarga());
            ps.setInt(3, barang.getStok());
            ps.setString(4, barang.getSatuan());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean delete(int id) {
        String sql = "DELETE FROM barang WHERE id_barang = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    public static boolean update(Barang barang) {
        String sql = "UPDATE barang SET nama_barang=?, harga=?, stok=?, satuan=? WHERE id_barang=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, barang.getNama_barang());
            ps.setInt(2, barang.getHarga());
            ps.setInt(3, barang.getStok());
            ps.setString(4, barang.getSatuan());
            ps.setInt(5, barang.getId_barang());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return false;
    }
}
