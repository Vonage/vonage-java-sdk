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
package com.vonage.client.verify2;

import com.vonage.client.common.E164;

/**
 * Intermediate class for number recipients.
 *
 * @since 8.2.0
 */
abstract class AbstractNumberWorkflow extends Workflow {

	protected AbstractNumberWorkflow(Builder<?, ?> builder) {
		super(builder);
	}

	protected AbstractNumberWorkflow(Channel channel, String to) {
		super(channel, to);
	}

	@Override
	protected String validateTo(String to) {
		return new E164(super.validateTo(to)).toString();
	}

	protected abstract static class Builder<
				N extends AbstractNumberWorkflow,
				B extends Builder<? extends N, ? extends B>
			> extends Workflow.Builder<N, B> {

		protected Builder(Channel channel, String to) {
			super(channel);
			to(to);
		}
	}
}
