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
    
    public String getName() {
        return fileName;
    }
    
    public void setName(String name) {
        this.fileName = name;
    }
    
    public String getFileSizeStr() {
        return this.fileSizeStr;
    }
    
    public void setFileSizeStr(String fileSizeStr) {
        this.fileSizeStr = fileSizeStr;
        this.fileSize = Long.parseLong(fileSizeStr);
    }
    
    public long getSize() {
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
    
    public FileType getType() {
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
    
    @Override
    public int hashCode() {
        return 31 + ((id == null) ? 0 : id.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FileMetadata other = (FileMetadata) obj;
        if (getId() != other.getId())
            return false;
        if (getName() == null) {
            if (other.getName() != null)
                return false;
        } else if (!getName().equals(other.getName()))
            return false;
        if (getSize() != other.getSize())
            return false;
        if (getType() != other.getType())
            return false;
        if (getId() == null) {
            if (other.getId() != null)
                return false;
        } else if (!getId().equals(other.getId()))
            return false;
        return true;
    }
}
