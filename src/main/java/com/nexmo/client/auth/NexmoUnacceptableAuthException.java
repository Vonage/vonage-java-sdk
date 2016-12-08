package com.nexmo.client.auth;/*
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

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

public class NexmoUnacceptableAuthException extends NexmoAuthException {
    private final Iterable<AuthMethod> availableAuths;
    private final Iterable<Class> acceptableAuthClasses;

    public NexmoUnacceptableAuthException(Collection<AuthMethod> availableAuths, Collection<Class>
            acceptableAuthClasses) {
        super();
        this.availableAuths = new ArrayList<AuthMethod>(availableAuths);
        this.acceptableAuthClasses = new ArrayList<Class>(acceptableAuthClasses);
    }

    public String getMessage() {
        return generateErrorMessage();
    }

    private String generateErrorMessage() {
        SortedSet<String> availableTypes = new TreeSet<String>();
        for (AuthMethod auth : this.availableAuths) {
            availableTypes.add(auth.getClass().getSimpleName());
        }

        SortedSet<String> acceptableTypes = new TreeSet<String>();
        for (Class klass : this.acceptableAuthClasses) {
            acceptableTypes.add(klass.getSimpleName());
        }

        return String.format("No acceptable authentication type could be found. Acceptable types are: %s. Supplied " +
                "types " +
                "were: %s", StringUtils.join(acceptableTypes, ", "), StringUtils.join(availableTypes, ", "));
    }
}
