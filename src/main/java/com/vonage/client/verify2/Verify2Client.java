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

import com.vonage.client.DynamicEndpoint;
import com.vonage.client.HttpWrapper;
import com.vonage.client.RestEndpoint;
import com.vonage.client.auth.JWTAuthMethod;
import com.vonage.client.auth.ApiKeyHeaderAuthMethod;
import com.vonage.client.common.HttpMethod;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

public class Verify2Client {
	final boolean hasJwtAuthMethod;
	final RestEndpoint<VerificationRequest, VerificationResponse> verifyUser;
	final RestEndpoint<VerifyCodeRequestWrapper, VerifyCodeResponse> verifyRequest;
	final RestEndpoint<UUID, Void> cancel, nextWorkflow, deleteTemplate;
	final RestEndpoint<ListTemplatesRequest, ListTemplatesResponse> listTemplates;
	final RestEndpoint<UUID, Template> getTemplate;
	final RestEndpoint<Template, Template> createTemplate, updateTemplate;
	final RestEndpoint<ListTemplatesRequest, ListTemplateFragmentsResponse> listFragments;
	final RestEndpoint<TemplateFragmentRequestWrapper, TemplateFragment> getFragment;
	final RestEndpoint<TemplateFragmentRequestWrapper, Void> deleteFragment;
	final RestEndpoint<TemplateFragment, TemplateFragment> createFragment, updateFragment;

	/**
	 * Create a new Verify2Client.
	 *
	 * @param wrapper Http Wrapper used to create verification requests.
	 */
	public Verify2Client(HttpWrapper wrapper) {
		hasJwtAuthMethod = wrapper.getAuthCollection().hasAuthMethod(JWTAuthMethod.class);

		@SuppressWarnings("unchecked")
		final class Endpoint<T, R> extends DynamicEndpoint<T, R> {
			Endpoint(Function<T, String> pathGetter, HttpMethod method, R... type) {
				super(DynamicEndpoint.<T, R> builder(type)
						.responseExceptionType(VerifyResponseException.class)
						.wrapper(wrapper).requestMethod(method)
						.authMethod(JWTAuthMethod.class, ApiKeyHeaderAuthMethod.class)
						.pathGetter((de, req) -> {
							String base = de.getHttpWrapper().getHttpConfig().getApiBaseUri() + "/v2/verify";
							return pathGetter != null ? base + "/" + pathGetter.apply(req) : base;
						})
				);
			}
		}

		verifyUser = new Endpoint<>(null, HttpMethod.POST);
		verifyRequest = new Endpoint<>(req -> req.requestId, HttpMethod.POST);
		cancel = new Endpoint<>(UUID::toString, HttpMethod.DELETE);
		nextWorkflow = new Endpoint<>(id -> id + "/next-workflow", HttpMethod.POST);

		final String templatesBase = "templates";
		listTemplates = new Endpoint<>(__ -> templatesBase, HttpMethod.GET);
		getTemplate = new Endpoint<>(id -> templatesBase+'/'+id, HttpMethod.GET);
		createTemplate = new Endpoint<>(__ -> templatesBase, HttpMethod.POST);
		updateTemplate = new Endpoint<>(req -> templatesBase+'/'+req.id, HttpMethod.PATCH);
		deleteTemplate = new Endpoint<>(id -> templatesBase+'/'+id, HttpMethod.DELETE);

		final String fragmentsBase = "/template_fragments";
		listFragments = new Endpoint<>(req -> templatesBase+'/'+req.templateId + fragmentsBase, HttpMethod.GET);
		getFragment = new Endpoint<>(req ->
				templatesBase+'/'+req.templateId + fragmentsBase+'/'+req.fragmentId, HttpMethod.GET
		);
		createFragment = new Endpoint<>(req -> templatesBase+'/'+req.templateId + fragmentsBase, HttpMethod.POST);
		updateFragment = new Endpoint<>(req ->
				templatesBase+'/'+req.getTemplateId() + fragmentsBase+'/'+req.fragmentId, HttpMethod.PATCH
		);
		deleteFragment = new Endpoint<>(req ->
				templatesBase+'/'+req.templateId + fragmentsBase+'/'+req.fragmentId, HttpMethod.DELETE
		);
	}

