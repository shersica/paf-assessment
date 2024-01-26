package vttp2023.batch4.paf.assessment.services;

import java.io.StringReader;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class ForexService {

	String apiCall = "https://api.frankfurter.app/latest";

	// TODO: Task 5 
	public float convert(String from, String to, float amount) {
		try {
			String url = UriComponentsBuilder
            .fromUriString(apiCall)
            .queryParam("from", from)
            .toUriString();

			RestTemplate template = new RestTemplate();

			ResponseEntity<String> resp = template.getForEntity(url, String.class);
			String payload = resp.getBody();

			JsonReader reader = Json.createReader(new StringReader(payload));
			JsonObject result = reader.readObject();
			JsonObject jsonObjectData = result.getJsonObject("rates");
			
			to = to.toUpperCase();
			Double rate = jsonObjectData.getJsonNumber(to).doubleValue();
			float convertedAmount = (float) (amount * rate);
			return convertedAmount;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("exception occured");
			return -1000f;
		}
	}
}
