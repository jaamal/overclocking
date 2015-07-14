package data.filters;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.apache.commons.io.IOUtils;

import commons.files.IFileManager;

import dataContracts.files.FileType;

public class TextFileFilter implements ITypedFileFilter {

    private IFileManager fileManager;

    public TextFileFilter(IFileManager fileManager) {
        this.fileManager = fileManager;
    }
    
    @Override
    public FileType getFileType() {
        return FileType.Text;
    }

    @Override
    public Path apply(Reader reader) {
        Path result = fileManager.createTempFile2().getPath();
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
