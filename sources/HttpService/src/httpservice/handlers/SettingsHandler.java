package httpservice.handlers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import commons.settings.ISettings;
import commons.settings.Setting;

public class SettingsHandler extends BaseHandler {

    private ISettings settings;
    public SettingsHandler(ISettings settings) {
        this.settings = settings;
    }
    
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Setting[] values = settings.toArray();
        respondJson(response, values);
    }

    @Override
    public String getRoute() {
        return "/settings";
    }

}
