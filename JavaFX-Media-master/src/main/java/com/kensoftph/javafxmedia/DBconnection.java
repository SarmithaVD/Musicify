package com.kensoftph.javafxmedia;

import java.sql.*;

public class DBconnection {
    public DBconnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("MySQL JDBC Driver not found.");
        }
    }
    private String DB_URL = "jdbc:mysql://localhost:3306/musicifydb"; // Replace with your database name
    private String DB_USER = "root"; // Replace with your database username
    private String DB_PASSWORD = "Kavushik@2004"; // Replace with your database password

    public boolean authenticateUser(String username, String password) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("\n\n\n\nYES established!\n\n\n");
            String query = "SELECT user_id, password FROM users WHERE username = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password");
                if (storedPassword.equals(password)) {
                    return true; // Authentication successful
                }
            }

            resultSet.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Authentication failed
    }
}
