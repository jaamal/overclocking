package data.filters;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.apache.commons.io.IOUtils;

import caching.connections.ITemporaryFileFactory;
import dataContracts.files.FileType;

public class TextFileFilter implements ITypedFileFilter {

    private ITemporaryFileFactory temporaryFileFactory;

    public TextFileFilter(ITemporaryFileFactory temporaryFileFactory) {
        this.temporaryFileFactory = temporaryFileFactory;
    }
    
    @Override
    public FileType getFileType() {
        return FileType.Text;
    }

    @Override
    public Path apply(Reader reader) {
        Path result = temporaryFileFactory.getTemporaryFile().toPath();
        try (BufferedWriter writer = Files.newBufferedWriter(result, Charset.defaultCharset(), StandardOpenOption.WRITE))
        {
            String str = IOUtils.toString(reader);
            writer.write(str);
            writer.flush();
        }
        catch (IOException e)
        {
            throw new RuntimeException(String.format("Fail to write to temporary file with path %s", result.toString()), e);
        }
        return result;
    }

}