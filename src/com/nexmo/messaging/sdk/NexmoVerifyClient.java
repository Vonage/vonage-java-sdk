package com.nexmo.messaging.sdk;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.nexmo.messaging.sdk.verify.CheckResponse;
import com.nexmo.messaging.sdk.verify.Checks;
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
	 * The end-point for verify search requests
	 */
	public static final String SUBMISSION_PATH_VERIFY_SEARCH = "/verify/search/json";
	
	/**
	 * Connection timeout
	 */
	public static final int CONNECTION_TIMEOUT = BaseConnectionClient.DEFAULT_CONNECTION_TIMEOUT;

	/**
	 * Read timeout
	 */
	public static final int DEFAULT_SO_TIMEOUT = BaseConnectionClient.DEFAULT_SO_TIMEOUT;
	
	/**
	 * Nexmo Date Format
	 */
	public static final String DEFAULT_DATE_FORMAT="yyyy-MM-dd HH:mm:ss";
	
	private final DateFormat format = new SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.ENGLISH);
	private final String apiKey;
	private final String apiSecret;
	private final boolean signRequests;
	private final String signatureSecretKey;
	
	/**
	 * 
	 * @param apiKey Your API public key
	 * @param apiSecret Your API secret key
	 * @throws Exception
	 */
	
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
							 

	/**
	 * 
	 * @param baseUrl Base API URL. Does not include the path.
	 * @param apiKey Your API public key
	 * @param apiSecret Your API secret key
	 * @param connectionTimeout HTTP connection timeout
	 * @param soTimeout HTTP Read timeout
	 * @param signRequests Should we sign requests?
	 * @param signatureSecretKey Request signing key
	 * @throws Exception
	 */
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
	 * @param verifyRequest Takes a VerifyRequest object
	 * @return VerifyResponse
	 */
	
	@SuppressWarnings("unchecked")
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
			Map<String,String> json = (Map<String,String>)parser.parse(response, containerFactory);
		
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
	
	/**
	 * This is the second step. Supply the request_id and authentication code the user received on their cell phone.
	 * This  method takes only the required parameters as a convenience.
	 * 
	 * @param requestId Identifier of the verification request to be checked
	 * @param code	PIN code your end user provided to you (maximum 6 digits)
	 * @return
	 * @throws Exception
	 */
	
	public CheckResponse checkRequest(String requestId, String code) throws Exception {
		return checkRequest(requestId, code, null);
	}
	
	/**
	 * 
	 * This is the second step. Supply the request_id and authentication code the user received on their cell phone.
	 * 
	 * @param requestId Identifier of the verification request to be checked.
	 * @param code PIN code your end user provided to you (maximum 6 digits)
	 * @param ipAddress The IP Address the end user provided you the PIN code from
	 * @return
	 * @throws Exception
	 */
	
	@SuppressWarnings("unchecked")
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
			Map<String,String> json = (Map<String,String>)parser.parse(response, containerFactory);
		
			final String eventIdString = "event_id";
			final String statusString = "status";
			final String priceString = "price";
			final String currencyString = "currency";
			final String errorTextString = "error_text";
			
			eventId =  json.get(eventIdString);
			status = Integer.parseInt(json.get(statusString));
			price = new BigDecimal(json.get(priceString));
			currency = json.get(currencyString);
			if ( json.containsKey(errorTextString) ) 
				errorText = json.get(errorTextString);
		}
		catch(ParseException pe){
			log.info(pe);
		}
		
		return new CheckResponse(eventId, status, price, currency, errorText);
		
	}
	/**
	 * The Verification Search API call enables you to get the status and many other attributes of a verification 
	 * request even if the verification is still in progress.
	 * 
	 * @param requestId Identifier of a single request to be looked up
	 * @return SearchResponse
	 * @throws Exception
	 */
	
	public SearchResponse search (String requestId) throws Exception {
		List<String> list = new ArrayList<>(1);
		list.add(requestId);
		SearchResponse[] searchResponse = search(list);
		return searchResponse[0];
	}
	
	/**
	 * The Verification Search API call enables you to get the status and many other attributes of a verification 
	 * request even if the verification is still in progress.
	 * 
	 * @param requestIds Identifier of up to 10 requests to be looked up.
	 * @return SearchResponse
	 * @throws Exception
	 */
	
	@SuppressWarnings("unchecked")
	public SearchResponse[] search (List<String> requestIds) throws Exception {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		final String requestIdString = "request_id";
		final String requestIdsString = "request_ids";
		String verificationRequestsString = "verification_requests";
		SearchResponse[] searchResponse = null;
		
		params.add(new BasicNameValuePair("api_key", this.apiKey));
		if (!this.signRequests)
			params.add(new BasicNameValuePair("api_secret", this.apiSecret));
		if (this.signRequests)
			RequestSigning.constructSignatureForRequestParameters(params, this.signatureSecretKey);
		
		if (requestIds.size() == 1) {
			params.add(new BasicNameValuePair(requestIdString, requestIds.get(0) ));
		} else if (requestIds.size() > 10 ) {
			throw new TooManyRequestIdsException("Maximum 10 Request IDs allowed by Nexmo Search API");
		} else {
			Iterator<String> it = requestIds.iterator();
			while(it.hasNext()) {
				params.add(new BasicNameValuePair(requestIdsString, it.next() ));
			}
		}
		
		String response = makeConnection(SUBMISSION_PATH_VERIFY_SEARCH, params);
		JSONParser parser = new JSONParser();
		ContainerFactory containerFactory = getContainerFactory(); 
		
		try{
			Map<String,Object> json = (Map<String,Object>)parser.parse(response, containerFactory);
		
			if (json.containsKey(verificationRequestsString)) {
				searchResponse = parseMultipleSearchResults((List<Object>)json.get(verificationRequestsString));
			} else {
				searchResponse = new SearchResponse[1];
				searchResponse[0] = parseSingleSearchResult(json);
			}
			
		}
		catch(ParseException pe){
			log.info(pe);
		}
		
		return searchResponse;
	}
	
	@SuppressWarnings("unchecked")
	private SearchResponse[] parseMultipleSearchResults(List<Object> results) throws java.text.ParseException {
		List<SearchResponse> searchResponse = new ArrayList<SearchResponse>();
		Iterator<Object> it = results.iterator();
		while(it.hasNext()) {
			searchResponse.add(parseSingleSearchResult((Map<String,Object>)it.next()));
		}
		return searchResponse.toArray(new SearchResponse[searchResponse.size()]);
	}
	
	@SuppressWarnings("unchecked")
	private SearchResponse parseSingleSearchResult(Map<String,Object> json) throws java.text.ParseException{
		String requestId;
		String accountId;
		String status;
		String number;
		BigDecimal price;
		String currency;
		String senderId;
		Date dateSubmitted;
		Date dateFinalized;
		Date firstEventSent;
		Date lastEventSent;
		Checks[] checks;
		String errorText;
		
		String requestIdString ="request_id";
		String accoundIdString = "account_id";
	    String statusString = "status";
	    String numberString = "number";
	    String priceString = "price";
	    String currencyString = "currency";
	    String senderIdString = "sender_id";
	    String dateSubmittedString = "date_submitted";
	    String dateFinalizedString = "date_finalized";
	    String firstEventString = "first_event_date";
	    String lastEventString = "last_event_date";
	    String checksString = "checks";
	    String errorTextString = "error_text";
	    
	    requestId = (String) json.get(requestIdString);
	    accountId = (String) json.get(accoundIdString);
	    status = (String) json.get(statusString);
	    number = (String) json.get(numberString);
	    price = new BigDecimal((String) json.get(priceString));
	    currency = (String) json.get(currencyString);
	    senderId = (String) json.get(senderIdString);
	    dateSubmitted = format.parse((String) json.get(dateSubmittedString));
	    dateFinalized = format.parse((String) json.get(dateFinalizedString));
	    firstEventSent = format.parse((String) json.get(firstEventString));
	    lastEventSent = format.parse((String) json.get(lastEventString));
	    checks = parseChecks((List<Object>)json.get(checksString));
	    errorText = (String) json.get(errorTextString);
	    
	    return new SearchResponse(requestId, accountId, status, number, price, currency,
	    		senderId, dateSubmitted, dateFinalized, firstEventSent, lastEventSent, checks, errorText);
	}
	
	@SuppressWarnings("unchecked")
	private Checks[] parseChecks(List<Object> checkList) throws java.text.ParseException {
		
		Date dateRecieved;
		String code;
		String status;
		String ipAddress;
		
		List<Checks> checks = new ArrayList<Checks>();
		Iterator<Object> it = checkList.iterator();
		Map<String,String> m;
		while(it.hasNext()) {
			m = (Map<String, String>) it.next();
			dateRecieved = format.parse(m.get("date_received"));
			code = m.get("code");
			status = m.get("status");
			ipAddress = m.get("ip_address");
			checks.add(new Checks(dateRecieved, code, status, ipAddress));
		}
		return checks.toArray(new Checks[checks.size()]);
	}
	
}

/**
 * An exception used only in this file.
 */
class TooManyRequestIdsException extends Exception {
	
	private static final long serialVersionUID = 267011845743861836L;
	
	public TooManyRequestIdsException(){
		super();
	}
	
	public TooManyRequestIdsException(String message){
		super(message);
	}
	
}

