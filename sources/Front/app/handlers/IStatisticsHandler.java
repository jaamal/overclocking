package handlers;

import java.util.Collection;
import database.DBException;

public interface IStatisticsHandler {
    Collection<?> getStatistics(Class<?> statsClass) throws DBException;
}
