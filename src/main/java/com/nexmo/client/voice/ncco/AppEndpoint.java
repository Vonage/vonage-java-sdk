/*
 * Copyright (c) 2011-2020 Nexmo Inc
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
package com.nexmo.client.voice.ncco;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Represents an app endpoint used in a {@link ConnectAction}
 * @since 5.4.0
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class AppEndpoint implements Endpoint {

  private static final String TYPE = "app";
  private String user;

  private AppEndpoint(Builder builder) {
    this.user = builder.user;
  }

  public String getType() {
    return TYPE;
  }

  public String getUser() {
    return user;
  }

  public static Builder builder(String user) {
    return new Builder(user);
  }

  public static class Builder {
    private String user;

    public Builder(String user) {
      this.user = user;
    }

    public AppEndpoint build() {
      return new AppEndpoint(this);
    }
  }
}