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

import java.util.HashMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

import at.medevit.elexis.geonames.interfaces.ICountryGeonameService;
import ch.elexis.core.types.CountryCode;


public class CountryGeonameServiceResolver {
	
	private static HashMap<CountryCode, ICountryGeonameService> providers = null;
	
	public static ICountryGeonameService getCountryGeonameService(CountryCode country){
		if(providers == null) {
			// TODO This does not take into account dynamic changes of available providers at runtime!
			providers = new HashMap<CountryCode, ICountryGeonameService>();
			initializeExtensions();
		}
		return providers.get(country);
	}
	
	private static void initializeExtensions(){
		IExtensionRegistry exr = Platform.getExtensionRegistry();
		IExtensionPoint exp = exr.getExtensionPoint(ICountryGeonameService.EXTENSION_POINT_ID);
		if (exp != null) {
			IExtension[] extensions = exp.getExtensions();
			for (IExtension ex : extensions) {
				IConfigurationElement[] elems = ex.getConfigurationElements();
				for (IConfigurationElement el : elems) {
					try {
						String country = el.getAttribute("countryCode");
						ICountryGeonameService cgs =
							(ICountryGeonameService) el
								.createExecutableExtension("countryGeonameService");
						providers.put(CountryCode.valueOf(country), cgs);
						// TODO: Log
						System.out.println("countrygeonameservice for " + country + " registered: "
							+ cgs);
					} catch (CoreException e) {
						// TODO Status handler
						e.printStackTrace();
					}
					continue;
				}
			}
		}
	}
	
}
