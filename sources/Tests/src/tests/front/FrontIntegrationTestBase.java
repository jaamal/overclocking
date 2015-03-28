package tests.front;
//package tests;
//
//import java.nio.file.Paths;
//import java.util.concurrent.atomic.AtomicInteger;
//import junit.framework.Assert;
//import models.FeedbackMessage;
//import models.FileInfo;
//import models.statistics.LZ77Statistics;
//import models.statistics.LZMAStatistics;
//import models.statistics.LZWStatistics;
//import models.statistics.SLPCartesianStatistics;
//import models.statistics.SLPClassicStatistics;
//import models.statistics.SLPOptimalStatistics;
//import overclocking.jrobocontainer.container.IContainer;
//import storage.cassandraClient.ClusterConfiguration;
//import storage.cassandraClient.IClusterConfiguration;
//import commons.config.ApplicationSettings;
//import commons.config.IApplicationSettings;
//import database.IDBContext;
//
//public abstract class FrontIntegrationTestBase extends IntegrationTestBase {
//
//	protected IContainer container;
//
//	@Override
//	public void setUp()
//	{
//		super.setUp();
//		
//		IApplicationSettings appSettings = ApplicationSettings.Load(Paths.get("conf", "application.settings").toAbsolutePath().toString());
//		
//		container.bindInstance(IApplicationSettings.class, appSettings);
//		container.bindInstance(IClusterConfiguration.class, new ClusterConfiguration(appSettings));
//	}
//
//	protected void clearDB()
//	{
//		truncateTable(LZMAStatistics.class);
//		truncateTable(LZ77Statistics.class);
//		truncateTable(LZWStatistics.class);
//		truncateTable(SLPCartesianStatistics.class);
//		truncateTable(SLPClassicStatistics.class);
//		truncateTable(SLPOptimalStatistics.class);
//		truncateTable(FileInfo.class);
//		truncateTable(FeedbackMessage.class);
//	}
//	
//    protected void truncateTable(Class<?> c) {
//        try {
//            IDBContext dbContext = container.get(IDBContext.class);
//            dbContext.truncateTable(c);
//        } catch (Exception e) {
//            System.out.println(String.format("Fail to truncate table %s.", c));
//            e.printStackTrace();
//        }
//    }
//
//    protected void runViaMultiThreads(final Runnable runnable, int threadsNumber, final int tasksNumber) {
//        final AtomicInteger currentTask = new AtomicInteger(-1);
//        final AtomicInteger finishedThreads = new AtomicInteger(0);
//        final AtomicInteger exceptionsNumber = new AtomicInteger(0);
//
//        Runnable action = new Runnable() {
//
//            @Override
//            public void run() {
//                int taskNumber = currentTask.incrementAndGet();
//                while (taskNumber < tasksNumber) {
//                    try {
//                        runnable.run();
//                    } catch (Throwable e) {
//                        exceptionsNumber.incrementAndGet();
//                    }
//                    taskNumber = currentTask.incrementAndGet();
//                }
//                finishedThreads.incrementAndGet();
//            }
//        };
//
//        Thread[] threads = new Thread[threadsNumber];
//        for (int i = 0; i < threadsNumber; i++) {
//            threads[i] = new Thread(action);
//            threads[i].start();
//        }
//
//        while (finishedThreads.get() != threadsNumber && exceptionsNumber.get() == 0) {
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//        for (int i = 0; i < threadsNumber; i++)
//            try {
//                threads[i].join(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//        Assert.assertEquals(0, exceptionsNumber.get());
//    }
//}
