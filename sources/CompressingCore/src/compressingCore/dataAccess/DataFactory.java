package compressingCore.dataAccess;

import java.nio.file.Path;

import data.charArray.IReadableCharArray;
import data.longArray.ILongArray;
import dataContracts.DataFactoryType;

public class DataFactory implements IDataFactory {

    private ITypedDataFactory[] typedDataFactories;

    public DataFactory(ITypedDataFactory[] typedDataFactories) {
        this.typedDataFactories = typedDataFactories;
    }
    
    @Override
    public IReadableCharArray getCharArray(DataFactoryType dataType, Path filePath) {
        return chooseDataFactoryOrDie(dataType).getCharArray(filePath);
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

    @Override
    public IReadableCharArray createCharArray(char[] chars) {
        return chooseDataFactoryOrDie(DataFactoryType.memory).createCharArray(chars);
    }

}
