//package tests.front.handlers;
//
//import handlers.FileInfoHandler;
//import java.util.UUID;
//import junit.framework.Assert;
//import models.FileInfo;
//import org.junit.Test;
//import tests.FrontIntegrationTestBase;
//import database.IDBContext;
//
//public class FileInfoIntegrationTest extends FrontIntegrationTestBase
//{
//	private FileInfoHandler fileInfoHandler;
//	private IDBContext dbContext;
//
//	@Override
//	public void setUp()
//	{
//		super.setUp();
//
//		clearDB();
//
//		dbContext = container.get(IDBContext.class);
//        fileInfoHandler = new FileInfoHandler(dbContext);
//	}
//
//	@Test
//	public void testWriteFileInfo()
//	{
//		FileInfo fileInfo1 = new FileInfo("id1", "name1", 1024, "type1");
//		FileInfo fileInfo2 = new FileInfo("id2", "name2", 2048, "type2");
//
//		dbContext.create(fileInfo1);
//		dbContext.create(fileInfo2);
//		FileInfo[] actuals = fileInfoHandler.find().toArray(new FileInfo[0]);
//
//		Assert.assertEquals(2, actuals.length);
//
//		FileInfo actualFileInfo1 = actuals[0];
//		Assert.assertEquals("id1", actualFileInfo1.getId());
//		Assert.assertEquals("name1", actualFileInfo1.getFileName());
//		Assert.assertEquals(1024, actualFileInfo1.getFileSize());
//		Assert.assertEquals("type1", actualFileInfo1.getFileType());
//
//		FileInfo actualFileInfo2 = actuals[1];
//		Assert.assertEquals("id2", actualFileInfo2.getId());
//		Assert.assertEquals("name2", actualFileInfo2.getFileName());
//		Assert.assertEquals(2048, actualFileInfo2.getFileSize());
//		Assert.assertEquals("type2", actualFileInfo2.getFileType());
//	}
//
//	@Test
//	public void testWriteViaMultiThreads()
//	{
//		Runnable testAction = new Runnable() {
//
//			@Override
//			public void run()
//			{
//				FileInfo fileInfo = new FileInfo(UUID.randomUUID().toString().substring(0, 20), UUID.randomUUID().toString().substring(0, 20), 1024,
//						"type");
//				dbContext.create(fileInfo);
//				fileInfoHandler.find();
//			}
//		};
//		runViaMultiThreads(testAction, 4, 100);
//
//		Assert.assertEquals(100, fileInfoHandler.find().size());
//	}
//}
