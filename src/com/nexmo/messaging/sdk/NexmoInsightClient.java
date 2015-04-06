
package com.nexmo.messaging.sdk;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.nexmo.messaging.sdk.insight.Feature;
import com.nexmo.messaging.sdk.insight.InsightRequest;
import com.nexmo.messaging.sdk.insight.InsightResponse;
import com.nexmo.security.RequestSigning;

/**
 * Client for Number Insight - https://docs.nexmo.com/index.php/number-insight/
 * 
 * @author Wale Olaleye
 * @version 1.0
 *
 */
public class NexmoInsightClient extends BaseConnectionClient {
	private static final Log log = LogFactory.getLog(NexmoInsightClient.class);
	private static final String ENDPOINT_BASEURL = "https://rest.nexmo.com";
	private static final String ENDPOINT_PATH = "/ni/json";
	private final String apiKey;
	private final String apiSecret;
	private final boolean signRequests;
	private final String signatureSecretKey;
	
	/**
	 * @param endpointBaseUrl	The base URL of the api endpoint, e.g "https://rest.nexmo.com"
	 * @param apiKey			Your API public Key
	 * @param apiSecret			Your API secret Key
	 * @param connectionTimeout	Http connection timeout for this client
	 * @param soTimeout			Read timeout for this client
	 * @param signRequests		Should we generate a signature for this request using the secret key?
	 * @param signatureSecretKey The secret key we will use to generate the signatures for signed requests
	 * 
	 */
	public NexmoInsightClient(final String endpointBaseUrl, final String apiKey, 
			final String apiSecret, final int connectionTimeout, final int soTimeout, final boolean signRequests, 
			final String signatureSecretKey) {
		
		super(endpointBaseUrl, connectionTimeout, soTimeout);
		
		this.apiKey = apiKey;
		this.apiSecret = apiSecret;
		this.signRequests = signRequests;
		this.signatureSecretKey = signatureSecretKey;
	}
	
	public NexmoInsightClient(final String apiKey, 
			final String apiSecret) {
		
		super(ENDPOINT_BASEURL, BaseConnectionClient.DEFAULT_CONNECTION_TIMEOUT, 
				BaseConnectionClient.DEFAULT_SO_TIMEOUT);
		
		this.apiKey = apiKey;
		this.apiSecret = apiSecret;
		this.signRequests = false;
		this.signatureSecretKey = null;
	}
	
	/**
	 * Submits a request to the number insight API and returns nothing. Check the callback URL for response.
	 * 
	 * @param  request A InsightRequest Object
	 * @return InsightResponse
	 * @throws Exception
	 */
	
	public InsightResponse submit (InsightRequest request) throws Exception {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String responseRequestId = null;
		String responseNumber = null;
		Integer responseStatus = null;
		String responseErrorText = null;
		BigDecimal responseRemainingBalance = null;
		Double responseRequestPrice = null;
		
		params.add(new BasicNameValuePair("api_key", this.apiKey));
		if (!this.signRequests)
			params.add(new BasicNameValuePair("api_secret", this.apiSecret));

		if (this.signRequests)
			RequestSigning.constructSignatureForRequestParameters(params, this.signatureSecretKey);
		
		params.add(new BasicNameValuePair("number",request.getNumber()));
		
		List<Feature> features = request.getFeatures();
		if (!features.isEmpty()) {
			StringBuilder featureString = new StringBuilder();
			// Clever code avoids adding delimiter before FIRST and after LAST element
			String delimiter = "";
			for (Feature feature : features) {
				featureString.append(delimiter).append(feature.getRequestString());
				delimiter= ",";
			}
			params.add(new BasicNameValuePair("features",featureString.toString()));
		}
		
		params.add(new BasicNameValuePair("callback", request.getCallback()));
		
		if (request.getCallbackTimeout() != null)
			params.add(new BasicNameValuePair("callback_timeout", request.getCallbackTimeout().toString()));
		if (request.getCallbackMethod() != null)
			params.add(new BasicNameValuePair("callback_method", request.getCallbackMethod()));
		if (request.getClientRef() != null)
			params.add(new BasicNameValuePair("client_ref", request.getClientRef()));
		
		//make connection
		String response = makeConnection(ENDPOINT_PATH,params);
		/* PROCESS THE HTTP RESPONSE */
		JSONParser parser = new JSONParser();
		ContainerFactory containerFactory = getContainerFactory(); 
		try{
			@SuppressWarnings("unchecked")
			Map<String,Object> json = (Map<String,Object>)parser.parse(response, containerFactory);
		
			
			final String errorTextString = "error_text";
			responseStatus = Integer.valueOf( ((Long)json.get("status")).intValue() );
			if (responseStatus == 0) {
				responseRequestId =  (String)json.get("request_id");
				responseNumber = (String)json.get("number");
				responseStatus = Integer.valueOf( ((Long)json.get("status")).intValue() );
				responseRemainingBalance = new BigDecimal((String)json.get("remaining_balance"));
				responseRequestPrice = (Double)json.get("request_price");
			} else {
				responseErrorText = (String)json.get(errorTextString);
			}
			
		}
		catch(ParseException pe){
			log.info(pe);
		}
		
		return new InsightResponse(responseRequestId, responseNumber, responseStatus,
				responseErrorText, responseRemainingBalance, responseRequestPrice);		
	}

}
