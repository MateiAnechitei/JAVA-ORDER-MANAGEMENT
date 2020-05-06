package model;
/**
 * Class used to store a client entry for the database with the necessary fields.
 */
public class Client {
	/**
	 * Client's unique id.
	 */
	private int idClient;
	/**
	 * Client's name.
	 */
	private String name;
	/**
	 * Client's city.
	 */
	private String city;
	/**
	 * Constructs a Client with no given information about it.
	 */
	public Client() {
	}
	/**
	 * Constructs a Client with his unique id, name and city.
	 * @param idClient Client's unique id
	 * @param name Client's name
	 * @param city Client's city
	 */
	public Client(int idClient, String name, String city) {
		super();
		this.idClient = idClient;
		this.name = name;
		this.city = city;
	}
	/**
	 * Constructs a Client just with his name and his city.
	 * @param name Client's name 
	 * @param city Client's city
	 */
	public Client(String name, String city) {
		super();
		this.name = name;
		this.city = city;
	}
	/**
	 * @return Client's unique id
	 */
	public int getIdClient() {
		return idClient;
	}
	/**
	 * @param idClient Client's id to set.
	 */
	public void setIdClient(int idClient) {
		this.idClient = idClient;
	}
	/**
	 * @return Client's name
	 */
	public String getName() {
		return name;
	}
	/**
	 * 
	 * @param name Client's name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 
	 * @return Client's city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * 
	 * @param city Client's city to set.
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * Special toString() method used for creating a string with the required values for SQL INSERT  
	 */
	@Override
	public String toString() {
		return "('" + name + "', '" + city + "')";
	}

}
