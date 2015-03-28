package services.statistics;

import handlers.IStatisticsHandler;
import handlers.StatisticsHandler;
import database.IDBContext;

public class StatisticHandlerFactory implements IStatisticHandlerFactory
{
	private IDBContext dbContext;

	public StatisticHandlerFactory(IDBContext dbContext){
		this.dbContext = dbContext;
		
	}
	
	@Override
	public IStatisticsHandler create()
	{
		return new StatisticsHandler(dbContext);
	}
}
