package compressionservice.compression.algorithms.lzInf;

import compressingCore.dataAccess.IReadableCharArray;
import dataContracts.DataFactoryType;

public interface ILZFactorizationProducer
{
    public void run(String textId, IReadableCharArray source, DataFactoryType dataFactoryType);
}
