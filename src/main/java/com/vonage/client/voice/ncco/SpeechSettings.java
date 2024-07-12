/*
 * Copyright 2024 Vonage
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.vonage.client.voice.ncco;

import com.fasterxml.jackson.annotation.*;
import com.vonage.client.JsonableBaseObject;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * ASR (Automatic Speech Recognition) settings for Input Actions that will be added to a NCCO object.
 */
public class SpeechSettings extends JsonableBaseObject {
    private Collection<String> uuid, context;
    private Double endOnSilence;
    private Integer startTimeout, maxDuration, sensitivity;
    private Language language;
    private Boolean saveAudio;

    /**
     * @deprecated This will be made private in a future release. Use {@link #builder()}.
     */
    @Deprecated
    public SpeechSettings() {}

    private SpeechSettings(Builder builder) {
        if (builder.uuid != null) {
            uuid = Collections.singletonList(builder.uuid);
        }
        if ((endOnSilence = builder.endOnSilence) != null && (endOnSilence < 0.4 || endOnSilence > 10)) {
            throw new IllegalArgumentException("endOnSilence must be between 0.4 and 10.");
        }
        if ((startTimeout = builder.startTimeout) != null && (startTimeout < 1 || startTimeout > 60)) {
            throw new IllegalArgumentException("startTimeout must be between 1 and 60.");
        }
        if ((maxDuration = builder.maxDuration) != null && (maxDuration < 1 || maxDuration > 60)) {
            throw new IllegalArgumentException("maxDuration must be between 1 and 60.");
        }
        if ((sensitivity = builder.sensitivity) != null && (sensitivity < 10 || sensitivity > 100)) {
            throw new IllegalArgumentException("sensitivity must be between 10 and 100.");
        }
        context = builder.context;
        language = builder.language;
        saveAudio = builder.saveAudio;
    }

    @JsonProperty("uuid")
    public Collection<String> getUuid() {
        return uuid;
    }

    @JsonProperty("language")
    public Language getLanguage() {
        return language;
    }

    @JsonProperty("context")
    public Collection<String> getContext() {
        return context;
    }

    @JsonProperty("endOnSilence")
    public Double getEndOnSilence() {
        return endOnSilence;
    }

    @JsonProperty("startTimeout")
    public Integer getStartTimeout() {
        return startTimeout;
    }

    @JsonProperty("maxDuration")
    public Integer getMaxDuration() {
        return maxDuration;
    }

    @JsonProperty("sensitivity")
    public Integer getSensitivity() {
        return sensitivity;
    }

    @JsonProperty("saveAudio")
    public Boolean getSaveAudio() {
        return saveAudio;
    }


    /**
     * @param language expected language of the user's speech. If empty, default value is en-US.
     * @deprecated Use {@link Builder#language(Language)}. This will be removed in a future release.
     */
    @Deprecated
    public void setLanguage(Language language) {
        this.language = language;
    }

    /**
     *
     * @param uuid The unique ID of the Call leg for the user to capture the speech of, wrapped in a collection.
     * @deprecated Use {@link Builder#uuid(String)}. This will be removed in a future release.
     */
    @Deprecated
    public void setUuid(Collection<String> uuid) {
        this.uuid = uuid;
    }

    /**
     * List of hints to improve recognition quality if certain words are expected from the user.
     *
     * @param context list of hints
     * @deprecated Use {@link Builder#context(Collection)}. This will be removed in a future release.
     */
    @Deprecated
    public void setContext(Collection<String> context) {
        this.context = context;
    }

    /**
     * Controls how long the system will wait after user stops speaking to decide the input is completed.
     * Timeout range 1-10 seconds. If empty,Default value is 2
     *
     * @param endOnSilence wait time for voice input to complete
     * @deprecated Use {@link Builder#endOnSilence(double)}. This will be removed in a future release.
     */
    @Deprecated
    public void setEndOnSilence(Integer endOnSilence) {
        this.endOnSilence = Double.valueOf(endOnSilence);
    }

    /**
     * Controls how long the system will wait for the user to start speaking.
     * Timeout range 1-10 seconds.
     *
     * @param startTimeout timeout for voice input initiation
     * @deprecated Use {@link Builder#startTimeout(int)}. This will be removed in a future release.
     */
    @Deprecated
    public void setStartTimeout(Integer startTimeout) {
        this.startTimeout = startTimeout;
    }

    /**
     * Controls maximum speech duration from the moment user starts speaking. Default value is 60
     *
     * @param maxDuration speech duration starting from user's initiation of speech
     * @deprecated Use {@link Builder#maxDuration(int)}. This will be removed in a future release.
     */
    @Deprecated
    public void setMaxDuration(Integer maxDuration) {
        this.maxDuration = maxDuration;
    }

