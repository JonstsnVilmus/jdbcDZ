import config.ConstantConfig;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ConnectionJdbc {
    static private Connection connection = null;
    static private Statement st = null;

    ConnectionJdbc(){
    }
    public ConnectionJdbc getInstance()  {
        if(connection == null && st == null) {
            Enumeration<Driver> drivers = DriverManager.getDrivers();
            while (drivers.hasMoreElements()) {
                System.out.println(drivers.nextElement());
            }
            try {
                Class.forName(ConstantConfig.JDBC_DRIVER_NAME);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("MySQL JDBC Driver not found.");
            }
            try {
                connection = DriverManager.getConnection(ConstantConfig.CONN_STRING);
            } catch (SQLException e) {
                throw new RuntimeException("Could not connect to " + ConstantConfig.CONN_STRING);
            }
            try {
                st = connection.createStatement();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return this;

    }

    public void createDatabase(String nameDatabase){

        try {
            st.executeUpdate("CREATE DATABASE "+nameDatabase);
        } catch (SQLException e) {
            throw new RuntimeException("Could not create database");
        }

    }
    public void updateTable(String nameDatabase, String tableName, Map<String, String> values){
        try {
            // ради примера
            String keys = String.join(",", values.keySet());
            String vals = values.values().stream().map(v -> "'" + v + "'").collect(Collectors.joining(","));
            String query = "INSERT INTO " + nameDatabase + "." + tableName + " (" + keys + ") VALUES (" + vals + ");";
            st.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Map<String, String> getData(String nameDatabase, String tableName){
        Map<String, String> result = new HashMap<>();
        try {
            ResultSet rs =st.executeQuery("select * from " + nameDatabase + "." + tableName + ";");
            while ( rs.next() ) {
                result.put("login", rs.getString("login"));
                result.put("password", rs.getNString("password"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
