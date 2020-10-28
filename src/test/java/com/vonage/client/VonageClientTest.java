/*
 *   Copyright 2020 Vonage
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


import com.vonage.client.auth.*;
import com.vonage.client.auth.hashutils.HashUtil;
import com.vonage.client.logging.LoggingUtils;
import com.vonage.client.voice.Call;
import com.vonage.client.voice.CallEvent;
import com.vonage.client.voice.CallStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(LoggingUtils.class)
@PowerMockIgnore("javax.crypto.*")
public class VonageClientTest {
    private TestUtils testUtils = new TestUtils();

    private HttpClient stubHttpClient(int statusCode, String content) throws Exception {
        HttpClient result = mock(HttpClient.class);
        mockStatic(LoggingUtils.class);

        HttpResponse response = mock(HttpResponse.class);
        StatusLine sl = mock(StatusLine.class);
        HttpEntity entity = mock(HttpEntity.class);

        when(result.execute(any(HttpUriRequest.class))).thenReturn(response);
        when(LoggingUtils.logResponse(any(HttpResponse.class))).thenReturn("response logged");
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
        when(sl.getStatusCode()).thenReturn(statusCode);
        when(response.getStatusLine()).thenReturn(sl);
        when(response.getEntity()).thenReturn(entity);

        return result;
    }

    @Test
    public void testConstructVonageClient() throws Exception {
        byte[] keyBytes = testUtils.loadKey("test/keys/application_key");
        VonageClient client = VonageClient.builder()
                .applicationId("951614e0-eec4-4087-a6b1-3f4c2f169cb0")
                .privateKeyContents(keyBytes)
                .httpClient(stubHttpClient(
                        200,
                        "{\n" + "  \"conversation_uuid\": \"63f61863-4a51-4f6b-86e1-46edebio0391\",\n"
                                + "  \"status\": \"started\",\n" + "  \"direction\": \"outbound\"\n" + "}"
                ))
                .build();

        CallEvent evt = client
                .getVoiceClient()
                .createCall(new Call("4499991111", "44111222333", "https://callback.example.com/"));
        assertEquals(CallStatus.STARTED, evt.getStatus());
    }

    @Test
    public void testConstructVonageClientWithDifferentHash() throws Exception {
        byte[] keyBytes = testUtils.loadKey("test/keys/application_key");
        VonageClient client = VonageClient.builder()
                .applicationId("951614e0-eec4-4087-a6b1-3f4c2f169cb0")
                .privateKeyContents(keyBytes)
                .hashType(HashUtil.HashType.HMAC_SHA256)
                .httpClient(stubHttpClient(
                        200,
                        "{\n" + "  \"conversation_uuid\": \"63f61863-4a51-4f6b-86e1-46edebio0391\",\n"
                                + "  \"status\": \"started\",\n" + "  \"direction\": \"outbound\"\n" + "}"
                ))
                .build();

        CallEvent evt = client
                .getVoiceClient()
                .createCall(new Call("4499991111", "44111222333", "https://callback.example.com/"));
        assertEquals(CallStatus.STARTED, evt.getStatus());
    }

    @Test
    public void testGenerateJwt() throws Exception {
        byte[] privateKeyBytes = testUtils.loadKey("test/keys/application_key");
        VonageClient client = VonageClient.builder()
                .applicationId("application-id")
                .privateKeyContents(privateKeyBytes)
                .build();

        String constructedToken = client.generateJwt();

        byte[] publicKeyBytes = testUtils.loadKey("test/keys/application_public_key.der");
        X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey key = kf.generatePublic(spec);

        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(constructedToken).getBody();

        assertEquals("application-id", claims.get("application_id"));
    }

    @Test(expected = VonageClientCreationException.class)
    public void testSoloApiKeyThrowsException() {
        VonageClient.builder().apiKey("api-key").build();
    }

    @Test(expected = VonageClientCreationException.class)
    public void testSoloApiSecret() {
        VonageClient.builder().apiSecret("api-secret").build();
    }

    @Test(expected = VonageClientCreationException.class)
    public void testSoloSignatureSecret() {
        VonageClient.builder().signatureSecret("api-secret").build();
    }

    @Test(expected = VonageClientCreationException.class)
    public void testSoloApplicationId() {
        VonageClient.builder().applicationId("app-id").build();
    }

    @Test(expected = VonageClientCreationException.class)
    public void testSoloPrivateKeyContents() {
        VonageClient.builder().privateKeyContents("").build();
    }

    @Test
    public void testApiKeyWithSecret() throws VonageUnacceptableAuthException {
        VonageClient vonageClient = VonageClient.builder().apiKey("api-key").apiSecret("api-secret").build();
        AuthCollection authCollection = vonageClient.getHttpWrapper().getAuthCollection();

        RequestBuilder requestBuilder = RequestBuilder.get();
        authCollection.getAuth(TokenAuthMethod.class).apply(requestBuilder);

        List<NameValuePair> parameters = requestBuilder.getParameters();
        assertContainsParam(requestBuilder.getParameters(), "api_key", "api-key");
        assertContainsParam(requestBuilder.getParameters(), "api_secret", "api-secret");
    }

    @Test
    public void testApiKeyWithSignatureSecret() throws VonageUnacceptableAuthException, NoSuchAlgorithmException, InvalidKeyException {
        VonageClient vonageClient = VonageClient.builder().apiKey("api-key").signatureSecret("api-secret").build();
        AuthCollection authCollection = vonageClient.getHttpWrapper().getAuthCollection();

        RequestBuilder requestBuilder = RequestBuilder.get();
        authCollection.getAuth(SignatureAuthMethod.class).apply(requestBuilder);

        List<NameValuePair> parameters = requestBuilder.getParameters();

        // This is messy but trying to generate a signature auth method and then comparing with what's on the request
        // could have a race condition depending on the returned timestamp.

        // So, we're going to generate the signature after trying to determine what the timestamp is.
        String timestamp = parameters
                .stream()
                .filter(pair -> "timestamp".equals(pair.getName()))
                .findFirst()
                .orElse(new BasicNameValuePair("", ""))
                .getValue();

        String sig = HashUtil.calculate("&api_key=api-key&timestamp=" + timestamp + "api-secret", HashUtil.HashType.MD5);
        assertContainsParam(parameters, "sig", sig);
    }

    @Test
    public void testApiKeyWithSignatureSecretAsHMACSHA256() throws VonageUnacceptableAuthException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        VonageClient vonageClient = VonageClient.builder().hashType(HashUtil.HashType.HMAC_SHA256).apiKey("api-key").signatureSecret("api-secret").build();
        AuthCollection authCollection = vonageClient.getHttpWrapper().getAuthCollection();

        RequestBuilder requestBuilder = RequestBuilder.get();
        authCollection.getAuth(SignatureAuthMethod.class).apply(requestBuilder);

        List<NameValuePair> parameters = requestBuilder.getParameters();

        // This is messy but trying to generate a signature auth method and then comparing with what's on the request
        // could have a race condition depending on the returned timestamp.

        // So, we're going to generate the signature after trying to determine what the timestamp is.
        String timestamp = parameters
                .stream()
                .filter(pair -> "timestamp".equals(pair.getName()))
                .findFirst()
                .orElse(new BasicNameValuePair("", ""))
                .getValue();

        String sig = HashUtil.calculate("&api_key=api-key&timestamp=" + timestamp, "api-secret", "UTF-8", HashUtil.HashType.HMAC_SHA256);
        assertContainsParam(parameters, "sig", sig);
    }

    @Test
    public void testApiKeyWithSignatureSecretAsHmacSHA256() throws VonageUnacceptableAuthException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        VonageClient vonageClient = VonageClient.builder().hashType(HashUtil.HashType.HMAC_SHA256).apiKey("api-key").signatureSecret("api-secret").build();
        AuthCollection authCollection = vonageClient.getHttpWrapper().getAuthCollection();

        RequestBuilder requestBuilder = RequestBuilder.get();
        authCollection.getAuth(SignatureAuthMethod.class).apply(requestBuilder);

        List<NameValuePair> parameters = requestBuilder.getParameters();

        // This is messy but trying to generate a signature auth method and then comparing with what's on the request
        // could have a race condition depending on the returned timestamp.

        // So, we're going to generate the signature after trying to determine what the timestamp is.
        String timestamp = parameters
                .stream()
                .filter(pair -> "timestamp".equals(pair.getName()))
                .findFirst()
                .orElse(new BasicNameValuePair("", ""))
                .getValue();

        String sig = HashUtil.calculate("&api_key=api-key&timestamp=" + timestamp, "api-secret", "UTF-8", HashUtil.HashType.HMAC_SHA256);
        assertContainsParam(parameters, "sig", sig);
    }

    @Test
    public void testApplicationIdWithCertContentsAsBytes() throws Exception {
        TestUtils testUtils = new TestUtils();
        byte[] keyBytes = testUtils.loadKey("test/keys/application_key");

        VonageClient vonageClient = VonageClient.builder()
                .applicationId("app-id")
                .privateKeyContents(keyBytes)
                .build();
        AuthCollection authCollection = vonageClient.getHttpWrapper().getAuthCollection();

        RequestBuilder requestBuilder = RequestBuilder.get();
        authCollection.getAuth(JWTAuthMethod.class).apply(requestBuilder);

        assertEquals(1, requestBuilder.getHeaders("Authorization").length);
        assertEquals("Bearer ", requestBuilder.getFirstHeader("Authorization").getValue().substring(0, 7));
    }

    @Test
    public void testApplicationIdWithCertContentsAsString() throws Exception {
        TestUtils testUtils = new TestUtils();
        String key = new String(testUtils.loadKey("test/keys/application_key"));

        VonageClient vonageClient = VonageClient.builder().applicationId("app-id").privateKeyContents(key).build();
        AuthCollection authCollection = vonageClient.getHttpWrapper().getAuthCollection();

        RequestBuilder requestBuilder = RequestBuilder.get();
        authCollection.getAuth(JWTAuthMethod.class).apply(requestBuilder);

        assertEquals(1, requestBuilder.getHeaders("Authorization").length);
        assertEquals("Bearer ", requestBuilder.getFirstHeader("Authorization").getValue().substring(0, 7));
    }

    @Test
    public void testApplicationIdWithCertPath() throws Exception {
        VonageClient vonageClient = VonageClient.builder()
                .applicationId("app-id")
                .privateKeyPath(Paths.get(this.getClass().getResource("test/keys/application_key").toURI()))
                .build();
        AuthCollection authCollection = vonageClient.getHttpWrapper().getAuthCollection();

        RequestBuilder requestBuilder = RequestBuilder.get();
        authCollection.getAuth(JWTAuthMethod.class).apply(requestBuilder);

        assertEquals(1, requestBuilder.getHeaders("Authorization").length);
        assertEquals("Bearer ", requestBuilder.getFirstHeader("Authorization").getValue().substring(0, 7));
    }

    @Test
    public void testApplicationIdWithCertPathAsString() throws Exception {
      VonageClient vonageClient = VonageClient.builder()
                .applicationId("app-id")
                .privateKeyPath(Paths.get(this.getClass().getResource("test/keys/application_key").toURI()).toString())
                .build();
        AuthCollection authCollection = vonageClient.getHttpWrapper().getAuthCollection();

        RequestBuilder requestBuilder = RequestBuilder.get();
        authCollection.getAuth(JWTAuthMethod.class).apply(requestBuilder);

        assertEquals(1, requestBuilder.getHeaders("Authorization").length);
        assertEquals("Bearer ", requestBuilder.getFirstHeader("Authorization").getValue().substring(0, 7));
    }

    @Test
    public void testDefaultHttpConfig() {
        HttpConfig config = HttpConfig.defaultConfig();
        VonageClient vonageClient = VonageClient.builder().build();

        assertEquals(config.getApiBaseUri(), vonageClient.getHttpWrapper().getHttpConfig().getApiBaseUri());
        assertEquals(config.getRestBaseUri(), vonageClient.getHttpWrapper().getHttpConfig().getRestBaseUri());
        assertEquals(config.getSnsBaseUri(), vonageClient.getHttpWrapper().getHttpConfig().getSnsBaseUri());
    }

    @Test
    public void testHttpConfig() {
        HttpConfig config = HttpConfig.builder().apiBaseUri("https://example.org").build();
        VonageClient vonageClient = VonageClient.builder().httpConfig(config).build();

        assertEquals(config, vonageClient.getHttpWrapper().getHttpConfig());
    }

    @Test(expected = VonageUnableToReadPrivateKeyException.class)
    public void testIOExceptionIsWrappedWithUnableToReadPrivateKeyException() {
        VonageClient.builder().privateKeyPath("this/path/does/not/exist");
    }

    private void assertContainsParam(List<NameValuePair> params, String key, String value) {
        NameValuePair item = new BasicNameValuePair(key, value);
        assertTrue("" + params + " should contain " + item, params.contains(item));
    }
}
