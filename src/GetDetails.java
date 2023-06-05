import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.testng.Assert;

public class GetDetails {

	public static void main(String[] args) {
		String promoName, getResponse, name, fetchedPromoName, fetchedPromoDescription;
		int s, count;
		boolean canRel;
		// Setting up the base url for API test
		RestAssured.baseURI = "https://api.tmsandbox.co.nz";

		// Sending the api request and storing the response in string format
		getResponse = given().when().get("/v1/Categories/6327/Details.json?catalogue=false").then().assertThat()
				.statusCode(200).body("Name", equalTo("Carbon credits")).extract().response().asString();

		// Parsing Json using JsonPath class
		JsonPath js1 = ReusableMethods.rawToJson(getResponse);

		// fetching and verifying the Name from response body
		name = js1.get("Name");
		System.out.println(name);

		Assert.assertEquals("Carbon credits", name);

		// fetching and verifying the CanRelist from response body
		canRel = js1.get("CanRelist");
		System.out.println(canRel);
		Assert.assertEquals(true, canRel);

		/*  Iterating through all the Promotions element and verifying whether it
	 		contains element with Name = "Gallery" has a Description that contains the
		 	text "Good position in category"*/

		s = js1.getInt("Promotions.size()");
		
		count = 0;
		for (int i = 0; i < s; i++) {
			promoName = js1.getString("Promotions["+i+"].Name");
			if (promoName.equalsIgnoreCase("Gallery")) {
				count = i;
				break;
			}

		}
		
		if(count != 0) {
			fetchedPromoName = js1.getString("Promotions["+count+"].Name");
			fetchedPromoDescription = js1.getString("Promotions["+count+"].Description");
			Assert.assertEquals("Gallery", fetchedPromoName);
			System.out.println(fetchedPromoName);
			Assert.assertEquals("Good position in category", fetchedPromoDescription);
		}
		else {
			System.out.println("No Promotion found with the name Gallery");
			Assert.fail();
		}

	}

}
