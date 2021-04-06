package basePackage;

import org.testng.asserts.SoftAssert;
import io.restassured.response.Response;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class BaseClass {
	
	public static Response resp;
	public static SoftAssert soft;
	public String BaseURL;
	public String body;
	public JsonObject Env;
	public Gson gson = new Gson();
}
