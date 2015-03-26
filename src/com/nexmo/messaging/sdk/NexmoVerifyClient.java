package com.nexmo.messaging.sdk;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
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
import com.nexmo.messaging.sdk.verify.CheckResponse;
import com.nexmo.messaging.sdk.verify.SearchResponse;
import com.nexmo.messaging.sdk.verify.VerifyRequest;
import com.nexmo.messaging.sdk.verify.VerifyResponse;
import com.nexmo.security.RequestSigning;

public class NexmoVerifyClient extends BaseConnectionClient {
	private static final Log log = LogFactory.getLog(NexmoVerifyClient.class);

	public static final String BASE_URL = "https://api.nexmo.com";

	/**
	 * The end-point path for submitting verify requests
	 */
	public static final String SUBMISSION_PATH_VERIFY = "/verify/json";
	
	/**
	 * The end-point for checking verify requests
	 */
	public static final String SUBMISSION_PATH_VERIFY_CHECK = "/verify/check/json";

	/**
	 * Connection timeout
	 */
	public static final int CONNECTION_TIMEOUT = BaseConnectionClient.DEFAULT_CONNECTION_TIMEOUT;

	/**
	 * Read timeout
	 */
	public static final int DEFAULT_SO_TIMEOUT = BaseConnectionClient.DEFAULT_SO_TIMEOUT;

	
	private final String apiKey;
	private final String apiSecret;
	private final boolean signRequests;
	private final String signatureSecretKey;
	
	public NexmoVerifyClient(final String apiKey,
							 final String apiSecret) throws Exception {
		this(BASE_URL,
			apiKey,
			apiSecret,
			CONNECTION_TIMEOUT,
			DEFAULT_SO_TIMEOUT,
			false,
			null
		);
	}
							 


	public NexmoVerifyClient(final String baseUrl,
			final String apiKey,
			final String apiSecret,
			final int connectionTimeout,
			final int soTimeout,
			final boolean signRequests,
			final String signatureSecretKey) throws Exception {
		
		super(baseUrl, connectionTimeout, soTimeout);
		
		this.apiKey = apiKey;
		this.apiSecret = apiSecret;
		this.signRequests = signRequests;
		this.signatureSecretKey = signatureSecretKey;     
	}
	
	/**
	 * Start here by initiating a verify request to the API.
	 * @see https://docs.nexmo.com/index.php/verify/verify
	 */
	
	public VerifyResponse beginVerifyRequest (VerifyRequest verifyRequest) throws Exception {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String verifyRequestId = null;
		Integer verifyStatus = null;
		String verifyErrorText = null;

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
		
		/* MAKE CONNECTION*/
		String response = makeConnection(SUBMISSION_PATH_VERIFY, params);
		
		/* PROCESS THE HTTP RESPONSE */
		JSONParser parser = new JSONParser();
		ContainerFactory containerFactory = getContainerFactory(); 
		try{
			Map<String,String> json = (Map)parser.parse(response, containerFactory);
		
			final String requestIdString = "request_id";
			final String statusString = "status";
			final String errorTextString = "error_text";
			
			verifyRequestId =  json.get(requestIdString);
			verifyStatus = Integer.parseInt(json.get(statusString));	
			if ( json.containsKey(errorTextString)) 
				verifyErrorText = json.get(errorTextString);
		}
		catch(ParseException pe){
			log.info(pe);
		}
		
		return new VerifyResponse(verifyRequestId, verifyStatus, verifyErrorText);
	}
	
	public CheckResponse checkRequest(String requestId, String code) throws Exception {
		return checkRequest(requestId, code, null);
	}
	
	public CheckResponse checkRequest(String requestId, String code, String ipAddress) throws Exception{
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String eventId = null;
		Integer status = null;
		BigDecimal price = null;
		String currency = null;
		String errorText = null;
		
		params.add(new BasicNameValuePair("api_key", this.apiKey));
		if (!this.signRequests)
			params.add(new BasicNameValuePair("api_secret", this.apiSecret));
		if (this.signRequests)
			RequestSigning.constructSignatureForRequestParameters(params, this.signatureSecretKey);
		params.add(new BasicNameValuePair("request_id", requestId));
		params.add(new BasicNameValuePair("code", code));
		if(ipAddress != null)
			params.add(new BasicNameValuePair("ip_address", requestId));
		
		String response = makeConnection(SUBMISSION_PATH_VERIFY_CHECK, params);
		/* PROCESS THE HTTP RESPONSE */
		JSONParser parser = new JSONParser();
		ContainerFactory containerFactory = getContainerFactory(); 
		try{
			Map<String,String> json = (Map)parser.parse(response, containerFactory);
		
			final String eventIdString = "event_id";
			final String statusString = "status";
			final String priceString = "price";
			final String currencyString = "currency";
			final String errorTextString = "error_text";
			
			eventId =  json.get(eventIdString);
			status = Integer.parseInt(json.get(statusString));
			price = new BigDecimal(json.get(priceString));
			currency = json.get(currencyString);
			if ( json.containsKey(errorTextString)) 
				errorText = json.get(errorTextString);
		}
		catch(ParseException pe){
			log.info(pe);
		}
		
		return new CheckResponse(eventId, status, price, currency, errorText);
		
	}
	
	public SearchResponse search (String requestId) throws TooManyRequestIdsException {
		List<String> list = new ArrayList<>(1);
		list.add(requestId);
		search(list);
	}
	
	public SearchResponse search (List<String> requestIds) throws TooManyRequestIdsException {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		final String requestIdString = "request_id";
		final String requestIdsString = "request_ids";
			
		if (requestIds.size() == 1) {
			params.add(new BasicNameValuePair(requestIdString, requestIds.get(0) ));
		} else if (requestIds.size() > 10 ) {
			throw new TooManyRequestIdsException("Maximum 10 Request IDs allowed by Nexmo");
		} else {
			Iterator<String> it = requestIds.iterator();
			while(it.hasNext()) {
				params.add(new BasicNameValuePair(requestIdsString, it.next() ));
			}
		}
		
	}
	
}

class TooManyRequestIdsException extends Exception {

	private static final long serialVersionUID = 267011845743861836L;
	
	public TooManyRequestIdsException(){
		super();
	}
	
	public TooManyRequestIdsException(String message){
		super(message);
	}
	
}

