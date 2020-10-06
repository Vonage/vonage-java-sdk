package com.vonage.client.auth.hashutils;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public abstract class Hasher {

    public abstract String calculate(String input) throws NoSuchAlgorithmException;

    public abstract String calculate(String input, String encoding) throws NoSuchAlgorithmException, UnsupportedEncodingException;
}
