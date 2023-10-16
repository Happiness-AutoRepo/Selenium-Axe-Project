package runners;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import com.deque.html.axecore.results.Results;
import com.deque.html.axecore.results.Rule;
import com.deque.html.axecore.selenium.AxeBuilder;
import com.deque.html.axecore.selenium.AxeReporter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import utilities.Driver;

public class TestAccessibilityCompliance {

	@Test
	public void testOne() throws JsonIOException, JsonSyntaxException, FileNotFoundException {

		Driver.getInstance().get("https://www.irs.gov");
		
		AxeBuilder builder = new AxeBuilder();
		Results results = builder.analyze(Driver.getInstance());
		List<Rule> violations = results.getViolations();
		
		System.out.println(violations);

//		if (violations.size() == 0) {
//			Assert.assertTrue(true, "No violations found");
//		} else {
//			JsonParser jsonParser = new JsonParser();
//			Gson gson = new GsonBuilder().setPrettyPrinting().create();
//			AxeReporter.writeResultsToJsonFile("filename", results);
//			JsonElement jsonElement = jsonParser.parse(new FileReader("filename" + ".json"));
//			String prettyJson = gson.toJson(jsonElement);
//			AxeReporter.writeResultsToTextFile("filename", prettyJson);
//			Assert.assertEquals(violations.size(), 0, violations.size() + " violations found");
//		}
	}
	
	@AfterTest
	public void wrapUp() {
		Driver.closeDriver();
	}
}
