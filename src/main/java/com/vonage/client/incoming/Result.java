/*
 * Copyright (c) 2020  Vonage
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

package com.vonage.client.incoming;

public class Result {
    private String text;
    private String confidence;

    /**
     * 
     * @return transcript text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text transcript text representing the words the user spoke.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * 
     * @return The confidence estimate between 0.0 and 1.0
     */
    public String getConfidence() {
        return confidence;
    }

    /**
     * @param confidence The confidence estimate between 0.0 and 1.0. A higher number indicates an estimated greater
     *                   likelihood that the recognized words are correct.
     */
    public void setConfidence(String confidence) {
        this.confidence = confidence;
    }
}
