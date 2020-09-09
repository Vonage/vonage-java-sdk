/*
 * Copyright (c) 2020 Vonage
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
package com.vonage.client.account;

import com.vonage.client.*;

/**
 * A client for talking to the Vonage Account API. The standard way to obtain an instance of this class is to use {@link
 * VonageClient#getAccountClient()} ()}.
 */
public class AccountClient extends AbstractClient {
    protected BalanceEndpoint balance;
    protected PricingEndpoint pricing;
    protected PrefixPricingEndpoint prefixPricing;
    protected TopUpEndpoint topUp;
    protected SecretManagementEndpoint secret;
    protected SettingsEndpoint settings;

    /**
     * Constructor.
     *
     * @param httpWrapper (required) shared HTTP wrapper object used for making REST calls.
     */
    public AccountClient(HttpWrapper httpWrapper) {
        super(httpWrapper);

        this.balance = new BalanceEndpoint(httpWrapper);
        this.pricing = new PricingEndpoint(httpWrapper);
        this.prefixPricing = new PrefixPricingEndpoint(httpWrapper);
        this.topUp = new TopUpEndpoint(httpWrapper);
        this.secret = new SecretManagementEndpoint(httpWrapper);
        this.settings = new SettingsEndpoint(httpWrapper);
    }

    public BalanceResponse getBalance() throws VonageResponseParseException, VonageClientException {
        return this.balance.execute();
    }

    /**
     * Retrieve the voice pricing for a specified country.
     *
     * @param country The two-character country code for which you would like to retrieve pricing.
     *
     * @return PricingResponse object which contains the results from the API.
     *
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     */
    public PricingResponse getVoicePrice(String country) throws VonageResponseParseException, VonageClientException {
        return getVoicePrice(new PricingRequest(country));
    }

    private PricingResponse getVoicePrice(PricingRequest pricingRequest) throws VonageResponseParseException, VonageClientException {
        return this.pricing.getPrice(ServiceType.VOICE, pricingRequest);
    }

    /**
     * Retrieve the SMS pricing for a specified country.
     *
     * @param country The two-character country code for which you would like to retrieve pricing.
     *
     * @return PricingResponse object which contains the results from the API.
     *
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     */
    public PricingResponse getSmsPrice(String country) throws VonageResponseParseException, VonageClientException {
        return getSmsPrice(new PricingRequest(country));
    }

    private PricingResponse getSmsPrice(PricingRequest pricingRequest) throws VonageResponseParseException, VonageClientException {
        return this.pricing.getPrice(ServiceType.SMS, pricingRequest);
    }

    /**
     * Retrieve the pricing for a specified prefix.
     *
     * @param type   The type of service to retrieve pricing for.
     * @param prefix The prefix to retrieve the pricing for.
     *
     * @return PrefixPricingResponse object which contains the results from the API.
     *
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     * @throws VonageClientException        if there was a problem with the Vonage request or response objects.
     */
    public PrefixPricingResponse getPrefixPrice(ServiceType type, String prefix) throws VonageResponseParseException, VonageClientException {
        return getPrefixPrice(new PrefixPricingRequest(type, prefix));
    }

    private PrefixPricingResponse getPrefixPrice(PrefixPricingRequest prefixPricingRequest) throws VonageResponseParseException, VonageClientException {
        return this.prefixPricing.getPrice(prefixPricingRequest);
    }

    /**
     * Top-up your account when you have enabled auto-reload in the dashboard. Amount added is based on your initial
     * reload-enabled payment.
     *
     * @param transaction The ID associated with your original auto-reload transaction
     *
     * @throws VonageResponseParseException if the response from the API could not be parsed.
     * @throws VonageClientException        if there was a problem with the Vonage request or response object indicating
     *                                     that the request was unsuccessful.
     */
    public void topUp(String transaction) throws VonageResponseParseException, VonageClientException {
        topUp(new TopUpRequest(transaction));
    }

    private void topUp(TopUpRequest request) throws VonageResponseParseException, VonageClientException {
        this.topUp.topUp(request);
    }

    /**
     * List the ID of each secret associated to the given API key.
     *
     * @param apiKey The API key to look up secrets for.
     *
     * @return ListSecretsResponse object which contains the results from the API.
     *
     * @throws VonageResponseParseException if a network error occurred contacting the Vonage Account API
     * @throws VonageClientException        if there was a problem with the Vonage request or response object indicating
     *                                     that the request was unsuccessful.
     */
    public ListSecretsResponse listSecrets(String apiKey) throws VonageResponseParseException, VonageClientException {
        return this.secret.listSecrets(apiKey);
    }

