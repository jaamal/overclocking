package statisticsservice.handlers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;

import statisticsservice.export.IStatsExporter;
import httpservice.handlers.BaseHandler;
import httpservice.handlers.HttpContentTypes;

public class ExportHandler extends BaseHandler {

    private IStatsExporter statsExporter;

    public ExportHandler(IStatsExporter statsExporter) {
        this.statsExporter = statsExporter;
    }
    
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String statsStr = statsExporter.exportAll();
        response.getWriter().println(statsStr);
        respondFile(baseRequest, response, HttpContentTypes.CSV, "result.csv");
    }

    @Override
    public String getRoute() {
        return "/exportall";
    }

}
