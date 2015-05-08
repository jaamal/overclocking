package tests.unit.BusinessObjects.serialization;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;

import org.junit.Test;

import serialization.ISerializer;
import serialization.Serializer;
import tests.unit.UnitTestBase;
import tests.unit.BusinessObjects.serialization.dataContracts.ComplexDataContract;
import tests.unit.BusinessObjects.serialization.dataContracts.DataContractWithArrays;
import tests.unit.BusinessObjects.serialization.dataContracts.DataContractWithFinalField;
import tests.unit.BusinessObjects.serialization.dataContracts.ISomeInterface;
import tests.unit.BusinessObjects.serialization.dataContracts.PrimitiveTypesDataContract;
import tests.unit.BusinessObjects.serialization.dataContracts.SimpleDataContract;
import tests.unit.BusinessObjects.serialization.dataContracts.SomeInterfaceImplementation;
import tests.unit.BusinessObjects.serialization.dataContracts.SubDataContract;
import tests.unit.BusinessObjects.serialization.dataContracts.SubSubDataContract;
import dataContracts.BusinessObject;

public class JsonSerializerTest extends UnitTestBase {
    
    private ISerializer serializer = new Serializer();

    @Test
    public void testSimpleDataContract()
    {
        SimpleDataContract simpleDataContract = new SimpleDataContract();
        simpleDataContract.setStringField("zzz");

        String serialized = serializer.stringify(simpleDataContract);
        System.out.println(serialized);

        SimpleDataContract afterDeserialize = serializer.deserialize(serialized, SimpleDataContract.class);
        assertEquals("zzz", afterDeserialize.getStringField());
    }
    
    @Test
    public void testSimpleDataContractNull()
    {
        SimpleDataContract simpleDataContract = new SimpleDataContract();

        String serialized = serializer.stringify(simpleDataContract);
        System.out.println(serialized);

        SimpleDataContract afterDeserialize = serializer.deserialize(serialized, SimpleDataContract.class);
        assertEquals(simpleDataContract.getStringField(), afterDeserialize.getStringField());
    }

    @Test
    public void testComplexDataContract()
    {
        SubSubDataContract subSubDataContract = new SubSubDataContract();
        subSubDataContract.setBooleanField(true);

        SubDataContract subDataContract = new SubDataContract();
        subDataContract.setIntField(123);
        subDataContract.setSubSubDataContractField(subSubDataContract);

        ComplexDataContract complexDataContract = new ComplexDataContract();
        complexDataContract.setStringField("zzz");
        complexDataContract.setSubDataContractField(subDataContract);

        String serialized = serializer.stringify(complexDataContract);
        System.out.println(serialized);

        ComplexDataContract afterDeserialize = serializer.deserialize(serialized, ComplexDataContract.class);
        assertEquals(complexDataContract.getStringField(), afterDeserialize.getStringField());
        assertEquals(complexDataContract.getSubDataContractField().getIntField(),
                afterDeserialize.getSubDataContractField().getIntField());
        assertEquals(complexDataContract.getSubDataContractField().getSubSubDataContractField().isBooleanField(),
                afterDeserialize.getSubDataContractField().getSubSubDataContractField().isBooleanField());
    }

    @Test
    public void testComplexDataContractNull()
    {
        SubDataContract subDataContract = new SubDataContract();
        ComplexDataContract complexDataContract = new ComplexDataContract();
        complexDataContract.setStringField("zzz");
        complexDataContract.setSubDataContractField(subDataContract);

        String serialized = serializer.stringify(complexDataContract);
        System.out.println(serialized);

        ComplexDataContract afterDeserialize = serializer.deserialize(serialized, ComplexDataContract.class);
        assertEquals(complexDataContract.getStringField(), afterDeserialize.getStringField());
        assertEquals(0, afterDeserialize.getSubDataContractField().getIntField());
        assertNull(afterDeserialize.getSubDataContractField().getSubSubDataContractField());
    }

    @Test
    public void testDeserializeEmpty()
    {
        ComplexDataContract emptyDeserialized = serializer.deserialize("{}", ComplexDataContract.class);
        assertNull(emptyDeserialized.getStringField());
        assertNull(emptyDeserialized.getSubDataContractField());
    }

