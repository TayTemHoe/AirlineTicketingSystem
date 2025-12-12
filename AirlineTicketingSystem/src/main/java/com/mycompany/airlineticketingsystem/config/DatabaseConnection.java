package com.mycompany.airlineticketingsystem.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for getting database connections using Singleton pattern.
 * Database credentials are configured here.
 * 
 * Update the URL, USERNAME, and PASSWORD constants below with your Supabase credentials.
 * You can get these from NetBeans: Services > Databases > Your Connection > Properties
 * 
 * @author Modernized Architecture
 */

public class DatabaseConnection {

    // 1. Singleton Instance (Holds the one and only connection)
    private static DatabaseConnection instance;
    private Connection connection;

    // 2. Database Credentials
    // Use the same details you put in the NetBeans Services tab
    private static final String URL = "jdbc:postgresql://aws-1-ap-northeast-1.pooler.supabase.com:6543/postgres"; 
    // IMPORTANT: Add ?sslmode=require if you get SSL errors
    // private static final String URL = "jdbc:postgresql://aws-1-ap-northeast-1.pooler.supabase.com:6543/postgres?sslmode=require";
    
    private static final String USERNAME = "postgres.ajdaciskaffuvaanizlw"; 
    private static final String PASSWORD = "ATS!y3s2G5MNT"; 

    // 3. Private Constructor (Prevents anyone else from saying "new DatabaseConnection()")
    private DatabaseConnection() throws SQLException {
        try {
            // Load the Driver (Optional in newer Java, but good for safety)
            Class.forName("org.postgresql.Driver");
            
            // Connect
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("✅ Database Connected Successfully!");
            
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL JDBC Driver not found. Make sure it's in your classpath.", e);
        } catch (SQLException e) {
            System.err.println("❌ Connection Failed: " + e.getMessage());
            throw new SQLException("Failed to connect to database. Check your credentials and network connection.", e);
        }
    }

    // 4. Public Access Point (The only way to get the connection)
    public static Connection getConnection() throws SQLException {
        if (instance == null) {
            instance = new DatabaseConnection();
        } else if (instance.connection == null || instance.connection.isClosed()) {
            // Reconnect if connection is null or closed
            instance = new DatabaseConnection();
        }
        return instance.connection;
    }
}
