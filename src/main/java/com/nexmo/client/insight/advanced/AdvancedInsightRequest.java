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
package com.nexmo.client.insight.advanced;

import com.nexmo.client.insight.BaseInsightRequest;
import org.apache.commons.lang3.builder.EqualsBuilder;

public class AdvancedInsightRequest extends BaseInsightRequest {
    private static final Boolean DEFAULT_CNAM = null;
    private static final String DEFAULT_IP_ADDRESS = null;
    private final String ipAddress;
    private final Boolean cnam;


    public AdvancedInsightRequest(String number) {
        this(number, DEFAULT_COUNTRY);
    }

    public AdvancedInsightRequest(String number, String country) {
        this(number, country, DEFAULT_IP_ADDRESS);
    }

    public AdvancedInsightRequest(String number, String country, String ipAddress) {
        this(number, country, ipAddress, DEFAULT_CNAM);
    }

    public AdvancedInsightRequest(String number, String country, String ipAddress, Boolean cnam) {
        super(number, country);
        this.cnam = cnam;
        this.ipAddress = ipAddress;
    }

    public Boolean getCnam() {
        return cnam;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj == this) {
            return true;
        } else if (obj.getClass() != this.getClass()) {
            return false;
        } else {
            AdvancedInsightRequest other = (AdvancedInsightRequest) obj;
            return new EqualsBuilder()
                    .appendSuper(super.equals(other))
                    .append(this.getCnam(), other.getCnam())
                    .append(this.getIpAddress(), other.getIpAddress())
                    .isEquals();
        }
    }
}
