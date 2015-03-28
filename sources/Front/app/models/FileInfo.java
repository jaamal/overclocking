package models;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Files")
public class FileInfo implements Serializable {
    private static final long serialVersionUID = -171537291587582232L;
    
    private String id;
	private String fileName;
	private long fileSize;
	private String fileType;
	
	public FileInfo(String id, String name, long size, String type){
		this.id = id;
		this.fileName = name;
		this.fileSize = size;
		this.fileType = type;
	}
	
	//NOTE: constructor for DBDriver
	public FileInfo() {}
	
	@Id
	@Column(name = "id", length = 50)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name="fileName", length=50)
	public String getFileName(){
		return fileName;
	}
	
	public void setFileName(String name){
		this.fileName = name;
	}
	
	@Column(name="fileSize")
	public long getFileSize(){
		return fileSize;
	}
	
	public void setFileSize(long size){
		this.fileSize = size;
	}

	@Column(name="fileType", length=50)
	public String getFileType(){
		return fileType;
	}
	
	public void setFileType(String type){
		this.fileType = type;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof FileInfo))
			return false;
		
		FileInfo info = (FileInfo) obj;
		return this.fileName.equals(info.fileName) && this.fileSize == info.fileSize && this.fileType.equals(info.fileType);
	}
}
