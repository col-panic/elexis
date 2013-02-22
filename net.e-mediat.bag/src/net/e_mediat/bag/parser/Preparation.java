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
package net.e_mediat.bag.parser;

import java.util.ArrayList;
import java.util.List;

public class Preparation {
	
	public boolean flagSb20;
	public String orgGenCode;
	public String swissmedicCategory;
	
	public List<Pack> packs;
	
	public Preparation(){
		packs = new ArrayList<Pack>();
	}
	
	public List<Pack> getPacks(){
		return packs;
	}
}
