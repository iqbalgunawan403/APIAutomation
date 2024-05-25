package apiauto;

import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.testng.annotations.Test;

import java.io.File;
import java.util.HashMap;


import static io.restassured.RestAssured.given;

public class apiTest {


    @Test
    public void testGetUser() {
        RestAssured
                .given().when()
                .get("https://reqres.in/api/users?page=2")
                .then().log().all()
                .assertThat().statusCode(200)
                .assertThat().body("per_page", Matchers.equalTo(6))
                .assertThat().body("page", Matchers.equalTo(2));

    }

    @Test
    public void testPostWrongUser() {

        Integer valueName = 2;
        Integer valueJob = 1;

        JSONObject bodyObj = new JSONObject();

        bodyObj.put("name", valueName);
        bodyObj.put("job", valueJob);

        RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(bodyObj)
                .when()
                .post("https://reqres.in/api/users")
                .then().log().all()
                .assertThat().statusCode(201)
                .assertThat().body("name", Matchers.equalTo(valueName));

    }


    @Test
    public void testPostUser() {

        String valueName = "iqbal";
        String valueJob = "QA";

        JSONObject bodyObj = new JSONObject();

        bodyObj.put("name", valueName);
        bodyObj.put("job", valueJob);

        RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(bodyObj.toString())
                .when()
                .post("https://reqres.in/api/users")
                .then().log().all()
                .assertThat().statusCode(201)
                .assertThat().body("name", Matchers.equalTo(valueName));

    }

    @Test
    public void testPutUser() {
        RestAssured.baseURI = "https://reqres.in";

        int userId = 2;
        String newName = "updatedUser";

        String fname = given().when().get("api/users/" + userId).getBody().jsonPath().get("data.first_name");
        String lname = given().when().get("api/users/" + userId).getBody().jsonPath().get("data.last_name");
        String avatar = given().when().get("api/users/" + userId).getBody().jsonPath().get("data.avatar");
        String email = given().when().get("api/users/" + userId).getBody().jsonPath().get("data.email");
        System.out.println("name before = " + fname);

        HashMap<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("id", userId);
        bodyMap.put("email", email);
        bodyMap.put("first_name", newName);
        bodyMap.put("last_name", lname);
        bodyMap.put("avatar", avatar);
        JSONObject jsonObject = new JSONObject(bodyMap);

        given().log().all()
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .put("api/users/" + userId)
                .then().log().all()
                .assertThat().statusCode(200)
                .assertThat().body("first_name", Matchers.equalTo(newName));


    }

    @Test
    public void testPatchUser(){
        RestAssured.baseURI="https://reqres.in";

        int userId =3;
        String newName = "updatedUser";

        String fname = given().when().get("api/users/"+userId)
                .getBody().jsonPath().get("data.first_name");
        System.out.println("name before = "+fname);

        HashMap<String, String> bodyMap = new HashMap<>();
        bodyMap.put("first_name", newName);
        JSONObject jsonObject = new JSONObject(bodyMap);

        given().log().all()
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .patch("api/users/"+userId)
                .then().log().all()
                .assertThat().statusCode(200)
                .assertThat().body("first_name", Matchers.equalTo(newName));

    }

    @Test
    public void testDeleteUser(){
        RestAssured.baseURI = "https://reqres.in";

        int userToDelete = 4;
        given().log().all()
                .when().delete("api/users/"+userToDelete)
                .then()
                .log().all()
                .assertThat().statusCode(204);
    }

    @Test
    public void testValidateJsonSchemaGetUser(){
        RestAssured.baseURI = "https://reqres.in";

        int userToGet = 5;
        
        File file = new File("src/test/resources/JsonSchema/getSingleUserSchema.json");

        given().log().all()
                .when().get("api/users/"+userToGet)
                .then().log().all()
                .assertThat().statusCode(200)
                .assertThat().body(JsonSchemaValidator.matchesJsonSchema(file));
    }
}
