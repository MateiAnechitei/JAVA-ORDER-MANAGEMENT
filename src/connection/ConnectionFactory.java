package connection;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * ConnectionFactory establishes the connection with the database.
 */
public class ConnectionFactory {
	/**
	 * is used to store log messages.
	 */
	private static final Logger LOGGER = Logger.getLogger(ConnectionFactory.class.getName());
	/**
	 * provides the name of the .jar file used for the connection.
	 */
	private static final String DRIVER = "com.mysql.jdbc.Driver";
	/**
	 * is the URL of the database in use.
	 */
	private static final String DBURL = "jdbc:mysql://localhost:3306/warehouse?useSSL=false";
	/**
	 * is the database username.
	 */
	private static final String USER = "tp";
	/**
	 * is the user's password.
	 */
	private static final String PASS = "tp";
	/**
	 * is an instance Object of the ConnectionFactory class used to call the methods.
	 */
	private static ConnectionFactory singleInstance = new ConnectionFactory();
	/**
	 * Constructor for the Class with no parameters.
	 */
	private ConnectionFactory() {
		try {
			Class.forName(DRIVER);
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Creates a connection between the java app and the database.
	 * @return the created connection 
	 */
	private Connection createConnection() {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(DBURL, USER, PASS);
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, "An error occured while trying to connect to the database");
			e.printStackTrace();
		}
		return connection;
	}
	/**
	 * @return current connection
	 */
	public static Connection getConnection() {
		return singleInstance.createConnection();	
	}
	
	/**
	 * Closes the current connection.
	 * @param connection current connection
	 */
	public static void close(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				LOGGER.log(Level.WARNING, "An error occured while trying to close the connection");
			}
		}
	}
	/**
	 * Closes the current statement.
	 * @param statement current statement
	 */
	public static void close(Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				LOGGER.log(Level.WARNING, "An error occured while trying to close the statement");
			}
		}
	}
	/**
	 * Closes the current resultSet.
	 * @param resultSet current resultSet
	 */
	public static void close(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				LOGGER.log(Level.WARNING, "An error occured while trying to close the ResultSet");
			}
		}
	}
	
	
}
