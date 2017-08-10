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
package com.nexmo.client;


import com.nexmo.client.auth.AuthMethod;
import com.nexmo.client.insight.InsightClient;
import com.nexmo.client.sms.SmsClient;
import com.nexmo.client.sns.SnsClient;
import com.nexmo.client.verify.VerifyClient;
import com.nexmo.client.voice.VoiceClient;
import org.apache.http.client.HttpClient;

/**
 * Top-level Nexmo API client object.
 * <p>
 * Construct an instance of this object with one or more {@link AuthMethod}s (providing all the authentication methods
 * for the APIs you wish to use), and then call {@link #getVoiceClient()} to obtain a client for the Nexmo Voice API.
 * <p>
 */
public class NexmoClient {
    private final InsightClient insight;
    private final SmsClient sms;
    private final VoiceClient voice;
    private final VerifyClient verify;
    private final SnsClient sns;

    private String baseUri;
    private HttpWrapper httpWrapper;

    /**
     *
     * @param baseUri Change the server this library calls when making requests. <b>Don't include a trailing slash</b>
     * @param authMethods (required) one or more {@link AuthMethod}s (providing all the authentication methods
     * for the APIs you wish to use)
     */
    public NexmoClient(String baseUri, AuthMethod... authMethods) {
        this.baseUri = baseUri;
        this.httpWrapper = new HttpWrapper(authMethods);

        this.insight = new InsightClient(this.httpWrapper, baseUri);
        this.verify = new VerifyClient(this.httpWrapper, baseUri);
        this.voice = new VoiceClient(this.httpWrapper, baseUri);
        this.sms = new SmsClient(this.httpWrapper, baseUri);
        this.sns = new SnsClient(this.httpWrapper, baseUri);
    }

    /**
     *
     * @param authMethods (required) one or more {@link AuthMethod}s (providing all the authentication methods
     * for the APIs you wish to use)
     */
    public NexmoClient(AuthMethod... authMethods) {
        this.httpWrapper = new HttpWrapper(authMethods);

        this.insight = new InsightClient(this.httpWrapper);
        this.verify = new VerifyClient(this.httpWrapper);
        this.voice = new VoiceClient(this.httpWrapper);
        this.sms = new SmsClient(this.httpWrapper);
        this.sns = new SnsClient(this.httpWrapper);
    }

    public void setHttpClient(HttpClient client) {
        this.httpWrapper.setHttpClient(client);
    }

    public InsightClient getInsightClient() {
        return this.insight;
    }

    public SmsClient getSmsClient() {
        return this.sms;
    }

    public SnsClient getSnsClient() {
        return this.sns;
    }

    public VerifyClient getVerifyClient() {
        return this.verify;
    }

    public VoiceClient getVoiceClient() {
        return this.voice;
    }

    /**
     * The server this library calls when making requests. Will be null if no overriding value has been set.
     * If this is null the client's {@code getBaseUri} method should be called.
     *
     * @return String uri the library calls when making requests or null
     */

    public String getBaseUri() {
        return baseUri;
    }

    /**
     *
     * @param baseUri Change the server this library calls when making requests. <b>Don't include a trailing slash</b>
     */
    public void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
        this.insight.setBaseUri(this.baseUri);
        this.verify.setBaseUri(this.baseUri);
        this.voice.setBaseUri(this.baseUri);
        this.sms.setBaseUri(this.baseUri);
        this.sns.setBaseUri(this.baseUri);
    }
}
