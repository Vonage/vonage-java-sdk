package com.nexmo.sns.sdk.request;

import java.util.Map;
import java.util.LinkedHashMap;
import java.net.URLEncoder;

/**
 * Request.java<br><br>
 *
 * Created on 5 January 2011, 17:34<br><br>
 *
 * Represents the details of a service request that is to be submitted via the http api.<br>
 * Not instanciated directly, but instead via one of the sub-classes that represent a particular service command
 *
 * @author  Paul Cook
 * @version 1.0
 */
public abstract class Request {

    public static final String COMMAND_SUBSCRIBE = "subscribe";
    public static final String COMMAND_PUBLISH = "publish";

    private final String command;
    private final Map<String, String> queryParameters;
    private final String queryString;

    public Request(final String command,
                   String... queryParams) throws Exception {
        this.command = command;

        Map<String, String> queryParameters = new LinkedHashMap<String, String>();

        // Construct a query string and a name/value pair Map from the query params supplied by the sub-class implementation
        int i = 0;
        StringBuilder sb = new StringBuilder();
        String param = null;
        for (String str: queryParams) {
            if ((i % 2) == 0) {
                param = str;
            } else {
                String value = str;
                queryParameters.put(param, value);
                if (i > 1)
                    sb.append('&');
                value = URLEncoder.encode(value, "UTF-8");
                sb.append(param).append('=').append(value);
            }
            i++;
        }

	this.queryString = sb.toString();
        this.queryParameters = queryParameters;
    }

    public String getCommand() {
        return this.command;
    }

    public Map<String, String> getQueryParameters() {
        return this.queryParameters;
    }

    public String getQueryString() {
        return this.queryString;
    }

}
