/*package tests.production;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.junit.Test;
import tests.integration.IntegrationTestBase;

public class DNABankUploader extends IntegrationTestBase
{
    @Test
    public void donwnloadDNAFilesFromDataBankOfJapan() {
        try {
            FTPClient ftpClient = new FTPClient();
            ftpClient.connect("ftp://ftp.ddbj.nig.ac.jp/ddbj_database/wgs/");
            FTPFile[] ftpFiles = ftpClient.listFiles();
            int x = 1;
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
*/