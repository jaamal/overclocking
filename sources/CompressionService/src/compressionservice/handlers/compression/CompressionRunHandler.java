package compressionservice.handlers.compression;

import httpservice.handlers.BaseHandler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;

import compressionservice.businessObjects.CompressionRunnerState;
import compressionservice.compression.parameters.ICompressionRunParams;
import compressionservice.compression.parameters.ICompressionRunParamsFactory;
import compressionservice.compression.running.ICompressionRunner;
import compressionservice.handlers.binding.Binder;

public class CompressionRunHandler extends BaseHandler {

    private ICompressionRunner compressionRunner;
    private ICompressionRunParamsFactory paramsFactory;

    public CompressionRunHandler(ICompressionRunner compressionRunner, ICompressionRunParamsFactory paramsFactory) {
        this.compressionRunner = compressionRunner;
        this.paramsFactory = paramsFactory;
    }
    
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ICompressionRunParams runParams = paramsFactory.create(Binder.getAllParams(request));
        
        if (!compressionRunner.isAvailable()) {
            respondText(baseRequest, response, "Compression service is busy at now.");
        }
        else {
            
            CompressionRunnerState result = compressionRunner.run(runParams);
            respondJson(baseRequest, response, result);
        }
    }

    @Override
    public String getRoute() {
        return "/compression/run";
    }

}
