package compressingCore.dataAccess;

import dataContracts.DataFactoryType;

public class UnknownDataFactoryTypeException extends RuntimeException
{
	private static final long serialVersionUID = -2170304357282820833L;

	public UnknownDataFactoryTypeException(DataFactoryType dataFactoryType)
    {
        super(dataFactoryType.name());
    }
}
