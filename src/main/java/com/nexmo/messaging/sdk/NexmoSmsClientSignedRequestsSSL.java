package com.nexmo.messaging.sdk;

/**
 * NexmoSmsClientSignedRequests.java<br><br>
 *
 * Client for talking to the Nexmo REST interface that signs requests using a MD5 signature derived from a secret key,
 * and that also uses a secure SSL / HTTPS connection to encrypt the requests<br><br>
 *
 * For usage information see {@link com.nexmo.messaging.sdk.NexmoSmsClient}<br>
 *
 * @author  Paul Cook
 * @version 1.0
 */
public class NexmoSmsClientSignedRequestsSSL extends NexmoSmsClient {

    /**
     * Instanciate a new NexmoSmsClientSignedRequests instance that will communicate using the supplied credentials.
     *
     * @param apiKey Your Nexmo account api key
     * @param signatureSecretKey Your Nexmo account secret key for signing api requests
     */
    public NexmoSmsClientSignedRequestsSSL(final String apiKey,
                                           final String signatureSecretKey) throws Exception {
        super(DEFAULT_BASE_URL,
             apiKey,
             null,   // apiKey,
             DEFAULT_CONNECTION_TIMEOUT,
             DEFAULT_SO_TIMEOUT,
             true,   // signRequests
             signatureSecretKey,
             true);  // useSSL
    }

    /**
     * Instanciate a new NexmoSmsClientSignedRequests instance that will communicate using the supplied credentials, and will use the supplied connection and read timeout values.
     *
     * @param apiKey Your Nexmo account api key
     * @param signatureSecretKey Your Nexmo account secret key for signing api requests
     * @param connectionTimeout over-ride the default connection timeout with this value (in milliseconds)
     * @param soTimeout over-ride the default read-timeout with this value (in milliseconds)
     */
    public NexmoSmsClientSignedRequestsSSL(final String apiKey,
                                           final String signatureSecretKey,
                                           final int connectionTimeout,
                                           final int soTimeout) throws Exception {
        super(DEFAULT_BASE_URL,
             apiKey,
             null,   // apiKey,
             connectionTimeout,
             soTimeout,
             true,   // signRequests
             signatureSecretKey,
             true);  // useSSL
    }

}
