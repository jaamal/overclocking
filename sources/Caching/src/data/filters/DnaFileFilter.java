package data.filters;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import caching.connections.ITemporaryFileFactory;
import data.filters.automatas.AutomataType;
import data.filters.automatas.IAutomata;
import data.filters.automatas.IAutomataFactory;
import dataContracts.files.FileType;

public class DnaFileFilter implements ITypedFileFilter {

    private IAutomataFactory automataFactory;
    private ITemporaryFileFactory temporaryFileFactory;

    public DnaFileFilter(IAutomataFactory automataFactory, ITemporaryFileFactory temporaryFileFactory) {
        this.automataFactory = automataFactory;
        this.temporaryFileFactory = temporaryFileFactory;
    }
    
    @Override
    public FileType getFileType() {
        return FileType.Dna;
    }

    @Override
    public Path apply(Reader reader) {
        IAutomata automata = automataFactory.createAutomata(AutomataType.DNA);
        Path result = temporaryFileFactory.getTemporaryFile().toPath();
        try (BufferedWriter writer = Files.newBufferedWriter(result, Charset.defaultCharset(), StandardOpenOption.WRITE))
        {
            int cp;
            while ((cp = reader.read()) != -1)
            {
                char chr = (char) cp;
                automata.move(chr);
                if (automata.isAcceptState())
                    writer.write(chr);
            }
            writer.flush();
        }
        catch (IOException e)
        {
            throw new RuntimeException(String.format("Fail to write to temporary file with path %s", result.toString()), e);
        }
        return result;
    }

}
