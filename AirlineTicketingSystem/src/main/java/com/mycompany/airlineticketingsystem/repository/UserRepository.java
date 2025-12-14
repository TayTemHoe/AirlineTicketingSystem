/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.airlineticketingsystem.repository;

import com.mycompany.airlineticketingsystem.model.Customer;
import com.mycompany.airlineticketingsystem.model.Staff;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Tay Tem Hoe
 */
public interface UserRepository {
    Optional<Staff> findStaffById(String staffId);
    Optional<Customer> findCustomerByIc(String icNo);
    boolean updateCustomer(Customer customer);
    boolean updateStaff(Staff staff);
    // Fetch all staff members
    List<Staff> findAllStaff();
}
