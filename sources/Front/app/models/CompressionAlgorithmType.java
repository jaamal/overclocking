package models;

public enum CompressionAlgorithmType {
	lz77("lz77", "#D140BE"), 
	
	lzma("lz77 with infinite window", "#19BD18"),
	
	lzw("lzw", "#EBF650"),
	
	slpClassic("slp classic (avl-trees)", "#0000FF"),
	
	slpModern("slp optimal (avl-trees)", "#FF9933"),
	
	slpCartesian("slp (cartesian trees)", "#B028A8");
	
	public final String color;
	public final String caption;
	
	private CompressionAlgorithmType(String caption, String color) {
		this.caption = caption;
		this.color = color;
	}
}