    @Test
    public void testDeserializeEmptyPrimitiveTypes()
    {
        PrimitiveTypesDataContract emptyDeserialized = serializer.deserialize("{}", PrimitiveTypesDataContract.class);
        assertEquals(0, emptyDeserialized.getIntField());
        assertEquals(0, emptyDeserialized.getLongField());
        assertEquals(0, emptyDeserialized.getShortField());
        assertEquals(0, emptyDeserialized.getByteField());
        assertEquals(0, emptyDeserialized.getCharField());
        assertEquals(false, emptyDeserialized.isBooleanField());
    }

    @Test
    public void testDataContractWithFinalField()
    {
        DataContractWithFinalField dataContractWithFinalField = new DataContractWithFinalField(123);
        dataContractWithFinalField.setStringField("zzz");
        String serialized = serializer.stringify(dataContractWithFinalField);
        System.out.println(serialized);

        DataContractWithFinalField afterDeserialize = serializer.deserialize(serialized, DataContractWithFinalField.class);

        assertEquals(dataContractWithFinalField.intField, afterDeserialize.intField);
        assertEquals(dataContractWithFinalField.getStringField(), afterDeserialize.getStringField());
    }

    @Test
    public void testDataContractWithArrays()
    {
        DataContractWithArrays dataContractWithArrays = new DataContractWithArrays();
        dataContractWithArrays.setByteArray(new byte[]{1, 2, 3, 4, 5, 6});
        dataContractWithArrays.setIntArray(new int[]{10, 20, 30, 40});
        SimpleDataContract[] simpleDataContractArray = new SimpleDataContract[3];
        simpleDataContractArray[0] = new SimpleDataContract();
        simpleDataContractArray[0].setStringField("1");

        simpleDataContractArray[2] = new SimpleDataContract();
        simpleDataContractArray[2].setStringField("3");
        dataContractWithArrays.setSimpleDataContract(simpleDataContractArray);

        String serialized = serializer.stringify(dataContractWithArrays);
        System.out.println(serialized);

        DataContractWithArrays afterDeserialize = serializer.deserialize(serialized, DataContractWithArrays.class);
        assertArrayEquals(dataContractWithArrays.getByteArray(), afterDeserialize.getByteArray());
        assertArrayEquals(dataContractWithArrays.getIntArray(), afterDeserialize.getIntArray());
        assertArrayEquals(dataContractWithArrays.getSimpleDataContract(), afterDeserialize.getSimpleDataContract());
    }

    @Test
    public void testClassImplementsInterface()
    {
        SomeInterfaceImplementation interfaceImplementation = new SomeInterfaceImplementation();
        interfaceImplementation.setIntField(123);
        interfaceImplementation.setStringField("zzz");

        ISomeInterface someInterface = interfaceImplementation;

        String serialized = serializer.stringify(someInterface);
        System.out.println(serialized);

        SomeInterfaceImplementation afterDeserialize = serializer.deserialize(serialized, SomeInterfaceImplementation.class);
        assertEquals(interfaceImplementation.getIntField(), afterDeserialize.getIntField());
        assertEquals(interfaceImplementation.getStringField(), afterDeserialize.getStringField());
    }
    
    @Test
    public void testSerializeHashmap() {
        HashMap<String, String> map = new HashMap<>();
        map.put("key1", "val1");
        map.put("key2", "val2");
        
        String serialized = serializer.stringify(map);
        System.out.println(serialized);
        
        HashMap<String, String> actuals = serializer.deserialize(serialized, HashMap.class);
        assertEquals(2, actuals.size());
        assertEquals("val1", actuals.get("key1"));
        assertEquals("val2", actuals.get("key2"));
    }
    
    @Test
    public void testSerializeBaseClass() {
        SimpleDataContract contract = new SimpleDataContract();
        contract.setId("testId");
        contract.setLastPersistMillis(10);
        contract.setStringField("something");
        
        String serialized = serializer.stringify(contract, BusinessObject.class);
        SimpleDataContract actuals = serializer.deserialize(serialized, SimpleDataContract.class);
        assertEquals(contract.getId(), actuals.getId());
        assertEquals(contract.getLastPersistMillis(), actuals.getLastPersistMillis());
        assertEquals(null, actuals.getStringField());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSerializeWithInvalidBaseClass() {
        serializer.stringify(new SimpleDataContract(), SubDataContract.class);
    }
    
    @Test
    public void testSerializeNull() {
        String serialized = serializer.stringify(null);
        Object actual = serializer.deserialize(serialized, BusinessObject.class);
        assertEquals(null, actual);
        
        serialized = serializer.stringify(null, BusinessObject.class);
        actual = serializer.deserialize(serialized, BusinessObject.class);
        assertEquals(null, actual);
    }
}
