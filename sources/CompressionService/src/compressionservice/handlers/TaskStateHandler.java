package compressionservice.handlers;

import httpservice.handlers.BaseHandler;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;

import compressionservice.handlers.binding.Binder;
import compressionservice.runner.state.ITasksRunnerStatesStorage;
import compressionservice.runner.state.TaskRunnerState;
import dataContracts.IIDFactory;

public class TaskStateHandler extends BaseHandler {

    private final static String requestIdKey = "requestId";
    private IIDFactory idFactory;
    private ITasksRunnerStatesStorage statesKeeper;

    public TaskStateHandler(ITasksRunnerStatesStorage statesKeeper, IIDFactory idFactory) {
        this.statesKeeper = statesKeeper;
        this.idFactory = idFactory;
    }
    
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        UUID defaultRequestId = idFactory.getEmpty();
        UUID requestId = Binder.getUUID(request, requestIdKey, defaultRequestId);
        if (requestId == defaultRequestId) {
            TaskRunnerState[] result = statesKeeper.getAll();
            respondJson(baseRequest, response, result);
        }
        else {
            TaskRunnerState result = statesKeeper.getState(requestId);
            respondJson(baseRequest, response, result);
        }
    }

    @Override
    public String getRoute() {
        return "/task/state";
    }

}
