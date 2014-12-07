package hex.repo;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by jason on 14-10-29.
 */
public class ConnectionManager {
    public static Connection getConnection() throws SQLException {
        return getInstance().createConnection();
    }

    public static void initialize(ClassLoader classLoader) throws SQLException {
        instance = new ConnectionManager();
        instance.loadConfig(classLoader);
    }

    private static ConnectionManager instance;

    private static ConnectionManager getInstance() throws SQLException {
        if(instance == null) {
            instance = new ConnectionManager();
            instance.loadConfig();
        }

        return instance;
    }

    private Properties properties;

    private ConnectionManager() {}

    private Connection createConnection() throws SQLException {
        String url = properties.getProperty("url");
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");
        return DriverManager.getConnection(url, username, password);
    }

    private void loadConfig() throws SQLException {
        loadConfig(getClass().getClassLoader());
    }

    private void loadConfig(ClassLoader classLoader) throws SQLException {
        try {
            this.properties = new Properties();
            properties.load(classLoader.getResourceAsStream("hex_repo.properties"));
            Class.forName(properties.getProperty("driver"));
        } catch (IOException e) {
            throw new SQLException("Error loading database connection info.", e);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Database driver class not found.", e);
        }
    }
}
