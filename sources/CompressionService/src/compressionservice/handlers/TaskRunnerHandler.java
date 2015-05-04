package compressionservice.handlers;

import httpservice.handlers.BaseHandler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import compressionservice.handlers.binding.Binder;
import compressionservice.runner.ITaskRunner;
import compressionservice.runner.parameters.IRunParams;
import compressionservice.runner.parameters.IRunParamsFactory;
import compressionservice.runner.state.TaskStateModel;

public class TaskRunnerHandler extends BaseHandler {

    private ITaskRunner taskRunner;
    private IRunParamsFactory paramsFactory;

    public TaskRunnerHandler(ITaskRunner compressionRunner, IRunParamsFactory paramsFactory) {
        this.taskRunner = compressionRunner;
        this.paramsFactory = paramsFactory;
    }
    
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        IRunParams runParams = paramsFactory.create(Binder.getAllParams(request));
        
        if (!taskRunner.isAvailable()) {
            respondText(response, "Compression service is busy at now.");
        }
        else {
            
            TaskStateModel result = taskRunner.run(runParams);
            respondJson(response, result);
        }
    }

    @Override
    public String getRoute() {
        return "/task/run";
    }

}
