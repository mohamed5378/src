package Connection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionManager {
    private static DatabaseConnectionManager instance;
    private Connection connection;
    private static final String URL = "jdbc:mysql://localhost:3306/EMS";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root"; // Replace with your MySQL password

    private DatabaseConnectionManager() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Database connection established.");
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database.");
            e.printStackTrace();
        }
    }

    public static DatabaseConnectionManager getInstance() {
        if (instance == null) {
            instance = new DatabaseConnectionManager();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
