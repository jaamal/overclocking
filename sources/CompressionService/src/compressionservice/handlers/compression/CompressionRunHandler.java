package compressionservice.handlers.compression;

import httpservice.handlers.BaseHandler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;

import compressionservice.businessObjects.CompressionRunnerState;
import compressionservice.compression.parameters.IRunParams;
import compressionservice.compression.parameters.IRunParamsFactory;
import compressionservice.compression.running.ITaskRunner;
import compressionservice.handlers.binding.Binder;

public class CompressionRunHandler extends BaseHandler {

    private ITaskRunner compressionRunner;
    private IRunParamsFactory paramsFactory;

    public CompressionRunHandler(ITaskRunner compressionRunner, IRunParamsFactory paramsFactory) {
        this.compressionRunner = compressionRunner;
        this.paramsFactory = paramsFactory;
    }
    
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        IRunParams runParams = paramsFactory.create(Binder.getAllParams(request));
        
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
