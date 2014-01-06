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
package at.medevit.elexis.geonames.ch.buildDB;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;

import org.apache.commons.io.FileUtils;

public class Main {

	private Connection connect = null;
	private Statement statement = null;
	private ResultSet resultSet = null;
	static String projectLocation;

	public Main() throws Exception {
		try {

			Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
			connect = DriverManager.getConnection("jdbc:derby:"
					+ projectLocation + "/db/geoInfoCH");

			// Tabelle GEMEINDEN erstellen -------------------------------------
			// BFSNR INTEGER NOT NULL CONSTRAINT bfsnr_no_pk PRIMARY KEY,
			// ORTSBEZEICHNUNG VARCHAR(30) NOT NULL,
			// KANTON VARCHAR(2)
			String inputGemeinden = FileUtils
					.readFileToString(new File(projectLocation
							+ "/rsc/match-zip/plz_c_20131223.txt.FIXED"), "UTF-8");
			PreparedStatement pstGemeinden = connect
					.prepareStatement("INSERT INTO GEMEINDEN VALUES (?, ?, ?)");

			String[] inputLinesGemeinden = inputGemeinden.split("\n");
			System.out.println("We have " + inputLinesGemeinden.length
					+ " gemeinden");

			for (int i = 0; i < (inputLinesGemeinden.length); i++) {
				String[] line = inputLinesGemeinden[i].split("\t");
				int BFSNR = Integer.parseInt(line[0].trim());
				String gemeindeName = line[1].trim();
				String kanton = line[2].trim();

				System.out.println("| + " + BFSNR + " " + gemeindeName + " "
						+ kanton);
				pstGemeinden.setInt(1, BFSNR);
				pstGemeinden.setString(2, gemeindeName);
				pstGemeinden.setString(3, kanton);
				pstGemeinden.execute();
			}
			pstGemeinden.close();

			// Tabelle PLZ erstellen -------------------------------------
			// ONRP INTEGER NOT NULL CONSTRAINT onrp_plz_fk PRIMARY KEY,
			// BFSNR INTEGER NOT NULL,
			// PLZ INTEGER NOT NULL,
			// HAUPTPLZ VARCHAR(1)
			String inputPlz = FileUtils.readFileToString(new File(
					projectLocation
							+ "/rsc/match-zip/plz_p1_20131223.txt.FIXED"), "UTF-8");
			PreparedStatement pstPlz = connect
					.prepareStatement("INSERT INTO PLZ VALUES (?, ?, ?, ?)");
			PreparedStatement pstPlzBfsnrExists = connect
					.prepareStatement("SELECT COUNT(*) FROM PLZ p WHERE p.BFSNR = ?");
			String[] inputLinesPlz = inputPlz.split("\n");
			for (int i = 0; i < (inputLinesPlz.length); i++) {
				String[] line = inputLinesPlz[i].split("\t");
				int PLZType = Integer.parseInt(line[1].trim());
				if (PLZType == 10 || PLZType == 20) {
					int ONRP = Integer.parseInt(line[0].trim());
					int PLZ = Integer.parseInt(line[2].trim());
					int BFSNr = Integer.parseInt(line[11].trim());
					
					pstPlzBfsnrExists.setInt(1, BFSNr);
					ResultSet rs = pstPlzBfsnrExists.executeQuery();
					rs.next();
					System.out.println("| + " + ONRP + " " + PLZ + " "
							+ BFSNr);
					if(rs.getInt(1)==0) {
						// primary zip code for community
						pstPlz.setInt(1, ONRP);
						pstPlz.setInt(2, BFSNr);
						pstPlz.setInt(3, PLZ);
						pstPlz.setInt(4, 1);
						pstPlz.execute();
					} else {
						// additional zip code for this community
						pstPlz.setInt(1, ONRP);
						pstPlz.setInt(2, BFSNr);
						pstPlz.setInt(3, PLZ);
						pstPlz.setInt(4, 0);
						pstPlz.execute();
					}
				}
			}

			// Tabelle STRASSEN erstellen
			// ------------------------------------------------
			// EIDGSTRID INTEGER NOT NULL CONSTRAINT eidgstrid_no_pk PRIMARY
			// KEY,
			// BFSNR INTEGER CONSTRAINT bfnsr_gemeinden_fk REFERENCES
			// GEMEINDEN(BFSNR),
			// STROFFI VARCHAR(50),
			// PLZ INTEGER
			String inputStrassen = FileUtils.readFileToString(new File(
					projectLocation + "/rsc/GWR-Strassenliste/9999_131201_STR.txt.FIXED"), "UTF-8");
			PreparedStatement pstStrassen = connect
					.prepareStatement("INSERT INTO STRASSEN VALUES (?, ?, ?, ?)");
			String[] inputLinesStreets = inputStrassen.split("\n");
			System.out.println("we have " + inputLinesStreets.length + " strassen");
			int violations = 0;
			for (int i = 0; i < inputLinesStreets.length; i++) {
				String[] line = inputLinesStreets[i].split("\t");

				int eidgStrassenId = Integer.parseInt(line[0].trim());
				int BFSNr = Integer.parseInt(line[4].trim());
				String name = line[7].trim();
				int PLZ = Integer.parseInt(line[5].trim());

				pstStrassen.setInt(1, eidgStrassenId);
				pstStrassen.setInt(2, BFSNr);
				pstStrassen.setString(3, name);
				pstStrassen.setInt(4, PLZ);
				try {
					// Sometimes eidgStrassenId is NOT unique
					pstStrassen.execute();
				} catch (SQLIntegrityConstraintViolationException se) {
					violations++;
				}
				System.out.println("| + S " + BFSNr + " " + PLZ + " " + name);
			}
			System.out.println("IMPORTED communities: "+inputLinesGemeinden.length+" zip codes: "+inputLinesPlz.length+" streets: "+inputLinesStreets.length);
			pstStrassen.close();
		} catch (Exception e) {
			throw e;
		} finally {
			close();
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
