/*******************************************************************************
 * Copyright (c) 2013 MEDEVIT.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     MEDEVIT <office@medevit.at> - initial API and implementation
 ******************************************************************************/
package at.medevit.ch.artikelstamm.ui;

import at.medevit.ch.artikelstamm.ArtikelstammConstants.TYPE;

/**
 * Interface defining the requirements for the {@link DetailComposite} data-binding
 */
public interface IArtikelstammItem {
	/**
	 * @return article description
	 */
	public String getDSCR();
	
	/**
	 * @return human readable label
	 */
	public String getLabel();
	
	/**
	 * @return Global Trade Index Number
	 */
	public String getGTIN();
	
	/**
	 * @return pharmacode
	 */
	public String getPHAR();
	
	/**
	 * @return ATC code
	 */
	public String getATCCode();
	
	/**
	 * @return {@link TYPE}
	 */
	public TYPE getType();
	
	/**
	 * @return human readable string of the manufacturer
	 */
	public String getManufacturerLabel();
	
	/**
	 * @return the Ex-Factory price
	 */
	public Double getExFactoryPrice();
	
	/**
	 * @return the public price
	 */
	public Double getPublicPrice();
	
	/**
	 * @return <code>true</code> if the article is part of the Spezialitaetenliste
	 */
	public boolean isInSLList();
	
	/**
	 * @return the swissmedic category this article is allocated to
	 */
	public String getSwissmedicCategory();
	
	/**
	 * @return the generica type, O if original, G if generica, may be <code>null</code>
	 */
	public String getGenericType();
	
	/**
	 * @return the percentage amount the patient has to pay ("Selbstbehalt")
	 */
	public Integer getDeductible();
	
	/**
	 * @return <code>true</code> if this article is narcotic
	 */
	public boolean isNarcotic();
	
}
