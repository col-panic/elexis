package com.ywesee.oddb2xml;

import java.util.function.Function;

public class Sequence {
	
	private String prodno;
	private String dscr;
	private String dcsrf;

	public Sequence(String prodno, String dscr, String dscrf){
		this.prodno = prodno;
		this.dscr = dscr;
		this.dcsrf = dscrf;
	}
	
	public String getProdno(){
		return prodno;
	}
	
	public String getDcsrf(){
		return dcsrf;
	}
	
	public String getDscr(){
		return dscr;
	}
	
	public static Function<String, Sequence> mapToPerson = (line) -> {
		  String[] p = line.split(";");
		  return new Sequence(p[0], p[2], "--missing--");
		};
}
