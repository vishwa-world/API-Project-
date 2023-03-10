package qtriptest.APITests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.http.Method;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.UUID;

public class testCase_API_04 {

    String resPayload = null;
    String email = "sam" + UUID.randomUUID().toString() + "@gmail.com";

    public int registerAPI_Call() {

        RestAssured.baseURI="https://content-qtripdynamic-qa-backend.azurewebsites.net/api/v1";
        RequestSpecification httprequest = RestAssured.given();  
        JSONObject requestParams = new JSONObject();
        requestParams.put("email", email);
        requestParams.put("password", "sam@789");
        requestParams.put("confirmpassword", "sam@789");
        httprequest.header("Content-Type","application/json");
        
        httprequest.body(requestParams.toString());
        Response response = httprequest.request(Method.POST,"/register");
        //RestAssured.given().log().all();
        resPayload = response.getBody().asString();
        return response.getStatusCode();
    }

    @Test(priority = 4,groups = {"API TESTING"})
    public void Testcase04() {
        
    //    RestAssured.baseURI="https://content-qtripdynamic-qa-backend.azurewebsites.net/api/v1";
    //    RequestSpecification httprequest = RestAssured.given();  

    //    JSONObject requestParams = new JSONObject();
    //    requestParams.put("email", "sam@gmail.com");
    //    requestParams.put("password", "sam@789");
    //    requestParams.put("confirmpassword", "sam@789");
    //    httprequest.header("Content-Type","application/json");
       
    //    httprequest.body(requestParams.toString());
    //    Response response = httprequest.request(Method.POST,"/register");
    //    //RestAssured.given().log().all();   
   
    //    int registerStatusCode = response.getStatusCode();
       int registerStatusCode1 = registerAPI_Call();
       Assert.assertEquals(registerStatusCode1, 201, "Failed to verify successful new registration for first time");
       String  payload1 = resPayload;
       JsonPath jsonPath1 = new JsonPath(payload1);
       Assert.assertTrue(jsonPath1.getBoolean("success"),"Failed to verify success field is true for first time new registration");
       
       
       int registerStatusCode2 = registerAPI_Call();
       String  payload2 = resPayload;
       JsonPath jsonPath2 = new JsonPath(payload2);
       Assert.assertEquals(registerStatusCode2, 400, "Failed to verify unsuccessful duplicate registration");
       Assert.assertFalse(jsonPath2.getBoolean("success"),"Failed to validate success field is false");
       String message = jsonPath2.getString("message");
       Assert.assertEquals(message,"Email already exists","Failed to validate the message as: Email already exists");

    }

}

