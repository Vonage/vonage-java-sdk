/*
 * Copyright (c) 2011-2017 Nexmo Inc
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
package com.nexmo.client.verify;


import com.nexmo.client.AbstractClient;
import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.NexmoResponseParseException;
import com.nexmo.client.verify.endpoints.CheckEndpoint;
import com.nexmo.client.verify.endpoints.SearchEndpoint;
import com.nexmo.client.verify.endpoints.VerifyEndpoint;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Locale;

/**
 * A client for talking to the Nexmo Verify API interface.
 * <p>
 * To request a verification to Nexmo, call one of the {@link #verify} methods.
 * The only mandatory parameters are the phone number and the brand name. The verification text message
 * and/or voice call will have the brand name you provide so the user can recognize it.
 * <p>
 * After receiving the verification code from the user, you should send it to Nexmo using the
 * {@link #check} method.
 * <p>
 * You can search for an in-progress or past verification request using {@link #search}.
 * <p>
 * Error codes are listed in {@link BaseResult} and also on the documentation website.
 * <p>
 * More information on method parameters can be found at Nexmo website:
 * <a href="https://docs.nexmo.com/verify">https://docs.nexmo.com/verify</a>
 *
 * @author Daniele Ricci
 */
public class VerifyClient extends AbstractClient {
    public enum LineType {
        ALL,
        MOBILE,
        LANDLINE,
    }

    /**
     * Service url used unless over-ridden on the constructor
     */
    public static final String DEFAULT_BASE_URL = "https://api.nexmo.com";

    private CheckEndpoint check;
    private VerifyEndpoint verify;
    private SearchEndpoint search;

    /**
     * Instantiate a new VerifyEndpoint instance that will communicate using the supplied credentials.
     *
     * @throws ParserConfigurationException if the XML parser could not be configured.
     */
    public VerifyClient(HttpWrapper httpWrapper) throws ParserConfigurationException {
        super(httpWrapper);

        this.check = new CheckEndpoint(httpWrapper);
        //this.search = new SearchEndpoint(httpWrapper);
        //this.verify = new VerifyEndpoint(httpWrapper);
    }

    public VerifyResult verify(final String number,
                               final String brand) throws IOException,
                                                          NexmoResponseParseException {
        return verify.verify(number, brand);
    }

    public VerifyResult verify(final String number,
                               final String brand,
                               final String from) throws IOException,
                                                         NexmoResponseParseException {
        return verify.verify(number, brand, from);
    }

    public VerifyResult verify(final String number,
                               final String brand,
                               final String from,
                               final int length,
                               final Locale locale) throws IOException,
                                                           NexmoResponseParseException {
        return verify.verify(number, brand, from, length, locale);
    }

    public VerifyResult verify(final String number,
                               final String brand,
                               final String from,
                               final int length,
                               final Locale locale,
                               final VerifyClient.LineType type) throws IOException,
                                                                        NexmoResponseParseException {
        return verify.verify(number, brand, from, length, locale, type);
    }


    public CheckResult check(final String requestId,
                             final String code) throws IOException, NexmoClientException {
        return check.check(requestId, code, null);
    }

    public CheckResult check(final String requestId,
                             final String code,
                             final String ipAddress) throws IOException, NexmoClientException {
        return check.check(requestId, code, ipAddress);
    }

    public SearchResult search(String requestId) throws IOException, NexmoResponseParseException {
        return search.search(requestId);
    }

    public SearchResult[] search(String... requestIds) throws IOException, NexmoResponseParseException {
        return search.search(requestIds);
    }
}
