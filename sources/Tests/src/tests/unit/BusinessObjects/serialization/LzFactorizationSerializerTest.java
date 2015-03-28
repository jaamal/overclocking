package tests.unit.BusinessObjects.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;

import serialization.lzFactorizations.ILzFactorizationSerializer;
import serialization.lzFactorizations.LzFactorizationSerializer;
import serialization.primitives.IntArraySerializer;
import tests.TestBase;
import dataContracts.FactorDef;
import dataContracts.LZFactorDef;

public class LzFactorizationSerializerTest extends TestBase {
    @Test
    public void Test() throws IOException {
        doTest(abrakadabra, new LzFactorizationSerializer(new IntArraySerializer()));
    }

    private void doTest(LZFactorDef[] factors, ILzFactorizationSerializer serializer) throws IOException {
        checkSerializer(serializer, factors);
    }

    private int checkSerializer(ILzFactorizationSerializer lzFactorizationSerializer, LZFactorDef[] factors) throws IOException {
        int bytesCount;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            lzFactorizationSerializer.serialize(outputStream, factors);
            byte[] bytes = outputStream.toByteArray();
            bytesCount = bytes.length;
            printInfo(bytes);
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes)) {
                FactorDef[] actualFactors = lzFactorizationSerializer.deserialize(inputStream);
                checkEqualsProducts(factors, actualFactors);
            }
        }
        return bytesCount;
    }

    private void printInfo(byte[] bytes) {
        System.out.println(String.format("LZ-factorization has been serialized into %d bytes", bytes.length));
        if (bytes.length <= 100) {
            for (byte b : bytes)
                System.out.print(b + " ");
            System.out.println();
        }
    }

    private void checkEqualsProducts(LZFactorDef[] expectedFactors, FactorDef[] actualFactors) {
        Assert.assertEquals(expectedFactors.length, actualFactors.length);
        for (int i = 0; i < actualFactors.length; ++i) {
            Assert.assertEquals(expectedFactors[i].isTerminal, actualFactors[i].isTerminal);
            Assert.assertEquals(expectedFactors[i].symbol, actualFactors[i].symbol);
            Assert.assertEquals(expectedFactors[i].beginPosition, actualFactors[i].beginPosition);
            Assert.assertEquals(expectedFactors[i].length, actualFactors[i].length);
        }
    }

    public final static LZFactorDef[] abrakadabra = new LZFactorDef[]{
        createTerminal('a'),
        createTerminal('b'),
        createTerminal('r'),
        createNonTerminal(0, 1),
        createTerminal('k'),
        createNonTerminal(0, 1),
        createTerminal('d'),
        createNonTerminal(0, 4)
    };

    private static LZFactorDef createTerminal(char ch) {
        return new LZFactorDef(true, -1, -1, ch);
    }

    private static LZFactorDef createNonTerminal(long beginPosition, long length) {
        return new LZFactorDef(false, beginPosition, length, ' ');
    }
}
