/*
 *   Copyright 2025 Vonage
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.vonage.client;

import com.vonage.client.account.AccountClient;
import com.vonage.client.application.ApplicationClient;
import com.vonage.client.auth.*;
import com.vonage.client.auth.hashutils.HashUtil;
import com.vonage.client.camara.numberverification.NumberVerificationClient;
import com.vonage.client.camara.simswap.SimSwapClient;
import com.vonage.client.conversations.ConversationsClient;
import com.vonage.client.conversion.ConversionClient;
import com.vonage.client.insight.InsightClient;
import com.vonage.client.meetings.MeetingsClient;
import com.vonage.client.messages.MessagesClient;
import com.vonage.client.numberinsight2.NumberInsight2Client;
import com.vonage.client.numbers.NumbersClient;
import com.vonage.client.proactiveconnect.ProactiveConnectClient;
import com.vonage.client.redact.RedactClient;
import com.vonage.client.sms.SmsClient;
import com.vonage.client.subaccounts.SubaccountsClient;
import com.vonage.client.users.UsersClient;
import com.vonage.client.verify.VerifyClient;
import com.vonage.client.video.VideoClient;
import com.vonage.client.verify2.Verify2Client;
import com.vonage.client.voice.VoiceClient;
import org.apache.http.client.HttpClient;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

/**
 * Top-level Vonage API client object.
 * <p>
 * Construct an instance of this object with one or more {@link AuthMethod}s (providing all the authentication methods
 * for the APIs you wish to use), and then call {@link #getVoiceClient()} to obtain a client for the Vonage Voice API.
 * <p>.
 */
public class VonageClient {
    /**
     * The HTTP wrapper for this client and its sub-clients.
     */
    final HttpWrapper httpWrapper;

    private final AccountClient account;
    private final ApplicationClient application;
    private final InsightClient insight;
    private final NumbersClient numbers;
    private final SmsClient sms;
    private final VoiceClient voice;
    private final VerifyClient verify;
    private final ConversionClient conversion;
    private final RedactClient redact;
    private final MessagesClient messages;
    private final Verify2Client verify2;
    private final SubaccountsClient subaccounts;
    private final ProactiveConnectClient proactiveConnect;
    private final MeetingsClient meetings;
    private final UsersClient users;
    private final VideoClient video;
    private final NumberInsight2Client numberInsight2;
    private final ConversationsClient conversations;
    private final SimSwapClient simSwap;
    private final NumberVerificationClient numberVerification;

    /**
     * Constructor which uses the builder pattern for instantiation.
     *
     * @param builder The builder object to use for configuration.
     */
    private VonageClient(Builder builder) {
        httpWrapper = new HttpWrapper(builder.httpConfig, builder.authCollection);
        if (builder.httpClient != null) {
            httpWrapper.setHttpClient(builder.httpClient);
        }

        account = new AccountClient(httpWrapper);
        application = new ApplicationClient(httpWrapper);
        insight = new InsightClient(httpWrapper);
        numbers = new NumbersClient(httpWrapper);
        verify = new VerifyClient(httpWrapper);
        voice = new VoiceClient(httpWrapper);
        sms = new SmsClient(httpWrapper);
        conversion = new ConversionClient(httpWrapper);
        redact = new RedactClient(httpWrapper);
        messages = new MessagesClient(httpWrapper);
        verify2 = new Verify2Client(httpWrapper);
        subaccounts = new SubaccountsClient(httpWrapper);
        proactiveConnect = new ProactiveConnectClient(httpWrapper);
        meetings = new MeetingsClient(httpWrapper);
        users = new UsersClient(httpWrapper);
        video = new VideoClient(httpWrapper);
        numberInsight2 = new NumberInsight2Client(httpWrapper);
        conversations = new ConversationsClient(httpWrapper);
        simSwap = new SimSwapClient(httpWrapper);
        numberVerification = new NumberVerificationClient(httpWrapper);
    }

    /**
     * Returns the Account API client.
     *
     * @return The {@linkplain AccountClient}.
     */
    public AccountClient getAccountClient() {
        return account;
    }

    /**
     * Returns the Application API client.
     *
     * @return The {@linkplain ApplicationClient}.
     */
    public ApplicationClient getApplicationClient() {
        return application;
    }

