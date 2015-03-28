package compressingCore.dataAccess;

import java.nio.file.Path;

import dataContracts.DataFactoryType;

public class DataFactory implements IDataFactory {

    private ITypedDataFactory[] typedDataFactories;

    public DataFactory(ITypedDataFactory[] typedDataFactories) {
        this.typedDataFactories = typedDataFactories;
    }
    
    @Override
    public IReadableCharArray readFile(DataFactoryType dataType, Path filePath) {
        return chooseDataFactoryOrDie(dataType).readFile(filePath);
    }

    @Override
    public ILongArray createLongArray(DataFactoryType dataType, long size) {
        return chooseDataFactoryOrDie(dataType).createLongArray(size);
    }
    
    private ITypedDataFactory chooseDataFactoryOrDie(DataFactoryType dataType) {
        for (ITypedDataFactory typedDataFactory : typedDataFactories) {
            if (typedDataFactory.getDataType() != dataType)
                continue;
            return typedDataFactory;
        }
        throw new UnknownDataFactoryTypeException(dataType);
    }

}
