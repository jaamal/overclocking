package statisticsservice.objectSender;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.log4j.Logger;

import commons.settings.ISettings;
import commons.settings.KnownKeys;
import serialization.ISerializer;

public abstract class ObjectSender implements IObjectSender {
    private static final Logger logger = Logger.getLogger(ObjectSender.class);

    private final ISerializer serializer;
    protected final ISettings settings;
    private Map<Class<?>, URI> urlCache;

    public ObjectSender(ISerializer serializer, ISettings settings) {
        this.serializer = serializer;
        this.settings = settings;
        this.urlCache = new HashMap<Class<?>, URI>();
    }

    @Override
    public <T> void send(T object) {
        HttpClient httpClient = createClient();
        try {
            HttpPost httpPost = new HttpPost(buildUrl(object));
            httpPost.setEntity(new ByteArrayEntity(serializer.stringify(object).getBytes()));

            HttpResponse response;
            try {
                response = httpClient.execute(httpPost);
            } catch (IOException e) {
                logger.error(String.format("Fail to http request for object %s", object.getClass().toString()), e);
                throw new ObjectSenderException(e);
            }
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() != HttpStatus.SC_OK) {
                logger.error("Sending failed: status code=" + statusLine.getStatusCode() + " reason=" + statusLine.getReasonPhrase());
                throw new ObjectSenderException("Sending failed: status code=" + statusLine.getStatusCode() + " reason=" + statusLine.getReasonPhrase());
            }
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

    protected abstract HttpClient createClient();

    private <T> URI buildUrl(T object) {
        Class<?> objectClass = object.getClass();
        if (urlCache.containsKey(objectClass))
            return urlCache.get(object);

        //TODO: publish url is null
        URI uri = null;
        try {
            String scheme = settings.getString(KnownKeys.HttpClientScheme);
            String host = settings.getString(KnownKeys.HttpClientHost);
            int port = settings.getInt(KnownKeys.HttpClientPort);
            uri = new URI(scheme, null, host, port, null, null, null);
        } catch (URISyntaxException e) {
            logger.error(String.format("Fail to build uri for class %s.", objectClass.toString()), e);
            throw new ObjectSenderException(e);
        }
        urlCache.put(objectClass, uri);
        return uri;
    }

}
