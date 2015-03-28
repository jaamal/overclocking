package controllers;

import models.FeedbackMessage;
import play.Logger;
import play.mvc.Before;
import play.mvc.Controller;
import serialization.ISerializer;
import configuration.Configurator;
import database.IDBContext;

public class Feedback extends Controller{
	
	static ISerializer serializer;
	static IDBContext dbContext;

	@Before
    static void init() {
		dbContext = Configurator.getContainer().get(IDBContext.class);
        serializer = Configurator.getContainer().get(ISerializer.class);
    }
	
	public static void send() {
		try {
			FeedbackMessage message = serializer.deserialize(request.body, FeedbackMessage.class);
			if (message.getFrom() == null || message.getFrom().length() == 0) 
				message.setFrom("anonymous");
			
			dbContext.create(message);
			Logger.info("Feedback message successfully added.");
			
			ok();
		}
		catch(Exception e) {
			Logger.error(e, "Fail to add feedback message. Error message: " + e.getMessage());
            badRequest();
		}
    }
}
