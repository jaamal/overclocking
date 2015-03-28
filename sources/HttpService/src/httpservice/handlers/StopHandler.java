package httpservice.handlers;

import httpservice.server.HttpServer;
import httpservice.server.StopParameters;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;

import commons.settings.ISettings;
import commons.settings.KnownKeys;

public class StopHandler extends BaseHandler {

    private ISettings settings;

	public StopHandler(ISettings settings) {
		this.settings = settings;
	}
	
	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		int stopTimeout = settings.getInt(KnownKeys.ServerStopTimeout);
	    respondText(baseRequest, response, format(stopTimeout));
        HttpServer.stop(StopParameters.createLazy(stopTimeout));
	}
	
	private static String format(int result) {
	    return String.format("HTTP server will be terminated after %d ms", result);
	}

	@Override
	public String getRoute() {
		return "/stop";
	}
}
