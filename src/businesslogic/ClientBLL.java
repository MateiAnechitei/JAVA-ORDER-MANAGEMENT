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
import dataaccess.ClientDAO;
import model.Client;
/**
 * Class used to verify the validity of the commands specific for clients and to execute them.
 */
public class ClientBLL {
	/**
	 * index used to change the number at the end of each client report pdf file
	 */
	static int clientReportIndex = 1;
	/**
	 * verifies the insert client command syntax and, if correct, executes it
	 * @param splitString the command parsed into several necessary fields
	 */
	public void insertClient(ArrayList<String> splitString) {
		if (splitString.size() != 3) {
			System.out.println("Insert client command syntax is wrong!");
		} else {
			ClientDAO clientAccess = new ClientDAO();
			if (clientAccess.findByName(splitString.get(1)) == null) {
				Client newClient = new Client(splitString.get(1), splitString.get(2));
				clientAccess.insert(newClient);
				System.out.println("Client inserted successfully!");
			} else {
				System.out.println("Client already exists!");
			}
		}
	}
	/**
	 * verifies the delete client command syntax and, if correct, executes it
	 * @param splitString the command parsed into several necessary fields
	 */
	public void deleteClient(ArrayList<String> splitString) {
		if (splitString.size() != 3) {
			System.out.println("Delete client command syntax is wrong!");
		} else {
			ClientDAO clientAccess = new ClientDAO();
			if (clientAccess.findByName(splitString.get(1)) == null) {
				System.out.println("Client doesn't exist, cannot be deleted!");
			} else {
				clientAccess.delete(splitString.get(1));
				System.out.println("Client deleted successfully!");
			}
		}
	}
	/**
	 * verifies the report client command syntax and, if correct, generates the necessary pdf file 
	 * @param splitString the command parsed into several necessary fields
	 */
	public void reportClient(ArrayList<String> splitString) {
		if (splitString.size() != 1) {
			System.out.println("Report client command syntax is wrong!");
		} else {
			try {
				String fileName = "C:\\Users\\Matei\\Desktop\\TP - projects\\Tema3OrderManagement\\Reports\\client_report_"
						+ clientReportIndex + ".pdf";
				Document document = new Document();
				PdfWriter.getInstance(document, new FileOutputStream(fileName));
				document.open();
				document.add(new Chunk(""));
				String query = "SELECT * FROM client";
				Connection connection = ConnectionFactory.getConnection();
				PreparedStatement statement = connection.prepareStatement(query);
				ResultSet resultSet = statement.executeQuery();
				PdfPTable table = new PdfPTable(3);
				PdfPCell cell = new PdfPCell(new Phrase("idClient"));
				table.addCell(cell);
				cell = new PdfPCell(new Phrase("name"));
				table.addCell(cell);
				cell = new PdfPCell(new Phrase("city"));
				table.addCell(cell);
				table.setHeaderRows(1);
				while (resultSet.next()) {
					cell = new PdfPCell(new Phrase(resultSet.getString(1)));
					table.addCell(cell);
					cell = new PdfPCell(new Phrase(resultSet.getString(2)));
					table.addCell(cell);
					cell = new PdfPCell(new Phrase(resultSet.getString(3)));
					table.addCell(cell);
				}
				document.add(table);
				document.close();
				System.out.println("Report generated successfully!");
				++clientReportIndex;
			} catch (Exception e) {
				System.err.println(e);
			}
		}
	}
}
