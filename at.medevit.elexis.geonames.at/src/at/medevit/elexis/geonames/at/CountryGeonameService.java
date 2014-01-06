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
package at.medevit.elexis.geonames.at;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import at.medevit.elexis.geonames.interfaces.ICountryGeonameService;

public class CountryGeonameService implements ICountryGeonameService {
	
	private static Connection connect = null;
	
	static {
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
			connect = DriverManager.getConnection("jdbc:derby:classpath:/geoInfoAT");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public CountryGeonameService(){}
	
	@Override
	public List<String> getZips(){
		String query = "SELECT p.plz FROM plz as p ORDER BY PLZ ASC";
		return queryDatabase(query, "PLZ");
	}
	
	@Override
	public String getDialPrefix(){
		return "+43";
	}
	
	@Override
	public List<String> getCityByZip(String zip){
		String query = 
				"SELECT g.name FROM gemeinden AS g, plz AS p WHERE p.kennziffer = g.kennziffer AND p.hauptplz = '1' AND p.plz = "+zip;
		return queryDatabase(query, "NAME");
	}
	
	@Override
	public List<String> getZipByCity(String city){
		String query =
			"SELECT p.plz FROM gemeinden AS g, plz AS p WHERE p.kennziffer = g.kennziffer AND p.hauptplz = '1' AND UPPER(g.name) LIKE '"
				+ city.toUpperCase() + "'";
		return queryDatabase(query, "PLZ");
	}
	
	@Override
	public List<String> getStreetByZip(String zip){
		String query =
				"SELECT stroffi FROM strassen WHERE plz = "+ Integer.parseInt(zip);		
		return queryDatabase(query, "STROFFI");
	}
	
	@Override
	public List<String> getStreetByCity(String city){
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<String[]> getLabeledCities(){
		String query =
				"SELECT g.name, p.plz FROM gemeinden AS g, plz AS p WHERE p.kennziffer = g.kennziffer AND p.hauptplz = '1'";
		return queryDatabase(query, new String[] {"NAME", "PLZ"});
	}

	private List<String[]> queryDatabase(String query, String[] rows){
		PreparedStatement statement;
		LinkedList<String[]> result = new LinkedList<String[]>();
		try {
			
			statement = connect.prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				String[] resultArray = new String[rows.length];
				for (int i = 0; i < resultArray.length; i++) {
					resultArray[i] = rs.getString(rows[i]);
				}
				result.add(resultArray);
			}
			rs.close();
			statement.close();
			return result;
		} catch (SQLException e) {
			// TODO How to handle this? Simply ignore?
			e.printStackTrace();
		}
		return null;
	}

	private List<String> queryDatabase(String query, String row){
		PreparedStatement statement;
		LinkedList<String> result = new LinkedList<String>();
		try {
			
			statement = connect.prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				result.add(rs.getString(row));
			}
			rs.close();
			statement.close();
			return result;
		} catch (SQLException e) {
			// TODO How to handle this? Simply ignore?
			e.printStackTrace();
		}
		return null;
	}
	
}
