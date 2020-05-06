package model;
/**
 * Class used to store an order entry for the database with the necessary fields.
 */
public class Order {
	/**
	 * Order's unique id.
	 */
	private int idOrder;
	/**
	 * Order's client's name.
	 */
	private String clientName;
	/**
	 * Order's product's name.
	 */
	private String productName;
	/**
	 * Order's product's quantity.
	 */
	private int productQuantity;
	/**
	 * Constructs an Order with no given information about it.
	 */
	public Order() {
	}
	/**
	 * Constructs an Order with it's unique id, client's name, product's name and product's quantity.
	 * @param idOrder Order's unique id.
	 * @param clientName Order's client's name.
	 * @param productName Order's product's name.
	 * @param productQuantity Order's product's quantity.
	 */
	public Order(int idOrder, String clientName, String productName, int productQuantity) {
		super();
		this.idOrder = idOrder;
		this.clientName = clientName;
		this.productName = productName;
		this.productQuantity = productQuantity;
	}
	/**
	 * Constructs an Order just with the client's name, product's name and product's quantity.
	 * @param clientName Order's client's name.
	 * @param productName Order's product's name.
	 * @param productQuantity Order's product's quantity.
	 */
	public Order(String clientName, String productName, int productQuantity) {
		super();
		this.clientName = clientName;
		this.productName = productName;
		this.productQuantity = productQuantity;
	}
	/**
	 * 
	 * @return Order's unique id.
	 */
	public int getIdOrder() {
		return idOrder;
	}
	/**
	 * 
	 * @param idOrder Order's unique id to set.
	 */
	public void setIdOrder(int idOrder) {
		this.idOrder = idOrder;
	}
	/**
	 * 
	 * @return Order's client's name.
	 */
	public String getClientName() {
		return clientName;
	}
	/**
	 * 
	 * @param clientName Order's client's name to set.
	 */
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	/**
	 * 
	 * @return Order's product's name.
	 */
	public String getProductName() {
		return productName;
	}
	/**
	 * 
	 * @param productName Order's product's name to set.
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}
	/**
	 * 
	 * @return Order's product's quantity.
	 */
	public int getProductQuantity() {
		return productQuantity;
	}
	/**
	 * 
	 * @param productQuantity Order's product's quantity to set.
	 */
	public void setProductQuantity(int productQuantity) {
		this.productQuantity = productQuantity;
	}
	/**
	 * Special toString() method used for creating a string with the required values for SQL INSERT.
	 */
	@Override
	public String toString() {
		return "('" + clientName + "', '" + productName + "'," + productQuantity + ")";
	}
	
}