    /**
     * Entry point for constructing an instance of this class.
     *
     * @return A new Builder.
     * @since 8.2.0
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for customising SpeechSettings parameters. All fields are optional.
     *
     * @since 8.2.0
     */
    public static final class Builder {
        private String uuid;
        private Collection<String> context;
        private Integer startTimeout, maxDuration, sensitivity;
        private Double endOnSilence;
        private Language language;
        private Boolean saveAudio;

        private Builder() {}

        /**
         * The unique ID of the Call leg for the user to capture the speech of.
         * The first joined leg of the call by default.
         *
         * @param uuid The call leg ID to capture as a string.
         * @return This builder.
         */
        public Builder uuid(String uuid) {
            this.uuid = uuid;
            return this;
        }

        /**
         * Hints to improve recognition quality if certain words are expected from the user.
         *
         * @param context The collection of hint strings.
         * @return This builder.
         * @see #context(String...)
         */
        public Builder context(Collection<String> context) {
            this.context = context;
            return this;
        }

        /**
         * Hints to improve recognition quality if certain words are expected from the user.
         *
         * @param context The hint strings.
         * @return This builder.
         * @see #context(Collection)
         */
        public Builder context(String... context) {
            return context(Arrays.asList(context));
        }

        /**
         * Controls how long the system will wait after user stops speaking to decide the input is completed.
         * The default value is 2.0 (seconds). The range of possible values is between 0.4 and 10.0 seconds.
         *
         * @param endOnSilence The input completion wait time in seconds as a double.
         * @return This builder.
         */
        public Builder endOnSilence(double endOnSilence) {
            this.endOnSilence = endOnSilence;
            return this;
        }

        /**
         * Controls how long the system will wait for the user to start speaking. The range of possible values
         * is between 1 second and 60 seconds. The default value is 10.
         *
         * @param startTimeout The initial speech timeout in seconds as an integer.
         * @return This builder.
         */
        public Builder startTimeout(int startTimeout) {
            this.startTimeout = startTimeout;
            return this;
        }

        /**
         * Controls maximum speech duration (from the moment user starts speaking). The default value is
         * 60 (seconds). The range of possible values is between 1 and 60 seconds.
         * 
         * @param maxDuration The maximum speech duration in seconds as an integer.
         * @return This builder.
         */
        public Builder maxDuration(int maxDuration) {
            this.maxDuration = maxDuration;
            return this;
        }

        /**
         * Audio sensitivity used to differentiate noise from speech. An integer value where 10 represents
         * low sensitivity and 100 maximum sensitivity. Default is 90.
         *
         * @param sensitivity The audio sensitivity as an integer.
         * @return This builder.
         */
        public Builder sensitivity(int sensitivity) {
            this.sensitivity = sensitivity;
            return this;
        }

        /**
         * Expected language of the user's speech. Default is {@link Language#ENGLISH_UNITED_STATES}.
         *
         * @param language The expected speech language as an enum.
         * @return This builder.
         */
        public Builder language(Language language) {
            this.language = language;
            return this;
        }

        /**
         * Controls whether the speech input recording ({@code recording_url}) is sent to your webhook
         * endpoint at {@code eventUrl}. The default value is {@code false}.
         *
         * @param saveAudio {@code true} to send the speech input to the event webhook.
         * @return This builder.
         */
        public Builder saveAudio(boolean saveAudio) {
            this.saveAudio = saveAudio;
            return this;
        }

        /**
         * Builds the SpeechSettings object with this builder's properties.
         *
         * @return A new SpeechSettings instance.
         */
        public SpeechSettings build() {
            return new SpeechSettings(this);
        }
    }

