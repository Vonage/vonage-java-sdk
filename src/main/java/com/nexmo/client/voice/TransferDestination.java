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
package com.nexmo.client.voice;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.nexmo.client.voice.ncco.Ncco;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class TransferDestination {
    private Type type;
    private String[] urls;
    private Ncco ncco;

    public TransferDestination(Type type, String url) {
        this.type = type;
        if (url != null) {
            this.urls = new String[]{url};
        }
    }

    public TransferDestination(Type type, String url, Ncco ncco) {
        this(type, url);
        this.ncco = ncco;
    }

    @JsonProperty("type")
    public Type getType() {
        return type;
    }

    @JsonProperty("url")
    public String[] getUrls() {
        return urls;
    }

    @JsonProperty("ncco")
    public Ncco getNcco() {
        return this.ncco;
    }

    enum Type {
        NCCO;

        @JsonValue
        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
}
