package httpservice.handlers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;

import commons.settings.ISettings;
import commons.settings.Setting;

public class SettingsHandler extends BaseHandler {

    private ISettings settings;
    public SettingsHandler(ISettings settings) {
        this.settings = settings;
    }
    
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Setting[] values = settings.toArray();
        respondJson(baseRequest, response, values);
    }

    @Override
    public String getRoute() {
        return "/settings";
    }

}
