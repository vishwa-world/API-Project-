package qtriptest.APITests;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import java.util.UUID;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

public class testCase_API_03 {

    @Test(priority = 3, groups = {"API TESTING"})
    public void TestCase03() throws InterruptedException {

        // Register API Testing
        RestAssured.baseURI = "https://content-qtripdynamic-qa-backend.azurewebsites.net/api/v1";
        RequestSpecification httpReq1 = RestAssured.given();
        String email = "TestUser_" + UUID.randomUUID() + "@gmail.com";
        JSONObject requestParams1 = new JSONObject();
        requestParams1.put("email", email);
        requestParams1.put("password", "samaboy@123");
        requestParams1.put("confirmpassword", "samaboy@123");
        httpReq1.header("Content-Type", "application/json");
        httpReq1.body(requestParams1.toString());
        Response response1 = httpReq1.request(Method.POST, "/register");
        int registerResStatusCode = response1.getStatusCode();
        Assert.assertEquals(registerResStatusCode, 201, "Failed to validate that the user regsitration is successful");

        // Login API Testing
        // RestAssured.baseURI = "https://content-qtripdynamic-qa-backend.azurewebsites.net/api/v1";
        // String email="TestUser_"+UUID.randomUUID()+"gmail.com";
        RequestSpecification httpReq2 = RestAssured.given();
        JSONObject requestParams2 = new JSONObject();
        requestParams2.put("email", email);
        requestParams2.put("password", "samaboy@123");
        httpReq2.header("Content-Type", "application/json");
        httpReq2.body(requestParams2.toString());
        Response response2 = httpReq2.request(Method.POST, "/login");
        String resBody = response2.getBody().asString();
        JsonPath jpath1 = new JsonPath(resBody);
        String token = jpath1.getString("data.token");
        String userId = jpath1.getString("data.id");
        int loginResStatusCode = response2.getStatusCode();
        Assert.assertEquals(loginResStatusCode, 201, "Failed to validate that the user is logged in successfully");
        Assert.assertEquals(resBody.contains("token"), true ,"Failed to validate that token is returned in login");
        Assert.assertEquals(resBody.contains("id"), true ,"Failed to validate that userId is returned in login");
        Assert.assertEquals(resBody.contains("success"), true ,"Failed to validate that login is successful");

        // POST Reservation API Testing
        RestAssured.baseURI =
                "https://content-qtripdynamic-qa-backend.azurewebsites.net/api/v1/reservations";
        RequestSpecification httpReq3 = RestAssured.given();
        JSONObject requestParams3 = new JSONObject();
        requestParams3.put("userId", userId);
        requestParams3.put("name", "Samaboy");
        requestParams3.put("date", "2023-12-12");
        requestParams3.put("person", "2");
        requestParams3.put("adventure", "2447910730");
        httpReq3.header("Content-Type", "application/json");
        httpReq3.body(requestParams3.toString());
        httpReq3.header("Authorization", "Bearer " + token);
        Response response3 = httpReq3.request(Method.POST, "/new");
        // String resBody3 = response3.getBody().asString();
        int reservationResStatusCode = response3.getStatusCode();
        Assert.assertEquals(reservationResStatusCode, 200, "Failed to verfiy that the post reservation is successful");


        // GET Reservation API Testing
        RestAssured.baseURI = "https://content-qtripdynamic-qa-backend.azurewebsites.net/api/v1/";
        RequestSpecification httpReq4 = RestAssured.given().queryParam("id", userId);
        httpReq4.header("Authorization", "Bearer " + token);
        Response response4 = httpReq4.request(Method.GET, "/reservations");

        // @SuppressWarnings("rawtypes")
        ResponseBody<?> body4 = response4.body();
        int getReservationsStatusCode = response4.getStatusCode();
        Assert.assertEquals(getReservationsStatusCode, 200);
        String resBody4 = body4.asString();
        JsonPath jpath2 = new JsonPath(resBody4);
        // String adventureName = jpath2.getString("adventureName");
        // Assert.assertEquals(resBody4.contains("adventureName"), true);
        String adventureId = jpath2.getString("adventure");
        Assert.assertEquals(adventureId, "[2447910730]", "Failed to validate the adventureId");
        String name = jpath2.getString("name");
        Assert.assertEquals(name, "[Samaboy]", "Failed to validate the adventureName");
    }
}
