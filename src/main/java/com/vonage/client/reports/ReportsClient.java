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
package com.vonage.client.reports;

import com.vonage.client.DynamicEndpoint;
import com.vonage.client.HttpWrapper;
import com.vonage.client.RestEndpoint;
import com.vonage.client.VonageClient;
import com.vonage.client.auth.ApiKeyHeaderAuthMethod;
import com.vonage.client.common.HttpMethod;
import java.util.Objects;
import java.util.function.Function;

/**
 * A client for communicating with the Vonage Reports API.
 * The standard way to obtain an instance of this class is to use
 * {@link VonageClient#getReportsClient()}.
 * <p>
 * The Reports API enables you to request reports of activity for your Vonage account.
 * It supports two modes:
 * </p>
 * <ul>
 *   <li><b>Synchronous</b> ({@link #getRecords(RecordsFilter)}): Immediately returns report data.
 *       Best for smaller, ad-hoc queries.</li>
 *   <li><b>Asynchronous</b> ({@link #createReport(AsyncReportRequest)}): Submits a report job
 *       for background processing. Use {@link #getReport(String)} to poll the status and
 *       {@link #downloadReport(String)} to download the result once complete.</li>
 * </ul>
 *
 * @since 9.9.0
 */
public class ReportsClient {

    final RestEndpoint<RecordsFilter, RecordsResponse> getRecords;
    final RestEndpoint<AsyncReportRequest, ReportResponse> createReport;
    final RestEndpoint<String, ReportResponse> getReport;
    final RestEndpoint<String, ReportResponse> cancelReport;
    final RestEndpoint<String, byte[]> downloadReport;

    /**
     * Create a new ReportsClient.
     *
     * @param wrapper Http Wrapper used to create requests.
     */
    public ReportsClient(HttpWrapper wrapper) {
        @SuppressWarnings("unchecked")
        final class Endpoint<T, R> extends DynamicEndpoint<T, R> {
            Endpoint(Function<T, String> pathSuffix, HttpMethod method, R... type) {
                super(DynamicEndpoint.<T, R>builder(type)
                        .responseExceptionType(ReportsResponseException.class)
                        .wrapper(wrapper)
                        .requestMethod(method)
                        .authMethod(ApiKeyHeaderAuthMethod.class)
                        .pathGetter((de, req) ->
                                wrapper.getHttpConfig().getApiBaseUri() + pathSuffix.apply(req)
                        )
                );
            }
        }

        getRecords = new Endpoint<>(req -> "/v2/reports/records", HttpMethod.GET);
        createReport = new Endpoint<>(req -> "/v2/reports", HttpMethod.POST);
        getReport = new Endpoint<>(reportId -> "/v2/reports/" + reportId, HttpMethod.GET);
        cancelReport = new Endpoint<>(reportId -> "/v2/reports/" + reportId, HttpMethod.DELETE);
        downloadReport = new Endpoint<>(fileId -> "/v3/media/" + fileId, HttpMethod.GET);
    }

    private static String requireId(String id, String name) {
        Objects.requireNonNull(id, name + " is required.");
        if (id.trim().isEmpty()) throw new IllegalArgumentException(name + " cannot be empty.");
        return id;
    }

    /**
     * Synchronously retrieve report records for a given product and date range.
     * <p>
     * This endpoint immediately returns data and supports pagination via cursor. For large
     * datasets, consider using the asynchronous endpoint ({@link #createReport(AsyncReportRequest)}).
     * </p>
     *
     * @param filter Query parameters specifying the product, account, date range and optional filters.
     *
     * @return A {@link RecordsResponse} containing the records and pagination metadata.
     *
     * @throws ReportsResponseException If the request was unsuccessful. Possible reasons:
     * <ul>
     *   <li><b>401</b>: Authentication failure.</li>
     *   <li><b>403</b>: Forbidden — insufficient permissions.</li>
     *   <li><b>422</b>: Unprocessable entity — invalid parameters.</li>
     *   <li><b>429</b>: Too many requests — rate limit exceeded.</li>
     *   <li><b>500</b>: Internal server error.</li>
     * </ul>
     */
    public RecordsResponse getRecords(RecordsFilter filter) {
        return getRecords.execute(Objects.requireNonNull(filter, "Records filter is required."));
    }

