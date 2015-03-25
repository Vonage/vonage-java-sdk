package com.nexmo.messaging.sdk;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.nexmo.common.http.HttpClientUtils;
import com.nexmo.messaging.sdk.verify.VerifyRequest;
import com.nexmo.messaging.sdk.verify.VerifyResponse;
import com.nexmo.security.RequestSigning;

public class NexmoVerifyClient {
	private static final Log log = LogFactory.getLog(NexmoVerifyClient.class);

	public static final String DEFAULT_BASE_URL = "https://api.nexmo.com";

	/**
	 * The endpoint path for submitting sms messages
	 */
	public static final String SUBMISSION_PATH_VERIFY = "/verify/json";

	/**
	 * Default connection timeout of 5000ms used by this client unless specifically overridden on the constructor
	 */
	public static final int DEFAULT_CONNECTION_TIMEOUT = 5000;

	/**
	 * Default read timeout of 30000ms used by this client unless specifically overridden on the constructor
	 */
	public static final int DEFAULT_SO_TIMEOUT = 30000;

	private final String baseUrl;
	private final String apiKey;
	private final String apiSecret;

	private final int connectionTimeout;
	private final int soTimeout;

	private final boolean signRequests;
	private final String signatureSecretKey;


	private HttpClient httpClient = null;
	
	public NexmoVerifyClient(final String apiKey,
							 final String apiSecret) throws Exception {
		this(DEFAULT_BASE_URL,
			apiKey,
			apiSecret,
			DEFAULT_CONNECTION_TIMEOUT,
			DEFAULT_SO_TIMEOUT,
			false,
			null
		);
	}
							 


	public NexmoVerifyClient(String baseUrl,
			final String apiKey,
			final String apiSecret,
			final int connectionTimeout,
			final int soTimeout,
			final boolean signRequests,
			final String signatureSecretKey) throws Exception {

		this.baseUrl = baseUrl.trim();
		this.apiKey = apiKey;
		this.apiSecret = apiSecret;
		this.connectionTimeout = connectionTimeout;
		this.soTimeout = soTimeout;
		this.signRequests = signRequests;
		this.signatureSecretKey = signatureSecretKey;
	}

	public VerifyResponse beginVerifyRequest (VerifyRequest verifyRequest) throws Exception {

		String submitPath = SUBMISSION_PATH_VERIFY;

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String verifyRequestId = null;
		Integer verifyStatus = null;
		String verifyErrorText = "";

		params.add(new BasicNameValuePair("api_key", this.apiKey));
		if (!this.signRequests)
			params.add(new BasicNameValuePair("api_secret", this.apiSecret));

		if (this.signRequests)
			RequestSigning.constructSignatureForRequestParameters(params, this.signatureSecretKey);

		params.add(new BasicNameValuePair("number", verifyRequest.getNumber()));
		params.add(new BasicNameValuePair("brand", verifyRequest.getBrand()));
		if (verifyRequest.getSenderId() != null)
			params.add(new BasicNameValuePair("sender_id", verifyRequest.getSenderId()));
		if (verifyRequest.getCodeLength() != null)
			params.add(new BasicNameValuePair("code_length","" + verifyRequest.getCodeLength()));
		if (verifyRequest.getLg() != null)
			params.add(new BasicNameValuePair("lg", verifyRequest.getLg()));
		if (verifyRequest.getRequireType() != null)
			params.add(new BasicNameValuePair("require_type", verifyRequest.getRequireType()));
		if (verifyRequest.getPinExpiry() != null)
			params.add(new BasicNameValuePair("pin_expiry", "" + verifyRequest.getPinExpiry()));
		
		String baseUrl = this.baseUrl;
		baseUrl = baseUrl + submitPath;

		String response = null;
		for (int pass=1;pass<=2;pass++) {
			HttpUriRequest method = null;
			String url = null;

			HttpPost httpPost = new HttpPost(baseUrl);
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			method = httpPost;
			url = baseUrl + "?" + URLEncodedUtils.format(params, "utf-8");


			try {
				if (this.httpClient == null)
					this.httpClient = HttpClientUtils.getInstance(this.connectionTimeout, this.soTimeout).getNewHttpClient();
				HttpResponse httpResponse = this.httpClient.execute(method);
				int status = httpResponse.getStatusLine().getStatusCode();
				if (status != 200)
					throw new Exception("got a non-200 response [ " + status + " ] from Nexmo-HTTP for url [ " + url + " ] ");
				response = new BasicResponseHandler().handleResponse(httpResponse);
				log.info(".. SUBMITTED NEXMO-HTTP URL [ " + url + " ] -- response [ " + response + " ] ");
				break;
			} catch (Exception e) {
				method.abort();
				log.info("communication failure: " + e);
				String exceptionMsg = e.getMessage();
				if (exceptionMsg.indexOf("Read timed out") >= 0) {
					log.info("we're still connected, but the target did not respond in a timely manner ..  drop ...");
				} else {
					if (pass == 1) {
						log.info("... re-establish http client ...");
						this.httpClient = null;
						continue;
					}
				}
			}
		}
		//TODO: USE LOGGER

		JSONParser parser = new JSONParser();
		ContainerFactory containerFactory = new ContainerFactory(){
			public List creatArrayContainer() {
				return new LinkedList();
			}

			public Map createObjectContainer() {
				return new LinkedHashMap();
			}

		};

		try{
			Map<String,String> json = (Map)parser.parse(response, containerFactory);
			//Iterator iter = json.entrySet().iterator();
			//log.info("==iterate result==");
			
			String requestIdString = "request_id";
			final String statusString = "status";
			final String errorTextString = "error_text";
			
			verifyRequestId =  json.get(requestIdString);
			verifyStatus = Integer.parseInt(json.get(statusString));	
			if ( json.containsKey(errorTextString)) 
				verifyErrorText = json.get(errorTextString);
			
			
			/*while(iter.hasNext()){
				Map.Entry entry = (Map.Entry)iter.next();
				//log.info(entry.getKey() + "=>" + entry.getValue());
				if (entry.getKey().equals("request_id")) {
					log.info("Request ID: " + entry.getValue());
				} else if ( entry.getKey().equals("status")) {
					log.info("Status :" + entry.getValue());
				} else if ( entry.getKey().equals("error_text")) {
					log.info("Error_Text: " + entry.getValue());
				}
			}*/

			//log.info("==toJSONString()==");
			//log.info(JSONValue.toJSONString(json));
		}
		catch(ParseException pe){
			log.info(pe);
		}
		
		return new VerifyResponse(verifyRequestId, verifyStatus, verifyErrorText);
	}
}

