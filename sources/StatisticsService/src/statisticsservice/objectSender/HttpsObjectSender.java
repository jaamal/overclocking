package statisticsservice.objectSender;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

import serialization.ISerializer;

import commons.settings.ISettings;
import commons.settings.KnownKeys;

public class HttpsObjectSender extends ObjectSender{
    private static final Logger logger = Logger.getLogger(HttpsObjectSender.class);

    public HttpsObjectSender(ISerializer serializer, ISettings settings) {
        super(serializer, settings);
    }

    @Override
    protected HttpClient createClient() {
        HttpClient client = new DefaultHttpClient();
        String certStorePath = settings.getString(KnownKeys.HttpClientCertStorePath);
        String certStorePassword = settings.getString(KnownKeys.HttpClientCertStorePassword);
        KeyStore store = loadKeyStore(new File(certStorePath), certStorePassword);
        String certTrustedStorePath = settings.getString(KnownKeys.HttpClientCertTrustedStorePath);
        KeyStore trustStore = loadKeyStore(new File(certTrustedStorePath), settings.getString(KnownKeys.HttpClientCertTrustedStorePassword));

        SSLSocketFactory socketFactory = null;
        AllowAllHostnameVerifier hostnameVerifier = new AllowAllHostnameVerifier();
        TrustSelfSignedStrategy trustStrategy = new TrustSelfSignedStrategy();
        try {
            socketFactory = new SSLSocketFactory("TLS", store, certStorePassword, trustStore, null, trustStrategy, hostnameVerifier);
        } catch (Exception e) {
            logger.error(String.format("Fail to create ssl factory for store %s and trust store &s", certStorePath, certTrustedStorePath), e);
            throw new ObjectSenderException(e);
        }

        Scheme sch = new Scheme("https", settings.getInt(KnownKeys.HttpClientPort), socketFactory);
        client.getConnectionManager().getSchemeRegistry().register(sch);
        return client;
    }

    private static KeyStore loadKeyStore(File path, String password) {
        KeyStore clientStore = null;
        try {
            clientStore = KeyStore.getInstance("JKS");
        } catch (KeyStoreException e) {
            logger.error("Fail to create key store instance.", e);
            throw new ObjectSenderException(e);
        }
        try (FileInputStream clientStoreStream = new FileInputStream(path)) {
            clientStore.load(clientStoreStream, password.toCharArray());
        } catch (Exception e) {
            logger.error(String.format("Fail to load key store by path %s.", path.toString()), e);
            throw new ObjectSenderException(e);
        }
        return clientStore;
    }

}
