package dataContracts.files;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import dataContracts.ContentType;

@Entity
public class FileMetadata
{
    @Id
    private String id;

    @Column(name="filename")
    private  String fileName;
    
    @Column(name="filesize")
    private String fileSizeStr;
    private long fileSize;
    
    @Column(name="filetype")
    private String fileTypeStr;
    private FileType fileType;
    
    @Column(name="contenttype")
    private String contentTypeStr;
    private ContentType contentType;

    public FileMetadata() {
    }
    
    public FileMetadata(
            String fileId,
            String fileName,
            long fileSize,
            FileType fileType,
            ContentType contentType)
    {
        this.id = fileId;
        this.fileName = fileName;
        this.fileSize = fileSize;
        fileSizeStr = String.valueOf(fileSize);
        this.fileType = fileType;
        fileTypeStr = fileType.toString();
        this.contentType = contentType;
        contentTypeStr = contentType.toString();
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public String getFileSizeStr() {
        return this.fileSizeStr;
    }
    
    public void setFileSizeStr(String fileSizeStr) {
        this.fileSizeStr = fileSizeStr;
        this.fileSize = Long.parseLong(fileSizeStr);
    }
    
    public long getFileSize() {
        this.fileSize = Long.parseLong(this.fileSizeStr);
        return this.fileSize;
    }
    
    public String getFileTypeStr() {
        return this.fileTypeStr;
    }
    
    public void setFileTypeStr(String fileTypeStr) {
        this.fileTypeStr = fileTypeStr;
        this.fileType = FileType.valueOf(fileTypeStr);
    }
    
    public FileType getFileType() {
        this.fileType = FileType.valueOf(fileTypeStr);
        return this.fileType;
    }
    
    public String getContentTypeStr() {
        return this.contentTypeStr;
    }
    
    public void setContentTypeStr(String contentTypeStr) {
        this.contentTypeStr = contentTypeStr;
        this.contentType = ContentType.valueOf(contentTypeStr);
    }
    
    public ContentType getContentType() {
        this.contentType = ContentType.valueOf(contentTypeStr);
        return this.contentType;
    }
}
