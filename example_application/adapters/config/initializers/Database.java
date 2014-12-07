package config.initializers;

import hex.action.initialization.InitializationException;
import hex.action.initialization.Initializer;
import hex.repo.ConnectionManager;

import java.sql.SQLException;

/**
 * Created by jason on 14-11-28.
 */
public class Database implements Initializer {
    @Override
    public void start() throws InitializationException {
        try {
            ConnectionManager.initialize(getClass().getClassLoader());
        } catch (SQLException e) {
            throw new InitializationException(e);
        }
    }
}
