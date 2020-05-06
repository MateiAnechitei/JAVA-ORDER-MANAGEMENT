package dataaccess;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import connection.ConnectionFactory;
/**
 * AbstractDAO<T> is an abstract class that generates and executes SQL queries that are given as commands in the input text file. Contains all the shared query formats between the 3 objects:
 * Client, Product, Order. 
 * @param <T> Placeholder for one of the 3 objects that we work with: Client, Product, Order.
 */
public class AbstractDAO<T> {
	/**
	 * used to keep log messages.
	 */
	protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());
	/**
	 * instance of the currently used object class: Client, Product, Order.
	 */
	private final Class<T> type;
	/**
	 * Constructs the type to be the currently used object class.
	 */
	@SuppressWarnings("unchecked")
	public AbstractDAO() {
		this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

	}
	/**
	 * Creates the SQL SELECT query based on a simple condition. 
	 * @param field the database table field which will be checked in the condition
	 * @return the created SQL SELECT query
	 */
	private String createSelectQuery(String field) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ");
		sb.append(" * ");
		sb.append(" FROM ");
		sb.append(type.getSimpleName());
		sb.append(" WHERE " + field + " =?");
		return sb.toString();
	}
	/**
	 * Creates the SQL INSERT query with the given values (the values must have all the fields in the given order in the database with the exception of the id; see the toString() methods in the
	 * Client, Product and Order classes).
	 * @param values String with all the values that need to be inserted into the database.
	 * @return the created SQL INSERT query 
	 */
	public String createInsertQuery(String values) {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ");
		if(type.getSimpleName().equals("Order")) {
			sb.append("`order`");
		} else {
			sb.append(type.getSimpleName());
		}
		sb.append(" (");
		boolean first = true;
		for(Field field : type.getDeclaredFields()) {
			if(first == true) {
				first = false;
			}else {
				sb.append(field.getName() + ", ");
			}	
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.deleteCharAt(sb.length() - 1);
		sb.append(") values ");
		sb.append(values);
		return sb.toString();
	}
	/**
	 * Creates the SQL DELETE query with the given name of the client/product that needs to be deleted.
	 * @param name client's/product's name to be deleted
	 * @return the created SQL DELETE query 
	 */
	public String createDeleteQuery(String name) {
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ");
		sb.append(type.getSimpleName());
		sb.append(" WHERE name='");
		sb.append(name);
		sb.append("'");
		return sb.toString();
	}
	/**
	 * executes a SQL SELECT query based on a given name and returns the found object from the database.
	 * @param name client's/product's name to be searched 
	 * @return the searched object
	 */
	public T findByName(String name) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String query = createSelectQuery("name");
		try {
			connection = ConnectionFactory.getConnection();
			statement = connection.prepareStatement(query);
			statement.setString(1, name);
			resultSet = statement.executeQuery();
			try {
				return createObjects(resultSet).get(0);
			}catch(IndexOutOfBoundsException e) {
				return null;
			}	
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, type.getName() + "DAO:findByName " + e.getMessage());
		} finally {
			ConnectionFactory.close(resultSet);
			ConnectionFactory.close(statement);
			ConnectionFactory.close(connection);
		}
		return null;
	}
	/**
	 * Transforms the ResultSet which came as a response of a database query into a java object.
	 * @param resultSet table of data returned by a SQL query 
	 * @return a java object from a database entry.
	 */
	private List<T> createObjects(ResultSet resultSet) {
		List<T> list = new ArrayList<T>();
		try {
			while (resultSet.next()) {
				T instance = type.newInstance();
				for (Field field : type.getDeclaredFields()) {
					Object value = resultSet.getObject(field.getName());
					PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), type);
					Method method = propertyDescriptor.getWriteMethod();
					if(value.getClass().toString().equals("class java.lang.Long")) {
						Integer newValue = (int)(long)value;
						method.invoke(instance,  newValue);
					} else {
						method.invoke(instance, value);
					}	
				}
				list.add(instance);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * Inserts a client/product/order into the database.
	 * @param t object from one of the 3 classes based on the database tables: Client, Product, Order.
	 * @return null
	 */
	public T insert(T t) {
		String values = t.toString();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String query = createInsertQuery(values);
		try {
			connection = ConnectionFactory.getConnection();
			statement = connection.prepareStatement(query);
			statement.executeUpdate();
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, type.getName() + "DAO:insert " + e.getMessage());
		} finally {
			ConnectionFactory.close(resultSet);
			ConnectionFactory.close(statement);
			ConnectionFactory.close(connection);
		}
		return null;
	}
	/**
	 * Deletes a client/product from the database.
	 * @param name client's/product's name to be deleted
	 * @return null
	 */
	public T delete(String name) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String query = createDeleteQuery(name);
		try {
			connection = ConnectionFactory.getConnection();
			statement = connection.prepareStatement(query);
			statement.executeUpdate();
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, type.getName() + "DAO:delete " + e.getMessage());
		} finally {
			ConnectionFactory.close(resultSet);
			ConnectionFactory.close(statement);
			ConnectionFactory.close(connection);
		}
		return null;
	}
	/**
	 * Truncates a table.
	 * @return null.
	 */
	public T deleteAll() {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String query = "TRUNCATE TABLE " + type.getSimpleName();
		try {
			connection = ConnectionFactory.getConnection();
			statement = connection.prepareStatement(query);
			statement.executeUpdate();
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, type.getName() + "DAO:deleteAll " + e.getMessage());
		} finally {
			ConnectionFactory.close(resultSet);
			ConnectionFactory.close(statement);
			ConnectionFactory.close(connection);
		}
		return null;
	}
}
