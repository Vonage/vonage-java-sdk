/**
 * Base class for HTTP making connections
 */
package com.nexmo.messaging.sdk;

import java.io.UnsupportedEncodingException;
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
import org.json.simple.parser.ContainerFactory;

import com.nexmo.common.http.HttpClientUtils;

/**
 * @author Wale Olaleye
 *
 */
class BaseConnectionClient {
	
	private static final Log log = LogFactory.getLog(BaseConnectionClient.class);
	/**
	 * Default connection timeout of 5000ms used by this client
	 */
	public static final int DEFAULT_CONNECTION_TIMEOUT = 5000;

	/**
	 * Default read timeout of 30000ms used by this client
	 */
	public static final int DEFAULT_SO_TIMEOUT = 30000;

	private final String baseUrl;
	private final int connectionTimeout;
	private final int soTimeout;
	private HttpClient httpClient = null;
	
	/**
	 * 
	 * @param baseUrl	The base of the API request URL. Does not include the path.
	 * @param connectionTimeout HTTP connection timeout
	 * @param soTimeout	HTTP read timeout
	 */
	
	protected BaseConnectionClient( final String baseUrl,
									final int connectionTimeout,
									final int soTimeout){
		this.baseUrl = baseUrl;
		this.connectionTimeout = connectionTimeout;
		this.soTimeout = soTimeout;	
	}
	
	protected String makeConnection(String submitPath, List<NameValuePair> params) throws UnsupportedEncodingException{
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
		return response;
	}
	
	protected ContainerFactory getContainerFactory(){
		return new ContainerFactory(){
			public List<Object> creatArrayContainer() {
				return new LinkedList<Object>();
			}

			public Map<String, Object> createObjectContainer() {
				return new LinkedHashMap<String, Object>();
			}

		};
	}

}
