package statisticsservice.handlers;

import httpservice.handlers.BaseHandler;
import httpservice.handlers.HttpContentTypes;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import statisticsservice.export.IStatsExporter;

public class ExportHandler extends BaseHandler {

    private IStatsExporter statsExporter;

    public ExportHandler(IStatsExporter statsExporter) {
        this.statsExporter = statsExporter;
    }
    
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String statsStr = statsExporter.exportAll();
        respondFile(response, HttpContentTypes.CSV, statsStr.length(), "result.csv");
        try (PrintWriter writer = response.getWriter()) {
            writer.print(statsStr);
            writer.flush();
        }
    }

    @Override
    public String getRoute() {
        return "/exportall";
    }

}
