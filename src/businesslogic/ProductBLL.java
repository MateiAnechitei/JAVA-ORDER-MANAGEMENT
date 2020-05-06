package businesslogic;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import connection.ConnectionFactory;
import dataaccess.ProductDAO;
import model.Product;
/**
 * Class used to verify the validity of the commands specific for products and to execute them.
 */
public class ProductBLL {
	/**
	 * index used to change the number at the end of each product report pdf file
	 */
	static int productReportIndex = 1;
	/**
	 * verifies the insert product command syntax and, if correct, executes it
	 * @param splitString the command parsed into several necessary fields
	 */
	public void insertProduct(ArrayList<String> splitString) {
		if (splitString.size() != 4) {
			System.out.println("Insert product command syntax is wrong!");
		} else {
			ProductDAO productAccess = new ProductDAO();
			Product checkProduct = productAccess.findByName(splitString.get(1));
			if (checkProduct == null) {
				try {
					int newQuantity = Integer.parseInt(splitString.get(2));
					float newPrice = Float.parseFloat(splitString.get(3));
					if (newQuantity <= 0 || newPrice <= 0.0f) {
						throw new NumberFormatException();
					}
					Product newProduct = new Product(splitString.get(1), newQuantity, newPrice);
					productAccess.insert(newProduct);
					System.out.println("Product inserted successfully");
				} catch (NumberFormatException e) {
					System.out.println("Quantity or price are not valid!");
				}
			} else {
				try {
					int newQuantity = Integer.parseInt(splitString.get(2));
					float newPrice = Float.parseFloat(splitString.get(3));
					if ((newQuantity <= 0) || (newPrice <= 0) || (checkProduct.getPrice() != newPrice)) {
						throw new NumberFormatException();
					}
					int updateQuantity = checkProduct.getQuantity() + newQuantity;
					productAccess.update(updateQuantity, checkProduct.getName());
					System.out.println("Product inserted successfully");
				} catch (NumberFormatException e) {
					System.out.println("Quantity or price are not valid!");
				}
			}
		}
	}
	/**
	 * verifies the delete client command syntax and, if correct, executes it
	 * @param splitString the command parsed into several necessary fields
	 */
	public void deleteProduct(ArrayList<String> splitString) {
		if (splitString.size() != 2) {
			System.out.println("Delete product command syntax is wrong!");
		} else {
			ProductDAO productAccess = new ProductDAO();
			if (productAccess.findByName(splitString.get(1)) == null) {
				System.out.println("Product doesn't exist, cannot be deleted!");
			} else {
				productAccess.delete(splitString.get(1));
				System.out.println("Product deleted successfully!");
			}
		}
	}
	/**
	 * verifies the report product command syntax and, if correct, generates the necessary pdf file 
	 * @param splitString the command parsed into several necessary fields
	 */
	public void reportProduct(ArrayList<String> splitString) {
		if (splitString.size() != 1) {
			System.out.println("Report product command syntax is wrong!");
		} else {
			try {
				String fileName = "C:\\Users\\Matei\\Desktop\\TP - projects\\Tema3OrderManagement\\Reports\\product_report_"
						+ productReportIndex + ".pdf";
				Document document = new Document();
				PdfWriter.getInstance(document, new FileOutputStream(fileName));
				document.open();
				document.add(new Chunk(""));
				String query = "SELECT * FROM product";
				Connection connection = ConnectionFactory.getConnection();
				PreparedStatement statement = connection.prepareStatement(query);
				ResultSet resultSet = statement.executeQuery();
				PdfPTable table = new PdfPTable(4);
				PdfPCell cell = new PdfPCell(new Phrase("idProduct"));
				table.addCell(cell);
				cell = new PdfPCell(new Phrase("name"));
				table.addCell(cell);
				cell = new PdfPCell(new Phrase("quantity"));
				table.addCell(cell);
				cell = new PdfPCell(new Phrase("price"));
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
				++productReportIndex;
			} catch (Exception e) {
				System.err.println(e);
			}
		}
	}
}
