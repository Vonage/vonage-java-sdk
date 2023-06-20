/*
 *   Copyright 2023 Vonage
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
package com.vonage.client.proactiveconnect;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 * Represents the {@code salesforce} datasource type for a Proactive Connect list.
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SalesforceDatasource extends Datasource {
	private String integrationId, soql;

	protected SalesforceDatasource() {
		super("salesforce");
	}

	/**
	 * Creates a new Salesforce datasource for the list.
	 *
	 * @param integrationId Integration ID defining Salesforce credential to use for this datasource.
	 * @param soql Salesforce query defining which data to fetch from salesforce.
	 */
	public SalesforceDatasource(String integrationId, String soql) {
		this();
		this.integrationId = Objects.requireNonNull(integrationId, "Integration ID is required.");
		this.soql = Objects.requireNonNull(soql, "Salesforce query is required.");
	}

	/**
	 * Integration ID defining Salesforce credential to use for this datasource.
	 *
	 * @return The integration ID.
	 */
	@JsonProperty("integration_id")
	public String getIntegrationId() {
		return integrationId;
	}


	/**
	 * Salesforce query defining which data to fetch from salesforce.
	 *
	 * @return The Salesforce query as a string.
	 */
	@JsonProperty("soql")
	public String getSoql() {
		return soql;
	}
}
