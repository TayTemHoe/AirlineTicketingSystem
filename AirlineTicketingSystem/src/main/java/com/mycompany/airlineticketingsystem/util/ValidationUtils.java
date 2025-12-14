/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.airlineticketingsystem.util;

/**
 *
 * @author Tay Tem Hoe
 */

import java.util.regex.Pattern;

public class ValidationUtils {

    // Regex: 6 digits, hyphen, 2 digits, hyphen, 4 digits (e.g., 990101-14-1234)
    private static final Pattern IC_PATTERN = Pattern.compile("^\\d{6}-\\d{2}-\\d{4}$");
    
    // Regex: Starts with 'S' followed by at least 3 digits (e.g., S001, S100)
    private static final Pattern STAFF_ID_PATTERN = Pattern.compile("^S\\d{3,}$");

    // Regex: Standard Email format
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    
    /**
     * Password Validation:
     * ^                 : Start of string
     * (?=.*[0-9])       : At least one digit
     * (?=.*[a-z])       : At least one lowercase letter
     * (?=.*[A-Z])       : At least one uppercase letter
     * (?=.*[@#$%^&+=!]) : At least one special character (Add more if needed)
     * (?=\\S+$)         : No whitespace allowed
     * .                 : Any character
     * {6,8}             : Length must be between 6 and 8
     * $                 : End of string
     */
    private static final Pattern PASSWORD_PATTERN = 
        Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{6,8}$");

    public static boolean isValidIC(String ic) {
        return ic != null && IC_PATTERN.matcher(ic).matches();
    }

    public static boolean isValidStaffId(String staffId) {
        return staffId != null && STAFF_ID_PATTERN.matcher(staffId).matches();
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    // NEW: Password Checker
    public static boolean isValidPassword(String password) {
        return password != null && PASSWORD_PATTERN.matcher(password).matches();
    }

    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }
}
