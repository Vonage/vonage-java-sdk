package com.nexmo.client.examples;/*
 * Copyright (c) 2011-2016 Nexmo Inc
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

import com.nexmo.client.NexmoClient;
import com.nexmo.client.auth.JWTAuthMethod;
import com.nexmo.client.voice.Call;
import org.apache.commons.cli.*;

import java.nio.file.FileSystems;
import java.util.LinkedList;
import java.util.Queue;

public class TestVoiceCall {
    public static void main(String[] argv) throws Exception {
        Options options = new Options()
                .addOption("v", "Verbosity")
                .addOption("f", "from", true, "Phone number to call from")
                .addOption("t", "to", true, "Phone number to call")
                .addOption("h", "webhook", true, "URL to call for instructions");
        CommandLineParser parser = new DefaultParser();

        CommandLine cli;
        try {
            cli = parser.parse(options, argv);
        } catch (ParseException exc) {
            System.err.println("Parsing failed: " + exc.getMessage());
            System.exit(128);
            return;
        }

        Queue<String> args = new LinkedList<String>(cli.getArgList());
        String from = cli.getOptionValue("f");
        String to = cli.getOptionValue("t");
        System.out.println("From: " + from);
        System.out.println("To: " + to);

        NexmoClient client = new NexmoClient(
            new JWTAuthMethod(
                    "951614e0-eec4-4087-a6b1-3f4c2f169cb0",
                    FileSystems.getDefault().getPath("valid_application_key.pem")));
        client.getVoiceClient().createCall(
                new Call(to, from, "https://nexmo-community.github.io/ncco-examples/first_call_talk.json"));
    }
}
