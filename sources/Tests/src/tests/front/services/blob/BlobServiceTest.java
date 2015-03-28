//package tests.front.services.blob;
//
//import commons.factories.IFileBatchFactory;
//import commons.fileStorage.FileBatch;
//import commons.fileStorage.IFilesRepository;
//import org.junit.Test;
//import services.blobs.BlobService;
//import services.blobs.BlobServiceException;
//import tests.UnitTestBase;
//
//import static org.easymock.EasyMock.*;
//
//public class BlobServiceTest extends UnitTestBase
//{
//	private IFilesRepository filesRepository;
//	private IFileBatchFactory fileBatchFactory;
//	private BlobService blobService;
//
//	@Override
//	public void setUp()
//	{
//		super.setUp();
//		
//		filesRepository = newMock(IFilesRepository.class);
//		fileBatchFactory = newMock(IFileBatchFactory.class);
//		blobService = new BlobService(filesRepository, fileBatchFactory);
//	}
//	
//	@Test(expected=BlobServiceException.class)
//	public void TestWriteWithEmptyStream() throws Exception {
//		String textId = "AADD";
//
//		replayAll();
//		
//		blobService.write(textId, new byte[]{});
//	}
//	
//	@Test
//	public void testWriteTinyContent() throws Exception {
//		String textId = "AADD";
//		FileBatch batch = new FileBatch(textId, 0, new byte[] { 1, 2, 3});
//
//		expect(fileBatchFactory.create(eq(textId), eq(0), aryEq(new byte[] { 1, 2, 3}))).andReturn(batch);
//		filesRepository.saveBatch(batch);
//		expectLastCall();
//		replayAll();
//		
//		blobService.write(textId, new byte[]{1, 2, 3});
//	}
//	
//	@Test
//	public void testWriteBigContent() throws Exception {
//		String textId = "AADD";
//		FileBatch batch1 = new FileBatch(textId, 0, new byte[] { 0, 0, 0});
//		FileBatch batch2 = new FileBatch(textId, 1, new byte[] { 0, 0, 0});
//		FileBatch batch3 = new FileBatch(textId, 2, new byte[] { 1, 2, 3});
//		
//		expect(fileBatchFactory.create(eq(textId), eq(0), aryEq(new byte[100 * 1024]))).andReturn(batch1);
//		filesRepository.saveBatch(batch1);
//		expectLastCall();
//		
//		expect(fileBatchFactory.create(eq(textId), eq(1), aryEq(new byte[100 * 1024]))).andReturn(batch2);
//		filesRepository.saveBatch(batch2);
//		expectLastCall();
//		
//		expect(fileBatchFactory.create(eq(textId), eq(2), aryEq(new byte[] { 1, 2, 3}))).andReturn(batch3);
//		filesRepository.saveBatch(batch3);
//		expectLastCall();
//		replayAll();
//		
//		int contentLength = 100 * 1024 * 2 + 3;
//		byte[] content = new byte[contentLength];
//		content[contentLength - 1] = 3;
//		content[contentLength - 2] = 2;
//		content[contentLength - 3] = 1;
//		blobService.write(textId, content);
//	}
//	
//}
