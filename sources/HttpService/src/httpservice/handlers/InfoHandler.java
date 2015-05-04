package httpservice.handlers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;

public class InfoHandler extends BaseHandler {

	private IHandlersCollector handlersCollector;

	public InfoHandler(IHandlersCollector handlersCollector){
		this.handlersCollector = handlersCollector;
	}
	
	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        BaseHandler[] handlers = handlersCollector.collectHandlers();
        respondText(baseRequest, response, format(handlers));
	}
	
	private static String format(BaseHandler[] handlers) {
	    StringBuilder builder = new StringBuilder();
        builder.append("<h3>Welcome to overclocking service.</h3>");
        builder.append("<div>Handlers list:</div>");
        builder.append("<table>");
        builder.append("<tr><td>Name</td><td>Route</td></tr>");
        for (int i = 0; i < handlers.length; i++) {
            String route = handlers[i].getRoute();
            String name = handlers[i].getClass().getSimpleName();
            builder.append(String.format("<tr><td>%s</td><td>%s</td>", name, route));
        }
        return builder.toString();
	}

	@Override
	public String getRoute() {
		return "/";
	}

}
