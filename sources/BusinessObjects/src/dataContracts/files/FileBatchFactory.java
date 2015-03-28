package dataContracts.files;

public class FileBatchFactory implements IFileBatchFactory
{
    public FileBatch create(String fileId, int batchNumber, byte[] batchData)
    {
        return new FileBatch(fileId, batchNumber, batchData);
    }
}