	private UUID validateId(String name, UUID id) {
		return Objects.requireNonNull(id, name + " ID is required.");
	}

	private UUID validateRequestId(UUID requestId) {
		return validateId("Request", requestId);
	}

	private UUID validateTemplateId(UUID templateId) {
		return validateId("Template", templateId);
	}

	private UUID validateFragmentId(UUID fragmentId) {
		return validateId("Fragment", fragmentId);
	}

	/**
	 * Request a verification be sent to a user. This is the first step in the verification process.
	 *
	 * @param request Properties of the verification request. You must specify the brand name and at least one
	 * contact method (workflow). For example, to verify using Whatsapp and fall back to a voice call as backup
	 * to the same number with a 6-digit code and a 3-minute wait between attempts:
	 * <pre>
	 * {@code VerificationRequest.builder()
	 *      .brand("My Company")
	 *      .addWorkflow(new WhatsappWorkflow("447000000001"))
	 *      .addWorkflow(new VoiceWorkflow("447000000001"))
	 *      .codeLength(6)
	 *      .channelTimeout(180)
	 *      .build()}.
	 * </pre>
	 *
	 * @return The server's response, if successful.
	 *
	 * @throws VerifyResponseException If the request was unsuccessful. This could be for the following reasons:
	 * <ul>
	 *     <li><b>409</b>: Concurrent verifications to the same number are not allowed.</li>
	 *     <li><b>422</b>: The value of one or more parameters is invalid.</li>
	 *     <li><b>429</b>: Rate limit hit. Please wait and try again.</li>
	 *     <li><b>500</b>: An error occurred on the Vonage platform.</li>
	 * </ul>
	 */
	public VerificationResponse sendVerification(VerificationRequest request) {
		if (request.isCodeless() && !hasJwtAuthMethod) {
			throw new IllegalStateException(
				"Codeless verification requires an application ID to be set in order to use webhooks."
			);
		}
		return verifyUser.execute(Objects.requireNonNull(request));
	}

	/**
	 * Check a supplied code against an existing verification request. If the code is valid,
	 * this method will return normally. Otherwise, a {@link VerifyResponseException} will be thrown.
	 *
	 * @param requestId ID of the verify request, obtained from {@link VerificationResponse#getRequestId()}.
	 * @param code The code supplied by the user.
	 *
	 * @return Details of the verification request (if the code matched).
	 *
	 * @throws VerifyResponseException If the code could not be verified. This could be for the following reasons:
	 * <ul>
	 *     <li><b>400</b>: The provided code does not match the expected value.</li>
	 *     <li><b>401</b>: Invalid credentials.</li>
	 *     <li><b>402</b>: Low balance.</li>
	 *     <li><b>404</b>: Request ID was not found or it has been verified already.</li>
	 *     <li><b>409</b>: The current workflow step does not support a code.</li>
	 *     <li><b>410</b>: An incorrect code has been provided too many times.</li>
	 *     <li><b>429</b>: Rate limit hit. Please wait and try again.</li>
	 *     <li><b>500</b>: An error occurred on the Vonage platform.</li>
	 * </ul>
	 */
	public VerifyCodeResponse checkVerificationCode(UUID requestId, String code) {
		return verifyRequest.execute(new VerifyCodeRequestWrapper(
				validateRequestId(requestId).toString(),
				Objects.requireNonNull(code, "Code is required.")
		));
	}

	/**
	 * Attempts to abort an active verification workflow.
	 * If successful (HTTP status 204), this method will return normally.
	 * Otherwise, a {@link VerifyResponseException} exception will be thrown, indicating a 404 response.
	 *
	 * @param requestId ID of the verify request, obtained from {@link VerificationResponse#getRequestId()}.
	 *
	 * @throws VerifyResponseException If the request could not be cancelled. This could be for the following reasons:
	 * <ul>
	 *     <li><b>401</b>: Invalid credentials.</li>
	 *     <li><b>402</b>: Low balance.</li>
	 *     <li><b>404</b>: Request ID was not found or it has been verified / cancelled already.</li>
	 *     <li><b>500</b>: An error occurred on the Vonage platform.</li>
	 * </ul>
	 */
	public void cancelVerification(UUID requestId) {
		cancel.execute(validateRequestId(requestId));
	}

