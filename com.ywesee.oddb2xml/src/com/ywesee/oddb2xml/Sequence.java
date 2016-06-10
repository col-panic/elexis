package com.ywesee.oddb2xml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Sequence {
	
	private String prodno;
	private String dscr;
	private String dcsrf;
	private Map<String, SequenceItem> sequenceItems = new HashMap<String, Sequence.SequenceItem>();
	
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
	
	public Map<String, SequenceItem> getSequenceItems(){
		return sequenceItems;
	}
	
	public static Function<String, Sequence> mapToPerson = (line) -> {
		Sequence seq = null;
		
		String[] p = line.split(";", Integer.MAX_VALUE);
		String prodno = p[0];
		p = Arrays.copyOfRange(p, 1, p.length);
		for (int i = 0; i < (p.length / 6); i++) {
			String[] value = Arrays.copyOfRange(p, i * 6, ((i + 1) * 6));
			if (i == 0) {
				seq = new Sequence(prodno, value[1], "--missing--");
			}
			
			SequenceItem si = new SequenceItem();
			si.setDesc1(value[0].trim());
			si.setDesc2(value[1].trim());
			si.setGalenicForm(value[2].trim());
			si.setGtin(value[3].trim());
			si.setAmount(value[4].trim());
			si.setMunit(value[5].trim());
			if (seq != null) {
				seq.getSequenceItems().put(si.getGtin(), si);
			}
		}
		
		return seq;
	};
	
	public static class SequenceItem {
		String desc1;
		String desc2;
		String galenicForm;
		String gtin;
		String amount;
		String munit;
		
		public String getDesc1(){
			return desc1;
		}
		
		public void setDesc1(String desc1){
			this.desc1 = desc1;
		}
		
		public String getDesc2(){
			return desc2;
		}
		
		public void setDesc2(String desc2){
			this.desc2 = desc2;
		}
		
		public String getGalenicForm(){
			return galenicForm;
		}
		
		public void setGalenicForm(String galenicForm){
			this.galenicForm = galenicForm;
		}
		
		public String getGtin(){
			return gtin;
		}
		
		public void setGtin(String gtin){
			this.gtin = gtin;
		}
		
		public String getAmount(){
			return amount;
		}
		
		public void setAmount(String amount){
			this.amount = amount;
		}
		
		public String getMunit(){
			return munit;
		}
		
		public void setMunit(String munit){
			this.munit = munit;
		}
		
	}
}
