package handlers;

import java.util.Collection;
import database.DBException;
import database.IDBContext;

public class StatisticsHandler implements IStatisticsHandler {
    private IDBContext dbContext;

    public StatisticsHandler(IDBContext dbContext) {
        this.dbContext = dbContext;
    }

    @Override
    public Collection<?> getStatistics(Class<?> statsClass) throws DBException {
        return dbContext.select(statsClass);
    }
}