	/**
	 * Move the request onto the next workflow, if available. If successful, this method will return normally.
	 * Otherwise, a {@link VerifyResponseException} will be thrown.
	 *
	 * @param requestId ID of the verify request, obtained from {@link VerificationResponse#getRequestId()}.
	 *
	 * @throws VerifyResponseException If the workflow could not be advanced. This could be for the following reasons:
	 * <ul>
	 *      <li><b>401</b>: Invalid credentials.</li>
	 *      <li><b>404</b>: Request ID was not found or it has been verified already.</li>
	 *      <li><b>409</b>: There are no more events left to trigger.</li>
	 *      <li><b>500</b>: An error occurred on the Vonage platform.</li>
	 * </ul>
	 *
	 * @since 8.5.0
	 */
	public void nextWorkflow(UUID requestId) {
		nextWorkflow.execute(validateRequestId(requestId));
	}

	/**
	 * Create a new custom template.
	 *
	 * @param name Reference name for the template. Must not contain spaces or special characters other than _ and -.
	 *
	 * @return The created template metadata.
	 *
	 * @throws VerifyResponseException If the template could not be created. This could be for the following reasons:
	 * <ul>
	 *     <li><b>401</b>: Invalid credentials.</li>
	 *     <li><b>402</b>: Low balance.</li>
	 *     <li><b>403</b>: Template management is not enabled for your account.</li>
	 *     <li><b>409</b>: A template with the same name already exists, or you have more than 9 templates.</li>
	 *     <li><b>429</b>: Rate limit hit. Please wait and try again.</li>
	 *     <li><b>500</b>: An error occurred on the Vonage platform.</li>
	 * </ul>
	 *
	 * @since 8.13.0
	 */
	public Template createTemplate(String name) {
		return createTemplate.execute(new Template(Objects.requireNonNull(name, "Name is required."), null, null));
	}

	/**
	 * List all custom templates associated with the account.
	 *
	 * @return The list of templates.
	 *
	 * @throws VerifyResponseException If the templates could not be retrieved. This could be for the following reasons:
	 * <ul>
	 *     <li><b>401</b>: Invalid credentials.</li>
	 *     <li><b>402</b>: Low balance.</li>
	 *     <li><b>429</b>: Rate limit hit. Please wait and try again.</li>
	 *     <li><b>500</b>: An error occurred on the Vonage platform.</li>
	 * </ul>
	 *
	 * @since 8.13.0
	 */
	public List<Template> listTemplates() {
		return listTemplates(1, 100).getTemplates();
	}

	// Not useful since there can only be 10 templates at a time.
	ListTemplatesResponse listTemplates(Integer page, Integer pageSize) {
		return listTemplates.execute(new ListTemplatesRequest(page, pageSize, null));
	}

	/**
	 * Retrieve a specific template.
	 *
	 * @param templateId ID of the template to retrieve.
	 *
	 * @return The template metadata.
	 *
	 * @throws VerifyResponseException If the template could not be retrieved. This could be for the following reasons:
	 * <ul>
	 *     <li><b>401</b>: Invalid credentials.</li>
	 *     <li><b>402</b>: Low balance.</li>
	 *     <li><b>404</b>: Template ID was not found.</li>
	 *     <li><b>429</b>: Rate limit hit. Please wait and try again.</li>
	 *     <li><b>500</b>: An error occurred on the Vonage platform.</li>
	 * </ul>
	 *
	 * @since 8.13.0
	 */
	public Template getTemplate(UUID templateId) {
		return getTemplate.execute(validateTemplateId(templateId));
	}

