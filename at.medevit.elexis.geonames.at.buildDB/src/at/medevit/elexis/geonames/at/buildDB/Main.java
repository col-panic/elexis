/*******************************************************************************
 * Copyright (c) 2012 MEDEVIT <office@medevit.at>.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     MEDEVIT <office@medevit.at> - initial API and implementation
 ******************************************************************************/
package at.medevit.elexis.geonames.at.buildDB;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class Main {

	private Connection connect = null;
	private Statement statement = null;
	private ResultSet resultSet = null;
	static String projectLocation;

	private static List<Datensatz> strassen = new LinkedList<Datensatz>();

	public static void addStrasse(Datensatz d) {
		strassen.add(d);
	}

	public Main() throws Exception {
		try {
			// POPULATE STRASSEN
			try {
				// XMLReader erzeugen
				XMLReader xmlReader = XMLReaderFactory.createXMLReader();
				FileReader reader = new FileReader(
						"rsc/gemplzstr/gemplzstr.xml");
				InputSource inputSource = new InputSource(reader);
				xmlReader.setContentHandler(new GemPlzStrContentHandler());

				// Parsen wird gestartet
				xmlReader.parse(inputSource);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			}

			Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
			connect = DriverManager.getConnection("jdbc:derby:"
					+ projectLocation + "/db/geoInfoAT");

			addCities();
			addStreets();
			
		} catch (Exception e) {
			throw e;
		} finally {
			close();
		}
	}

	private void addStreets() throws SQLException{
		System.out.println("we have " + strassen.size() + " strassen");
		// Add Strassen
		Statement st;
		for (int j = 0; j < strassen.size(); j++) {
			Datensatz d = strassen.get(j);
			String query = "INSERT INTO STRASSEN VALUES("
					+ Integer.parseInt(d.getGemnr2()) + ","
					+ Integer.parseInt(d.getPlznr()) + ",'"
					+ d.getStroffi() + "'," + Integer.parseInt(d.getSkz())
					+ ")";
			System.out.println(query);
			st = connect.createStatement();
			st.executeUpdate(query);
			st.close();
		}
	}

	private void addCities() throws IOException, SQLException{
		String inputDocument = FileUtils.readFileToString(new File(
			projectLocation + "/rsc/gemeinden_c_022953_191213.csv"), Charsets.UTF_8);
	String[] inputLines = inputDocument.split("\n");

	for (int i = 3; i < (inputLines.length - 1); i++) {
		String[] line = inputLines[i].split(";");

		Statement st = connect.createStatement();
		String insertGemeinde = "INSERT INTO GEMEINDEN VALUES("
				+ line[0].trim() + ",'" + line[1].trim() + "',"
				+ line[2].trim() + ",'" + line[3].trim() + "'" + ")";
		System.out.print(insertGemeinde);
		int result = st.executeUpdate(insertGemeinde);
		System.out.print(" :" + result + "\n");
		st.close();

		st = connect.createStatement();
		String insertHauptPlz = "INSERT INTO PLZ VALUES("
				+ line[4].trim() + "," + line[2].trim() + ",'1')";
		System.out.println(insertHauptPlz);
		st.executeUpdate(insertHauptPlz);
		st.close();

		String[] otherPlzs = line[5].trim().split(" ");
		if (otherPlzs != null && otherPlzs.length > 0) {
			for (int j = 0; j < otherPlzs.length; j++) {
				if (otherPlzs[j].isEmpty())
					continue;
				st = connect.createStatement();
				String otherHauptPlz = "INSERT INTO PLZ VALUES("
						+ otherPlzs[j].trim() + "," + line[2].trim()
						+ ",'0')";
				System.out.println(otherHauptPlz);
				st.executeUpdate(otherHauptPlz);
				st.close();
			}
		}
	}
	}

	private void close() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}
			if (statement != null) {
				statement.close();
			}
			if (connect != null) {
				connect.close();
			}
		} catch (Exception e) {

		}
	}

	public static void main(String[] args) throws Exception {
		projectLocation = new File("").getAbsolutePath();
		new Main();
	}
}
