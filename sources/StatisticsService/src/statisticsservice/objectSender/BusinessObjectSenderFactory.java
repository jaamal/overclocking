package statisticsservice.objectSender;

import commons.settings.ISettings;
import commons.settings.KnownKeys;
import serialization.ISerializer;

public class BusinessObjectSenderFactory implements IObjectSenderFactory {
    private ISerializer serializer;
    private ISettings settings;

    public BusinessObjectSenderFactory(ISerializer serializer, ISettings settings) {
        this.serializer = serializer;
        this.settings = settings;
    }

    public IObjectSender create() {
        String scheme = settings.getString(KnownKeys.HttpClientScheme);
        switch (scheme) {
            case "http":
                return new HttpObjectSender(serializer, settings);
            case "https":
                return new HttpsObjectSender(serializer, settings);
            default:
                throw new IllegalArgumentException(String.format("Fail to create client for scheme %s.", scheme));
        }
    }
}
