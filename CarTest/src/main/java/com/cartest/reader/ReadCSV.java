package com.cartest.reader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;

/**
 * <p>
 * Command Line Application for Parsing and Processing CAR (XML) and CSV Files
 * </p>
 * 
 * This is the class used to read csv files
 * 
 * @author ematoza.ofc
 * @version 1.0
 * @since 2025-08-20
 */
public class ReadCSV {

	private static final String COMMA_DELIMITER = ",";

	/**
	* Reads the file CarsBrand.csv to get the Car Brand and Release Date
	*
	* @return  LinkedHashMap<String, String> releaseDate returns the Car Brand and Release Date 
	*/
	public LinkedHashMap<String, String> readReleaseDate() throws Exception {

		InputStream inputReader = this.getClass().getResourceAsStream("/files/CarsBrand.csv");

		LinkedHashMap<String, String> releaseDate = new LinkedHashMap<String, String>();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputReader, StandardCharsets.UTF_8))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] values = line.split(COMMA_DELIMITER);
				if (values[1].contains("ReleaseDate")) {
					continue; // ignores header
				}

				values[0] = values[0].replace("\"", ""); // removes extra quotation (")

				SimpleDateFormat toDate = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH); // for formatting String
																								// to Date with intended
																								// format
				SimpleDateFormat toString = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH); // for formatting Date
																								// to String with
																								// intended format
				Date tempDate = toDate.parse(values[1].replace("\"", "")); // removes extra quotation (") and then
																			// parses string to date
				values[1] = toString.format(tempDate); // formats date to string with intended format

				releaseDate.put(values[0], values[1]);
			}
		}

		inputReader.close();

		return releaseDate;
	}

	/**
	* Reads the file CarsModel.csv to get the Car Brand and Model
	*
	* @return  LinkedHashMap<String, String> releaseDate returns the Car Brand and Model 
	*/
	public LinkedHashMap<String, String> readModels() throws Exception {

		InputStream inputReader = this.getClass().getResourceAsStream("/files/CarsModel.csv");

		LinkedHashMap<String, String> model = new LinkedHashMap<String, String>();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputReader, StandardCharsets.UTF_8))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] values = line.split(COMMA_DELIMITER);
				if (values[0].contains("Brand")) {
					continue; // ignores header
				}

				values[0] = values[0].replace("\"", ""); // removes extra quotation (")
				values[1] = values[1].replace("\"", ""); // removes extra quotation (")
				model.put(values[1], values[0]);
			}
		}

		inputReader.close();

		return model;
	}

}
