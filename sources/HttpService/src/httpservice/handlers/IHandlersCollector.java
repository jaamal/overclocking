package httpservice.handlers;

import org.eclipse.jetty.server.handler.ContextHandlerCollection;

public interface IHandlersCollector {
	ContextHandlerCollection collect();
	BaseHandler[] collectHandlers();
}
