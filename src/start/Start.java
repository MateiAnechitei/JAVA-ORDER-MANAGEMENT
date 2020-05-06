package start;


import java.util.logging.Logger;
import presentation.CommandControl;
/**
 * 
 * @author Matei Anechitei
 * @Source https://ibytecode.com/blog/jdbc-mysql-create-database-example/
 */
public class Start {
	protected static final Logger LOGGER = Logger.getLogger(Start.class.getName());
	/**
	 * This is the main class of the program. The program will be executed from the command line using the provided .jar file. Format of the execution:
	 * java -jar [].jar file.txt
	 * @param args args[0] is the text file containing the commands to be executed
	 */
	public static void main(String[] args){
		String inputFileName = args[0];
		CommandControl cc = new CommandControl();
		cc.fileProcessing(inputFileName);
	}
}
