package compressionservice.compression.algorithms.lzInf.suffixArray;

import java.io.InputStream;

import org.apache.commons.lang.ArrayUtils;

public class ExternalProcessExecutor implements IExternalProcessExecutor
{
    @Override
    public InputStream execute(String command, String[] args) {
        try {
            String[] cmdArray = (String[]) ArrayUtils.addAll(new String[] {command}, args);
            Process process = Runtime.getRuntime().exec(cmdArray);
            
            int exitValue = process.waitFor();
            if (exitValue == -1)
                throw new RuntimeException(String.format("fail to execute command %s, exit code -1", command));
            
            return process.getInputStream();
        } catch (Exception e) {
            throw new RuntimeException(String.format("fail to execute command %s", command), e);
        }
    }
}
