//package tests.front.handlers;
//
//import handlers.IFeedbackHandler;
//import junit.framework.Assert;
//import models.FeedbackMessage;
//import org.junit.Test;
//import tests.FrontIntegrationTestBase;
//import database.IDBContext;
//
//public class FeedbackHandlerTest extends FrontIntegrationTestBase
//{
//	private IFeedbackHandler feedbackHandler;
//	private IDBContext dbContext;
//
//	@Override
//	public void setUp()
//	{
//		super.setUp();
//
//		clearDB();
//
//		feedbackHandler = container.get(IFeedbackHandler.class);
//		dbContext = container.get(IDBContext.class);
//	}
//
//	@Test
//	public void testWriteAndSelect()
//	{
//		FeedbackMessage message = new FeedbackMessage("zzz", "hello", "yyy");
//
//		dbContext.create(message);
//
//		FeedbackMessage[] actuals = feedbackHandler.selectAll().toArray(new FeedbackMessage[0]);
//		Assert.assertEquals(1, actuals.length);
//		Assert.assertEquals("zzz", actuals[0].getFrom());
//		Assert.assertEquals("hello", actuals[0].getMessage());
//		Assert.assertEquals("yyy", actuals[0].getRespondTo());
//	}
//}
