package com.cartest.reader;

import java.io.InputStream;
import java.util.LinkedHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * <p>
 * Command Line Application for Parsing and Processing CAR (XML) and CSV Files
 * </p>
 * 
 * This is the class that reads the XML file carsType.xml
 * 
 * @author ematoza.ofc
 * @version 1.0
 * @since 2025-08-20
 */
public class ReadXML {

	/**
	* Reads the file CarsBrand.csv to get the Car Brand and Release Date
	*
	* @return  LinkedHashMap<String, String> releaseDate returns the Car Brand and Release Date 
	*/
	public LinkedHashMap<String, LinkedHashMap<String, String>> readXML(LinkedHashMap<String, String> models) {
		
		LinkedHashMap<String, LinkedHashMap<String, String>> carDetailsOutput = new LinkedHashMap<String, LinkedHashMap<String, String>>();
		
		
		InputStream inputReader = this.getClass().getResourceAsStream("/files/carsType.xml");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
		    DocumentBuilder builder = factory.newDocumentBuilder();
		    Document xml = builder.parse(inputReader);
            xml.getDocumentElement().normalize();

            NodeList carDetails = xml.getElementsByTagName("car");

            for (int i = 0; i < carDetails.getLength(); i++) {
                Node node = carDetails.item(i);
                LinkedHashMap<String, String> carDetail = new LinkedHashMap<String, String>();
                String carBrand = "";
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    String type = element.getElementsByTagName("type").item(0).getTextContent();
                    carDetail.put("Type", type);

                    String model = element.getElementsByTagName("model").item(0).getTextContent();
                    carDetail.put("Model", model);
                    carBrand = models.get(model);
                    
                    Element usdPrice = (Element) element.getElementsByTagName("price").item(0);
                    String price = usdPrice.getTextContent();
                    String currency = usdPrice.getAttribute("currency");
                    carDetail.put(currency, price);

                    NodeList otherPrices = element.getElementsByTagName("prices").item(0).getChildNodes();

                    for (int j = 0; j < otherPrices.getLength(); j++) {
                        Node innerNode = otherPrices.item(j);
                        if (innerNode.getNodeType() == Node.ELEMENT_NODE && innerNode.getNodeName().equals("price")) {
                            Element innerElement = (Element) innerNode;
                            String otherCurrency = innerElement.getAttribute("currency");
                            String otherPrice = innerElement.getTextContent();
                            carDetail.put(otherCurrency, otherPrice);
                        }
                    }
                }
                
                carDetailsOutput.put(carBrand, carDetail);
            }

        } catch (Exception e) {
        	System.out.println("------[E R R O R]------");
            System.out.println("An error was encountered with the input file carsType.xml");
            System.out.println("Complete Error Trace:");
            e.printStackTrace();
            System.out.println("Exiting Application");
            System.exit(0);
        }
		
		
		return carDetailsOutput;
	}
	
}
