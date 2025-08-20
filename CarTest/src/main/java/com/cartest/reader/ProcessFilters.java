package com.cartest.reader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.stream.IntStream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.vandermeer.asciitable.AsciiTable;

/**
 * <p>
 * Command Line Application for Parsing and Processing CAR (XML) and CSV Files
 * </p>
 * 
 * This is the class that Processes the choices from the main class
 * 
 * @author ematoza.ofc
 * @version 1.0
 * @since 2025-08-20
 */
public class ProcessFilters {

	//calls GSONBuilder to create JSON output
	private static final Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

	/**
	* Processes the choices from the main screen
	*
	* @param  filter	filter choice from user
	* @param  brand		brands chosen by user
	* @param  sort		sorting choice from user
	* @param  format	output format choice from user
	* @param  filter	filter choice from user 
	* @param  releaseDate	brand with release date from CarsBrand.csv
	* @param  models	car models with brands from CarsModel.csv
	* @param brandChoices names of car brands
	* @param carDetails	car details from carsType.csv
	* @return  StringBuffer outputStr returns the formatted output
	*/
	public StringBuffer processOutput(String filter, String brand, String sort, String format,
			LinkedHashMap<String, String> releaseDate, LinkedHashMap<String, String> models,
			LinkedHashMap<String, String> brandChoices,
			LinkedHashMap<String, LinkedHashMap<String, String>> carDetails) {

		StringBuffer outputStr = new StringBuffer();

		Object[] choices = brand.split(",");
		if (choices.length == 1 && choices[0].toString().equals("11")) {
			choices = IntStream.rangeClosed(1, 10).boxed().toArray();
		} else if (choices.length > 1) {
			choices = removeDuplicates(choices);
		}

		ArrayList<LinkedHashMap<String, String>> carList = new ArrayList<LinkedHashMap<String, String>>();

		for (Object choice : choices) {
			LinkedHashMap<String, String> output = new LinkedHashMap<String, String>();
			String carBrand = brandChoices.get(choice.toString());
			LinkedHashMap<String, String> carInfo = carDetails.get(carBrand);
			if (filter.equals("1")) {

				output.put("Brand", carBrand);
				output.put("Type", carInfo.get("Type"));
				output.put("Model", carInfo.get("Model"));
				output.put("Price (USD)", carInfo.get("USD"));
				output.put("Price (EUR)", carInfo.get("EUR"));
				output.put("Price (GBP)", carInfo.get("GBP"));
				output.put("Price (JPY)", carInfo.get("JPY"));

			} else if (filter.equals("2")) {

				output.put("Brand", carBrand);
				output.put("Type", carInfo.get("Type"));
				output.put("Model", carInfo.get("Model"));
				output.put("Release Date", releaseDate.get(carBrand));

			}
			carList.add(output);
		}

		if (carList.size() > 1) {
			carList = processSorting(filter, sort, carList);
		}

		if (format.equals("1")) {
			outputStr = createTable(carList, filter);
		} else if (format.equals("2")) {
			outputStr = createXML(carList, filter);
		} else if (format.equals("3")) {
			outputStr = createJSON(carList, filter);
		}

		return outputStr;

	}

	/**
	* Sorts the data based on user choices
	*
	* @param  filter	filter choice from user
	* @param  sort		sorting choice from user
	* @param  carList	car details based on user choices
	* @return  ArrayList<LinkedHashMap<String, String>> carList returns the sorted car list
	*/
	public ArrayList<LinkedHashMap<String, String>> processSorting(String filter, String sort,
			ArrayList<LinkedHashMap<String, String>> carList) {

		if (filter.equals("1") && sort.equals("1")) {

			Comparator<LinkedHashMap<String, String>> mapComparator = new Comparator<LinkedHashMap<String, String>>() {
				@Override
				public int compare(LinkedHashMap<String, String> map1, LinkedHashMap<String, String> map2) {
					String value1 = map1.get("Price (USD)");
					String value2 = map2.get("Price (USD)");
					return value2.compareTo(value1);
				}
			};
			Collections.sort(carList, mapComparator);

		} else if (filter.equals("1") && sort.equals("2")) {

			Comparator<LinkedHashMap<String, String>> mapComparator = new Comparator<LinkedHashMap<String, String>>() {
				@Override
				public int compare(LinkedHashMap<String, String> map1, LinkedHashMap<String, String> map2) {
					String value1 = map1.get("Price (USD)");
					String value2 = map2.get("Price (USD)");
					return value1.compareTo(value2);
				}
			};
			Collections.sort(carList, mapComparator);

		} else if (filter.equals("2") && sort.equals("1")) {

			Comparator<LinkedHashMap<String, String>> mapComparator = new Comparator<LinkedHashMap<String, String>>() {
				@Override
				public int compare(LinkedHashMap<String, String> map1, LinkedHashMap<String, String> map2) {
					String value1 = map1.get("Release Date");
					String value2 = map2.get("Release Date");
					return value2.compareTo(value1); 
				}
			};
			Collections.sort(carList, mapComparator);

		} else if (filter.equals("2") && sort.equals("2")) {

			Comparator<LinkedHashMap<String, String>> mapComparator = new Comparator<LinkedHashMap<String, String>>() {
				@Override
				public int compare(LinkedHashMap<String, String> map1, LinkedHashMap<String, String> map2) {
					String value1 = map1.get("Release Date");
					String value2 = map2.get("Release Date");
					return value1.compareTo(value2);
				}
			};
			Collections.sort(carList, mapComparator);
		}

		return carList;
	}
	
