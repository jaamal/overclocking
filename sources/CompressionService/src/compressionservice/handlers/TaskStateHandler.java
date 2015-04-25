package compressionservice.handlers;

import httpservice.handlers.BaseHandler;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;

import compressionservice.handlers.binding.Binder;
import compressionservice.handlers.models.TaskStateExtendedModel;
import compressionservice.runner.state.ITaskOperationalLog;
import compressionservice.runner.state.ITaskStatesStorage;
import compressionservice.runner.state.TaskStateModel;

import dataContracts.IDFactory;

public class TaskStateHandler extends BaseHandler {

    private final static String requestIdKey = "requestId";
    private final UUID defaultRequestId;
    
    private ITaskStatesStorage statesStorage;
    private ITaskOperationalLog taskOperationalLog;

    public TaskStateHandler(
            IDFactory idFactory,
            ITaskStatesStorage statesStorage,
            ITaskOperationalLog taskOperationalLog) {
        this.statesStorage = statesStorage;
        this.taskOperationalLog = taskOperationalLog;
        defaultRequestId = idFactory.getEmpty();
    }
    
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        UUID requestId = Binder.getUUID(request, requestIdKey, defaultRequestId);
        if (requestId == defaultRequestId) {
            TaskStateModel[] taskStateModels = statesStorage.getAll();
            respondJson(baseRequest, response, taskStateModels);
        }
        else {
            TaskStateModel taskStateModel = statesStorage.getState(requestId);
            String opLog = taskOperationalLog.get(requestId);
            respondJson(baseRequest, response, new TaskStateExtendedModel(taskStateModel, opLog));
        }
    }

    @Override
    public String getRoute() {
        return "/task/state";
    }

}
