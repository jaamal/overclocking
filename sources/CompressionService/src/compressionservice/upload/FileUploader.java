package compressionservice.upload;

import java.io.IOException;
import java.util.Arrays;

import org.apache.log4j.Logger;

import storage.filesRepository.IFilesRepository;

import commons.files.IFile;
import commons.settings.ISettings;
import commons.settings.KnownKeys;

import dataContracts.ContentType;
import dataContracts.IIDFactory;
import dataContracts.files.FileType;
import dataContracts.files.IFileBatchFactory;
import dataContracts.files.IFileMetadataFactory;

public class FileUploader implements IFileUploader {
    private static Logger logger = Logger.getLogger(FileUploader.class);
    
    private IFilesRepository filesRepository;
    private IFileMetadataFactory fileMetadataFactory;
    private IFileBatchFactory fileBatchFactory;
    private ISettings settings;
    private IIDFactory idFactory;

    public FileUploader(IFilesRepository filesRepository, 
                        IFileMetadataFactory fileMetadataFactory, 
                        IFileBatchFactory fileBatchFactory,
                        ISettings settings,
                        IIDFactory idFactory) {
        this.filesRepository = filesRepository;
        this.fileMetadataFactory = fileMetadataFactory;
        this.fileBatchFactory = fileBatchFactory;
        this.settings = settings;
        this.idFactory = idFactory;
    }
    
    @Override
    public String upload(IFile file, FileType fileType, ContentType contentType) {
        uploadFileBatchs(file);
        logger.info(String.format("File batches for file %s are loaded.", file.getPath()));
        String fileId = uploadMetaData(file, fileType, contentType);
        logger.info(String.format("Metadata for file %s are loaded.", file.getPath()));
        return fileId;
    }

    private void uploadFileBatchs(IFile file)
    {
        int fileBatchSize = settings.getInt(KnownKeys.MemoryMappedFileBatchSize);
        byte[] fileBatchBuffer = new byte[fileBatchSize];
        String fileId = file.getPath();
        
        long offset = 0;
        int batchNumber = 0;
        long fileSize = file.size();
        while (offset < fileSize)
        {
            int received;
            try {
                received = file.read(offset, fileBatchBuffer);
            } 
            catch (IOException e) {
                throw new UploadException(String.format("Fail to upload file %s.", file.getPath()), e);
            }
            filesRepository.saveBatch(fileBatchFactory.create(fileId, batchNumber, Arrays.copyOfRange(fileBatchBuffer, 0, received)));
            offset += received;
            ++batchNumber;
        }
    }

    private String uploadMetaData(IFile file, FileType fileType, ContentType contentType)
    {
        String fileId = idFactory.create().toString();
        filesRepository.saveMeta(fileMetadataFactory.create(fileId, file.getName(), file.size(), fileType, contentType));
        return fileId;
    }
}
