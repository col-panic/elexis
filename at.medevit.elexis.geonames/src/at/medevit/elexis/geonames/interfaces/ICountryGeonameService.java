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
package at.medevit.elexis.geonames.interfaces;

import java.util.List;

/**
 * An extending plug-in registers this interface via the OSGI service extension.
 * Additional information presented by the respective plug-in is for what
 * country it provides the information, hence this is assumed to be known here.
 */
public interface ICountryGeonameService {

	public static final String EXTENSION_POINT_ID = "at.medevit.elexis.geonames.countryGeonameService";

	/**
	 * @return all ZIP codes for the country the plug-in registered with or an
	 *         empty {@link List}
	 */
	List<String> getZips();

	/**
	 * @return the dial prefix for the country the plug-in registered with or an
	 *         empty String
	 */
	String getDialPrefix();

	/**
	 * @return a list of values in the form "city (main postal code)" or an
	 *         empty {@link List}
	 */
	List<String[]> getLabeledCities();

	/**
	 * @param zip
	 *            the ZIP code
	 * @return all city names registered for this ZIP code or an empty
	 *         {@link List}
	 */
	List<String> getCityByZip(String zip);

	/**
	 * 
	 * @param city the city to lookup the ZIP code
	 * @return the {@link List} of ZIP codes registered for this city, or an empty {@link List}
	 */
	List<String> getZipByCity(String city);

	/**
	 * @param zip the ZIP code to lookup the street information for
	 * @return the {@link List} of streets registered for this ZIP code, or an empty {@link List}
	 */
	List<String> getStreetByZip(String zip);

	/**
	 * @param city the city name to lookup the street information for
	 * @return the {@link List} of streets registered for this ZIP code, or an empty {@link List}
	 */
	List<String> getStreetByCity(String city);

}
