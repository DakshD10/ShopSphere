package com.shopsphere.dao;

import com.shopsphere.model.Address;
import com.shopsphere.util.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for customer shipping and billing addresses.
 */
public class AddressDAO {

    public List<Address> findByUserId(int userId) throws SQLException {
        List<Address> list = new ArrayList<>();
        String sql = """
            SELECT address_id, user_id, address_line, city, state, pincode, address_type, created_at
            FROM addresses
            WHERE user_id = ?
            ORDER BY address_id DESC
            """;
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    public Address findById(int addressId) throws SQLException {
        String sql = """
            SELECT address_id, user_id, address_line, city, state, pincode, address_type, created_at
            FROM addresses
            WHERE address_id = ?
            """;
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, addressId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    public int createAddress(Address addr) throws SQLException {
        String sql = """
            INSERT INTO addresses (user_id, address_line, city, state, pincode, address_type)
            VALUES (?, ?, ?, ?, ?, ?)
            """;
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, addr.getUserId());
            ps.setString(2, addr.getAddressLine());
            ps.setString(3, addr.getCity());
            ps.setString(4, addr.getState());
            ps.setString(5, addr.getPincode());
            ps.setString(6, addr.getAddressType());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        addr.setAddressId(id);
                        return id;
                    }
                }
            }
            return -1;
        }
    }

    public boolean deleteAddress(int addressId, int userId) throws SQLException {
        String sql = "DELETE FROM addresses WHERE address_id = ? AND user_id = ?";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, addressId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        }
    }

    private Address mapRow(ResultSet rs) throws SQLException {
        Address a = new Address();
        a.setAddressId(rs.getInt("address_id"));
        a.setUserId(rs.getInt("user_id"));
        a.setAddressLine(rs.getString("address_line"));
        a.setCity(rs.getString("city"));
        a.setState(rs.getString("state"));
        a.setPincode(rs.getString("pincode"));
        a.setAddressType(rs.getString("address_type"));
        a.setCreatedAt(rs.getTimestamp("created_at"));
        return a;
    }
}
