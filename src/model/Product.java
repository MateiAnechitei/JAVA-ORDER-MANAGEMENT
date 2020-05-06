package model;
/**
 * Class used to store a product entry for the database with the necessary fields.
 */
public class Product {
	/**
	 * Product's unique id.
	 */
	private int idProduct;
	/**
	 * Product's name.
	 */
	private String name;
	/**
	 * Product's quantity.
	 */
	private int quantity;
	/**
	 * Product's price.
	 */
	private float price;
	/**
	 * Constructs a Product with no given information about it.
	 */
	public Product() {
	}
	/**
	 * Constructs a Product with it's unique id, name, quantity and price.
	 * @param idProduct Product's unique id.
	 * @param name Product's name.
	 * @param quantity Product's quantity.
	 * @param price Product's price.
	 */
	public Product(int idProduct, String name, int quantity, float price) {
		super();
		this.idProduct = idProduct;
		this.name = name;
		this.quantity = quantity;
		this.price = price;
	}
	/**
	 * Constructs a Product just with the name, quantity and price.
	 * @param name Product's name.
	 * @param quantity Product's quantity.
	 * @param price Product's price.
	 */
	public Product(String name, int quantity, float price) {
		super();
		this.name = name;
		this.quantity = quantity;
		this.price = price;
	}
	/**
	 * 
	 * @return Product's unique id.
	 */
	public int getIdProduct() {
		return idProduct;
	}
	/**
	 * 
	 * @param idProduct Product's unique id to set.
	 */
	public void setIdProduct(int idProduct) {
		this.idProduct = idProduct;
	}
	/**
	 * 
	 * @return Product's name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * 
	 * @param name Product's name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 
	 * @return Product's quantity.
	 */
	public int getQuantity() {
		return quantity;
	}
	/**
	 * 
	 * @param quantity Product's quantity to set.
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	/**
	 * 
	 * @return Product's price.
	 */
	public float getPrice() {
		return price;
	}
	/**
	 * 
	 * @param price Product's price to set.
	 */
	public void setPrice(float price) {
		this.price = price;
	}
	/**
	 * Special toString() method used for creating a string with the required values for SQL INSERT.
	 */
	@Override
	public String toString() {
		return "('" + name + "', " + quantity + ", " + price + ")";
	}
	
}