	/**
	 * Update an existing template.
	 *
	 * @param templateId ID of the template to update.
	 * @param name New reference name for the template. Must not contain spaces or special characters.
	 * @param isDefault Whether this template should be the default for the account.
	 *
	 * @return The updated template metadata.
	 *
	 * @throws VerifyResponseException If the template could not be updated. This could be for the following reasons:
	 * <ul>
	 *     <li><b>401</b>: Invalid credentials.</li>
	 *     <li><b>402</b>: Low balance.</li>
	 *     <li><b>403</b>: Template management is not enabled for your account.</li>
	 *     <li><b>404</b>: Template ID was not found.</li>
	 *     <li><b>409</b>: A template with the same name already exists, or you have more than 9 templates.</li>
	 *     <li><b>429</b>: Rate limit hit. Please wait and try again.</li>
	 *     <li><b>500</b>: An error occurred on the Vonage platform.</li>
	 * </ul>
	 *
	 * @since 8.13.0
	 */
	public Template updateTemplate(UUID templateId, String name, Boolean isDefault) {
		return updateTemplate.execute(new Template(name, isDefault, validateTemplateId(templateId)));
	}

	/**
	 * Delete a template.
	 *
	 * @param templateId ID of the template to delete.
	 *
	 * @throws VerifyResponseException If the template could not be deleted. This could be for the following reasons:
	 * <ul>
	 *     <li><b>401</b>: Invalid credentials.</li>
	 *     <li><b>402</b>: Low balance.</li>
	 *     <li><b>403</b>: Template management is not enabled for your account.</li>
	 *     <li><b>404</b>: Template not found.</li>
	 *     <li><b>409</b>: Template contains undeleted fragments or is the default.</li>
	 *     <li><b>429</b>: Rate limit hit. Please wait and try again.</li>
	 *     <li><b>500</b>: An error occurred on the Vonage platform.</li>
	 * </ul>
	 *
	 * @since 8.13.0
	 */
	public void deleteTemplate(UUID templateId) {
		deleteTemplate.execute(validateTemplateId(templateId));
	}

	/**
	 * Create a new template fragment.
	 *
	 * @param templateId ID of the template to which the fragment belongs.
	 * @param fragment The fragment to create.
	 *
	 * @return The created fragment metadata.
	 *
	 * @throws VerifyResponseException If the fragment could not be created. This could be for the following reasons:
	 * <ul>
	 *     <li><b>401</b>: Invalid credentials.</li>
	 *     <li><b>402</b>: Low balance.</li>
	 *     <li><b>403</b>: Template management is not enabled for your account.</li>
	 *     <li><b>404</b>: Template ID was not found.</li>
	 *     <li><b>409</b>: Fragment for this channel and locale already exists.</li>
	 *     <li><b>422</b>: Invalid parameters (e.g. unsupported locale or invalid text variables).</li>
	 *     <li><b>429</b>: Rate limit hit. Please wait and try again.</li>
	 *     <li><b>500</b>: An error occurred on the Vonage platform.</li>
	 * </ul>
	 *
	 * @since 8.13.0
	 */
	public TemplateFragment createTemplateFragment(UUID templateId, TemplateFragment fragment) {
		Objects.requireNonNull(fragment, "Template fragment is required.").templateId = validateTemplateId(templateId);
		return createFragment.execute(fragment);
	}

	/**
	 * List all fragments associated with a template.
	 *
	 * @param templateId ID of the template to list fragments for.
	 *
	 * @return The list of fragments.
	 *
	 * @throws VerifyResponseException If the fragments could not be retrieved. This could be for the following reasons:
	 * <ul>
	 *     <li><b>401</b>: Invalid credentials.</li>
	 *     <li><b>402</b>: Low balance.</li>
	 *     <li><b>404</b>: Template ID was not found.</li>
	 *     <li><b>429</b>: Rate limit hit. Please wait and try again.</li>
	 *     <li><b>500</b>: An error occurred on the Vonage platform.</li>
	 * </ul>
	 *
	 * @since 8.13.0
	 */
	public List<TemplateFragment> listTemplateFragments(UUID templateId) {
		return listTemplateFragments(templateId, 1, 1000).getTemplateFragments();
	}