    public enum Language {
        AFRIKAANS("af-ZA"),
        ALBANIAN("sq-AL"),
        AMHARIC("am-ET"),
        ARABIC_ALGERIA("ar-DZ"), ARABIC_BAHRAIN("ar-BH"), ARABIC_EGYPT("ar-EG"), ARABIC_IRAQ("ar-IQ"),
        ARABIC_ISRAEL("ar-IL"), ARABIC_JORDAN("ar-JO"), ARABIC_KUWAIT("ar-KW"), ARABIC_LEBANON("ar-LB"),
        ARABIC_MOROCCO("ar-MA"), ARABIC_OMAN("ar-OM"), ARABIC_QATAR("ar-QA"), ARABIC_SAUDI_ARABIA("ar-SA"),
        ARABIC_STATE_OF_PALESTINE("ar-PS"), ARABIC_TUNISIA("ar-TN"), ARABIC_UNITED_ARAB_EMIRATES("ar-AE"),
        ARMENIAN("hy-AM"),
        AZERBAIJANI("az-AZ"),
        BASQUE("eu-EX"),
        BENGALI_BANGLADESH("bn-BD"),
        BENGALI_INDIA("BN-IN"),
        BULGARIAN("bg-BG"),
        CATALAN("ca-ES"),
        CHINESE("zh"),
        MANDARIN_CHINESE_SIMPLIFIED("zh-cmn-Hans-CN"),
        CROATIAN("hr-HR"),
        CZECH("cs-CZ"),
        DANISH("da-DK"),
        DUTCH("nl-NL"),
        ENGLISH_AUSTRALIA("en-AU"), ENGLISH_CANADA("en-CA"), ENGLISH_GHANA("en-GH"), ENGLISH_INDIA("en-IN"),
        ENGLISH_IRELAND("en-IE"), ENGLISH_KENYA("en-KE"), ENGLISH_NEW_ZEALAND("en-NZ"), ENGLISH_NIGERIA("en-NG"),
        ENGLISH_PHILIPPINES("en-PH"), ENGLISH_SOUTH_AFRICA("en-ZA"), ENGLISH_TANZANIA("en-TZ"),
        ENGLISH_UNITED_KINGDOM("en-GB"), ENGLISH_UNITED_STATES("en-US"),
        FINNISH("fi-FI"),
        FRENCH_CANADA("fr-CA"),
        FRENCH_FRANCE("fr-FR"),
        GALICIAN("gl-ES"),
        GEORGIAN("ka-GE"),
        GERMAN("de-DE"),
        GREEK("el-GR"),
        GUJARATI("gu-IN"),
        HEBREW("he-IL"),
        HINDI("hi-IN"),
        HUNGARIAN("hu-HU"),
        ICELANDIC("is-IS"),
        INDONESIAN("id-ID"),
        ITALIAN("it-IT"),
        JAPANESE_JAPAN("ja-JP"),
        JAVANESE("jv-ID"),
        KANNADA("kn-IN"),
        KHMER("km-KH"),
        KOREAN("ko-KR"),
        LAO("lo-LA"),
        LATVIAN("lv-LV"),
        LITHUANIAN("lt-LT"),
        MALAY("ms-MY"),
        MALAYALAM("ml-IN"),
        MARATHI("mr-IN"),
        NEPALI("ne-NP"),
        NORWEGIAN_BOKMAL("nb-NO"),
        PERSIAN("fa-IR"),
        POLISH("pl-PL"),
        PORTUGUESE_BRAZIL("pt-BR"), PORTUGUESE_PORTUGAL("pt-PT"),
        ROMANIAN("ro-RO"),
        RUSSIAN("ru-RU"),
        SERBIAN("sr-RS"),
        SINHALA("si-LK"),
        SLOVAK("sk-SK"),
        SLOVENIAN("sl-SI"),
        SPANISH_ARGENTINA("es-AR"), SPANISH_BOLIVIA("es-BO"), SPANISH_CHILE("es-CL"), SPANISH_COLOMBIA("es-CO"),
        SPANISH_COSTA_RICA("es-CR"), SPANISH_DOMINICAN_REPUBLIC("es-DO"), SPANISH_ECUADOR("es-EC"), SPANISH_EL_SALVADOR("es-SV"),
        SPANISH_GUATEMALA("es-GT"), SPANISH_HONDURAS("es-HN"), SPANISH_MEXICO("es-MX"), SPANISH_NICARAGUA("es-NI"),
        SPANISH_PANAMA("es-PA"), SPANISH_PARAGUAY("es-PY"), SPANISH_PERU("es-PE"), SPANISH_PUERTO_RICO("es-PR"),
        SPANISH_SPAIN("es-ES"), SPANISH_UNITED_STATES("es-US"), SPANISH_URUGUAY("es-UY"), SPANISH_VENEZUELA("es-VE"),
        SUNDANESE("su-ID"),
        SWAHILI_KENYA("sw-KE"), SWAHILI_TANZANIA("sw-TZ"),
        SWEDISH("sv-SE"),
        TAMIL_INDIA("ta-IN"), TAMIL_MALAYSIA("ta-MY"), TAMIL_SINGAPORE("ta-SG"), TAMIL_SRI_LANKA("ta-LK"),
        TELUGU("te-IN"),
        THAI("th-TH"),
        TURKISH("tr-TR"),
        UKRAINIAN("uk-UA"),
        URDU_INDIA("ur-IN"), URDU_PAKISTAN("ur-PK"),
        VIETNAMESE("vi-VN"),
        ZULU("zu-ZA"),
        @JsonEnumDefaultValue
        UNKNOWN("Unknown");

        private final String language;

        @JsonCreator
        Language(String language) {
            this.language = language;
        }

        @JsonValue
        public String getLanguage() {
            return language;
        }
    }
}
