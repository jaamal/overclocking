package compressionservice.compression.algorithms.lzInf.suffixArray;

import java.io.InputStream;

public interface IExternalProcessExecutor
{
    InputStream execute(String command, String[] args);
}
