package models.statistics;

public enum UnitType {
	time("ms"),
	size("bytes"),
	counter("number"),
	percent("%");
	
	public final String name;
	
	private UnitType(String name) {
		this.name = name;
	}
}
