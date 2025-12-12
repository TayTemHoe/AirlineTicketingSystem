/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.airlineticketingsystem.repository;

/**
 *
 * @author Tay Tem Hoe
 */

import com.mycompany.airlineticketingsystem.model.Seat;
import java.util.List;

public interface SeatRepository {
    void saveAll(List<Seat> seats);
    List<Seat> findByFlightId(String flightId);
}
