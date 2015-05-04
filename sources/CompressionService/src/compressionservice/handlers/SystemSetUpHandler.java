package compressionservice.handlers;

import httpservice.handlers.BaseHandler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;

import storage.cassandraClient.ISchemeInitializer;

public class SystemSetUpHandler extends BaseHandler
{
    private ISchemeInitializer schemeInitializer;

    public SystemSetUpHandler(ISchemeInitializer schemeInitializer) {
        this.schemeInitializer = schemeInitializer;
    }
    
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        schemeInitializer.setUpCluster();
        respondText(baseRequest, response, "Cluster succefully initialized.");
    }

    @Override
    public String getRoute() {
        return "/system/setup";
    }
}
