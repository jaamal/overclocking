package statisticsservice.objectSender;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import commons.settings.ISettings;

import serialization.ISerializer;

public class HttpObjectSender extends ObjectSender{
    public HttpObjectSender(ISerializer serializer, ISettings settings) {
        super(serializer, settings);
    }

    @Override
    protected HttpClient createClient() {
        return new DefaultHttpClient();
    }
}
