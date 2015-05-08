package services.blobs;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import play.Logger;
import storage.filesRepository.IFilesRepository;
import dataContracts.files.FileBatch;
import dataContracts.files.FileMetadata;
import dataContracts.files.IFileBatchFactory;

public class BlobService implements IBlobService
{
	private IFilesRepository filesRepository;
	private IFileBatchFactory fileBatchFactory;

	public BlobService(IFilesRepository filesRepository, IFileBatchFactory fileBatchFactory){
		this.filesRepository = filesRepository;
		this.fileBatchFactory = fileBatchFactory;
	}
	
	@Override
	public byte[] drain(String id)
	{
		try {
		    FileMetadata meta = filesRepository.getMeta(id);
			InputStream stream = filesRepository.getFileStream(meta);
			return IOUtils.toByteArray(stream);
		}
		catch(Exception e){
			String message = "Fail to read blob with id " + id;
			Logger.error(e, message);
			throw new BlobServiceException(message);
		}
	}

	private static final int batchSize = 100 * 1024;
	
	@Override
	public void write(String id, byte[] content)
	{
		if (content == null || content.length == 0) 
			throw new BlobServiceException("Unable to save empty file with id " + id);
		
		int blocksNumber = (int) Math.floor(content.length / batchSize);
		byte[] buffer = new byte[batchSize];
		
		try {
			for (int i = 0; i < blocksNumber; i++) {
				System.arraycopy(content, i * batchSize, buffer, 0, batchSize);
				FileBatch batch = fileBatchFactory.create(id, i, buffer);
				filesRepository.saveBatch(batch);
			}
			
			int tailLength = content.length % batchSize;
			if (tailLength != 0) {
				buffer = new byte[tailLength];
				System.arraycopy(content, blocksNumber * batchSize, buffer, 0, tailLength);
				FileBatch batch = fileBatchFactory.create(id, blocksNumber, buffer);
				filesRepository.saveBatch(batch);
			}
		}
		catch(Exception e) {
			String message = "Fail to write blob with id " + id;
			Logger.error(e, message);
			throw new BlobServiceException(message);
		}
	}
}