    /**
     * Returns the Number Insight API client.
     *
     * @return The {@linkplain InsightClient}.
     */
    public InsightClient getInsightClient() {
        return insight;
    }

    /**
     * Returns the Numbers API client.
     *
     * @return The {@linkplain NumbersClient}.
     */
    public NumbersClient getNumbersClient() {
        return numbers;
    }

    /**
     * Returns the SMS API client.
     *
     * @return The {@linkplain SmsClient}.
     */
    public SmsClient getSmsClient() {
        return sms;
    }

    /**
     * Returns the Verify v1 API client.
     *
     * @return The {@linkplain VerifyClient}.
     */
    public VerifyClient getVerifyClient() {
        return verify;
    }

    /**
     * Returns the Voice API client.
     *
     * @return The {@linkplain VoiceClient}.
     */
    public VoiceClient getVoiceClient() {
        return voice;
    }

    /**
     * Returns the Conversion API client.
     *
     * @return The {@linkplain ConversionClient}.
     */
    public ConversionClient getConversionClient() {
        return conversion;
    }

    /**
     * Returns the Redact API client.
     *
     * @return The {@linkplain RedactClient}.
     */
    public RedactClient getRedactClient() {
        return redact;
    }

    /**
     * Returns the Messages v1 API client.
     *
     * @return The {@linkplain MessagesClient}.
     * @since 6.5.0
     */
    public MessagesClient getMessagesClient() {
        return messages;
    }

    /**
     * Returns the Proactive Connect API client.
     *
     * @return The {@linkplain ProactiveConnectClient}.
     * @since 7.6.0
     * @deprecated This API is sunset and will be removed in the next major release.
     */
    @Deprecated
    public ProactiveConnectClient getProactiveConnectClient() {
        return proactiveConnect;
    }

    /**
     * Returns the Meetings API client.
     *
     * @return The {@linkplain MeetingsClient}.
     * @since 7.6.0
     * @deprecated Support for this API will be removed in the next major release.
     */
    @Deprecated
    public MeetingsClient getMeetingsClient() {
        return meetings;
    }

    /**
     * Returns the Verify v2 API client.
     *
     * @return The {@linkplain Verify2Client}.
     * @since 7.4.0
     */
    public Verify2Client getVerify2Client() {
        return verify2;
    }

    /**
     * Returns the Subaccounts API client.
     *
     * @return The {@linkplain SubaccountsClient}.
     * @since 7.5.0
     */
    public SubaccountsClient getSubaccountsClient() {
        return subaccounts;
    }

    /**
     * Returns the Users API client.
     *
     * @return The {@linkplain UsersClient}.
     * @since 7.7.0
     */
    public UsersClient getUsersClient() {
        return users;
    }

    /**
     * Returns the Video API client.
     *
     * @return The {@linkplain VideoClient}.
     * @since 8.0.0-beta1
     */
    public VideoClient getVideoClient() {
        return video;
    }

    /**
     * Returns the Fraud Detection API client.
     *
     * @return The {@linkplain NumberInsight2Client}.
     * @since 8.2.0
     */
    public NumberInsight2Client getNumberInsight2Client() {
        return numberInsight2;
    }

    /**
     * Returns the Conversations v1 client.
     *
     * @return The Conversation client.
     * @since 8.4.0
     */
    public ConversationsClient getConversationsClient() {
        return conversations;
    }

    /**
     * Returns the CAMARA SIM Swap API client.
     *
     * @return The {@linkplain SimSwapClient}.
     * @since 8.8.0
     */
    public SimSwapClient getSimSwapClient() {
        return simSwap;
    }

    /**
     * Returns the CAMARA Number Verification API client.
     *
     * @return The {@linkplain NumberVerificationClient}.
     * @since 8.9.0
     */
    public NumberVerificationClient getNumberVerificationClient() {
        return numberVerification;
    }

    /**
     * Generate a JWT for the application the client has been configured with.
     *
     * @return A String containing the token data.
     *
     * @throws VonageUnacceptableAuthException if no {@link JWTAuthMethod} is available
     */
    public String generateJwt() throws VonageUnacceptableAuthException {
        return httpWrapper.getAuthCollection().getAuth(JWTAuthMethod.class).generateToken();
    }

