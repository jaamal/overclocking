package compressionservice.handlers.models;

import dataContracts.ContentType;
import dataContracts.files.FileMetadata;
import dataContracts.files.FileType;

public class FileModel
{
    public final String id;
    public final String name;
    public final long size;
    public final FileType type;
    public final ContentType contentType;

    private FileModel(String id, String name, long size, FileType type, ContentType contentType) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.type = type;
        this.contentType = contentType;
        
    }
    
    public static FileModel create(FileMetadata fileMetadata) {
        return new FileModel(fileMetadata.getId(), fileMetadata.getName(), fileMetadata.getSize(), 
                fileMetadata.getType(), fileMetadata.getContentType());
    }
}
