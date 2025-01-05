/*
 *   Copyright 2025 Vonage
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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.messages.MediaMessageRequest;
import com.vonage.client.messages.MessageType;
import com.vonage.client.messages.internal.MessagePayload;

/**
 * @since 7.2.0
 */
public final class ViberFileRequest extends ViberRequest implements MediaMessageRequest {

	ViberFileRequest(Builder builder) {
		super(builder, MessageType.FILE);
		MessagePayload.validateExtension(media.getName() != null ? media.getName() : media.getUrl().getPath(),
				"doc", "docx", "rtf", "dot", "dotx", "odt", "odf", "fodt", "txt", "info",
				"pdf", "xps", "pdax", "eps", "xls", "xlsx", "ods", "fods", "csv", "xlsm", "xltx"
		);
	}

	@JsonProperty("file")
	public MessagePayload getFile() {
		return media;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder extends ViberRequest.Builder<ViberFileRequest, Builder> implements MediaMessageRequest.Builder<Builder> {
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
		@Override
		public Builder url(String url) {
			return super.url(url);
		}

		/**
		 * (OPTIONAL)
		 * The name and extension of the file.
		 *
		 * @param name The filename and extension as a string.
		 * @return This builder.
		 */
		@Override
		public Builder name(String name) {
			return super.name(name);
		}

		public ViberFileRequest build() {
			return new ViberFileRequest(this);
		}
	}
}
