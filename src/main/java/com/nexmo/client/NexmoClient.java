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


import com.nexmo.client.account.AccountClient;
import com.nexmo.client.application.ApplicationClient;
import com.nexmo.client.auth.*;
import com.nexmo.client.conversation.ConversationClient;
import com.nexmo.client.conversion.ConversionClient;
import com.nexmo.client.insight.InsightClient;
import com.nexmo.client.numbers.NumbersClient;
import com.nexmo.client.redact.RedactClient;
import com.nexmo.client.sms.SmsClient;
import com.nexmo.client.sns.SnsClient;
import com.nexmo.client.verify.VerifyClient;
import com.nexmo.client.voice.VoiceClient;
import org.apache.http.client.HttpClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
    private AccountClient account;
    private ApplicationClient application;
    private InsightClient insight;
    private NumbersClient numbers;
    private SmsClient sms;
    private VoiceClient voice;
    private VerifyClient verify;
    private SnsClient sns;
    private ConversionClient conversion;
    private RedactClient redact;
    private ConversationClient conversations;
    private HttpWrapper httpWrapper;

    private NexmoClient(Builder builder) {
        this.httpWrapper = new HttpWrapper(builder.httpConfig, builder.authCollection);
        this.httpWrapper.setHttpClient(builder.httpClient);

        this.account = new AccountClient(this.httpWrapper);
        this.application = new ApplicationClient(this.httpWrapper);
        this.insight = new InsightClient(this.httpWrapper);
        this.numbers = new NumbersClient(this.httpWrapper);
        this.verify = new VerifyClient(this.httpWrapper);
        this.voice = new VoiceClient(this.httpWrapper);
        this.sms = new SmsClient(this.httpWrapper);
        this.sns = new SnsClient(this.httpWrapper);
        this.conversion = new ConversionClient(this.httpWrapper);
        this.redact = new RedactClient(this.httpWrapper);
        this.conversations = new ConversationClient(this.httpWrapper);
    }

    public AccountClient getAccountClient() {
        return this.account;
    }

    public ApplicationClient getApplicationClient() {
        return this.application;
    }

    public InsightClient getInsightClient() {
        return this.insight;
    }

    public NumbersClient getNumbersClient() {
        return this.numbers;
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

    public ConversionClient getConversionClient() {
        return this.conversion;
    }

    public RedactClient getRedactClient() {
        return this.redact;
    }

    public ConversationClient getConversationsClient(){return this.conversations;}

    /**
     * Generate a JWT for the application the client has been configured with.
     *
     * @return A String containing the token data.
     *
     * @throws NexmoUnacceptableAuthException if no {@link JWTAuthMethod} is available
     */
    public String generateJwt() throws NexmoUnacceptableAuthException {
        JWTAuthMethod authMethod = this.httpWrapper.getAuthCollection().getAuth(JWTAuthMethod.class);
        return authMethod.generateToken();
    }

    /**
     * @return The {@link HttpWrapper}
     */
    HttpWrapper getHttpWrapper() {
        return httpWrapper;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private AuthCollection authCollection;
        private HttpConfig httpConfig = HttpConfig.defaultConfig();
        private HttpClient httpClient;
        private String applicationId;
        private String apiKey;
        private String apiSecret;
        private String signatureSecret;
        private byte[] privateKeyContents;

        /**
         * @param httpConfig Configuration options for the {@link HttpWrapper}
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder httpConfig(HttpConfig httpConfig) {
            this.httpConfig = httpConfig;
            return this;
        }

        /**
         * @param httpClient Custom implementation of {@link HttpClient}.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder httpClient(HttpClient httpClient) {
            this.httpClient = httpClient;
            return this;
        }

        /**
         * When setting an applicationId, it is also expected that the {@link #privateKeyContents} will also be set.
         *
         * @param applicationId Used to identify each application.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder applicationId(String applicationId) {
            this.applicationId = applicationId;
            return this;
        }

        /**
         * When setting an apiKey, it is also expected that {@link #apiSecret(String)} and/or {@link
         * #signatureSecret(String)} will also be set.
         *
         * @param apiKey The API Key found in the dashboard for your account.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        /**
         * When setting an apiSecret, it is also expected that {@link #apiKey(String)} will also be set.
         *
         * @param apiSecret The API Secret found in the dashboard for your account.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder apiSecret(String apiSecret) {
            this.apiSecret = apiSecret;
            return this;
        }

        /**
         * When setting a signatureSecret, it is also expected that {@link #apiKey(String)} will also be set.
         *
         * @param signatureSecret The Signature Secret found in the dashboard for your account.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder signatureSecret(String signatureSecret) {
            this.signatureSecret = signatureSecret;
            return this;
        }

        /**
         * When setting the contents of your private key, it is also expected that {@link #applicationId(String)} will
         * also be set.
         *
         * @param privateKeyContents The contents of your private key used for JWT generation.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder privateKeyContents(byte[] privateKeyContents) {
            this.privateKeyContents = privateKeyContents;
            return this;
        }

        /**
         * When setting the contents of your private key, it is also expected that {@link #applicationId(String)} will
         * also be set.
         *
         * @param privateKeyContents The contents of your private key used for JWT generation.
         *
         * @return The {@link Builder} to keep building.
         */
        public Builder privateKeyContents(String privateKeyContents) {
            return privateKeyContents(privateKeyContents.getBytes());
        }

        /**
         * When setting the path of your private key, it is also expected that {@link #applicationId(String)} will also
         * be set.
         *
         * @param privateKeyPath The path to your private key used for JWT generation.
         *
         * @return The {@link Builder} to keep building.
         *
         * @throws NexmoUnableToReadPrivateKeyException if the private key could not be read from the file system.
         */
        public Builder privateKeyPath(Path privateKeyPath) throws NexmoUnableToReadPrivateKeyException {
            try {
                return privateKeyContents(Files.readAllBytes(privateKeyPath));
            } catch (IOException e) {
                throw new NexmoUnableToReadPrivateKeyException("Unable to read private key at " + privateKeyPath, e);
            }
        }

        /**
         * When setting the path of your private key, it is also expected that {@link #applicationId(String)} will also
         * be set.
         *
         * @param privateKeyPath The path to your private key used for JWT generation.
         *
         * @return The {@link Builder} to keep building.
         *
         * @throws NexmoUnableToReadPrivateKeyException if the private key could not be read from the file system.
         */
        public Builder privateKeyPath(String privateKeyPath) throws NexmoUnableToReadPrivateKeyException {
            return privateKeyPath(Paths.get(privateKeyPath));
        }

        /**
         * @return a new {@link NexmoClient} from the stored builder options.
         *
         * @throws NexmoClientCreationException if credentials aren't provided in a valid pairing or there were issues
         *                                      generating an {@link JWTAuthMethod} with the provided credentials.
         */
        public NexmoClient build() {
            this.authCollection = generateAuthCollection(this.applicationId,
                    this.apiKey,
                    this.apiSecret,
                    this.signatureSecret,
                    this.privateKeyContents
            );
            return new NexmoClient(this);
        }

        private AuthCollection generateAuthCollection(String applicationId, String key, String secret, String signature, byte[] privateKeyContents) {
            AuthCollection authMethods = new AuthCollection();

            try {
                validateAuthParameters(applicationId, key, secret, signature, privateKeyContents);
            } catch (IllegalStateException e) {
                throw new NexmoClientCreationException("Failed to generate authentication methods.", e);
            }

            if (key != null && secret != null) {
                authMethods.add(new TokenAuthMethod(key, secret));
            }

            if (key != null && signature != null) {
                authMethods.add(new SignatureAuthMethod(key, signature));
            }

            if (applicationId != null && privateKeyContents != null) {
                authMethods.add(new JWTAuthMethod(applicationId, privateKeyContents));
            }

            return authMethods;
        }

        private void validateAuthParameters(String applicationId, String key, String secret, String signature, byte[] privateKeyContents) {
            if (key != null && secret == null && signature == null) {
                throw new IllegalStateException(
                        "You must provide an API secret or signature secret in addition to your API key.");
            }

            if (secret != null && key == null) {
                throw new IllegalStateException("You must provide an API key in addition to your API secret.");
            }

            if (signature != null && key == null) {
                throw new IllegalStateException("You must provide an API key in addition to your signature secret.");
            }

            if (applicationId == null && privateKeyContents != null) {
                throw new IllegalStateException("You must provide an application ID in addition to your private key.");
            }

            if (applicationId != null && privateKeyContents == null) {
                throw new IllegalStateException("You must provide a private key in addition to your application id.");
            }
        }
    }
}
