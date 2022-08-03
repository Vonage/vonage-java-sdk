/*
 *   Copyright 2022 Vonage
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
package com.vonage.client.voice.ncco;

import com.vonage.client.voice.TextToSpeechLanguage;
import org.junit.Test;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.Currency;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PayActionTest {

    @Test
    public void testAllFields() {
        String eventUrl = "http://example.com/callback", currency = "gbp";
        double amount = 9.99;
        int style = 5;
        TextToSpeechLanguage language = TextToSpeechLanguage.UNITED_KINGDOM_ENGLISH;
        PaymentPrompt
            number = PaymentPrompt.builder(PaymentPrompt.Type.CARD_NUMBER)
                .text("Enter 16 digits")
                .invalidCardNumber("Malformed number")
                .invalidCardType("Unsupported provider")
                .build(),
            expiration = PaymentPrompt.builder(PaymentPrompt.Type.EXPIRATION_DATE)
                    .text("Expiry")
                    .invalidExpirationDate("Date elapsed")
                    .build(),
            security = PaymentPrompt.builder(PaymentPrompt.Type.SECURITY_CODE)
                    .text("CVC").invalidSecurityCode("Wrong code")
                    .timeout("Timed out")
                    .build();

        PayAction pay = PayAction.builder().eventUrl(eventUrl)
                .amount(amount).currency(currency)
                .language(language).style(style)
                .prompts(Arrays.asList(number, expiration, security))
                .build();

        String expectedJson = "[{\"eventUrl\":[\""+eventUrl+"\"],\"currency\":\""+currency +
                "\",\"amount\":"+amount+",\"voice\":{\"language\":\""+language.getLanguage() +
                "\",\"style\":"+style+"},\"prompts\":[{\"type\":\""+number.getType() +
                "\",\"text\":\""+number.getText()+"\",\"errors\":{\"InvalidCardNumber\":{" +
                "\"text\":\""+number.getErrors().getInvalidCardNumber().getText()+"\"}," +
                "\"InvalidCardType\":{\"text\":\""+number.getErrors().getInvalidCardType().getText() +
                "\"}}},{\"type\":\"ExpirationDate\",\"text\":\""+expiration.getText()+"\",\"errors\":{\"" +
                "InvalidExpirationDate\":{\"text\":\""+expiration.getErrors().getInvalidExpirationDate().getText() +
                "\"}}},{\"type\":\"SecurityCode\",\"text\":\""+security.getText()+"\",\"errors\":{\"Timeout\":" +
                "{\"text\":\""+security.getErrors().getTimeout().getText()+"\"},\"InvalidSecurityCode\":" +
                "{\"text\":\""+security.getErrors().getInvalidSecurityCode().getText()+"\"}}}],\"action\":\"pay\"}]";

        String actualJson = new Ncco(pay).toJson();
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testWithoutErrors() {
        String eventUrl = "http://example.com/callback", currency = "chf";
        double amount = 3.50;
        TextToSpeechLanguage language = TextToSpeechLanguage.GERMAN;
        int style = 1;
        String promptText = "Please enter long card number";
        PaymentPrompt prompt = PaymentPrompt.builder(PaymentPrompt.Type.CARD_NUMBER)
                .text(promptText).build();

        PayAction pay = PayAction.builder().eventUrl(URI.create(eventUrl))
                .currency(Currency.getInstance(currency.toUpperCase())).amount(amount)
                .language(language).style(style)
                .prompts(Collections.singleton(prompt))
                .build();

        String expectedJson = "[{\"eventUrl\":[\""+eventUrl+"\"],\"currency\":\""+currency +
                "\",\"amount\":"+amount+",\"voice\":{\"language\":\""+language.getLanguage() +
                "\",\"style\":"+style+"},\"prompts\":[{\"type\":\"CardNumber\"," +
                "\"text\":\""+promptText+"\"}],\"action\":\"pay\"}]";

        String actualJson = new Ncco(pay).toJson();
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testRequiredFieldsOnly() {
        double amount = 4.33;
        PayAction pay = PayAction.builder().amount(amount).build();
        String expectedJson = "[{\"amount\":"+amount+",\"action\":\""+pay.getAction()+"\"}]";
        String actualJson = new Ncco(pay).toJson();
        assertEquals(expectedJson, actualJson);
        assertNull(pay.getCurrency());
        assertNull(pay.getPrompts());
        assertNull(pay.getEventUrl());
        assertNull(pay.getVoice());
    }

    @Test(expected = IllegalStateException.class)
    public void testWithoutAmount() {
        PayAction.builder().build();
    }
}
