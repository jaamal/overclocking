package tests.unit.BusinessObjects.serialization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import serialization.Serializer;
import tests.unit.UnitTestBase;
import tests.unit.BusinessObjects.serialization.immutableObjects.ClassWithArray;
import tests.unit.BusinessObjects.serialization.immutableObjects.ClassWithCleverConstructor;
import tests.unit.BusinessObjects.serialization.immutableObjects.ClassWithEnum;
import tests.unit.BusinessObjects.serialization.immutableObjects.ClassWithObjectArray;
import tests.unit.BusinessObjects.serialization.immutableObjects.ClassWithPrimitives;
import tests.unit.BusinessObjects.serialization.immutableObjects.ComplexType;
import tests.unit.BusinessObjects.serialization.immutableObjects.SimpleClass;
import tests.unit.BusinessObjects.serialization.immutableObjects.SimpleClassInheritor;
import tests.unit.BusinessObjects.serialization.immutableObjects.SubSubType;
import tests.unit.BusinessObjects.serialization.immutableObjects.SubType;
import tests.unit.BusinessObjects.serialization.immutableObjects.TestClass;
import tests.unit.BusinessObjects.serialization.immutableObjects.TestEnum;

public class JsonSerializerImmutableObjectsTest extends UnitTestBase
{
    private Serializer serializer;

    @Override
    public void setUp()
    {
        super.setUp();
        serializer = new Serializer();
    }

    private static void debug(String text)
    {
        System.out.println(text);
        System.out.flush();
    }

    @Test
    public void testSerializeDeserializeSimple()
    {
        TestClass testClass = new TestClass("testFieldValue");
        String serialized = serializer.stringify(testClass);
        debug(serialized);
        TestClass afterDeserialize = serializer.deserialize(serialized, TestClass.class);
        org.junit.Assert.assertEquals(testClass.stringField, afterDeserialize.stringField);
    }

    @Test
    public void testSerializeDeserializeSimpleNull()
    {
        TestClass testClass = new TestClass(null);
        String serialized = serializer.stringify(testClass);
        debug(serialized);
        TestClass afterDeserialize = serializer.deserialize(serialized, TestClass.class);
        org.junit.Assert.assertEquals(testClass.stringField, afterDeserialize.stringField);
    }

    @Test
    public void testSerializeTestClassDeserializeSimple()
    {
        TestClass testClass = new TestClass(null);
        String serialized = serializer.stringify(testClass);
        debug(serialized);
        SimpleClass afterDeserialize = serializer.deserialize(serialized, SimpleClass.class);
        org.junit.Assert.assertNull(afterDeserialize.firstField);
        org.junit.Assert.assertNull(afterDeserialize.secondField);
    }

    @Test
    public void testComplexType()
    {
        SubSubType subSubType = new SubSubType("zzz");
        SubType subType = new SubType(true, Long.MAX_VALUE, subSubType);
        ComplexType complexType = new ComplexType("qxx", Integer.MIN_VALUE, subType);
        String serialized = serializer.stringify(complexType);
        debug(serialized);

        ComplexType afterDeserialize = serializer.deserialize(serialized, ComplexType.class);

        org.junit.Assert.assertEquals(complexType.intValue, afterDeserialize.intValue);
        org.junit.Assert.assertEquals(complexType.stringValue, afterDeserialize.stringValue);

        org.junit.Assert.assertEquals(complexType.subType.booleanValue, afterDeserialize.subType.booleanValue);
        org.junit.Assert.assertEquals(complexType.subType.longValue, afterDeserialize.subType.longValue);

        org.junit.Assert.assertEquals(complexType.subType.subSubTypeValue.zzzString,
                afterDeserialize.subType.subSubTypeValue.zzzString);
    }

    @Test
    public void testComplexTypeSubTypeIsNull()
    {
        ComplexType complexType = new ComplexType("qxx", Integer.MIN_VALUE, null);
        String serialized = serializer.stringify(complexType);
        debug(serialized);

        ComplexType afterDeserialize = serializer.deserialize(serialized, ComplexType.class);

        org.junit.Assert.assertEquals(complexType.intValue, afterDeserialize.intValue);
        org.junit.Assert.assertEquals(complexType.stringValue, afterDeserialize.stringValue);

        org.junit.Assert.assertEquals(complexType.subType, afterDeserialize.subType);
    }

    @Test
    public void testArrays()
    {
        ClassWithArray classWithArray = new ClassWithArray(new int[]{1, 2, Integer.MIN_VALUE, Integer.MAX_VALUE, -1, 228});
        String serialized = serializer.stringify(classWithArray);
        debug(serialized);
        ClassWithArray afterDeserialize = serializer.deserialize(serialized, ClassWithArray.class);

        org.junit.Assert.assertArrayEquals(classWithArray.intArray, afterDeserialize.intArray);
    }

    @Test
    public void testArraysNull()
    {
        ClassWithArray classWithArray = new ClassWithArray(null);
        String serialized = serializer.stringify(classWithArray);
        debug(serialized);
        ClassWithArray afterDeserialize = serializer.deserialize(serialized, ClassWithArray.class);

        org.junit.Assert.assertArrayEquals(classWithArray.intArray, afterDeserialize.intArray);
    }

