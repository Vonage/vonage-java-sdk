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
package com.vonage.client.messages.viber;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.messages.MessageType;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public final class ViberFileRequest extends ViberRequest {
	final File file;

	ViberFileRequest(Builder builder) {
		super(builder, MessageType.FILE);
		file = new File(builder.url, builder.name);
	}

	@JsonProperty("file")
	public File getFile() {
		return file;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder extends ViberRequest.Builder<ViberFileRequest, Builder> {
		String url, name;

		Builder() {}

		/**
		 * (REQUIRED)
		 * The URL for the file attachment or the path for the location of the file attachment.
		 * If name is included, can just be the path.
		 * If name is not included, must include the filename and extension.
		 * <p>
		 * Supported file types are .doc, .docx, .rtf, .dot, .dotx, .odt, .odf, .fodt, .txt, .info, .pdf, .xps,
		 * .pdax, .eps, .xls, .xlsx, .ods, .fods, .csv, .xlsm, .xltx. Maximum file size is 200MB.
		 *
		 * @param url The file URL as a string.
		 * @return This builder.
		 */
		public Builder url(String url) {
			this.url = url;
			return this;
		}

		/**
		 * (OPTIONAL)
		 * The name and extension of the file.
		 *
		 * @param name The filename and extension as a string.
		 * @return This builder.
		 */
		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public ViberFileRequest build() {
			return new ViberFileRequest(this);
		}
	}
}
