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
<<<<<<< HEAD
import com.nexmo.client.messaging.NexmoSmsClient;
=======
>>>>>>> master
import com.nexmo.client.verify.VerifyClient;
import com.nexmo.client.voice.VoiceClient;
import org.apache.http.client.HttpClient;

/**
 * Top-level Nexmo API client object.
 * <p>
 * Construct an instance of this object with one or more {@link AuthMethod}s (providing all the authentication methods
 * for the APIs you wish to use), and then call {@link #getVoiceClient()} to obtain a client for the Nexmo Voice API.
 * <p>
 * Currently this object only constructs and provides access to {@link VoiceClient}. In the future it will manage
 * clients for all of the Nexmo APIs.
 */
public class NexmoClient {
    private final VoiceClient voice;
    private final VerifyClient verify;

    private final NexmoSmsClient sms;

    private HttpWrapper httpWrapper;

    public NexmoClient(AuthMethod... authMethods) {
        this.httpWrapper = new HttpWrapper(authMethods);

        this.voice = new VoiceClient(this.httpWrapper);
        this.verify = new VerifyClient(this.httpWrapper);
        this.sms = new NexmoSmsClient(this.httpWrapper);
    }

    public void setHttpClient(HttpClient client) {
        this.httpWrapper.setHttpClient(client);
    }

    public VoiceClient getVoiceClient() {
        return this.voice;
    }

    public VerifyClient getVerifyClient() {
        return this.verify;
    }

    public NexmoSmsClient getSmsClient() {
        return this.sms;
    }

}
