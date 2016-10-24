package sam.util;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TextToXML {

	public static void main(String[] args) {
		// gets filename/path from command line, gets file, parses by numbers and lines and adds XML elements, then save to a path
		
		Scanner scan = new Scanner(System.in);
		String filepath;
		System.out.print("text file name: ");
		filepath = scan.nextLine();
		String arrayName;
		String saveLocation;
		System.out.print("xml array name: ");
		arrayName = scan.nextLine();
		System.out.print("save to? ");
		saveLocation = scan.nextLine();
		
		FileReader file;
		BufferedReader reader;
		try {
			file = new FileReader(filepath);
			reader = new BufferedReader(file);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("File not found");
		}
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			Document xmlDoc = docBuilder.newDocument();
			Element rootElement = xmlDoc.createElement("resources");
			xmlDoc.appendChild(rootElement);
			
			Element arrayElement = xmlDoc.createElement("string-array");
			rootElement.appendChild(arrayElement);
			arrayElement.setAttribute("name", arrayName);
			
			//loop through the textDoc by number
			String line = "";
			String textItem = "";
			int currentInt = 0;
			try {
				while ((line = reader.readLine()) != null) {
					try {
						currentInt = Integer.parseInt(line.trim());
						//if an int, append the previous string to the doc and then clear it
						Element itemElement = xmlDoc.createElement("item");
						itemElement.appendChild(xmlDoc.createTextNode(textItem));
						arrayElement.appendChild(itemElement);
						textItem = "";
					} catch (NumberFormatException ee) {
						//if not an int, build a string
						textItem += line.trim() + " ";
					}
				}
				reader.close();
			} catch (IOException eee) {
				throw new RuntimeException("IO Error occured");
			}
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(xmlDoc);
			StreamResult result = new StreamResult(new File(saveLocation).getAbsolutePath());
			
			//StreamResult result = new StreamResult(System.out);
			
			transformer.transform(source, result);

			System.out.println("File saved!");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}
}
