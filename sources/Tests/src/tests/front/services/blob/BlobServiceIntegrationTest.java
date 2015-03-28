//package tests.front.services.blob;
//
//import commons.businessObjectsStorage.ColumnFamilyRegistry;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import services.blobs.IBlobService;
//import storage.cassandraClient.ICassandraCluster;
//import tests.FrontIntegrationTestBase;
//import tests.commons.CassandraClusterInitializerForTests;
//
//public class BlobServiceIntegrationTest extends FrontIntegrationTestBase
//{
//	private IBlobService blobService;
//
//	@Override
//	@Before
//	public void setUp() {
//		super.setUp();
//        CassandraClusterInitializerForTests cassandraClusterInitializer = new CassandraClusterInitializerForTests(container.get(ICassandraCluster.class), ColumnFamilyRegistry.getInstance());
//        cassandraClusterInitializer.init();
//        cassandraClusterInitializer.clean();
//		
//		blobService = container.get(IBlobService.class);
//
//	}
//		
//	@Test
//	public void testWriteAndDrainBigFile() {
//		byte[] body = new byte[1024 * 1024];
//		String textId = "testId";
//		blobService.write(textId, body);
//		byte[] actuals = blobService.drain(textId);
//		Assert.assertArrayEquals(body, actuals);
//	}
//}
