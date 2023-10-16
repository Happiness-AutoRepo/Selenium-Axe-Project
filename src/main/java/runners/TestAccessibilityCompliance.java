package runners;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import org.slf4j.Marker;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
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
	
	ExtentReports extent;
	ExtentTest test;
	
	@BeforeTest
	public void setUp() {
		extent = new ExtentReports();

		ExtentSparkReporter spark = new ExtentSparkReporter("target/Spark.html");
		extent.attachReporter(spark);
		
		test = extent.createTest("Acessibility test of the IRS landing page");
	}

	@Test
	public void testOne() throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		
		test.log(Status.INFO, "Navigating to the website");
		Driver.getInstance().get("https://www.irs.gov");
		
		test.log(Status.INFO, "Analyzing 508 compliance");
		AxeBuilder builder = new AxeBuilder();
		Results results = builder.analyze(Driver.getInstance());
		List<Rule> violations = results.getViolations();
		
		//Output violations to the console		
		for (Rule rule : violations) {
			System.out.println("=========================");
			System.out.println(rule);
		}
		

		test.log(Status.INFO, "Checking if violations are present on the page");
		//Output violations to .txt and .json file
		if (violations.size() == 0) {
			test.log(Status.PASS, "No violations found");
			Assert.assertTrue(true, "No violations found");
		} 
		else {
			test.log(Status.INFO, "Accessibility violations found.");
			
			JsonParser jsonParser = new JsonParser();
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			
			AxeReporter.writeResultsToJsonFile("508results", results);
			JsonElement jsonElement = jsonParser.parse(new FileReader("508results" + ".json"));
			String prettyJson = gson.toJson(jsonElement);
			
			AxeReporter.writeResultsToTextFile("508results", prettyJson);
			
			test.log(Status.FAIL, MarkupHelper.createCodeBlock(prettyJson, CodeLanguage.JSON));
			Assert.assertEquals(violations.size(), 0, violations.size() + " violations found");
		}
	}
	
	@AfterTest
	public void wrapUp() {
		Driver.closeDriver();
		extent.flush();
	}
}
