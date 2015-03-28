package compressionservice.handlers.compression;

import httpservice.handlers.BaseHandler;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;

import compressionservice.businessObjects.CompressionRunnerState;
import compressionservice.compression.ICompressionStatesKeeper;
import compressionservice.handlers.binding.Binder;

import dataContracts.IIDFactory;

public class CompressionStateHandler extends BaseHandler {

    private final static String requestIdKey = "requestId";
    private IIDFactory idFactory;
    private ICompressionStatesKeeper statesKeeper;

    public CompressionStateHandler(ICompressionStatesKeeper statesKeeper, IIDFactory idFactory) {
        this.statesKeeper = statesKeeper;
        this.idFactory = idFactory;
    }
    
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        UUID defaultRequestId = idFactory.getEmpty();
        UUID requestId = Binder.getUUID(request, requestIdKey, defaultRequestId);
        if (requestId == defaultRequestId) {
            CompressionRunnerState[] result = statesKeeper.getAll();
            respondJson(baseRequest, response, result);
        }
        else {
            CompressionRunnerState result = statesKeeper.getState(requestId);
            respondJson(baseRequest, response, result);
        }
    }

    @Override
    public String getRoute() {
        return "/compression/state";
    }

}
