package tests.unit.BusinessObjects.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import junit.framework.Assert;

import org.junit.Test;

import serialization.lzFactorizations.ILzFactorizationSerializer;
import serialization.lzFactorizations.LzFactorizationSerializer;
import serialization.primitives.IntArraySerializer;
import tests.unit.UnitTestBase;
import dataContracts.FactorDef;
import dataContracts.LZFactorDef;

public class LzFactorizationSerializerTest extends UnitTestBase {
    @Test
    public void Test() {
        checkSerializer(new LzFactorizationSerializer(new IntArraySerializer()), abrakadabra);
    }

    private void checkSerializer(ILzFactorizationSerializer lzFactorizationSerializer, LZFactorDef[] factors) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            lzFactorizationSerializer.serialize(outputStream, factors);
            printInfo(outputStream.toByteArray());
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray())) {
                FactorDef[] actualFactors = lzFactorizationSerializer.deserialize(inputStream);
                checkEqualsProducts(factors, actualFactors);
            }
        }
        catch (Exception e) {
            throw new RuntimeException("Fail to check serializer.", e);
        }
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
        new LZFactorDef('a'),
        new LZFactorDef('b'),
        new LZFactorDef('r'),
        new LZFactorDef(0L, 1L),
        new LZFactorDef('k'),
        new LZFactorDef(0L, 1L),
        new LZFactorDef('d'),
        new LZFactorDef(0L, 4L)
    };
}
