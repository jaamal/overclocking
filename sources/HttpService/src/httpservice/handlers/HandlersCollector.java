package httpservice.handlers;

import java.util.Arrays;
import java.util.Comparator;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;

import overclocking.jrobocontainer.container.IContainer;

public class HandlersCollector implements IHandlersCollector
{
    private IContainer container;

    public HandlersCollector(IContainer container)
    {
        this.container = container;
    }

    @Override
    public ContextHandlerCollection collect() {
        BaseHandler[] baseHandlers = container.getAll(BaseHandler.class);
        Handler[] handlers = new Handler[baseHandlers.length];
        for (int i = 0; i < baseHandlers.length; i++) {
            ContextHandler contextHandler = new ContextHandler(baseHandlers[i].getRoute());
            contextHandler.setHandler(baseHandlers[i]);
            handlers[i] = contextHandler;
        }

        ContextHandlerCollection result = new ContextHandlerCollection();
        result.setHandlers(handlers);
        return result;
    }

    @Override
    public BaseHandler[] collectHandlers() {
        BaseHandler[] handlers = container.getAll(BaseHandler.class);
        Arrays.sort(handlers, new Comparator<BaseHandler>() {
            @Override
            public int compare(BaseHandler handler1, BaseHandler handler2) {
                return handler1.getRoute().compareToIgnoreCase(handler2.getRoute());
            }
        });
        return handlers;
    }
}
