package statisticsservice.handlers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;

import httpservice.handlers.BaseHandler;

public class SyncHandler extends BaseHandler {

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        respondText(baseRequest, response, "syncing");
    }

    @Override
    public String getRoute() {
        return "/sync";
    }

}
