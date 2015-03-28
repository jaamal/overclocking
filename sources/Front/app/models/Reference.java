package models;

public class Reference {
	
	private final String authors;
	private final String title;
	private final String metainfo;
	private final String downloadLink;
	
	public Reference(String authors, String title, String metainfo, String downloadLink){
		this.authors = authors;
		this.title = title;
		this.metainfo = metainfo;
		this.downloadLink = downloadLink;
	}
	
	public String getAuthors() {
		return this.authors;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public String getMetainfo() {
		return this.metainfo;
	}
	
	public String getDownloadLink() {
		return this.downloadLink;
	}
}