    /**
     * Create an asynchronous report generation request.
     * <p>
     * The report will be processed in the background. Use the returned {@code request_id} to poll
     * for status with {@link #getReport(String)}, and once the status is {@link ReportStatus#SUCCESS},
     * use the file ID from {@link ReportResponse#getFileId()} with
     * {@link #downloadReport(String)} to retrieve the data.
     * </p>
     *
     * @param request The report request specifying the product, account, date range and optional filters.
     *
     * @return A {@link ReportResponse} containing the {@code request_id} and initial status.
     *
     * @throws ReportsResponseException If the request was unsuccessful. Possible reasons:
     * <ul>
     *   <li><b>400</b>: Bad request — invalid parameters.</li>
     *   <li><b>401</b>: Authentication failure.</li>
     *   <li><b>403</b>: Forbidden — insufficient permissions.</li>
     *   <li><b>422</b>: Unprocessable entity — invalid parameters.</li>
     *   <li><b>429</b>: Too many requests — rate limit exceeded.</li>
     *   <li><b>500</b>: Internal server error.</li>
     * </ul>
     */
    public ReportResponse createReport(AsyncReportRequest request) {
        return createReport.execute(Objects.requireNonNull(request, "Report request is required."));
    }

    /**
     * Retrieve the current status and details of an asynchronous report.
     * <p>
     * Reports are retained for 4 days; reports older than 4 days cannot be retrieved.
     * </p>
     *
     * @param reportId The {@code request_id} of the report to retrieve.
     *
     * @return A {@link ReportResponse} with the current status and report details.
     *
     * @throws ReportsResponseException If the request was unsuccessful. Possible reasons:
     * <ul>
     *   <li><b>401</b>: Authentication failure.</li>
     *   <li><b>404</b>: Report not found or older than 4 days.</li>
     *   <li><b>429</b>: Too many requests — rate limit exceeded.</li>
     *   <li><b>500</b>: Internal server error.</li>
     * </ul>
     */
    public ReportResponse getReport(String reportId) {
        return getReport.execute(requireId(reportId, "Report ID"));
    }

    /**
     * Cancel the execution of a pending or processing asynchronous report.
     * <p>
     * Reports that have already completed ({@link ReportStatus#SUCCESS}) cannot be cancelled.
     * </p>
     *
     * @param reportId The {@code request_id} of the report to cancel.
     *
     * @return A {@link ReportResponse} confirming the cancellation.
     *
     * @throws ReportsResponseException If the request was unsuccessful. Possible reasons:
     * <ul>
     *   <li><b>401</b>: Authentication failure.</li>
     *   <li><b>404</b>: Report not found.</li>
     *   <li><b>409</b>: Conflict — report cannot be cancelled in its current state.</li>
     *   <li><b>429</b>: Too many requests — rate limit exceeded.</li>
     *   <li><b>500</b>: Internal server error.</li>
     * </ul>
     */
    public ReportResponse cancelReport(String reportId) {
        return cancelReport.execute(requireId(reportId, "Report ID"));
    }

    /**
     * Download the completed report as a zip archive containing a CSV file.
     * <p>
     * The file is available for download for 72 hours after the report completes.
     * The file ID can be obtained via {@link ReportResponse#getFileId()}.
     * </p>
     *
     * @param fileId The UUID of the file to download, obtained from {@link ReportResponse#getFileId()}.
     *
     * @return The raw bytes of the zip archive.
     *
     * @throws ReportsResponseException If the request was unsuccessful. Possible reasons:
     * <ul>
     *   <li><b>401</b>: Authentication failure.</li>
     *   <li><b>404</b>: File not found or download link expired.</li>
     *   <li><b>429</b>: Too many requests — rate limit exceeded.</li>
     *   <li><b>500</b>: Internal server error.</li>
     * </ul>
     */
    public byte[] downloadReport(String fileId) {
        return downloadReport.execute(requireId(fileId, "File ID"));
    }
}
