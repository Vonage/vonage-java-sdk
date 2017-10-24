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
package com.nexmo.client.auth;

import com.auth0.jwt.Algorithm;
import com.auth0.jwt.JWTSigner;
import com.nexmo.client.NexmoUnexpectedException;
import org.apache.http.client.methods.RequestBuilder;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JWTAuthMethod extends AbstractAuthMethod {
    private static final Pattern pemPattern = Pattern.compile(
            "-----BEGIN PRIVATE KEY-----" + // File header
            "(.*\\n)" +                     // Key data
            "-----END PRIVATE KEY-----" +   // File footer
            "\\n?",                         // Optional trailing line break
            Pattern.MULTILINE | Pattern.DOTALL);
    public final int SORT_KEY = 10;
    private String applicationId;
    private JWTSigner signer;

    public JWTAuthMethod(final String applicationId, final byte[] privateKey)
            throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {
        this.applicationId = applicationId;

        byte[] decodedPrivateKey = privateKey;
        if (privateKey[0] == '-') {
            decodedPrivateKey = decodePrivateKey(privateKey);
        }
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decodedPrivateKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey key = kf.generatePrivate(spec);
        this.signer = new JWTSigner(key);
    }

    public JWTAuthMethod(String applicationId, Path path)
            throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, IOException {
        this(applicationId, Files.readAllBytes(path));
    }

    public static String constructJTI() {
        return UUID.randomUUID().toString();
    }

    protected byte[] decodePrivateKey(byte[] data) throws InvalidKeyException {
        try {
            String s = new String(data, "UTF-8");
            Matcher extracter = pemPattern.matcher(s);
            if (extracter.matches()) {
                String pemBody = extracter.group(1);
                return DatatypeConverter.parseBase64Binary(pemBody);
            } else {
                throw new InvalidKeyException("Private key should be provided in PEM format!");
            }
        } catch (UnsupportedEncodingException exc) {
            // This should never happen.
            throw new NexmoUnexpectedException("UTF-8 is an unsupported encoding in this JVM", exc);
        }
    }

    @Override
    public RequestBuilder apply(RequestBuilder request) {
        String token = this.constructToken(
                System.currentTimeMillis() / 1000L,
                constructJTI());
        request.setHeader("Authorization", "Bearer " + token);
        return request;
    }

    public String constructToken(long iat, String jti) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("iat", iat);
        claims.put("application_id", this.applicationId);
        claims.put("jti", jti);

        JWTSigner.Options options = new JWTSigner.Options()
                .setAlgorithm(Algorithm.RS256);
        String signed = this.signer.sign(claims, options);

        return signed;
    }

    @Override
    public int getSortKey() {
        return this.SORT_KEY;
    }
}