	/**
	* Formats the output into a table
	* 
	* @param  carList	car details based on user choices
	* @param  filter	filter choice from user
	* @return  StringBuffer output returns the data in a table-formatted string
	*/
	private StringBuffer createTable(ArrayList<LinkedHashMap<String, String>> carList, String filter) {

		StringBuffer output = new StringBuffer();

		AsciiTable table = new AsciiTable();

		if (filter.equals("1")) {
			table.addRule();
			table.addRow("Brand", "Type", "Model", "Price (USD)", "Price (EUR)", "Price (GBP)", "Price (JPY)");
			table.addRule();
			for (LinkedHashMap<String, String> carDetails : carList) {
				table.addRow(carDetails.get("Brand"), carDetails.get("Type"), carDetails.get("Model"),
						carDetails.get("Price (USD)"), carDetails.get("Price (EUR)"), carDetails.get("Price (GBP)"),
						carDetails.get("Price (JPY)"));
				table.addRule();
			}
		} else {
			table.addRule();
			table.addRow("Brand", "Type", "Model", "Release Date");
			table.addRule();
			for (LinkedHashMap<String, String> carDetails : carList) {
				table.addRow(carDetails.get("Brand"), carDetails.get("Type"), carDetails.get("Model"),
						carDetails.get("Release Date"));
				table.addRule();
			}
		}

		String rend = table.render();
		output.append(rend);

		return output;
	}

	/**
	* Formats the output into an XML format
	* 
	* @param  carList	car details based on user choices
	* @param  filter	filter choice from user
	* @return  StringBuffer output returns the data in a XML-formatted string
	*/
	private StringBuffer createXML(ArrayList<LinkedHashMap<String, String>> carList, String filter) {

		StringBuffer output = new StringBuffer();

		output.append("<Cars>");
		output.append("\n");
		for (LinkedHashMap<String, String> carDetails : carList) {
			output.append("\t<Car>");
			output.append("\n");
			for (Map.Entry<String, String> entry : carDetails.entrySet()) {
				output.append("\t\t");
				output.append("<").append(entry.getKey()).append(">");
				output.append(entry.getValue());
				output.append("</").append(entry.getKey()).append(">");
				output.append("\n");
			}
			output.append("\t</Car>\n");
		}
		output.append("</Cars>");

		return output;
	}

	/**
	* Formats the output into a JSON format
	* 
	* @param  carList	car details based on user choices
	* @param  filter	filter choice from user
	* @return  StringBuffer output returns the data in a JSON-formatted string
	*/
	private StringBuffer createJSON(ArrayList<LinkedHashMap<String, String>> carList, String filter) {

		StringBuffer output = new StringBuffer();
		for (LinkedHashMap<String, String> carDetails : carList) {
			gson.toJson(carDetails, output);
			output.append("\n");
		}

		return output;
	}

	/**
	* Removes duplicates from the user input
	* 
	* @param  choices	array of user choices for brands
	* @return  Object[] choices returns the choices without any duplicates
	*/
	private Object[] removeDuplicates(Object[] choices) {

		LinkedHashSet<Object> set = new LinkedHashSet<Object>();

		for (int i = 0; i < choices.length; i++) {
			set.add(choices[i]);
		}
			
		choices = set.toArray();
		
		return choices;
	}

}
