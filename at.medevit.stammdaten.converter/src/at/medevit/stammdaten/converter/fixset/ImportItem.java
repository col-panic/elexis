package at.medevit.stammdaten.converter.fixset;

public class ImportItem {
	
	String itemNumber;
	String name;
	String gtin;
	String atc;
	String pharm;
	
	public void setItemNumber(String string){
		itemNumber = string;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setGTIN(String gtin){
		this.gtin = gtin;
	}
	
	public void setATC(String atc){
		this.atc = atc;
		
	}
	
	public void setPHARM(String pharm){
		this.pharm = pharm;
	}
	
	@Override
	public boolean equals(Object obj){
		if (obj instanceof ImportItem) {
			ImportItem ii = (ImportItem) obj;
			return ii.gtin.equals(((ImportItem) obj).gtin);
		}
		return super.equals(obj);
	}
	
	@Override
	public int hashCode(){
		return gtin.trim().hashCode();
	}
	
}
