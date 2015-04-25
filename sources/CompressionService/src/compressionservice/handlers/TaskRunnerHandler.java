package compressionservice.handlers;

import httpservice.handlers.BaseHandler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;

import compressionservice.handlers.binding.Binder;
import compressionservice.runner.ITaskRunner;
import compressionservice.runner.parameters.IRunParams;
import compressionservice.runner.parameters.IRunParamsFactory;
import compressionservice.runner.state.TaskRunnerState;

public class TaskRunnerHandler extends BaseHandler {

    private ITaskRunner compressionRunner;
    private IRunParamsFactory paramsFactory;

    public TaskRunnerHandler(ITaskRunner compressionRunner, IRunParamsFactory paramsFactory) {
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
            
            TaskRunnerState result = compressionRunner.run(runParams);
            respondJson(baseRequest, response, result);
        }
    }

    @Override
    public String getRoute() {
        return "/task/run";
    }

}
