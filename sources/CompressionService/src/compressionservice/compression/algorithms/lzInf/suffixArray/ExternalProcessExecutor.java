package compressionservice.compression.algorithms.lzInf.suffixArray;

import java.io.InputStream;

public class ExternalProcessExecutor implements IExternalProcessExecutor
{
    @Override
    public int execute(String commandLine)
    {
        try {
            Process process = Runtime.getRuntime().exec(commandLine);
            InputStream inputStream = process.getInputStream();

            while (inputStream.available() > 0)
                inputStream.read();

            return process.waitFor();
        }
        catch (Exception e) {
            throw new ExternalProcessCrashesException(e);
        }
    }
}
