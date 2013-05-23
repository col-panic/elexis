package ch.artikelstamm.elexisv21.common;

public enum BlackBoxReason {
	
	NOT_BLACKBOXED(0), IS_ON_STOCK(1), IS_REFERENCED_IN_FIXMEDICATION(2),
		IS_REFERENCED_IN_CONSULTATION(3);
	
	private int numercialReason;
	
	private BlackBoxReason(int numercialReason){
		this.numercialReason = numercialReason;
		
	}
	
	public int getNumercialReason(){
		return numercialReason;
	}
	
	public String getNumericalReasonString(){
		return numercialReason + "";
	}
	
	public String getReasonExplanationString(){
		switch (numercialReason) {
		case 0:
			return "";
		case 1:
			return "Artikel wird auf Lager geführt";
		case 2:
			return "Artikel ist als Fixmedikation geführt";
		case 3:
			return "Artikel ist in einer Konsultation verschrieben";
		default:
			return "Invalid value";
		}
	}
	
	/**
	 * @param reason
	 * @return a {@link BlackBoxReason} or <code>null</code> if invalid
	 */
	public static BlackBoxReason getByInteger(int reason){
		switch (reason) {
		case 0:
			return NOT_BLACKBOXED;
		case 1:
			return IS_ON_STOCK;
		case 2:
			return IS_REFERENCED_IN_FIXMEDICATION;
		case 3:
			return IS_REFERENCED_IN_CONSULTATION;
		default:
			return null;
		}
	}
}
