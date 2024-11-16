package org.servlets.quiz.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class UsersDAO extends BaseDAO {
    private static final Logger LOGGER = LogManager.getLogger(UsersDAO.class);
    private static final String VERIFICATION = "select * from users where name = ? and password = ?";

    public boolean validate(String name, String pass) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(VERIFICATION);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, pass);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException exception) {
            LOGGER.error("Error validating user name {}", name, exception);
            throw exception;
        }
    }
}
