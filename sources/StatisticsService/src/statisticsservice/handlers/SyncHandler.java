package statisticsservice.handlers;

import httpservice.handlers.BaseHandler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SyncHandler extends BaseHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        respondText(response, "syncing");
    }

    @Override
    public String getRoute() {
        return "/sync";
    }

}
