package tests.production;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.Random;

public class FileGeneratorTest extends ProductionTestBase {

    @Test
    public void generateRandomFiles() throws IOException {
        for (int i = 1; i <= 25; ++i) {
            File file = new File(String.format("D:\\overclocking\\Random\\random_%dMb.txt", i));
            if (file.exists())
                Assert.assertTrue(file.delete());
            Assert.assertTrue(file.createNewFile());
            Random random = new Random();
            char[] alphabet = new char[]{'A', 'G', 'C', 'T'};
            try (BufferedOutputStream w = new BufferedOutputStream(new FileOutputStream(file))) {
                int size = i * 1024 * 1024;
                for (int j = 0; j < size; ++j)
                    w.write(alphabet[random.nextInt(alphabet.length)]);
            }
        }
    }

    @Test
    public void generateBadCutFiles() throws IOException {
        for (int i = 1; i <= 25; ++i) {
            File file = new File(String.format("D:\\overclocking\\Bad_cut\\bad_%dMb.txt", i));
            if (file.exists())
                Assert.assertTrue(file.delete());
            Assert.assertTrue(file.createNewFile());
            try (BufferedOutputStream w = new BufferedOutputStream(new FileOutputStream(file))) {
                printBadFileFixedLength(w, i * 1024 * 1024);
            }
        }
    }

    @Test
    public void generateBadFiles() throws IOException {
        for (int i = 1; i <= 18; ++i) {
            File file = new File(String.format("D:\\overclocking\\Bad\\bad_%d.txt", i));
            if (file.exists())
                Assert.assertTrue(file.delete());
            Assert.assertTrue(file.createNewFile());
            try (BufferedOutputStream w = new BufferedOutputStream(new FileOutputStream(file))) {
                printBadFile(w, i);
            }
        }
    }

    private void printBadFileFixedLength(BufferedOutputStream w, int length) throws IOException {
        byte[] bytes;
        int p = 0;
        do {
            ++p;
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            printBadFile(stream, p);
            bytes = stream.toByteArray();
        } while (bytes.length < length);
        w.write(bytes, 0, length);
    }

    private void printBadFile(OutputStream writer, final int P) throws IOException {
        final int ALL = (1 << P);
        for (int i = 0; i < ALL; ++i) {
            printX(writer, i, P);
            writer.write((i == ALL - 1) ? 'E' : 'D');
        }
        printX(writer, 0, P);
        printX(writer, 1, P);
        for (int i = 2; i < ALL; ++i) {
            printX(writer, i - 2, P);
            printX(writer, i - 1, P);
            printX(writer, i, P);
        }
    }

    void printX(OutputStream writer, int index, int n) throws IOException {
        for (int i = n - 1; i >= 0; --i)
            writer.write((index & (1 << i)) != 0 ? 'B' : 'A');
        writer.write('C');
    }
}
