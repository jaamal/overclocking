package httpservice.handlers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PingHandler extends BaseHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        respondText(response, "pong");
	}

	@Override
	public String getRoute() {
		return "/ping";
	}
}
