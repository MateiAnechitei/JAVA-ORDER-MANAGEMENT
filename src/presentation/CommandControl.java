package presentation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import businesslogic.ClientBLL;
import businesslogic.OrderBLL;
import businesslogic.ProductBLL;
/**
 * Processes the input text file and extracts the commands to be executed. 
 */
public class CommandControl {
	/**
	 * int number used to keep track of the text file line currently being processed.
	 */
	static int fileLine = 1;
	/**
	 * Parses a command using RegEx into several parts.
	 * @param command a full line(command to be executed) of the text file
	 * @return a string array with the different parts of the command
	 */
	public ArrayList<String> commandSplit(String command) {
		ArrayList<String> splitString = new ArrayList<String>();

		for (String val : command.split("[:|,]\\s?")) {
			splitString.add(val);
		}

		return splitString;
	}
	/**
	 * Based on what the first part of the command is, calls the Business Logic Layer to execute a certain action.
	 * @param command a full line(command to be executed) of the text file
	 */
	public void commandProcessing(String command) {
		ArrayList<String> splitString = commandSplit(command);
		ClientBLL clientBLL = new ClientBLL();
		OrderBLL orderBLL = new OrderBLL();
		ProductBLL productBLL = new ProductBLL();

		if (splitString.get(0).equals("Insert client")) {
			clientBLL.insertClient(splitString);
		} else if (splitString.get(0).equals("Delete client")) {
			clientBLL.deleteClient(splitString);
		} else if (splitString.get(0).equals("Report client")) {
			clientBLL.reportClient(splitString);
		} else if (splitString.get(0).equals("Insert product")) {
			productBLL.insertProduct(splitString);
		} else if (splitString.get(0).equals("Delete product")) {
			productBLL.deleteProduct(splitString);
		} else if (splitString.get(0).equals("Report product")) {
			productBLL.reportProduct(splitString);
		} else if (splitString.get(0).equals("Order")) {
			orderBLL.insertOrder(splitString, command);
		} else if (splitString.get(0).equals("Report order")) {
			orderBLL.reportOrder(splitString);
		} else {
			System.out.println("Unknown command at line " + fileLine + " in the input text file!");
		}
		fileLine++;
	}
	/**
	 * Parses the input text file into a list which contains all the commands, and processes those commands separately.
	 * For clarity purposes, a Thread.sleep(300) is called to observe the output. If the text file is of bigger size, delete it.
	 * @param fileName the input text file name
	 */
	public void fileProcessing(String fileName) {
		try {
			List<String> lines = Files.readAllLines(Paths.get(fileName));
			for (String s : lines) {
				System.out.print(fileLine + ": ");
				commandProcessing(s);
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
