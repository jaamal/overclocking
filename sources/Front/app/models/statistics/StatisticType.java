package models.statistics;

public enum StatisticType {
	inputSize("Input Size", UnitType.size, "bytes"),
	executionTime("Time", UnitType.time, "ms"),
	compressionRate("Compression Rate", UnitType.percent, "%"),
	avlRotationsNumber("AVL Rotations", UnitType.counter, "number"),
	factorizationSize("Factorization Size", UnitType.counter, "number"),
	slpHeight("SLP Height", UnitType.counter, "number");
	
	public final String name;
	public final UnitType unitType;
	public final String label;
	
	private StatisticType(String name, UnitType unitType, String label){
		this.name = name;
		this.unitType = unitType;
		this.label = label;
	}
}
