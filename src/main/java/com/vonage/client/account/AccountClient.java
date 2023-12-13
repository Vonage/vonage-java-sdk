/*
 *   Copyright 2023 Vonage
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
package com.vonage.client.account;

import com.vonage.client.*;
import com.vonage.client.auth.SignatureAuthMethod;
import com.vonage.client.auth.TokenAuthMethod;
import com.vonage.client.common.HttpMethod;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A client for talking to the Vonage Account API. The standard way to obtain an instance of this class is to use {@link
 * VonageClient#getAccountClient()}.
 */
public class AccountClient {
    final Supplier<String> apiKeyGetter;

    final RestEndpoint<Void, BalanceResponse> balance;
    final RestEndpoint<PricingRequest, PricingResponse> pricing;
    final RestEndpoint<FullPricingRequest, FullPricingResponse> fullPricing;
    final RestEndpoint<PrefixPricingRequest, PrefixPricingResponse> prefixPricing;
    final RestEndpoint<TopUpRequest, Void> topUp;
    final RestEndpoint<SettingsRequest, SettingsResponse> settings;
    final RestEndpoint<String, ListSecretsResponse> listSecrets;
    final RestEndpoint<SecretRequest, SecretResponse> getSecret;
    final RestEndpoint<CreateSecretRequest, SecretResponse> createSecret;
    final RestEndpoint<SecretRequest, Void> revokeSecret;

    /**
     * Constructor.
     *
     * @param wrapper (required) shared HTTP wrapper object used for making REST calls.
     */
    @SuppressWarnings("unchecked")
    public AccountClient(HttpWrapper wrapper) {
        apiKeyGetter = () -> wrapper.getAuthCollection().getAuth(TokenAuthMethod.class).getApiKey();

        class Endpoint<T, R> extends DynamicEndpoint<T, R> {
            static final String SECRETS_PATH = "s/%s/secrets";

            Endpoint(Function<T, String> pathGetter, R... type) {
                this(pathGetter, HttpMethod.GET, type);
            }
            Endpoint(Function<T, String> pathGetter, HttpMethod method, R... type) {
                this(pathGetter, method, false, method == HttpMethod.POST, type);
            }
            Endpoint(Function<T, String> pathGetter, HttpMethod method,
                     boolean signatureAuth, boolean formEncoded, R... type
            ) {
                super(DynamicEndpoint.<T, R> builder(type)
                        .wrapper(wrapper).requestMethod(method).applyAsBasicAuth(signatureAuth)
                        .authMethod(TokenAuthMethod.class, signatureAuth ? SignatureAuthMethod.class : null)
                        .responseExceptionType(AccountResponseException.class)
                        .contentTypeHeader(formEncoded ? "application/x-www-form-urlencoded" : null)
                        .pathGetter((de, req) -> {
                            HttpConfig config = de.getHttpWrapper().getHttpConfig();
                            String base = signatureAuth ? config.getApiBaseUri() : config.getRestBaseUri();
                            return base + "/account" + pathGetter.apply(req);
                        })
                );
            }
        }

        class SecretsEndpoint<T, R> extends Endpoint<T, R> {
            SecretsEndpoint(Function<T, String> apiKeyGetter, HttpMethod method, R... type) {
                super(req -> String.format(SECRETS_PATH, apiKeyGetter.apply(req)), method, true, false, type);
            }
        }

        class SecretRequestEndpoint<R> extends Endpoint<SecretRequest, R> {
            SecretRequestEndpoint(HttpMethod method, R... type) {
                super(req -> String.format(SECRETS_PATH, req.getApiKey()) + "/" + req.getSecretId(),
                        method, true, false, type
                );
            }
        }

        balance = new Endpoint<>(req -> "/get-balance");
        pricing = new Endpoint<>(req -> "/get-pricing/outbound/" + req.getServiceType());
        fullPricing = new Endpoint<>(req -> "/get-full-pricing/outbound/" + req.getServiceType());
        prefixPricing = new Endpoint<>(req -> "/get-prefix-pricing/outbound/" + req.getServiceType());
        topUp = new Endpoint<>(req -> "/top-up", HttpMethod.POST);
        settings = new Endpoint<>(req -> "/settings", HttpMethod.POST);
        listSecrets = new SecretsEndpoint<>(Function.identity(), HttpMethod.GET);
        createSecret = new SecretsEndpoint<>(CreateSecretRequest::getApiKey, HttpMethod.POST);
        getSecret = new SecretRequestEndpoint<>(HttpMethod.GET);
        revokeSecret = new SecretRequestEndpoint<>(HttpMethod.DELETE);
    }