    /**
     * Entry point for constructing an instance of this class.
     *
     * @return A new Builder with default initial configuration.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for specifying the properties of the client.
     */
    public static class Builder {
        private AuthCollection authCollection;
        private HttpConfig httpConfig = HttpConfig.defaultConfig();
        private HttpClient httpClient;
        private String apiKey, apiSecret, signatureSecret;
        private UUID applicationId;
        private byte[] privateKeyContents;
        private HashUtil.HashType hashType = HashUtil.HashType.MD5;

        /**
         * Configure the HTTP client parameters.
         *
         * @param httpConfig Configuration options for the {@link HttpWrapper}.
         *
         * @return This builder.
         */
        public Builder httpConfig(HttpConfig httpConfig) {
            this.httpConfig = httpConfig;
            return this;
        }

        /**
         * Set the underlying HTTP client instance.
         *
         * @param httpClient Custom implementation of {@link HttpClient}.
         *
         * @return This builder.
         *
         * @deprecated This method will be removed in the next major release.
         */
        @Deprecated
        public Builder httpClient(HttpClient httpClient) {
            this.httpClient = httpClient;
            return this;
        }

        /**
         * Set the application ID for this client. This will be used alongside your private key
         * (se via {@link #privateKeyContents}) for authenticating requests.
         *
         * @param applicationId The application UUID.
         *
         * @return This builder.
         *
         * @since 7.11.0
         */
        public Builder applicationId(UUID applicationId) {
            this.applicationId = applicationId;
            return this;
        }

        /**
         * When setting an applicationId, it is also expected that the {@link #privateKeyContents} will also be set.
         *
         * @param applicationId Used to identify each application.
         *
         * @return This builder.
         */
        public Builder applicationId(String applicationId) {
            return applicationId(UUID.fromString(applicationId));
        }

        /**
         * When setting an apiKey, it is also expected that {@link #apiSecret(String)} and/or {@link
         * #signatureSecret(String)} will also be set.
         *
         * @param apiKey The API Key found in the dashboard for your account.
         *
         * @return This builder.
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
         * @return This builder.
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
         * @return This builder.
         */
        public Builder signatureSecret(String signatureSecret) {
            this.signatureSecret = signatureSecret;
            return this;
        }

        /**
         * Sets the hash type to use for signing requests.
         *
         * @param hashType The hashing strategy for signature keys as an enum.
         *
         * @return This builder.
         */
        public Builder hashType(HashUtil.HashType hashType) {
            this.hashType = hashType;
            return this;
        }

        /**
         * When setting the contents of your private key, it is also expected that {@link #applicationId(String)} will
         * also be set.
         *
         * @param privateKeyContents The contents of your private key used for JWT generation.
         *
         * @return This builder.
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
         * @return This builder.
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
         * @return This builder.
         *
         * @throws VonageUnableToReadPrivateKeyException if the private key could not be read from the file system.
         */
        public Builder privateKeyPath(Path privateKeyPath) throws VonageUnableToReadPrivateKeyException {
            try {
                return privateKeyContents(Files.readAllBytes(privateKeyPath));
            } catch (IOException e) {
                throw new VonageUnableToReadPrivateKeyException("Unable to read private key at " + privateKeyPath, e);
            }
        }

        /**
         * When setting the path of your private key, it is also expected that {@link #applicationId(String)} will also
         * be set.
         *
         * @param privateKeyPath The path to your private key used for JWT generation.
         *
         * @return This builder.
         *
         * @throws VonageUnableToReadPrivateKeyException if the private key could not be read from the file system.
         */
        public Builder privateKeyPath(String privateKeyPath) throws VonageUnableToReadPrivateKeyException {
            return privateKeyPath(Paths.get(privateKeyPath));
        }

        /**
         * Builds the client with this builder's parameters.
         *
         * @return A new {@link VonageClient} from the stored builder options.
         *
         * @throws VonageClientCreationException if credentials aren't provided in a valid pairing or there were issues
         *                                      generating an {@link JWTAuthMethod} with the provided credentials.
         */
        public VonageClient build() {
            try {
                authCollection = new AuthCollection(
                        applicationId, privateKeyContents,
                        apiKey, apiSecret,
                        hashType, signatureSecret
                );
            }
            catch (IllegalStateException ex) {
                throw new VonageClientCreationException("Failed to generate authentication methods.", ex);
            }
          
            return new VonageClient(this);
        }
    }
}
