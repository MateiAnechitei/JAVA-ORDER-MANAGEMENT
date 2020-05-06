package businesslogic;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import connection.ConnectionFactory;
import dataaccess.ClientDAO;
import dataaccess.OrderDAO;
import dataaccess.ProductDAO;
import model.Order;
import model.Product;
/**
 * Class used to verify the validity of the commands specific for orders and to execute them.
 */
public class OrderBLL {
	/**
	 * index used to change the number at the end of each order report pdf file
	 */
	static int orderReportIndex = 1;
	/**
	 * index used to change the number at the end of each bill pdf file
	 */
	static int billIndex = 1;
	/**
	 * index used to change the number at the end of each under-stock pdf file
	 */
	static int underStockIndex = 1;
	/**
	 * verifies the insert order command syntax and, if correct, executes it
	 * @param splitString the command parsed into several necessary fields
	 * @param command the command non-parsed, placed in the under-stock pdf file to see which command could not be executed due to unavailable quantity
	 */
	public void insertOrder(ArrayList<String> splitString, String command) {
		if (splitString.size() != 4) {
			System.out.println("Order command syntax is wrong!");
		} else {
			OrderDAO orderAccess = new OrderDAO();
			ClientDAO clientAccess = new ClientDAO();
			if (clientAccess.findByName(splitString.get(1)) == null) {
				System.out.println("Client doesn't exist!");
			} else {
				ProductDAO productAccess = new ProductDAO();
				Product checkProduct = productAccess.findByName(splitString.get(2));
				if (checkProduct == null) {
					System.out.println("Product doesn't exist!");
				} else {
					try {
						int orderQuantity = Integer.parseInt(splitString.get(3));
						if (orderQuantity <= 0) {
							throw new NumberFormatException();
						} else if (checkProduct.getQuantity() < orderQuantity) {
							generateUnderStock(command);
						} else {
							String clientName = splitString.get(1);
							String productName = splitString.get(2);
							Order newOrder = new Order(clientName, productName, orderQuantity);
							orderAccess.insert(newOrder);
							int updateQuantity = checkProduct.getQuantity() - orderQuantity;
							if (updateQuantity > 0) {
								productAccess.update(updateQuantity, checkProduct.getName());
							} else {
								productAccess.delete(checkProduct.getName());
								System.out.println("Product sold out. Deleted the product from the database!");
							}
							generateBill(newOrder, checkProduct.getPrice());
							System.out.println("Order inserted successfully");
						}
					} catch (NumberFormatException e) {
						System.out.println("Quantity not valid!");
					}
				}
			}
		}
	}
	/**
	 * generates the bill pdf 
	 * @param order the order to be put in the bill
	 * @param productPrice final price of the order
	 */
	public void generateBill(Order order, float productPrice) {
		try {
			String fileName = "C:\\Users\\Matei\\Desktop\\TP - projects\\Tema3OrderManagement\\Reports\\bill_"
					+ billIndex + ".pdf";
			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(fileName));
			document.open();
			document.add(new Chunk(""));
			PdfPTable table = new PdfPTable(4);
			PdfPCell cell = new PdfPCell(new Phrase("Client"));
			table.addCell(cell);
			cell = new PdfPCell(new Phrase("Product"));
			table.addCell(cell);
			cell = new PdfPCell(new Phrase("Quantity"));
			table.addCell(cell);
			cell = new PdfPCell(new Phrase("Price"));
			table.addCell(cell);
			table.setHeaderRows(1);
			cell = new PdfPCell(new Phrase(order.getClientName()));
			table.addCell(cell);
			cell = new PdfPCell(new Phrase(order.getProductName()));
			table.addCell(cell);
			cell = new PdfPCell(new Phrase(String.valueOf(order.getProductQuantity())));
			table.addCell(cell);
			float orderPrice = (float) order.getProductQuantity() * productPrice;
			cell = new PdfPCell(new Phrase(String.valueOf(orderPrice)));
			table.addCell(cell);
			document.add(table);
			document.close();
			System.out.println("Report generated successfully!");
			++billIndex;
		} catch (Exception e) {
			System.err.println(e);
		}
	}
	/**
	 * generates the under-stock pdf file in case of unavailable quantity
	 * @param command the command non-parsed, placed in the under-stock pdf file to see which command could not be executed due to unavailable quantity
	 */
	public void generateUnderStock(String command) {
		try {
			String fileName = "C:\\Users\\Matei\\Desktop\\TP - projects\\Tema3OrderManagement\\Reports\\under_stock_"
					+ underStockIndex + ".pdf";
			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(fileName));
			document.open();
			document.add(new Chunk(""));
			Paragraph paragraph = new Paragraph("Command: " + command + " cannot be executed due to lack of asked product!");
			document.add(paragraph);
			document.close();
			System.out.println("UnderStock generated successfully!");
			++underStockIndex;
		} catch (Exception e) {
			System.err.println(e);
		}
	}
	/**
	 * verifies the report order command syntax and, if correct, generates the necessary pdf file 
	 * @param splitString the command parsed into several necessary fields
	 */
	public void reportOrder(ArrayList<String> splitString) {
		if (splitString.size() != 1) {
			System.out.println("Report order command syntax is wrong!");
		} else {
			try {
				String fileName = "C:\\Users\\Matei\\Desktop\\TP - projects\\Tema3OrderManagement\\Reports\\order_report_"
						+ orderReportIndex + ".pdf";
				Document document = new Document();
				PdfWriter.getInstance(document, new FileOutputStream(fileName));
				document.open();
				document.add(new Chunk(""));
				String query = "SELECT * FROM `order`";
				Connection connection = ConnectionFactory.getConnection();
				PreparedStatement statement = connection.prepareStatement(query);
				ResultSet resultSet = statement.executeQuery();
				PdfPTable table = new PdfPTable(4);
				PdfPCell cell = new PdfPCell(new Phrase("idOrder"));
				table.addCell(cell);
				cell = new PdfPCell(new Phrase("clientName"));
				table.addCell(cell);
				cell = new PdfPCell(new Phrase("productName"));
				table.addCell(cell);
				cell = new PdfPCell(new Phrase("productQuantity"));
				table.addCell(cell);
				table.setHeaderRows(1);
				while (resultSet.next()) {
					cell = new PdfPCell(new Phrase(resultSet.getString(1)));
					table.addCell(cell);
					cell = new PdfPCell(new Phrase(resultSet.getString(2)));
					table.addCell(cell);
					cell = new PdfPCell(new Phrase(resultSet.getString(3)));
					table.addCell(cell);
					cell = new PdfPCell(new Phrase(resultSet.getString(4)));
					table.addCell(cell);
				}
				document.add(table);
				document.close();
				System.out.println("Report generated successfully!");
				++orderReportIndex;
			} catch (Exception e) {
				System.err.println(e);
			}
		}
	}
}
