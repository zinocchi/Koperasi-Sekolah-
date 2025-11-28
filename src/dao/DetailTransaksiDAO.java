package dao;

import util.DatabaseConnection;
import model.DetailTransaksi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DetailTransaksiDAO {

    public boolean insertDetail(DetailTransaksi d) {
        String sql = "INSERT INTO detail_transaksi (id_transaksi, id_barang, jumlah, subtotal) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, d.getId_transaksi());
            stmt.setInt(2, d.getId_barang());
            stmt.setInt(3, d.getJumlah());
            stmt.setInt(4, d.getSubtotal());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error insert detail transaksi: " + e.getMessage());
            return false;
        }
    }
}
