package httpservice.handlers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import serialization.ISerializer;
import serialization.Serializer;

public abstract class BaseHandler extends AbstractHandler
{
    public abstract String getRoute();

    private static ISerializer serializer = new Serializer();

    protected void respondText(Request baseRequest, HttpServletResponse response, String content) throws IOException {
        response.getWriter().print(content);
        response.setContentType(HttpContentTypes.TEXT);
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
    }

    protected void respondJson(Request baseRequest, HttpServletResponse response, Object obj) throws IOException {
        response.getWriter().print(serializer.stringify(obj));
        response.setContentType(HttpContentTypes.JSON);
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
    }

    protected void respondFile(Request baseRequest, HttpServletResponse response, String contentType, String filename) throws IOException {
        response.setContentType(contentType);
        response.setHeader(HttpHeaders.ContentDisposition, String.format("attachment; filename=\"%s\"", filename));
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
    }
}