    @Test
    public void testObjectArray()
    {
        ClassWithObjectArray classWithObjectArray = new ClassWithObjectArray(
                new SimpleClass[]{new SimpleClass("1", "2"), new SimpleClass(null, "3"),
                        new SimpleClass("4", null), new SimpleClass(null, null)}
        );

        String serialized = serializer.stringify(classWithObjectArray);
        debug(serialized);
        ClassWithObjectArray afterDeserialize = serializer.deserialize(serialized, ClassWithObjectArray.class);

        org.junit.Assert.assertEquals(4, afterDeserialize.simpleClassArray.length);
        assertSimpleClassEquals(classWithObjectArray.simpleClassArray[0], afterDeserialize.simpleClassArray[0]);
        assertSimpleClassEquals(classWithObjectArray.simpleClassArray[1], afterDeserialize.simpleClassArray[1]);
        assertSimpleClassEquals(classWithObjectArray.simpleClassArray[2], afterDeserialize.simpleClassArray[2]);
        assertSimpleClassEquals(classWithObjectArray.simpleClassArray[3], afterDeserialize.simpleClassArray[3]);
    }

    @Test
    public void testObjectArrayWithNullElements()
    {
        ClassWithObjectArray classWithObjectArray = new ClassWithObjectArray(
                new SimpleClass[]{
                        new SimpleClass("1", "2"), null,
                        new SimpleClass("4", null), new SimpleClass(null, null)}
        );

        String serialized = serializer.stringify(classWithObjectArray);
        debug(serialized);
        ClassWithObjectArray afterDeserialize = serializer.deserialize(serialized, ClassWithObjectArray.class);

        org.junit.Assert.assertEquals(4, afterDeserialize.simpleClassArray.length);
        assertSimpleClassEquals(classWithObjectArray.simpleClassArray[0], afterDeserialize.simpleClassArray[0]);
        org.junit.Assert.assertNull(afterDeserialize.simpleClassArray[1]);
        assertSimpleClassEquals(classWithObjectArray.simpleClassArray[2], afterDeserialize.simpleClassArray[2]);
        assertSimpleClassEquals(classWithObjectArray.simpleClassArray[3], afterDeserialize.simpleClassArray[3]);
    }

    @Test
    public void testDeserializeSuperClass()
    {
        SimpleClassInheritor simpleClassInheritor = new SimpleClassInheritor("a", "b", "c");
        String serialized = serializer.stringify(simpleClassInheritor);
        debug(serialized);

        SimpleClass afterDeserialize = serializer.deserialize(serialized, SimpleClass.class);

        assertEquals(simpleClassInheritor.firstField, afterDeserialize.firstField);
        assertEquals(simpleClassInheritor.secondField, afterDeserialize.secondField);
    }

    @Test
    public void testDeserializeInheritor()
    {
        SimpleClass simpleClass = new SimpleClass("a", "b");
        String serialized = serializer.stringify(simpleClass);
        debug(serialized);

        SimpleClassInheritor afterDeserialize = serializer.deserialize(serialized, SimpleClassInheritor.class);

        assertEquals(simpleClass.firstField, afterDeserialize.firstField);
        assertEquals(simpleClass.secondField, afterDeserialize.secondField);
        assertNull(afterDeserialize.element);
    }

    @Test
    public void testDeserializeEmptyClassWithPrimitives()
    {
        String serialized = "{}";
        ClassWithPrimitives afterDeserialize = serializer.deserialize(serialized, ClassWithPrimitives.class);
        assertEquals(0, afterDeserialize.intField);
        assertEquals(0L, afterDeserialize.longField);
        assertEquals((byte) 0, afterDeserialize.byteValue);
        assertEquals((char) 0, afterDeserialize.charField);
        assertEquals((short) 0, afterDeserialize.shortValue);
        assertEquals(false, afterDeserialize.booleanValue);
        assertEquals(null, afterDeserialize.stringValue);
    }

    @Test
    public void testDeserializeClassWithCleverConstructor()
    {
        ClassWithCleverConstructor classWithCleverConstructor = new ClassWithCleverConstructor();
        String serialized = serializer.stringify(classWithCleverConstructor);
        debug(serialized);
        ClassWithCleverConstructor afterDeserialize = serializer.deserialize(serialized, ClassWithCleverConstructor.class);
        assertEquals(25, afterDeserialize.qxx);
        assertEquals("zzz", afterDeserialize.zzz);
    }

    @Test
    public void testDeserializeComplexTypeWhenEmpty()
    {
        ComplexType complexType = serializer.deserialize("{}", ComplexType.class);
        assertNull(complexType.stringValue);
        assertNull(complexType.subType);
        assertEquals(0, complexType.intValue);
    }

    @Test
    public void testDeserializeWhenEnumEmpty()
    {
        ClassWithEnum classWithEnum = serializer.deserialize("{}", ClassWithEnum.class);
        assertEquals(null, classWithEnum.enumField);
    }

    @Test
    public void testSerializeDeserializeWhenValue1()
    {
        ClassWithEnum classWithEnum = new ClassWithEnum(TestEnum.value1);
        String serialized = serializer.stringify(classWithEnum);
        ClassWithEnum afterDeserialize = serializer.deserialize(serialized, ClassWithEnum.class);
        assertEquals(TestEnum.value1, afterDeserialize.enumField);
    }

    @Test
    public void testSerializeDeserializeWhenValue2()
    {
        ClassWithEnum classWithEnum = new ClassWithEnum(TestEnum.value2);
        String serialized = serializer.stringify(classWithEnum);
        ClassWithEnum afterDeserialize = serializer.deserialize(serialized, ClassWithEnum.class);
        assertEquals(TestEnum.value2, afterDeserialize.enumField);
    }

    private static void assertSimpleClassEquals(SimpleClass expected, SimpleClass actual)
    {
        org.junit.Assert.assertEquals(expected.firstField, actual.firstField);
        org.junit.Assert.assertEquals(expected.secondField, actual.secondField);
    }
}
