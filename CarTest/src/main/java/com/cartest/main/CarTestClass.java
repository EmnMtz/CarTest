package com.cartest.main;

import java.util.LinkedHashMap;
import java.util.Scanner;

import com.cartest.reader.ProcessFilters;
import com.cartest.reader.ReadCSV;
import com.cartest.reader.ReadXML;

/**
 * This is the main class used to run the application
 * <p>
 * Command Line Application for Parsing and Processing CAR (XML) and CSV Files
 * </p>
 * @author ematoza.ofc
 * @version 1.0
 * @since 2025-08-20
 */
public class CarTestClass {

	public static void main(String[] args) throws Exception{
		
		System.out.println("Command Line Application for Parsing and Processing CAR (XML) and CSV Files");
		
		ReadCSV readCSV = new ReadCSV();
		ReadXML readXML = new ReadXML();
		ProcessFilters process = new ProcessFilters();
		
		Scanner scan = new Scanner(System.in);
		
		String filter = "";
		String brand = "";
		String sort = "";
		String display = "";
		
		LinkedHashMap<String, String> brandChoices = new LinkedHashMap<String, String>();	
		LinkedHashMap<String, String> releaseDate = readCSV.readReleaseDate();
		LinkedHashMap<String, String> models = readCSV.readModels();
		LinkedHashMap<String, LinkedHashMap<String, String>> carDetails = readXML.readXML(models);
		
		
		//checks if the documents are in sync
		//documents should have the same item size
		if(releaseDate.size() != models.size() || 
				releaseDate.size() != carDetails.size() ||
				models.size() != carDetails.size()) {
			System.out.println("------[E R R O R]------");
			System.out.println("Input Data inconsistent or not in sync.");
			System.out.println("Please check Input Data.");
			System.out.println("Exiting program");
			System.exit(0);
			
		}
		
		//gets the car brand names from CarBrand.csv
		Object[] brands = releaseDate.keySet().toArray();
		
		//prompts the user to choose filter to use
		//if choice is not in the displayed choices or is invalid, it will return to the previous message
		while(filter.isBlank()) {
			System.out.println("Please input filters (Please input only the number):");
			System.out.println("1. Brand and Price");
			System.out.println("2. Brand and Release Date");
			filter = scan.nextLine();
			try
		    {
		        if(Integer.parseInt(filter)<0 || Integer.parseInt(filter)>2) {
		        	
		        	System.out.println("Invalid input.");
		        	System.out.println("Please select one of the choices.");
		        	filter = "";
		        }
		    } catch (NumberFormatException ex)
		    {
		    	System.out.println("Invalid input.");
	        	System.out.println("Please select one of the choices.");
	        	filter = "";
		    }
		}
		
		//prompts the user to choose brands to use
		//if choice is not in the displayed choices or is invalid, it will return to the previous message
		//multiple choices can be made by adding comma between choices
		while(brand.isBlank()) {
			System.out.println("Please input brand (Please input only the number):");
			System.out.println("[For multiple choices, please use comma to indicate other choices]");
			
			Integer num = 1;
			for(Object brandItem: brands) {
				System.out.println(num + ". " + brandItem);
				brandChoices.put(String.valueOf(num), String.valueOf(brandItem));
				num++;
			}
			System.out.println(num + ". ALL BRANDS");
			brandChoices.put(String.valueOf(num), "ALL BRANDS");
			
			brand = scan.nextLine();
			try
		    {
				
				String[] choices = brand.split(",");
				for(String choice : choices) {
					if(Integer.parseInt(choice)<0 || Integer.parseInt(choice)>num) {
			        	
			        	System.out.println("Invalid input.");
			        	System.out.println("Please select one of the choices.");
			        	brand = "";
			        }
				}
		        
		    } catch (NumberFormatException ex)
		    {
		    	System.out.println("Invalid input.");
	        	System.out.println("Please select one of the choices.");
	        	brand = "";
		    }
		}
		
		//prompts the user to choose how to sort based on the filter chosen before
		//if choice is not in the displayed choices or is invalid, it will return to the previous message
		while(sort.isBlank()) {
			System.out.println("Please input sorting filter (Please input only the number):");
			
			if(filter.equals("1")) {
				System.out.println("[Price (USD)]");
				System.out.println("1. highest to lowest");
				System.out.println("2. lowest to highest");
			}else {
				System.out.println("[Release Year]");
				System.out.println("1. latest to oldest");
				System.out.println("2. oldest to latest");
			}
			sort = scan.nextLine();
			try
		    {
		        if(Integer.parseInt(sort)<0 || Integer.parseInt(sort)>2) {
		        	
		        	System.out.println("Invalid input.");
		        	System.out.println("Please select one of the choices.");
		        	sort = "";
		        }
		    } catch (NumberFormatException ex)
		    {
		    	System.out.println("Invalid input.");
	        	System.out.println("Please select one of the choices.");
		    	sort = "";
		    }
		}
		
		//prompts the user to choose how the output will be displayed
		//if choice is not in the displayed choices or is invalid, it will return to the previous message
		while(display.isBlank()) {
			System.out.println("Please input display format (Please input only the number):");
			System.out.println("1. Table Format");
			System.out.println("2. XML Format");
			System.out.println("3. JSON Format");
			display = scan.nextLine();
			try
		    {
		        if(Integer.parseInt(display)<0 || Integer.parseInt(display)>3) {
		        	
		        	System.out.println("Invalid input.");
		        	System.out.println("Please select one of the choices.");
		        	display = "";
		        }
		    } catch (NumberFormatException ex)
		    {
		    	System.out.println("Invalid input.");
	        	System.out.println("Please select one of the choices.");
		    	display = "";
		    }
		}
		
		//processes the choices
		StringBuffer output = process.processOutput(filter, brand, sort, display, releaseDate, models, brandChoices, carDetails);
		
		//shows the output on the command line
		System.out.println(output);
		
		//closes the input from the command line
		scan.close();
    }


}
