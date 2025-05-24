package SoftPet.backend.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBase
{

    private static final String URL = "jdbc:postgresql://localhost:5432/SoftPet";
    private static final String USER = "postgres";
    private static final String PASSWORD = " postgres123";

    private static Connection connection;

    // Singleton de conexão
    private DataBase() { }

    public static Connection getInstance() throws SQLException
    {
        if (connection == null || connection.isClosed())
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

        return connection;
    }
}