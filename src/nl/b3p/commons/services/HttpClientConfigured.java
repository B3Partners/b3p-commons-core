package nl.b3p.commons.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProxySelector;
import java.net.URL;
import java.util.Arrays;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;

/**
 * bundle of objects needed to do httpclient call
 * 
 * @author Chris
 */
public class HttpClientConfigured {
    private final static int maxResponseTime = 20000; //0=infinite
    private HttpClient httpClient;
    private HttpClientContext httpContext;

    public HttpClientConfigured(B3PCredentials credentials) {
        this(credentials, -1);
    }

    public HttpClientConfigured(B3PCredentials credentials, int timeout) {
        if (timeout < 0) {
            timeout = maxResponseTime;
        }
        String username = null;
        String password = null;
        String url = null;
        boolean preemptive = false;
        if (credentials != null) {
            password = credentials.getPassword();
            username = credentials.getUserName();
            url = credentials.getUrl();
            preemptive = credentials.isPreemptive();
        }
        
        RequestConfig defaultRequestConfig = RequestConfig.custom()
            .setStaleConnectionCheckEnabled(false)
            .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
            .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
            .setConnectionRequestTimeout(timeout)
            .build();
        
        HttpClientBuilder hcb = HttpClients.custom()
                .setDefaultRequestConfig(defaultRequestConfig);

        HttpClientContext context = HttpClientContext.create();
        if (username != null && password != null) {
            String hostname = null; //any
            int port = -1; //any
            URL aURL;
            try {
                aURL = new URL(url);
                hostname = aURL.getHost();
                port = aURL.getPort();
            } catch (MalformedURLException ex) {
                // ignore
            }
            
            CredentialsProvider credentialsProvider
                    =                    new BasicCredentialsProvider();
            Credentials defaultcreds = 
                    new UsernamePasswordCredentials(username, password);
            AuthScope authScope = 
                    new AuthScope(hostname, port);
            credentialsProvider.setCredentials(authScope, defaultcreds);

            hcb = hcb.setDefaultCredentialsProvider(credentialsProvider);
            
            if (preemptive) {
                // Create AuthCache instance for preemptive authentication
                AuthCache authCache = new BasicAuthCache();
                BasicScheme basicAuth = new BasicScheme();
                HttpHost targetHost = new HttpHost(hostname, port);
                authCache.put(targetHost, basicAuth);
                // Add AuthCache to the execution context
                context.setCredentialsProvider(credentialsProvider);
                context.setAuthCache(authCache);
            }

        }
        //Use standard JRE proxy selector to obtain proxy information 
        SystemDefaultRoutePlanner routePlanner = 
                new SystemDefaultRoutePlanner(ProxySelector.getDefault());
        hcb.setRoutePlanner(routePlanner); 
        
        this.httpClient = hcb.build();
        this.httpContext = context; 
    }
    
    public HttpResponse execute(HttpUriRequest method) throws IOException {
        return httpClient.execute(method, httpContext);
    }
   
    /**
     * @return the httpClient
     */
    public HttpClient getHttpClient() {
        return httpClient;
    }

    /**
     * @param httpClient the httpClient to set
     */
    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * @return the context
     */
    public HttpClientContext getContext() {
        return httpContext;
    }

    /**
     * @param context the context to set
     */
    public void setContext(HttpClientContext context) {
        this.httpContext = context;
    }
}
