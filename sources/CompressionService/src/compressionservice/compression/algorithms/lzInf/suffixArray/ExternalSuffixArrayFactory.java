package compressionservice.compression.algorithms.lzInf.suffixArray;

import commons.files.IFileManager;
import commons.settings.ISettings;
import compressingCore.dataAccess.IDataFactory;
import compressingCore.dataAccess.IReadableCharArray;

import dataContracts.DataFactoryType;

public class ExternalSuffixArrayFactory implements ISuffixArrayFactory
{
    private IFileManager fileManager;
    private IExternalProcessExecutor externalProcessExecutor;
    private ISettings settings;
    private IDataFactory dataFactory;

    public ExternalSuffixArrayFactory(
            IDataFactory dataFactory,
            IFileManager fileManager,
            IExternalProcessExecutor externalProcessExecutor,
            ISettings settings)
    {
        this.dataFactory = dataFactory;
        this.fileManager = fileManager;
        this.externalProcessExecutor = externalProcessExecutor;
        this.settings = settings;
    }

    @Override
    public ISuffixArray create(DataFactoryType dataFactoryType, IReadableCharArray source)
    {
        return new ExternalSuffixArray(source, dataFactory, fileManager, externalProcessExecutor, settings, dataFactoryType);
    }
}
