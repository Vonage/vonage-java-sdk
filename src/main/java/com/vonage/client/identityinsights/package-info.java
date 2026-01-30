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

/**
 * This package contains classes to support usage of the
 * <a href="https://developer.vonage.com/en/api/identity-insights">Identity Insights API</a>.
 * Please refer to the
 * <a href="https://developer.vonage.com/en/identity-insights/overview">developer documentation</a>
 * for an overview of the concepts.
 * <p>
 * The Identity Insights API allows clients to request real-time information related to a phone number.
 * Users can retrieve any combination of different datasets, known as insights (e.g., number formatting,
 * SIM swap information), in a single API call.
 * </p>
 * <p>
 * Each insight is processed independently, and the response includes a structured result for each insight
 * along with a status code. The API supports the following insights:
 * </p>
 * <ul>
 *   <li><b>Format</b> - Validates phone number format and provides metadata.</li>
 *   <li><b>SIM Swap</b> - Information about recent SIM card swaps.</li>
 *   <li><b>Original Carrier</b> - Information about the originally assigned carrier.</li>
 *   <li><b>Current Carrier</b> - Information about the currently assigned carrier.</li>
 * </ul>
 *
 * @since 9.1.0
 */
package com.vonage.client.identityinsights;