    /**
     * Obtains the current account remaining balance.
     *
     * @return The account balance along with other metadata.
     *
     * @throws AccountResponseException If the balance could not be retrieved.
     */
    public BalanceResponse getBalance() throws AccountResponseException {
        return balance.execute(null);
    }

    /**
     *
     * @param service The service type to retrieve pricing and network information for.
     *
     * @return The
     */
    public List<PricingResponse> listPriceAllCountries(ServiceType service) {
        Objects.requireNonNull(service, "Service type is required.");
        return fullPricing.execute(new FullPricingRequest(service)).getCountries();
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
    public PricingResponse getVoicePrice(String country) throws AccountResponseException {
        return pricing.execute(new PricingRequest(country, ServiceType.VOICE));
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
    public PricingResponse getSmsPrice(String country) throws AccountResponseException {
        return pricing.execute(new PricingRequest(country, ServiceType.SMS));
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
    public PrefixPricingResponse getPrefixPrice(ServiceType type, String prefix) throws AccountResponseException {
        return prefixPricing.execute(new PrefixPricingRequest(type, prefix));
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
    public void topUp(String transaction) throws AccountResponseException {
        topUp.execute(new TopUpRequest(transaction));
    }

    /**
     * List the ID of each secret associated with this account's main API key.
     *
     * @return ListSecretsResponse object which contains the results from the API.
     *
     * @throws AccountResponseException If there was an error making the request or retrieving the response.
     *
     * @since 7.9.0
     */
    public ListSecretsResponse listSecrets() throws AccountResponseException {
        return listSecrets(apiKeyGetter.get());
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
    public ListSecretsResponse listSecrets(String apiKey) throws AccountResponseException {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new IllegalArgumentException("API key is required.");
        }
        return listSecrets.execute(apiKey);
    }

    /**
     * Get information for a specific secret id associated with this account's main API key.
     *
     * @param secretId The id of the secret to get information on.
     *
     * @return SecretResponse object which contains the results from the API.
     *
     * @throws AccountResponseException If there was an error making the request or retrieving the response.
     *
     * @since 7.9.0
     */
    public SecretResponse getSecret(String secretId) throws AccountResponseException {
        return getSecret(apiKeyGetter.get(), secretId);
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
    public SecretResponse getSecret(String apiKey, String secretId) throws AccountResponseException {
        return getSecret.execute(new SecretRequest(apiKey, secretId));
    }

    /**
     * Create a secret to be used with this account's main API key.
     *
     * @param secret The contents of the secret.
     *
     * @return SecretResponse object which contains the created secret from the API.
     *
     * @throws AccountResponseException If there was an error making the request or retrieving the response.
     *
     * @since 7.9.0
     */
    public SecretResponse createSecret(String secret) throws AccountResponseException {
        return createSecret(apiKeyGetter.get(), secret);
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
    public SecretResponse createSecret(String apiKey, String secret) throws AccountResponseException {
        return createSecret.execute(new CreateSecretRequest(apiKey, secret));
    }

    /**
     * Revoke a secret associated with this account's main API key.
     *
     * @param secretId The id of the secret to revoke.
     *
     * @throws AccountResponseException If there was an error making the request or retrieving the response.
     *
     * @since 7.9.0
     */
    public void revokeSecret(String secretId) throws AccountResponseException {
        revokeSecret(apiKeyGetter.get(), secretId);
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
    public void revokeSecret(String apiKey, String secretId) throws AccountResponseException {
        revokeSecret.execute(new SecretRequest(apiKey, secretId));
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
    public SettingsResponse updateSmsIncomingUrl(String url) throws AccountResponseException {
        return updateSettings(SettingsRequest.withIncomingSmsUrl(url));
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
    public SettingsResponse updateDeliveryReceiptUrl(String url) throws AccountResponseException {
        return updateSettings(SettingsRequest.withDeliveryReceiptUrl(url));
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
    public SettingsResponse updateSettings(SettingsRequest request) throws AccountResponseException {
        return settings.execute(Objects.requireNonNull(request, "Settings request cannot be null."));
    }
}
