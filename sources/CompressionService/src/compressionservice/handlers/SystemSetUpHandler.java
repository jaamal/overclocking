package compressionservice.handlers;

import httpservice.handlers.BaseHandler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import storage.cassandraClient.ISchemeInitializer;

public class SystemSetUpHandler extends BaseHandler
{
    private ISchemeInitializer schemeInitializer;

    public SystemSetUpHandler(ISchemeInitializer schemeInitializer) {
        this.schemeInitializer = schemeInitializer;
    }
    
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        schemeInitializer.setUpCluster();
        respondText(response, "Cluster succefully initialized.");
    }

    @Override
    public String getRoute() {
        return "/system/setup";
    }
}