	// Not useful in the general case to expose.
	ListTemplateFragmentsResponse listTemplateFragments(UUID templateId, Integer page, Integer pageSize) {
		return listFragments.execute(new ListTemplatesRequest(page, pageSize, validateTemplateId(templateId)));
	}

	/**
	 * Retrieve a specific fragment.
	 *
	 * @param templateId ID of the template to which the fragment belongs.
	 * @param fragmentId ID of the fragment to retrieve.
	 *
	 * @return The fragment metadata.
	 *
	 * @throws VerifyResponseException If the fragment could not be retrieved. This could be for the following reasons:
	 * <ul>
	 *     <li><b>401</b>: Invalid credentials.</li>
	 *     <li><b>402</b>: Low balance.</li>
	 *     <li><b>404</b>: Fragment not found for the provided IDs.</li>
	 *     <li><b>429</b>: Rate limit hit. Please wait and try again.</li>
	 *     <li><b>500</b>: An error occurred on the Vonage platform.</li>
	 * </ul>
	 *
	 * @since 8.13.0
	 */
	public TemplateFragment getTemplateFragment(UUID templateId, UUID fragmentId) {
		return getFragment.execute(new TemplateFragmentRequestWrapper(
				validateTemplateId(templateId), validateFragmentId(fragmentId)
		));
	}

	/**
	 * Update an existing template fragment.
	 *
	 * @param templateId ID of the template to which the fragment belongs.
	 *
	 * @param fragmentId ID of the fragment to update.
	 *
	 * @param text New text for the fragment.
	 * There are 4 reserved variables available to use: ${code}, ${brand}, ${time-limit} and ${time-limit-unit}.
	 *
	 * @return The updated fragment metadata.
	 *
	 * @throws VerifyResponseException If the fragment could not be updated. This could be for the following reasons:
	 * <ul>
	 *     <li><b>401</b>: Invalid credentials.</li>
	 *     <li><b>402</b>: Low balance.</li>
	 *     <li><b>403</b>: Template management is not enabled for your account.</li>
	 *     <li><b>404</b>: Fragment not found for the provided IDs.</li>
	 *     <li><b>409</b>: Fragment for this channel and locale already exists.</li>
	 *     <li><b>422</b>: Invalid parameters (e.g. unsupported locale or invalid text variables).</li>
	 *     <li><b>429</b>: Rate limit hit. Please wait and try again.</li>
	 *     <li><b>500</b>: An error occurred on the Vonage platform.</li>
	 * </ul>
	 *
	 * @since 8.13.0
	 */
	public TemplateFragment updateTemplateFragment(UUID templateId, UUID fragmentId, String text) {
		return updateFragment.execute(new TemplateFragment(
				text, validateTemplateId(templateId), validateFragmentId(fragmentId)
		));
	}

	/**
	 * Delete a template fragment.
	 *
	 * @param templateId ID of the template to which the fragment belongs.
	 * @param fragmentId ID of the fragment to delete.
	 *
	 * @throws VerifyResponseException If the fragment could not be deleted. This could be for the following reasons:
	 * <ul>
	 *     <li><b>401</b>: Invalid credentials.</li>
	 *     <li><b>402</b>: Low balance.</li>
	 *     <li><b>403</b>: Template management is not enabled for your account.</li>
	 *     <li><b>404</b>: Fragment not found for the provided IDs.</li>
	 *     <li><b>409</b>: Fragment is in use by a template or is the default.</li>
	 *     <li><b>429</b>: Rate limit hit. Please wait and try again.</li>
	 *     <li><b>500</b>: An error occurred on the Vonage platform.</li>
	 * </ul>
	 *
	 * @since 8.13.0
	 */
	public void deleteTemplateFragment(UUID templateId, UUID fragmentId) {
		deleteFragment.execute(new TemplateFragmentRequestWrapper(
				validateTemplateId(templateId),
				validateFragmentId(fragmentId)
		));
	}
}
