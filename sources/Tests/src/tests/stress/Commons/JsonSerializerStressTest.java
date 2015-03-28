package tests.stress.Commons;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

import serialization.ISerializer;
import serialization.Serializer;
import tests.stress.StressTestBase;
import tests.unit.BusinessObjects.serialization.dataContracts.ComplexDataContract;
import tests.unit.BusinessObjects.serialization.dataContracts.SubDataContract;
import tests.unit.BusinessObjects.serialization.dataContracts.SubSubDataContract;

public class JsonSerializerStressTest extends StressTestBase
{
    private ISerializer serializer;

    @Override
    public void setUp()
    {
        super.setUp();
        serializer = new Serializer();
    }

    @Test(timeout = 1500)
    @Ignore("This test should not find regularly.")
    public void testStress()
    {
        SubSubDataContract subSubDataContract = new SubSubDataContract();
        subSubDataContract.setBooleanField(true);
        SubDataContract subDataContract = new SubDataContract();
        subDataContract.setIntField(123);
        subDataContract.setSubSubDataContractField(subSubDataContract);
        ComplexDataContract complexDataContract = new ComplexDataContract();
        complexDataContract.setStringField("zzz");
        complexDataContract.setSubDataContractField(subDataContract);

        for (int i = 0; i < 100000; i++)
        {
            String s = serializer.stringify(complexDataContract);
            ComplexDataContract deserialized = serializer.deserialize(s, ComplexDataContract.class);
            assertEquals(complexDataContract.getStringField(), deserialized.getStringField());
            assertEquals(complexDataContract.getSubDataContractField().getIntField(),
                    deserialized.getSubDataContractField().getIntField());
            assertEquals(
                    complexDataContract.getSubDataContractField().getSubSubDataContractField().isBooleanField(),
                    deserialized.getSubDataContractField().getSubSubDataContractField().isBooleanField());
        }
    }

    @Test
    public void testMultithreading() throws Exception
    {
        RunThread runnable1 = new RunThread();
        RunThread runnable2 = new RunThread();
        RunThread runnable3 = new RunThread();

        Thread thread1 = new Thread(runnable1);
        thread1.start();
        Thread thread2 = new Thread(runnable2);
        thread2.start();
        Thread thread3 = new Thread(runnable3);
        thread3.start();

        started = true;

        thread1.join();
        thread2.join();
        thread3.join();

        if (lastException != null)
            throw lastException;
    }

    private volatile Exception lastException;
    private volatile boolean started;

    private class RunThread implements Runnable
    {
        @Override
        public void run()
        {
            while (!started)
            {
            }
            try
            {
                SubSubDataContract subSubDataContract = new SubSubDataContract();
                subSubDataContract.setBooleanField(true);
                SubDataContract subDataContract = new SubDataContract();
                subDataContract.setIntField(123);
                subDataContract.setSubSubDataContractField(subSubDataContract);
                ComplexDataContract complexDataContract = new ComplexDataContract();
                complexDataContract.setStringField("zzz");
                complexDataContract.setSubDataContractField(subDataContract);

                for (int i = 0; i < 10000; i++)
                {
                    String s = serializer.stringify(complexDataContract);
                    ComplexDataContract deserialized = serializer.deserialize(s, ComplexDataContract.class);
                    assertEquals(complexDataContract.getStringField(), deserialized.getStringField());
                    assertEquals(complexDataContract.getSubDataContractField().getIntField(),
                            deserialized.getSubDataContractField().getIntField());
                    assertEquals(
                            complexDataContract.getSubDataContractField().getSubSubDataContractField().isBooleanField(),
                            deserialized.getSubDataContractField().getSubSubDataContractField().isBooleanField());
                }
            } catch (Exception e)
            {
                lastException = e;
            }
        }
    }
}
