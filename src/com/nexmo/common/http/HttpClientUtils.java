package com.nexmo.common.http;

import java.util.Map;
import java.util.HashMap;

import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.HttpClient;

/**
 * HttpClientUtils.java<br><br>
 *
 * A Helper factory for instanciating HttpClient instances<br><br>
 *
 * Created on 5 January 2011, 17:34
 *
 * @author  Paul Cook
 * @version 1.0
 */
public class HttpClientUtils {

    private final static Map<String, HttpClientUtils> instances = new HashMap<String, HttpClientUtils>();

    private final MultiThreadedHttpConnectionManager multiThreadedHttpConnectionManager;

    private HttpClientUtils(int connectionTimeout, int soTimeout) {
        this.multiThreadedHttpConnectionManager = new MultiThreadedHttpConnectionManager();
        this.multiThreadedHttpConnectionManager.getParams().setConnectionTimeout(connectionTimeout);
        this.multiThreadedHttpConnectionManager.getParams().setSoTimeout(soTimeout);
        this.multiThreadedHttpConnectionManager.getParams().setMaxTotalConnections(200);
        this.multiThreadedHttpConnectionManager.getParams().setDefaultMaxConnectionsPerHost(200);
        this.multiThreadedHttpConnectionManager.getParams().setTcpNoDelay(true);
    }

    /**
     * Return an existing or instanciate a new HttpClient factory instance with explicitely specified connection and read timeout values
     *
     * @param connectionTimeout the timeout value in milliseconds to use when establishing a new http socket
     * @param soTimeout the timeout value in milliseconds to wait for a http response before closing the socket
     *
     * @return HttpClientUtils an instance of the HttpClient factory primed with the requested timeout values
     */
    public static HttpClientUtils getInstance(int connectionTimeout, int soTimeout) {
        String key = "c-" + connectionTimeout + "-so-" + soTimeout;
        HttpClientUtils instance = instances.get(key);
        if (instance == null) {
            instance = new HttpClientUtils(connectionTimeout, soTimeout);
            instances.put(key, instance);
        }
        return instance;
    }

    /**
     * Instanciate a new HttpClient instance that uses the timeout values associated with this factory instance
     *
     * @return HttpClient a new HttpClient instance
     */
    public HttpClient getNewHttpClient() {
        return new HttpClient(this.multiThreadedHttpConnectionManager);
    }

}
