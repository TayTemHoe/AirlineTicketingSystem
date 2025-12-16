package com.mycompany.airlineticketingsystem.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {

    // 1. The DataSource (The Pool itself)
    private static final HikariDataSource dataSource;

    // 2. Database Credentials (KEEP YOUR PASSWORD SECRET!)
    // keep ?prepareThreshold=0 to be safe, but Hikari often handles this better.
    private static final String URL = "jdbc:postgresql://aws-1-ap-northeast-1.pooler.supabase.com:6543/postgres?prepareThreshold=0"; 
    private static final String USER = "postgres.ajdaciskaffuvaanizlw"; 
    private static final String PASSWORD = "ATS!y3s2G5MNT"; // ⚠️ Replace if changed

    // 3. Static Initializer (Runs once when app starts)
    static {
        try {
            // Create Configuration
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(URL);
            config.setUsername(USER);
            config.setPassword(PASSWORD);
            config.setDriverClassName("org.postgresql.Driver");

            // --- PERFORMANCE SETTINGS (The "Magic" Part) ---
            
            // Maximum number of connections in the pool
            // 10 is usually enough for a desktop app. Supabase free tier has limits (usually 60-100 total).
            config.setMaximumPoolSize(10); 
            
            // Minimum idle connections to keep ready
            config.setMinimumIdle(2);
            
            // How long a connection can sit idle before being closed (10 minutes)
            config.setIdleTimeout(600000); 
            
            // Max lifetime of a connection in the pool (30 minutes)
            config.setMaxLifetime(1800000); 

            // Initialize the Pool
            dataSource = new HikariDataSource(config);
            System.out.println("✅ HikariCP Connection Pool Initialized!");

        } catch (Exception e) {
            System.err.println("❌ Failed to initialize HikariCP Pool!");
            throw new RuntimeException(e);
        }
    }

    // 4. Public Access Point
    // Now, instead of giving a "Static Connection", we give a "Leased Connection"
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    
    // Optional: Call this when shutting down the app to close all connections
    public static void shutdown() {
        if (dataSource != null) {
            dataSource.close();
            System.out.println("🛑 Database Pool Closed.");
        }
    }
}
