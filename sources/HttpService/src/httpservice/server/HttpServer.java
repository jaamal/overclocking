package httpservice.server;

import java.nio.file.Paths;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import commons.settings.ISettings;
import commons.settings.KnownKeys;
import httpservice.configuration.Configurator;
import httpservice.handlers.IHandlersCollector;
import overclocking.jrobocontainer.container.IContainer;

public class HttpServer
{

    private static Logger logger = Logger.getLogger(HttpServer.class);
    private static String settingsPath = Paths.get("conf", "application.settings").toAbsolutePath().toString();
    private static IContainer container = null;
    private static Server server = null;

    public static void run()
    {
        try {
            logger.info("Configuring compression service.");
            container = new Configurator().configure(settingsPath);
            logger.info(String.format("Compression service configuration finished. Settings path: %s", settingsPath));

            ISettings settings = container.get(ISettings.class);
            int port = settings.getInt(KnownKeys.ServerPort);
            logger.info(String.format("Start HTTP server on port %d", port));
            server = new Server(port);
            server.setHandler(container.get(IHandlersCollector.class).collect());
            server.start();
            server.join();
        }
        catch (Exception e) {
            stop(StopParameters.createImmediate(e));
        }
    }

    public static void stop(final StopParameters parameters)
    {
        if (server != null) {
            switch (parameters.type) {
                case lazy:
                    logger.info(String.format("HTTP server will be terminated after %d ms", parameters.timeout));
                    new Thread() {
                        @Override
                        public void run()
                        {
                            HttpServer.stop(parameters.timeout, parameters.reason);
                        }
                    }.start();
                    break;
                default:
                    stop(parameters.timeout, parameters.reason);
                    break;
            }
        }
        else {
            logger.error(String.format("HTTP server crashed with %s.", parameters.reason == null ? "no reason" : parameters.reason.getMessage()));
        }
    }

    private static void stop(int timout, Exception reason)
    {
        try {
            Thread.sleep(timout);
            server.stop();
        }
        catch (Exception ex) {
            logger.error("Unable to stop HTTP server.", ex);
            return;
        }

        logger.error(String.format("HTTP server crashed with %s.", reason == null ? "no reason" : reason.getMessage()));
        server.destroy();
    }
}
