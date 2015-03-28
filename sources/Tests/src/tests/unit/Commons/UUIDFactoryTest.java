package tests.unit.Commons;

import java.util.HashSet;
import java.util.UUID;

import junit.framework.Assert;

import org.junit.Test;

import tests.unit.UnitTestBase;
import dataContracts.IDFactory;
import dataContracts.IIDFactory;

public class UUIDFactoryTest extends UnitTestBase
{
	private IIDFactory uuidFactory;

	@Override
	public void setUp()
	{
		super.setUp();
		uuidFactory = new IDFactory();
	}

	@Test
	public void testGetDeterministicUUIDSimple()
	{
		Assert.assertEquals(uuidFactory.getDeterministicID("string"), uuidFactory.getDeterministicID("string"));
		Assert.assertTrue(!uuidFactory.getDeterministicID("string1").equals(uuidFactory.getDeterministicID("string2")));
	}

	@Test
	public void testGetDeterministicUUIDHard()
	{
		HashSet<String> uuids = new HashSet<String>();
		final int iterations = 1000000;
		for (int i = 0; i < iterations; i++)
		{
			String str = UUID.randomUUID().toString() + "@" + UUID.randomUUID().toString() + "@" + UUID.randomUUID().toString();
			uuids.add(uuidFactory.getDeterministicID(str).toString());
		}
		Assert.assertEquals(iterations, uuids.size());
	}

	@Test
	public void testHalfUUIDCollision()
	{
		HashSet<String> halfs = new HashSet<String>();
		final int iterations = 1000000;
		for (int i = 0; i < iterations; i++)
		{
			String str = uuidFactory.getDeterministicID(UUID.randomUUID().toString()).toString();
			halfs.add(str.substring(0, 16));
			halfs.add(str.substring(16, 32));
		}
		Assert.assertEquals(iterations * 2, halfs.size());
	}
}
