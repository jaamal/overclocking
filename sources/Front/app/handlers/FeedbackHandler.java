package handlers;

import java.util.List;
import models.FeedbackMessage;
import database.DBException;
import database.IDBContext;

public class FeedbackHandler implements IFeedbackHandler {

	private IDBContext dbContext;

    public FeedbackHandler(IDBContext dbContext) {
        this.dbContext = dbContext;
    }

    @Override
    public List<FeedbackMessage> selectAll() throws DBException {
        return dbContext.select(FeedbackMessage.class);
    }
}
