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

import com.nexmo.client.auth.*;
import com.nexmo.client.logging.LoggingUtils;
import com.nexmo.client.voice.Call;
import com.nexmo.client.voice.CallEvent;
import com.nexmo.client.voice.CallStatus;
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
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
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
public class NexmoClientTest {
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
    public void testConstructNexmoClient() throws Exception {
        byte[] keyBytes = testUtils.loadKey("test/keys/application_key");
        NexmoClient client = NexmoClient.builder()
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
    public void testGenerateJwt() throws Exception {
        byte[] privateKeyBytes = testUtils.loadKey("test/keys/application_key");
        NexmoClient client = NexmoClient.builder()
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

    @Test(expected = NexmoClientCreationException.class)
    public void testSoloApiKeyThrowsException() {
        NexmoClient.builder().apiKey("api-key").build();
    }

    @Test(expected = NexmoClientCreationException.class)
    public void testSoloApiSecret() {
        NexmoClient.builder().apiSecret("api-secret").build();
    }

    @Test(expected = NexmoClientCreationException.class)
    public void testSoloSignatureSecret() {
        NexmoClient.builder().signatureSecret("api-secret").build();
    }

    @Test(expected = NexmoClientCreationException.class)
    public void testSoloApplicationId() {
        NexmoClient.builder().applicationId("app-id").build();
    }

    @Test(expected = NexmoClientCreationException.class)
    public void testSoloPrivateKeyContents() {
        NexmoClient.builder().privateKeyContents("").build();
    }

    @Test
    public void testApiKeyWithSecret() throws NexmoUnacceptableAuthException {
        NexmoClient nexmoClient = NexmoClient.builder().apiKey("api-key").apiSecret("api-secret").build();
        AuthCollection authCollection = nexmoClient.getHttpWrapper().getAuthCollection();

        RequestBuilder requestBuilder = RequestBuilder.get();
        authCollection.getAuth(TokenAuthMethod.class).apply(requestBuilder);

        List<NameValuePair> parameters = requestBuilder.getParameters();
        assertContainsParam(requestBuilder.getParameters(), "api_key", "api-key");
        assertContainsParam(requestBuilder.getParameters(), "api_secret", "api-secret");
    }

    @Test
    public void testApiKeyWithSignatureSecret() throws NexmoUnacceptableAuthException, NoSuchAlgorithmException {
        NexmoClient nexmoClient = NexmoClient.builder().apiKey("api-key").signatureSecret("api-secret").build();
        AuthCollection authCollection = nexmoClient.getHttpWrapper().getAuthCollection();

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

        String sig = MD5Util.calculateMd5("&api_key=api-key&timestamp=" + timestamp + "api-secret");
        assertContainsParam(parameters, "sig", sig);
    }

    @Test
    public void testApplicationIdWithCertContentsAsBytes() throws Exception {
        TestUtils testUtils = new TestUtils();
        byte[] keyBytes = testUtils.loadKey("test/keys/application_key");

        NexmoClient nexmoClient = NexmoClient.builder()
                .applicationId("app-id")
                .privateKeyContents(keyBytes)
                .build();
        AuthCollection authCollection = nexmoClient.getHttpWrapper().getAuthCollection();

        RequestBuilder requestBuilder = RequestBuilder.get();
        authCollection.getAuth(JWTAuthMethod.class).apply(requestBuilder);

        assertEquals(1, requestBuilder.getHeaders("Authorization").length);
        assertEquals("Bearer ", requestBuilder.getFirstHeader("Authorization").getValue().substring(0, 7));
    }

    @Test
    public void testApplicationIdWithCertContentsAsString() throws Exception {
        TestUtils testUtils = new TestUtils();
        String key = new String(testUtils.loadKey("test/keys/application_key"));

        NexmoClient nexmoClient = NexmoClient.builder().applicationId("app-id").privateKeyContents(key).build();
        AuthCollection authCollection = nexmoClient.getHttpWrapper().getAuthCollection();

        RequestBuilder requestBuilder = RequestBuilder.get();
        authCollection.getAuth(JWTAuthMethod.class).apply(requestBuilder);

        assertEquals(1, requestBuilder.getHeaders("Authorization").length);
        assertEquals("Bearer ", requestBuilder.getFirstHeader("Authorization").getValue().substring(0, 7));
    }

    @Test
    public void testApplicationIdWithCertPath() throws Exception {
        NexmoClient nexmoClient = NexmoClient.builder()
                .applicationId("app-id")
                .privateKeyPath(Paths.get(this.getClass().getResource("test/keys/application_key").getPath()))
                .build();
        AuthCollection authCollection = nexmoClient.getHttpWrapper().getAuthCollection();

        RequestBuilder requestBuilder = RequestBuilder.get();
        authCollection.getAuth(JWTAuthMethod.class).apply(requestBuilder);

        assertEquals(1, requestBuilder.getHeaders("Authorization").length);
        assertEquals("Bearer ", requestBuilder.getFirstHeader("Authorization").getValue().substring(0, 7));
    }

    @Test
    public void testApplicationIdWithCertPathAsString() throws Exception {
        NexmoClient nexmoClient = NexmoClient.builder()
                .applicationId("app-id")
                .privateKeyPath(this.getClass().getResource("test/keys/application_key").getPath())
                .build();
        AuthCollection authCollection = nexmoClient.getHttpWrapper().getAuthCollection();

        RequestBuilder requestBuilder = RequestBuilder.get();
        authCollection.getAuth(JWTAuthMethod.class).apply(requestBuilder);

        assertEquals(1, requestBuilder.getHeaders("Authorization").length);
        assertEquals("Bearer ", requestBuilder.getFirstHeader("Authorization").getValue().substring(0, 7));
    }

    @Test
    public void testDefaultHttpConfig() {
        HttpConfig config = HttpConfig.defaultConfig();
        NexmoClient nexmoClient = NexmoClient.builder().build();

        assertEquals(config.getApiBaseUri(), nexmoClient.getHttpWrapper().getHttpConfig().getApiBaseUri());
        assertEquals(config.getRestBaseUri(), nexmoClient.getHttpWrapper().getHttpConfig().getRestBaseUri());
        assertEquals(config.getSnsBaseUri(), nexmoClient.getHttpWrapper().getHttpConfig().getSnsBaseUri());
    }

    @Test
    public void testHttpConfig() {
        HttpConfig config = HttpConfig.builder().apiBaseUri("https://example.org").build();
        NexmoClient nexmoClient = NexmoClient.builder().httpConfig(config).build();

        assertEquals(config, nexmoClient.getHttpWrapper().getHttpConfig());
    }

    @Test(expected = NexmoUnableToReadPrivateKeyException.class)
    public void testIOExceptionIsWrappedWithUnableToReadPrivateKeyException() {
        NexmoClient.builder().privateKeyPath("this/path/does/not/exist");
    }

    private void assertContainsParam(List<NameValuePair> params, String key, String value) {
        NameValuePair item = new BasicNameValuePair(key, value);
        assertTrue("" + params + " should contain " + item, params.contains(item));
    }
}
