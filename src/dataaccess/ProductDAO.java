package dataaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import connection.ConnectionFactory;
import model.Product;
/**
 * Class that extends the AbstractDAO<T> class which contains all the particular database operations for clients.
 */
public class ProductDAO extends AbstractDAO<Product> {
	/**
	 * Creates the SQL UPDATE query that updates the quantity of a given product.
	 * @param newQuantity new quantity of the given product
	 * @param name name of the product
	 * @return the required SQL UPDATE query 
	 */
	public String createProductUpdateQuery(int newQuantity, String name) {
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE product SET quantity = ");
		sb.append(newQuantity);
		sb.append(" WHERE name = '");
		sb.append(name + "';");
		return sb.toString();
	}
	/**
	 * Executes a SQL UPDATE query based on the new quantity of a product and the product's name.
	 * @param newQuantity new quantity of the given product
	 * @param name name of the product
	 * @return null
	 */
	public Product update(int newQuantity, String name) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String query = createProductUpdateQuery(newQuantity, name);
		try {
			connection = ConnectionFactory.getConnection();
			statement = connection.prepareStatement(query);
			statement.executeUpdate();
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, "Product" + "DAO:update " + e.getMessage());
		} finally {
			ConnectionFactory.close(resultSet);
			ConnectionFactory.close(statement);
			ConnectionFactory.close(connection);
		}
		return null;
	}
}