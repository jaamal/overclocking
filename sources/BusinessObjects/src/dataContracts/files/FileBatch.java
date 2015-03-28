package dataContracts.files;

public class FileBatch
{
    public String fileId;
    public int batchNumber;
    public byte[] batchData;

    public FileBatch(
            String fileId,
            int batchNumber,
            byte[] batchData)
    {
        this.fileId = fileId;
        this.batchNumber = batchNumber;
        this.batchData = batchData;
    }

    public int getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(int batchNumber) {
        this.batchNumber = batchNumber;
    }

    public byte[] getBatchData() {
        return batchData;
    }

    public void setBatchData(byte[] batchData) {
        this.batchData = batchData;
    }
}
