/**
 * Client for Number Insight
 * @see https://docs.nexmo.com/index.php/number-insight/
 */
package com.nexmo.messaging.sdk;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import com.nexmo.messaging.sdk.insight.Feature;
import com.nexmo.messaging.sdk.insight.InsightRequest;
import com.nexmo.security.RequestSigning;

/**
 * @author Wale Olaleye
 * @version 1.0
 *
 */
public class NexmoInsightClient extends BaseConnectionClient {
	
	private final String apiKey;
	private final String apiSecret;
	private final boolean signRequests;
	private final String signatureSecretKey;
	private final String endpointPath;
	
	/**
	 * @param endpointBaseUrl	The base URL of the api endpoint, e.g "https://rest.nexmo.com"
	 * @param endpointPath		The path to append to endpointBaseUrl e.g "/ni/json" OR "/ni/xml". Must start with "/".
	 * @param apiKey			Your API public Key
	 * @param apiSecret			Your API secret Key
	 * @param connectionTimeout	Http connection timeout for this client
	 * @param soTimeout			Read timeout for this client
	 * @param signRequests		Should we generate a signature for this request using the secret key?
	 * @param signatureSecretKey The secret key we will use to generate the signatures for signed requests
	 * 
	 */
	public NexmoInsightClient(final String endpointBaseUrl, String endpointPath, final String apiKey, 
			final String apiSecret, final int connectionTimeout, final int soTimeout, final boolean signRequests, 
			final String signatureSecretKey) {
		
		super(endpointBaseUrl, connectionTimeout, soTimeout);
		
		this.apiKey = apiKey;
		this.apiSecret = apiSecret;
		this.signRequests = signRequests;
		this.signatureSecretKey = signatureSecretKey;
		this.endpointPath = endpointPath;
	}
	
	public NexmoInsightClient(final String endpointBaseUrl, final String endpointPath, final String apiKey, 
			final String apiSecret) {
		
		super(endpointBaseUrl, BaseConnectionClient.DEFAULT_CONNECTION_TIMEOUT, 
				BaseConnectionClient.DEFAULT_SO_TIMEOUT);
		
		this.endpointPath = endpointPath;
		this.apiKey = apiKey;
		this.apiSecret = apiSecret;
		this.signRequests = false;
		this.signatureSecretKey = null;
	}
	
	/**
	 * Submits a request to the number insight API and returns nothing. Check the callback URL for response.
	 * 
	 * @param  request A InsightRequest Object
	 * @throws Exception
	 */
	
	public void submit (InsightRequest request) throws Exception {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
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
		makeConnection(endpointPath,params);		
	}

}
