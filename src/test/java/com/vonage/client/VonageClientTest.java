/*
 *   Copyright 2024 Vonage
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
import com.vonage.client.voice.Call;
import com.vonage.client.voice.CallEvent;
import com.vonage.client.voice.CallStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class VonageClientTest extends ClientTest<VonageClient> {
    private static final UUID APPLICATION_ID = UUID.randomUUID();
    private final TestUtils testUtils = new TestUtils();

    final String key = "api-key", secret = "api-secret";

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
                .privateKeyContents(privateKeyBytes)
                .applicationId(APPLICATION_ID).build();

        String constructedToken = client.generateJwt();

        byte[] publicKeyBytes = testUtils.loadKey("test/keys/application_public_key.der");
        X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey key = kf.generatePublic(spec);

        Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(constructedToken).getPayload();

        assertEquals(APPLICATION_ID.toString(), claims.get("application_id"));
    }

    @Test
    public void testSoloApiKeyThrowsException() {
        assertThrows(VonageClientCreationException.class, () ->
                VonageClient.builder().apiKey(key).build()
        );
    }

    @Test
    public void testSoloApiSecret() {
        assertThrows(VonageClientCreationException.class, () ->
                VonageClient.builder().apiSecret(secret).build()
        );
    }

    @Test
    public void testSoloSignatureSecret() {
        assertThrows(VonageClientCreationException.class, () ->
                VonageClient.builder().signatureSecret(secret).build()
        );
    }

    @Test
    public void testSoloApplicationId() {
        assertThrows(VonageClientCreationException.class, () ->
                VonageClient.builder().applicationId(APPLICATION_ID).build()
        );
    }

    @Test
    public void testSoloPrivateKeyContents() {
        assertThrows(VonageClientCreationException.class, () ->
                VonageClient.builder().privateKeyContents("").build()
        );
    }

    @Test
    public void testApiKeyWithSecret() throws VonageUnacceptableAuthException {
        VonageClient vonageClient = VonageClient.builder().apiKey("api-key").apiSecret("api-secret").build();
        AuthCollection authCollection = vonageClient.getHttpWrapper().getAuthCollection();
        assertTrue(authCollection.hasAuthMethod(ApiKeyHeaderAuthMethod.class));
    }

    @Test
    public void testApiKeyWithSignatureSecret() throws Exception {
        var vonageClient = VonageClient.builder()
                .apiKey(key).signatureSecret(secret).build();

        var authCollection = vonageClient.getHttpWrapper().getAuthCollection();

        Map<String, String> params = new LinkedHashMap<>();
        authCollection.getAuth(SignatureAuthMethod.class).apply(params);

        // This is messy but trying to generate a signature auth method and then comparing with what's on the request
        // could have a race condition depending on the returned timestamp.

        // So, we're going to generate the signature after trying to determine what the timestamp is.
        String timestamp = params.get("timestamp");
        String sig = HashUtil.calculate("&api_key="+key+"&timestamp=" + timestamp + secret, HashUtil.HashType.MD5);
        assertEquals(sig, params.get("sig"));

        for (var hashType : HashUtil.HashType.values()) {
            if (hashType == HashUtil.HashType.MD5) continue;

            vonageClient = VonageClient.builder()
                    .apiKey(key).signatureSecret(secret).hashType(hashType).build();

            authCollection = vonageClient.getHttpWrapper().getAuthCollection();

            params = new LinkedHashMap<>();
            authCollection.getAuth(SignatureAuthMethod.class).apply(params);

            timestamp = params.get("timestamp");
            sig = HashUtil.calculate("&api_key="+key+"&timestamp=" + timestamp, secret, "UTF-8", hashType);
            assertEquals(sig, params.get("sig"));
        }
    }

    @Test
    public void testApplicationIdWithCertContentsAsBytes() throws Exception {
        TestUtils testUtils = new TestUtils();
        byte[] keyBytes = testUtils.loadKey("test/keys/application_key");

        VonageClient vonageClient = VonageClient.builder()
                .applicationId(APPLICATION_ID)
                .privateKeyContents(keyBytes)
                .build();

        AuthCollection authCollection = vonageClient.getHttpWrapper().getAuthCollection();
        assertTrue(authCollection.hasAuthMethod(JWTAuthMethod.class));
    }

    @Test
    public void testApplicationIdWithCertContentsAsString() throws Exception {
        TestUtils testUtils = new TestUtils();
        String key = new String(testUtils.loadKey("test/keys/application_key"));

        VonageClient vonageClient = VonageClient.builder().applicationId(APPLICATION_ID).privateKeyContents(key).build();
        AuthCollection authCollection = vonageClient.getHttpWrapper().getAuthCollection();
        assertTrue(authCollection.hasAuthMethod(JWTAuthMethod.class));
    }

    @Test
    public void testApplicationIdWithCertPath() throws Exception {
        VonageClient vonageClient = VonageClient.builder()
                .applicationId(APPLICATION_ID)
                .privateKeyPath(Paths.get(getClass().getResource("test/keys/application_key").toURI()))
                .build();
        AuthCollection authCollection = vonageClient.getHttpWrapper().getAuthCollection();
        assertTrue(authCollection.hasAuthMethod(JWTAuthMethod.class));
    }

    @Test
    public void testApplicationIdWithCertPathAsString() throws Exception {
      VonageClient vonageClient = VonageClient.builder()
                .applicationId(APPLICATION_ID)
                .privateKeyPath(Paths.get(getClass().getResource("test/keys/application_key").toURI()).toString())
                .build();
        AuthCollection authCollection = vonageClient.getHttpWrapper().getAuthCollection();
        assertTrue(authCollection.hasAuthMethod(JWTAuthMethod.class));
    }

    @Test
    public void testDefaultHttpConfig() {
        HttpConfig config = HttpConfig.defaultConfig();
        VonageClient vonageClient = VonageClient.builder().build();

        assertEquals(config.getApiBaseUri(), vonageClient.getHttpWrapper().getHttpConfig().getApiBaseUri());
        assertEquals(config.getRestBaseUri(), vonageClient.getHttpWrapper().getHttpConfig().getRestBaseUri());
    }

    @Test
    public void testHttpConfig() {
        HttpConfig config = HttpConfig.builder().apiBaseUri("https://example.org").build();
        VonageClient vonageClient = VonageClient.builder().httpConfig(config).build();

        assertEquals(config, vonageClient.getHttpWrapper().getHttpConfig());
    }

    @Test
    public void testIOExceptionIsWrappedWithUnableToReadPrivateKeyException() {
        assertThrows(VonageUnableToReadPrivateKeyException.class, () ->
                VonageClient.builder().privateKeyPath("this/path/does/not/exist")
        );
    }

    @Test
    public void testSubClientGetters() {
        VonageClient client = VonageClient.builder().build();
        assertNotNull(client.getAccountClient());
        assertNotNull(client.getApplicationClient());
        assertNotNull(client.getConversationsClient());
        assertNotNull(client.getConversionClient());
        assertNotNull(client.getVoiceClient());
        assertNotNull(client.getInsightClient());
        assertNotNull(client.getMeetingsClient());
        assertNotNull(client.getMessagesClient());
        assertNotNull(client.getNumberInsight2Client());
        assertNotNull(client.getNumbersClient());
        assertNotNull(client.getProactiveConnectClient());
        assertNotNull(client.getRedactClient());
        assertNotNull(client.getSmsClient());
        assertNotNull(client.getSubaccountsClient());
        assertNotNull(client.getUsersClient());
        assertNotNull(client.getVerifyClient());
        assertNotNull(client.getVerify2Client());
        assertNotNull(client.getVideoClient());
    }
}
