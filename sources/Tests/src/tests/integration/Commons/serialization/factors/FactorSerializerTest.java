package tests.integration.Commons.serialization.factors;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;
import dataContracts.FactorDef;
import helpers.FactorizationScenarios;
import serialization.factors.IFactorSerializer;
import tests.integration.IntegrationTestBase;

public class FactorSerializerTest extends IntegrationTestBase
{
    @Test
    public void testFactorSerializer() throws IOException {
        FactorDef[] factorization = FactorizationScenarios.generate2(1000);
        
        IFactorSerializer factorSerializer = container.get(IFactorSerializer.class);
        byte[] bytes;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            factorSerializer.serialize(outputStream, factorization);
            bytes = outputStream.toByteArray();
        }
        
        FactorDef[] actuals;
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes)) {
            actuals = factorSerializer.deserialize(inputStream);
        }
        
        Assert.assertArrayEquals(factorization, actuals);
    }
}