    /**
     * Get information for a specific secret id associated to a given API key.
     *
     * @param apiKey   The API key that the secret is associated to.
     * @param secretId The id of the secret to get information on.
     *
     * @return SecretResponse object which contains the results from the API.
     *
     * @throws VonageResponseParseException if a network error occurred contacting the Vonage Account API
     * @throws VonageClientException        if there was a problem with the Vonage request or response object indicating
     *                                     that the request was unsuccessful.
     */
    public SecretResponse getSecret(String apiKey, String secretId) throws VonageResponseParseException, VonageClientException {
        return getSecret(new SecretRequest(apiKey, secretId));
    }

    private SecretResponse getSecret(SecretRequest secretRequest) throws VonageResponseParseException, VonageClientException {
        return this.secret.getSecret(secretRequest);
    }

    /**
     * Create a secret to be used with a specific API key.
     *
     * @param apiKey The API key that the secret is to be used with.
     * @param secret The contents of the secret.
     *
     * @return SecretResponse object which contains the created secret from the API.
     *
     * @throws VonageResponseParseException if a network error occurred contacting the Vonage Account API
     * @throws VonageClientException        if there was a problem with the Vonage request or response object indicating
     *                                     that the request was unsuccessful.
     */
    public SecretResponse createSecret(String apiKey, String secret) throws VonageResponseParseException, VonageClientException {
        return createSecret(new CreateSecretRequest(apiKey, secret));
    }

    private SecretResponse createSecret(CreateSecretRequest createSecretRequest) throws VonageResponseParseException, VonageClientException {
        return this.secret.createSecret(createSecretRequest);
    }

    /**
     * Revoke a secret associated with a specific API key.
     *
     * @param apiKey   The API key that the secret is associated to.
     * @param secretId The id of the secret to revoke.
     *
     * @throws VonageResponseParseException if a network error occurred contacting the Vonage Account API
     * @throws VonageClientException        if there was a problem with the Vonage request or response object indicating
     *                                     that the request was unsuccessful.
     */
    public void revokeSecret(String apiKey, String secretId) throws VonageResponseParseException, VonageClientException {
        revokeSecret(new SecretRequest(apiKey, secretId));
    }

    private void revokeSecret(SecretRequest secretRequest) throws VonageResponseParseException, VonageClientException {
        this.secret.revokeSecret(secretRequest);
    }

    /**
     * @param url The new incoming sms webhook url to associate to your account.
     *
     * @return A {@link SettingsResponse} containing the newly-updated account settings.
     *
     * @throws VonageResponseParseException if a network error occurred contacting the Vonage Account API
     * @throws VonageClientException        if there was a problem with the Vonage request or response object indicating
     *                                     that the request was unsuccessful.
     */
    public SettingsResponse updateSmsIncomingUrl(String url) throws VonageResponseParseException, VonageClientException {
        return this.updateSettings(SettingsRequest.withIncomingSmsUrl(url));
    }

    /**
     * @param url The new delivery receipt webhook url to associate to your account.
     *
     * @return A {@link SettingsResponse} containing the newly-updated account settings.
     *
     * @throws VonageResponseParseException if a network error occurred contacting the Vonage Account API
     * @throws VonageClientException        if there was a problem with the Vonage request or response object indicating
     *                                     that the request was unsuccessful.
     */
    public SettingsResponse updateDeliveryReceiptUrl(String url) throws VonageResponseParseException, VonageClientException {
        return this.updateSettings(SettingsRequest.withDeliveryReceiptUrl(url));
    }

    /**
     * @param request The {@link SettingsRequest} containing the fields to update.
     *
     * @return A {@link SettingsResponse} containing the newly-updated account settings.
     *
     * @throws VonageResponseParseException if a network error occurred contacting the Vonage Account API
     * @throws VonageClientException        if there was a problem with the Vonage request or response object indicating
     *                                     that the request was unsuccessful.
     */
    public SettingsResponse updateSettings(SettingsRequest request) throws VonageResponseParseException, VonageClientException {
        return this.settings.updateSettings(request);
    }
}
