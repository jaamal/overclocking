package tests.stress.BusinessObjects;

import helpers.TestHelpers;

import org.junit.Assert;
import org.junit.Test;

import tests.stress.StressTestBase;
import avlTree.nodes.AvlTreeNode;
import avlTree.nodes.AvlTreeNodeSerializer;

import commons.files.IFileManager;
import commons.settings.ISettings;

import data.charArray.CharSerializer;
import data.enumerableData.IItemSerializer;
import data.enumerableData.MemoryMappedFileEnumerableData;
import data.longArray.LongSerializer;

public class MemoryMappedFileStorageTest extends StressTestBase
{
    private IFileManager fileManager;

    @Override
    public void setUp() {
        super.setUp();
        
        fileManager = container.get(IFileManager.class);
    }

    @Test
    public void testWithIntArray()
    {
        IItemSerializer<Integer> serializer = new IntegerSerializer();
        MemoryMappedFileEnumerableData<Integer> storage = new MemoryMappedFileEnumerableData<Integer>(serializer, fileManager, container.get(ISettings.class));

        final int count = 1056;
        int[] array = TestHelpers.genIntArray(count);
        for (int i = 0; i < count; ++i)
            storage.save(i, array[i]);
        for (int i = 0; i < count; ++i)
            Assert.assertEquals(array[i], (int) storage.load(i));

        storage.close();
    }
    
    @Test
    public void testWithLongArray()
    {
        IItemSerializer<Long> serializer = new LongSerializer();
        MemoryMappedFileEnumerableData<Long> storage = new MemoryMappedFileEnumerableData<Long>(serializer, fileManager, container.get(ISettings.class));

        final int count = 1056;
        long[] array = TestHelpers.genLongArray(count);
        for (int i = 0; i < count; ++i)
            storage.save(i, array[i]);
        for (int i = 0; i < count; ++i)
            Assert.assertEquals(array[i], (long) storage.load(i));

        storage.close();
    }
    
    @Test
    public void testWithCharArray()
    {
        IItemSerializer<Character> serializer = new CharSerializer();
        MemoryMappedFileEnumerableData<Character> storage = new MemoryMappedFileEnumerableData<Character>(serializer, fileManager, container.get(ISettings.class));

        final int count = 100000;
        char[] array = TestHelpers.genCharArray(count);
        for (int i = 0; i < count; ++i)
            storage.save(i, array[i]);
        for (int i = 0; i < count; ++i)
            Assert.assertEquals(array[i], (char) storage.load(i));

        storage.close();
    }
    
    @Test
    public void testWithAvlNodeArray()
    {
        IItemSerializer<AvlTreeNode> serializer = new AvlTreeNodeSerializer();
        MemoryMappedFileEnumerableData<AvlTreeNode> storage = new MemoryMappedFileEnumerableData<AvlTreeNode>(serializer, fileManager, container.get(ISettings.class));

        final int count = 10000;
        AvlTreeNode[] array = TestHelpers.genAvlTreeNodeArray(count);
        for (int i = 0; i < count; ++i)
            storage.save(i, array[i]);
        for (int i = 0; i < count; ++i)
            Assert.assertEquals(array[i], storage.load(i));

        storage.close();
    }

}

