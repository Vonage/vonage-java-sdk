package com.nexmo.messaging.sdk;

/**
 * NexmoSmsClientSSL.java<br><br>
 *
 * Client for talking to the Nexmo REST interface that uses uses a secure SSL / HTTPS connection to encrypt the requests<br><br>
 *
 * For usage information see {@link com.nexmo.messaging.sdk.NexmoSmsClient}<br>
 *
 * @author  Paul Cook
 * @version 1.0
 */
public class NexmoSmsClientSSL extends NexmoSmsClient {

    /**
     * Instanciate a new NexmoSmsClientSSL instance that will communicate using the supplied credentials.
     *
     * @param apiKey Your Nexmo account api key
     * @param apiSecret Your Nexmo account api secret
     */
    public NexmoSmsClientSSL(final String apiKey, final String apiSecret) throws Exception {
        super(DEFAULT_BASE_URL,
              apiKey,
              apiSecret,
              DEFAULT_CONNECTION_TIMEOUT,
              DEFAULT_SO_TIMEOUT,
              false,  // signRequests
              null,   // signatureSecretKey
              true);  // useSSL
    }

    /**
     * Instanciate a new NexmoSmsClientSSL instance that will communicate using the supplied credentials, and will use the supplied connection and read timeout values.
     *
     * @param apiKey Your Nexmo account api key
     * @param apiSecret Your Nexmo account api secret
     * @param connectionTimeout over-ride the default connection timeout with this value (in milliseconds)
     * @param soTimeout over-ride the default read-timeout with this value (in milliseconds)
     */
    public NexmoSmsClientSSL(final String apiKey,
                             final String apiSecret,
                             final int connectionTimeout,
                             final int soTimeout) throws Exception {
        super(DEFAULT_BASE_URL,
              apiKey,
              apiSecret,
              connectionTimeout,
              soTimeout,
              false,  // signRequests
              null,   // signatureSecretKey
              true);  // useSSL
    }

}
