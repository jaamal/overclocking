package handlers;

import java.util.List;

import models.FeedbackMessage;

public interface IFeedbackHandler {
	List<FeedbackMessage> selectAll();
}
