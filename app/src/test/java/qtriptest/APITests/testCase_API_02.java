package qtriptest.APITests;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ResponseBody;
import org.testng.Assert;
import org.testng.annotations.*;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.RestAssured;
import java.io.File;

public class testCase_API_02 {


    @Test(priority = 2, groups = {"API TESTING"})
    public void TestCase02() {

        // Search for a city API Testing
        RestAssured.baseURI = "https://content-qtripdynamic-qa-backend.azurewebsites.net/api/v1";
        RequestSpecification httpRequest = RestAssured.given().queryParam("q", "beng");
        Response response = httpRequest.request(Method.GET, "/cities");
        ResponseBody body = response.body();
        int searchCityResStatusCode = response.getStatusCode();
        Assert.assertEquals(searchCityResStatusCode, 200,
                "Failed to validate that the search city response status code is 200");
        String resBody = body.asString();
        JsonPath jpath = new JsonPath(resBody);
        String description = jpath.getString(   "description");
        Assert.assertEquals(resBody.contains("description"), true,
                "Failed to validate that description field is present in the search response body");
        Assert.assertEquals(description, "[100+ Places]",
                "Failed to validate the contents of search response description field");
                int searchResSize = jpath.getInt("size()");
        // System.out.println("Length of the Json Array is:"+s);
        Assert.assertEquals(searchResSize, 1, "Failed to validate that search size response is 1");
        response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchema(new File(
                "/home/crio-user/workspace/vishvajeet-criodo-ME_API_TESTING_PROJECT/app/src/test/resources/jsonSchemas/TestCase_02_Schema.json")));
    }


}
