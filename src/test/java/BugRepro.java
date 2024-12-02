import com.vonage.client.*;
import java.util.*;

/**
 * Convenience class for debugging / live testing.
 * This is .gitignored so that changes can be made locally without accidental commits to the repo.
 */
public class BugRepro {
	public static void main(String[] args) throws Throwable {
		String TO_NUMBER = System.getenv("TO_NUMBER");

		VonageClient client = VonageClient.builder()
				.httpConfig(HttpConfig.builder().timeoutMillis(12_000).build())
				.apiKey(System.getenv("VONAGE_API_KEY"))
				.apiSecret(System.getenv("VONAGE_API_SECRET"))
				.applicationId(System.getenv("VONAGE_APPLICATION_ID"))
				.privateKeyPath(System.getenv("VONAGE_PRIVATE_KEY_PATH"))
				.build();

		try {
			// Debug code here
			// client.getVoiceClient().createCall(new Call(TO_NUMBER, System.getenv("VONAGE_NUMBER"), "https://example.com/answer"));

			System.out.println("Success");
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
