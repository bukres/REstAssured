package theseAPI.RestAssured;

import java.io.FileNotFoundException;
import java.io.FileReader;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import basePackage.BaseClass;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;

public class ResearchInvestorsTest extends BaseClass {

	SoftAssert soft = new SoftAssert();

	@BeforeTest
	public void prepareTests() throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		Env = gson.fromJson(new FileReader("src/main/java/utilities/Environment.json"), JsonObject.class);
		BaseURL = Env.get("BaseURL").getAsString();
		body = Env.get("body").getAsString();
	}

	@Test(priority = 0, enabled = true)
	public void research() {
		RestAssured.baseURI = BaseURL;
		resp = RestAssured.given().accept(ContentType.JSON).header("User-Agent", "PostmanRuntime/7.6.0")
				.contentType(ContentType.JSON).body(body).post().then().assertThat().statusCode(200).and().extract()
				.response();
		String researchRespString = resp.asString();
		
		//System.out.println(researchRespString);

		JsonPath walker = new JsonPath(researchRespString);
		String marketStatus = walker.getString("GetDataResult.currentMarketStatus");
		String source = walker.getString("GetDataResult.source");
		String URL = walker.getString("GetDataResult.summary.URL");

		int holidaysSize = walker.get("GetDataResult.holidays.size()");
		int i;
		String holidays;

		// soft.assertEquals(source, "BATS", "source is not BATS!");
		if (source.equals("BATS") || source.equals("IDCCLOSE")) {
			System.out.println("source is as expected");
		} else {
			System.out.println("There is a problem with source");
		}

		soft.assertEquals(URL, "www.solaredge.com", "URL is not www.solaredge.com!");

		// soft.assertEquals(marketStatus, "", "market status is not PREMARKET!");
		if (marketStatus.equals("") || marketStatus.equals("PREMARKET") || marketStatus.equals("AFTER HOURS")) {
			System.out.println("URL is as expected");
		} else {
			System.out.println("There is a problem with URL");
		}

		for (i = 0; i < holidaysSize; i++) {
			holidays = walker.getString("GetDataResult.holidays[" + i + "].holiday");
			soft.assertEquals(holidays, "Holiday", "holidays is not Holiday!");
		}

		soft.assertAll();
	}
}
