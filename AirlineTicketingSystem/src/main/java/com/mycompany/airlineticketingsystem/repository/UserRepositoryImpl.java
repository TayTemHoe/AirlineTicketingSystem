//handle both customer and staff
package com.mycompany.airlineticketingsystem.repository;

import com.mycompany.airlineticketingsystem.config.DatabaseConnection;
import com.mycompany.airlineticketingsystem.enums.Gender;
import com.mycompany.airlineticketingsystem.model.Customer;
import com.mycompany.airlineticketingsystem.model.Staff;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

/**
 *
 * @author Tay Tem Hoe
 */
public class UserRepositoryImpl implements UserRepository{

    // --- FIND STAFF ---
    @Override
    public Optional<Staff> findStaffById(String staffId) {
        String sql = "SELECT * FROM staff WHERE staff_id = ?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, staffId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return Optional.of(new Staff(
                        rs.getString("staff_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        Gender.fromString(rs.getString("gender")),
                        rs.getString("position"),
                        rs.getString("password")
                    ));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return Optional.empty();
    }

    // --- FIND CUSTOMER ---
    @Override
    public Optional<Customer> findCustomerByIc(String icNo) {
        String sql = "SELECT * FROM customer WHERE ic_no = ?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, icNo);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return Optional.of(new Customer(
                        rs.getString("ic_no"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        Gender.fromString(rs.getString("gender")),
                        rs.getString("password")
                    ));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return Optional.empty();
    }
    
    public boolean saveCustomer(Customer customer) {
        String sql = "INSERT INTO customer (ic_no, name, email, phone_number, gender, password) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, customer.getIcNo());
            stmt.setString(2, customer.getName());
            stmt.setString(3, customer.getEmail());
            stmt.setString(4, customer.getPhoneNumber());
            stmt.setString(5, customer.getGender().name()); // Store Enum as String
            stmt.setString(6, customer.getPassword()); // In real app, HASH this!
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean saveStaff(Staff staff) {
        String sql = "INSERT INTO staff (staff_id, name, email, phone_number, gender, position, password) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, staff.getStaffId());
            stmt.setString(2, staff.getName());
            stmt.setString(3, staff.getEmail());
            stmt.setString(4, staff.getPhoneNumber());
            stmt.setString(5, staff.getGender().name());
            stmt.setString(6, staff.getPosition());
            stmt.setString(7, staff.getPassword());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public String generateNextStaffId() {
        String sql = "SELECT staff_id FROM staff ORDER BY staff_id DESC LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                String lastId = rs.getString("staff_id"); // e.g., "S005"
                int num = Integer.parseInt(lastId.substring(1)); // 5
                return String.format("S%03d", num + 1); // "S006"
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "S001"; // Default if table empty
    }
    
    // ... existing code ...

    @Override
    public boolean updateCustomer(Customer customer) {
        String sql = "UPDATE customer SET name=?, email=?, phone_number=?, gender=?, password=? WHERE ic_no=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getEmail());
            stmt.setString(3, customer.getPhoneNumber());
            stmt.setString(4, customer.getGender().name());
            stmt.setString(5, customer.getPassword());
            stmt.setString(6, customer.getIcNo()); // Where clause
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateStaff(Staff staff) {
        String sql = "UPDATE staff SET name=?, email=?, phone_number=?, gender=?, password=? WHERE staff_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, staff.getName());
            stmt.setString(2, staff.getEmail());
            stmt.setString(3, staff.getPhoneNumber());
            stmt.setString(4, staff.getGender().name());
            stmt.setString(5, staff.getPassword());
            stmt.setString(6, staff.getStaffId()); // Where clause
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // ✅ NEW: Implementation of findAllStaff
    @Override
    public List<Staff> findAllStaff() {
        List<Staff> staffList = new ArrayList<>();
        String sql = "SELECT * FROM staff ORDER BY staff_id";

        // ✅ FIX: Connection outside try block
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    Gender gender = Gender.fromString(rs.getString("gender"));
                    Staff s = new Staff(
                        rs.getString("staff_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        gender,
                        rs.getString("position"),
                        rs.getString("password")
                    );
                    staffList.add(s);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return staffList;
    }
}