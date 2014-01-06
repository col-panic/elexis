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
package at.medevit.elexis.geonames.impl;

import java.util.List;

import at.medevit.elexis.geonames.interfaces.ICountryGeonameService;
import at.medevit.elexis.geonames.interfaces.IGeonameService;
import ch.elexis.core.types.CountryCode;

public class GeonameServiceImpl implements IGeonameService {
	
	@Override
	public List<String> getZipByCountry(CountryCode country){
		ICountryGeonameService igs =
			CountryGeonameServiceResolver.getCountryGeonameService(country);
		if (igs != null) {
			return igs.getZips();
		} else {
			return null;
		}
	}
	
	@Override
	public String getDialPrefixByCountry(CountryCode country){
		ICountryGeonameService igs =
			CountryGeonameServiceResolver.getCountryGeonameService(country);
		if (igs != null) {
			return igs.getDialPrefix();
		} else {
			return null;
		}
	}
	
	@Override
	public List<String> getCityByZipAndCountry(String zip, CountryCode country){
		ICountryGeonameService igs =
			CountryGeonameServiceResolver.getCountryGeonameService(country);
		if (igs != null) {
			return igs.getCityByZip(zip);
		} else {
			return null;
		}
	}
	
	@Override
	public List<String> getZipByCityAndCountry(String city, CountryCode country){
		ICountryGeonameService igs =
			CountryGeonameServiceResolver.getCountryGeonameService(country);
		if (igs != null) {
			return igs.getZipByCity(city);
		} else {
			return null;
		}
	}
	
	@Override
	public List<String> getStreetByZipAndCountry(String zip, CountryCode country){
		ICountryGeonameService igs =
			CountryGeonameServiceResolver.getCountryGeonameService(country);
		if (igs != null) {
			return igs.getStreetByZip(zip);
		} else {
			return null;
		}
	}
	
	@Override
	public List<String> getStreetByCityAndCountry(String city, CountryCode country){
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<String[]> getLabeledCitiesByCountry(CountryCode country){
		ICountryGeonameService igs =
			CountryGeonameServiceResolver.getCountryGeonameService(country);
		if (igs != null) {
			return igs.getLabeledCities();
		} else {
			return null;
		}
	}
	
}
