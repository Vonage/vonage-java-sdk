package com.nexmo.sns.sdk.request;
/*
 * Copyright (c) 2011-2013 Nexmo Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

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

        Map<String, String> params = new LinkedHashMap<>();

        // Construct a query string and a name/value pair Map from the query params supplied by the sub-class implementation
        int i = 0;
        StringBuilder sb = new StringBuilder();
        String param = null;
        for (String str: queryParams) {
            if ((i % 2) == 0) {
                param = str;
            } else {
                String value = str;
                params.put(param, value);
                if (i > 1)
                    sb.append('&');
                value = URLEncoder.encode(value, "UTF-8");
                sb.append(param).append('=').append(value);
            }
            i++;
        }

	this.queryString = sb.toString();
        this.queryParameters = params;
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
