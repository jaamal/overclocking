package dataContracts.files;


public interface IFileBatchFactory
{
    FileBatch create(String fileId, int batchNumber, byte[] batchData);
}
