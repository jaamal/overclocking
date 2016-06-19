package tests.production;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.junit.Test;
import tests.integration.IntegrationTestBase;

public class DNADownloader extends IntegrationTestBase
{
    private final static String ddbjFtpAddress = "ftp.ddbj.nig.ac.jp";
    private final static String wgsFtpPath = "/ddbj_database/wgs";

    @Test
    public void donwnloadDNAFilesFromDataBankOfJapan() throws IOException {
        FTPClient ftpClient = new FTPClient();
        WgsFileFilter filter = new WgsFileFilter(100 * 1024 * 1024, 20, 3);
        try {
            ftpClient.connect(ddbjFtpAddress);
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())){
                ftpClient.disconnect();
                throw new RuntimeException("Fail to connect to ftp server.");
            }
            
            ftpClient.login("anonymous", "");
            ftpClient.setFileTransferMode(FTPClient.BLOCK_TRANSFER_MODE);
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            
            FTPFile[] files = ftpClient.listFiles(wgsFtpPath);
            ArrayList<FTPFile> filteredFiles = new ArrayList<FTPFile>();
            for (FTPFile file : files) {
                if (filter.Apply(file)){
                    filteredFiles.add(file);
                }
            }
            
            HashSet<String> exceptedFileNames = new HashSet<>(Arrays.asList("AAAC.gz","AAAD.gz","AAAE.gz","AAAF.gz","AAAG.gz","AABL.gz","AABS.gz","AABU.gz","AABX.gz","AACB.gz","AACC.gz","AACD.gz","AACG.gz","AACI.gz","AACM.gz","AACO.gz","AACQ.gz","AADE.gz","AADG.gz","AADS.gz","AAEC.gz","AAEU.gz","AAEY.gz","AAFS.gz","AAFX.gz","AAGD.gz","AAGF.gz","AAGH.gz","AAHC.gz","AAHK.gz","AAIZ.gz","AAJJ.gz","AAKO.gz","AANI.gz","AAPP.gz","AAPQ.gz","AAPT.gz","AAPU.gz","AAQA.gz","AAQB.gz","AAQM.gz","AAQX.gz","AAQY.gz","AASR.gz","AASS.gz","AASU.gz","AASW.gz","AATU.gz","AAUQ.gz","AAWC.gz","AAXH.gz","AAXN.gz"));
            for (FTPFile ftpFile : filteredFiles) {
                if (exceptedFileNames.contains(ftpFile.getName()))
                    break;
                
                System.out.println(String.format("Start downloading the file %s of size %s.", ftpFile.getName(), ftpFile.getSize()));
                downloadFile(ftpClient, wgsFtpPath, FilesConsts.wgsFolderPath, ftpFile.getName());
                System.out.println(String.format("Finish downloading the file %s.", ftpFile.getName()));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if(ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch(IOException e) { }
            }
        }
    }
    private void downloadFile(FTPClient ftpClient, String fromDirPath, String toDirPath, String fileName) {
        try {
            File localFile = new File(String.format("%s/%s", toDirPath, fileName));
            if (localFile.exists()) {
                localFile.delete();
            }
            FileOutputStream outputStream = new FileOutputStream(new File(String.format("%s/%s", toDirPath, fileName)));
            boolean isDownloaded = ftpClient.retrieveFile(String.format("%s/%s", fromDirPath, fileName), outputStream);
            outputStream.flush();
            outputStream.close();
            if (!isDownloaded)
                throw new RuntimeException();
        }
        catch (Exception e) {
            throw new RuntimeException(String.format("Fail to download file %s.", fileName), e);
        }
    }
    
    private class WgsFileFilter {
        private final static String wgsExtensions = ".gz";
        private int maxFileSizeInBytes;
        private int maxFilesPerPartition;
        private int partitionSizeInBytes;
        private HashMap<Integer, Integer> partitionsMap;

        public WgsFileFilter(int maxFileSizeInBytes, int partitionsNumber, int maxFilesPerPartition) {
            this.maxFileSizeInBytes = maxFileSizeInBytes;
            this.maxFilesPerPartition = maxFilesPerPartition;
            this.partitionSizeInBytes = maxFileSizeInBytes / partitionsNumber;
            
            this.partitionsMap = new HashMap<Integer, Integer>();
            for (int i = 1; i < partitionsNumber + 1; i++)
                partitionsMap.put(i, 0);
        }
        
        public boolean Apply(FTPFile file) {
            if (file.getName().length() > 7 || !file.getName().endsWith(wgsExtensions))
                return false;
            if (file.getSize() > maxFileSizeInBytes)
                return false;
            
            int partitionNumber = ((int) file.getSize() / partitionSizeInBytes) + 1;
            if (partitionsMap.get(partitionNumber) > maxFilesPerPartition)
                return false;
            
            partitionsMap.put(partitionNumber, partitionsMap.get(partitionNumber) + 1);
            return true;
        }
    }
}
