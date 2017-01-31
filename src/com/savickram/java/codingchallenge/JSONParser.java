package com.savickram.java.codingchallenge;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONParser {

	/**
	 * 
	 * This method reads from the given file and returns the JSON string.
	 * 
	 * @param filePath
	 *            - Location of file from which we read the JSON
	 * @return - Returns the JSON string from the given file
	 */
	public String readJSON(String filePath) {

		String json = null;

		// Read from the file using FileReader & BufferedReader
		try {
			FileReader in = new FileReader(filePath);
			BufferedReader br = new BufferedReader(in);

			StringBuilder sb = new StringBuilder();
			String line = null;

			// Reads each line and appends to StringBuilder if it is NOT NULL
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}

			// Converting StringBuilder to String
			json = sb.toString();
			in.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return json;

	}

	/**
	 * 
	 * This method parses the given JSON and aligns the data into respective class objects
	 * 
	 * @param jsonString - JSON content which needs to be parsed
	 * @return - Returns a list of Elements parsed from JSON
	 */
	public ArrayList<Element> parseJSON(String jsonString) {

		JSONObject obj;
		JSONObject formObject;

		ArrayList<Element> elementList = new ArrayList<Element>();

		// Creates a JSON Object from JSONString
		try {
			obj = new JSONObject(jsonString);

			// Fetching the FORM object
			formObject = obj.getJSONObject("form");

			// Reading the FORM values
			int elementsCount = Integer.parseInt(formObject
					.getString("elementsCount"));

			// Reading all the Elements in the FORM
			JSONArray elementArray = formObject.getJSONArray("elements");

			// Iterating through ElementArray and fetch individual element to
			// read Element data
			for (int i = 1; i <= elementsCount; i++) {

				JSONObject jInObject = elementArray.getJSONObject(0)
						.getJSONObject("element" + i);

				Element element = new Element();

				element.setId(jInObject.getInt("id"));
				element.setName(jInObject.get("name").toString());
				element.setType(jInObject.get("type").toString());
				element.setHasChild(Boolean.parseBoolean(jInObject.get(
						"hasChild").toString()));
				element.setHasParent(Boolean.parseBoolean(jInObject.get(
						"hasParent").toString()));

				JSONArray jAarray = jInObject.getJSONArray("values");
				List<String> list = new ArrayList<String>();

				for (int index = 0; index < jAarray.length(); index++) {

					list.add(jAarray.getString(index));
				}

				String[] values = list.toArray(new String[list.size()]);

				element.setValues(values);

				// Read PARENT DEATILS of each element & set it into POJO class
				if (element.isHasChild()) {

					JSONObject childDetails = jInObject.getJSONArray(
							"childDetails").getJSONObject(0);
					ArrayList<ChildElement> childList = new ArrayList<ChildElement>();

					for (int index = 1; index <= childDetails.length(); index++) {

						ChildElement childElement = new ChildElement();
						JSONObject childJSONElement = childDetails
								.getJSONObject("child" + (index));

						childElement.setChildElementId(childJSONElement
								.getInt("elementId"));
						childElement.setChildElementName(childJSONElement
								.getString("elementName"));
						childElement.setChildElementValueType(childJSONElement
								.getString("elementValueType"));
						childElement
								.setParentElementTriggerValue(childJSONElement
										.getString("elementTriggerValue"));

						childList.add(childElement);
					}
					element.setChildList(childList);
				}
				elementList.add(element);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return elementList;
	}

	/**
	 * Main function where program starts
	 * @param param - Program input
	 */
	public static void main(String[] param) {

		// Creating object for calling methods
		JSONParser form = new JSONParser();

		// Reading from JSON from given file
		String jsonString = form.readJSON("./input/DyForms.json");

		// Parsing the JSON string into POJO objects
		ArrayList<Element> elementList = form.parseJSON(jsonString);

		// Creates a GUI form based on above parsed list of elements
		new DynamicForms(elementList);

	}

}
