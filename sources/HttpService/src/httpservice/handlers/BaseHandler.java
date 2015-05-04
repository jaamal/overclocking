package httpservice.handlers;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import serialization.ISerializer;
import serialization.Serializer;

public abstract class BaseHandler extends AbstractHandler
{
    public abstract String getRoute();
    protected abstract void handle(HttpServletRequest request, HttpServletResponse response) throws Exception;
    
    private static ISerializer serializer = new Serializer();
    private static Logger logger = Logger.getLogger(BaseHandler.class);

    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            response.reset();
            handle(request, response);
            response.setStatus(HttpServletResponse.SC_OK);
        }
        catch (Exception e) {
            logger.error(String.format("Unhandled exception on request with uri %s.", request.getRequestURI()), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        finally {
            baseRequest.setHandled(true);
        }
    }
    
    protected void respondText(HttpServletResponse response, String content) throws IOException {
        response.setContentType(HttpContentTypes.TEXT);
        response.setContentLength((int) content.length());
        try (PrintWriter writer = response.getWriter()) {
            writer.print(content);
            writer.flush();
        }
    }

    protected void respondJson(HttpServletResponse response, Object obj) throws IOException {
        String content = serializer.stringify(obj);
        
        response.setContentType(HttpContentTypes.JSON);
        response.setContentLength((int) content.length());
        try (PrintWriter writer = response.getWriter()) {
            writer.print(content);
            writer.flush();
        }
    }

    protected void respondFile(HttpServletResponse response, String contentType, int contentLength, String filename) throws IOException {
        response.setContentType(contentType);
        response.setContentLength(contentLength);
        response.setHeader(HttpHeaders.ContentDisposition, String.format("attachment; filename=\"%s\"", filename));
    }
}
