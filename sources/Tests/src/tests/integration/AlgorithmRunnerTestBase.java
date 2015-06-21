package tests.integration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.io.IOUtils;

import storage.KeySpaces;
import storage.cassandraClient.ISchemeInitializer;
import data.filters.IFileFilter;
import dataContracts.files.FileMetadata;

public class AlgorithmRunnerTestBase extends StorageTestBase {

    private IFileFilter fileFilter;

    @Override
    public void setUp() {
        super.setUp();

        container.get(ISchemeInitializer.class).setUpCluster();
        container.get(ISchemeInitializer.class).truncateKeyspace(KeySpaces.statistics.name());
        container.get(ISchemeInitializer.class).truncateKeyspace(KeySpaces.files.name());
        container.get(ISchemeInitializer.class).truncateKeyspace(KeySpaces.factorizations.name());
        container.get(ISchemeInitializer.class).truncateKeyspace(KeySpaces.slps.name());
        container.get(ISchemeInitializer.class).setUpCluster();

        fileFilter = container.get(IFileFilter.class);
    }

    protected String readFileText(FileMetadata fileMetadata) {
        try (InputStream stream = filesRepository.getFileStream(fileMetadata);
            Reader reader = new InputStreamReader(stream)) {
            Path pathToFile = fileFilter.apply(fileMetadata.getType(), reader);
            try (BufferedReader localFileReader = Files.newBufferedReader(pathToFile, Charset.defaultCharset())) {
                return IOUtils.toString(localFileReader);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
