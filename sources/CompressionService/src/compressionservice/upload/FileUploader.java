package compressionservice.upload;

import java.io.IOException;
import java.util.Arrays;

import org.apache.log4j.Logger;

import storage.filesRepository.IFilesRepository;
import commons.files.IFile;
import commons.settings.ISettings;
import commons.settings.KnownKeys;
import dataContracts.ContentType;
import dataContracts.files.FileType;
import dataContracts.files.IFileBatchFactory;
import dataContracts.files.IFileMetadataFactory;

public class FileUploader implements IFileUploader {
    private static Logger logger = Logger.getLogger(FileUploader.class);
    
    private IFilesRepository filesRepository;
    private IFileMetadataFactory fileMetadataFactory;
    private IFileBatchFactory fileBatchFactory;
    private ISettings settings;

    public FileUploader(IFilesRepository filesRepository, 
                        IFileMetadataFactory fileMetadataFactory, 
                        IFileBatchFactory fileBatchFactory,
                        ISettings settings) {
        this.filesRepository = filesRepository;
        this.fileMetadataFactory = fileMetadataFactory;
        this.fileBatchFactory = fileBatchFactory;
        this.settings = settings;
    }
    
    @Override
    public void upload(IFile file, FileType fileType, ContentType contentType) {
        uploadFileBatchs(file);
        logger.info(String.format("File batches for file %s are loaded.", file.getFileName()));
        uploadMetaData(file, fileType, contentType);
        logger.info(String.format("Metadata for file %s are loaded.", file.getFileName()));
    }

    private void uploadFileBatchs(IFile file)
    {
        int fileBatchSize = settings.getInt(KnownKeys.MemoryMappedFileBatchSize);
        byte[] fileBatchBuffer = new byte[fileBatchSize];
        String fileId = file.getFileName();
        
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
                throw new UploadException(String.format("Fail to upload file %s.", file.getFileName()), e);
            }
            filesRepository.saveBatch(fileBatchFactory.create(fileId, batchNumber, Arrays.copyOfRange(fileBatchBuffer, 0, received)));
            offset += received;
            ++batchNumber;
        }
    }

    private void uploadMetaData(IFile file, FileType fileType, ContentType contentType)
    {
        final String fileName = file.getFileName();
        filesRepository.saveMeta(fileMetadataFactory.create(fileName, fileName, file.size(), fileType, contentType));
    }
}
