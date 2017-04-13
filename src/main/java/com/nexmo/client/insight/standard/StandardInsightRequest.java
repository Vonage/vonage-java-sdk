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
package com.nexmo.client.insight.standard;

import com.nexmo.client.insight.BaseInsightRequest;
import org.apache.commons.lang3.builder.EqualsBuilder;

public class StandardInsightRequest extends BaseInsightRequest {
    private static final Boolean DEFAULT_CNAM = null;

    private final Boolean cnam;

    public StandardInsightRequest(String number) {
        this(number, DEFAULT_COUNTRY);
    }

    public StandardInsightRequest(String number, String country) {
        this(number, country, DEFAULT_CNAM);
    }

    public StandardInsightRequest(String number, String country, Boolean cnam) {
        super(number, country);
        this.cnam = cnam;
    }

    public Boolean getCnam() {
        return cnam;
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
            StandardInsightRequest other = (StandardInsightRequest) obj;
            return new EqualsBuilder()
                    .appendSuper(super.equals(obj))
                    .append(this.getCnam(), other.getCnam())
                    .isEquals();
        }
    }
}
