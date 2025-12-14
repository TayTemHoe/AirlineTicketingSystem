/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.airlineticketingsystem.enums;

/**
 *
 * @author Tay Tem Hoe
 */
public enum Gender {
    MALE,
    FEMALE,
    OTHER;

    // Helper to safely convert from DB String
    public static Gender fromString(String str) {
        if (str == null) return OTHER;
        try {
            return Gender.valueOf(str.toUpperCase());
        } catch (IllegalArgumentException e) {
            return OTHER;
        }
    }
}