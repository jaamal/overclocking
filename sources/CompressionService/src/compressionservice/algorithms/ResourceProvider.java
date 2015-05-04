package compressionservice.algorithms;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Path;
import java.util.List;

import org.apache.log4j.Logger;

import storage.factorsRepository.IFactorsRepository;
import storage.factorsRepository.IFactorsRepositoryFactory;
import storage.filesRepository.IFilesRepository;

import compressingCore.dataAccess.IDataFactory;
import compressingCore.dataAccess.IReadableCharArray;
import compressingCore.dataFiltering.IFileFilter;

import dataContracts.DataFactoryType;
import dataContracts.FactorDef;
import dataContracts.files.FileMetadata;

public class ResourceProvider implements IResourceProvider {
    private static Logger logger = Logger.getLogger(ResourceProvider.class);
    
    private IFactorsRepository factorsRepository;
    private IDataFactory dataFactory;
    private IFilesRepository filesRepository;
    private IFileFilter fileFilter;
    
    public ResourceProvider(
            IFactorsRepositoryFactory factorsRepositoryFactory, 
            IDataFactory dataFactory,
            IFilesRepository filesRepository, 
            IFileFilter fileFilter) {
        this.dataFactory = dataFactory;
        this.filesRepository = filesRepository;
        this.fileFilter = fileFilter;
        factorsRepository = factorsRepositoryFactory.getLZRepository();
    }
    
    @Override
    public FactorDef[] getFactorization(String sourceId) {
        logger.info("Start read factors from factorization with id = " + sourceId);
        List<FactorDef> lzFactors = factorsRepository.readItems(sourceId);
        logger.info("End read factors. Factors count = " + lzFactors.size());
        return lzFactors.toArray(new FactorDef[0]);
    }

    @Override
    public IReadableCharArray getText(String sourceId, DataFactoryType dataFactoryType) {   
        logger.info(String.format("Start filtering of file %s...", sourceId));
        FileMetadata fileMetadata = filesRepository.getMeta(sourceId);
        try (InputStream stream = filesRepository.getFileStream(fileMetadata);
             Reader reader = new InputStreamReader(stream))
        {
            Path pathToFile = fileFilter.apply(fileMetadata.getType(), reader);
            IReadableCharArray result = dataFactory.readFile(dataFactoryType, pathToFile);
            logger.info(String.format("Filtration of file %s is finished. File name = %s", sourceId, pathToFile));
            return result;
        } catch (IOException e) {
            logger.error(String.format("Filtration of file %s is failed.", sourceId), e);
            throw new RuntimeException(String.format("Filtration of file %s is failed.", sourceId), e);
        }
    }

}
