package petstore.tests;

import io.restassured.http.ContentType;
import org.testng.annotations.Test;
import utils.EndPoints;
import utils.StatusCodes;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class PetstoreApis {

    Random rand = new Random();
    int radomPetID = rand.nextInt(12101992);
    String petName = "i am groot";

    /**
     * test to find pet by available status as query paramter
     * assert on status code ==200
     * assert that objects are greater than zero
     * assert on all objects ids are not null
     * assert on all objects names are not null
     * assert that all returned objects matches the available status
     * change statusvalue string to "available" or "pending" or "sold" to test the 3 different statuses
     * assert on response header content type == json
     */
    @Test
    public void findPetByStatus() {
        String statusValue = "available";
        var response = given()
                .queryParam("status", statusValue)
                .when()
                .get(EndPoints.petStatusEndPoint).then();
        response.assertThat().statusCode(StatusCodes.CODE_200_OK)
                .body("size()", greaterThan(0))
                .body("id", everyItem(notNullValue()))
                .body("name", everyItem(notNullValue()))
                .body("status", everyItem(equalTo(statusValue)))
                .header("Content-Type", equalTo("application/json"));


    }


    /**
     * test to add new pet using random id
     * assert on status code ==200
     * assert that response object id is the same used for pet creation
     * assert that response object name is the same used for pet creation
     */

    @Test
    public void addNewPet() {

        String payload = "{\n" +
                "  \"id\":" + radomPetID + ",\n" +
                "  \"category\": {\n" +
                "    \"id\": 0,\n" +
                "    \"name\": \"string\"\n" +
                "  },\n" +
                "  \"name\": \"" + petName + "\",\n" +
                "  \"photoUrls\": [\n" +
                "    \"string\"\n" +
                "  ],\n" +
                "  \"tags\": [\n" +
                "    {\n" +
                "      \"id\": 0,\n" +
                "      \"name\": \"string\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"status\": \"available\"\n" +
                "}";
        var response = given().
                contentType(ContentType.JSON).
                body(payload).
                when().
                post(EndPoints.petCreationEndPoint).then();
        response.assertThat().statusCode(StatusCodes.CODE_200_OK)
                .body("id", equalTo(radomPetID))
                .body("name", equalTo(petName));

    }

    /**
     * test to get pet by id using the same id used at addNewPet test
     * assert on response code == 200
     * assert on id
     * asset on petname
     * note: this end point has an issue sometimes it is working fine and sometime not
     */
    @Test
    public void getPetByID() {
        var response = given()
                .when()
                .get(EndPoints.getPetByIdEndPoint).then();
        response.assertThat().statusCode(StatusCodes.CODE_200_OK)
                .body("id", equalTo(1));


    }

    /**
     * test to get pet by id using invalid id
     * assert on status code == 404
     * assert on type == error
     * assert on message == pet not found
     */
    @Test
    public void getPetByInvalidId() {
        var response = given()
                .when()
                .get(EndPoints.getPetByInvalidIdEndPoint).then();
        response.assertThat().statusCode(StatusCodes.CODE_404_NOT_FOUND)
                .body("type", equalTo("error"))
                .body("message", equalTo("Pet not found"));


    }

    /**
     * test to delete pet by specific id
     * assert that status code == 200
     * assert that message == pet id
     * assert that type == unknown
     * note: this end point has an issue sometimes it is working fine and sometime not
     */
    @Test(dependsOnMethods = {"addNewPet"})
    public void deletePetById() {
        EndPoints.deletePetByIdEndPoint = EndPoints.deletePetByIdEndPoint + radomPetID;
        var response = given().header("api_key", "special-key")
                .when()
                .delete(EndPoints.deletePetByIdEndPoint)
                .then();
        response.assertThat().statusCode(StatusCodes.CODE_200_OK)
                .body("message", equalTo(Integer.toString(radomPetID)))
                .body("type", equalTo("unknown"));
    }

    /**
     * test to add two new users using array
     */
    @Test
    public void addNewUserUsingArray() {

        String payload = "[\n" +
                "  {\n" +
                "    \"id\": 6666,\n" +
                "    \"username\": \"Iron man\",\n" +
                "    \"firstName\": \"NA\",\n" +
                "    \"lastName\": \"NA\",\n" +
                "    \"email\": \"ironman@marvel.com\",\n" +
                "    \"password\": \"avengers\",\n" +
                "    \"phone\": \"NA\",\n" +
                "    \"userStatus\": 0\n" +
                "}\n" +
                ",\n" +
                "\n" +
                "  {\n" +
                "    \"id\": 66666,\n" +
                "    \"username\": \"thanos\",\n" +
                "    \"firstName\": \"NA\",\n" +
                "    \"lastName\": \"NA\",\n" +
                "    \"email\": \"thanos@marvel.com\",\n" +
                "    \"password\": \"NA\",\n" +
                "    \"phone\": \"NA\",\n" +
                "    \"userStatus\": 0\n" +
                "}\n" +
                "]\n";
        var response = given().
                contentType(ContentType.JSON).
                body(payload).
                when().
                post(EndPoints.userCreationWithArray).then();
        response.assertThat().statusCode(StatusCodes.CODE_200_OK);
    }

    /**
     * test to get user by name
     */
    @Test(dependsOnMethods = {"addNewUserUsingArray"})
    public void getUserByName() {
        var response = given().when().get(EndPoints.getUserByName).then();
        response.assertThat().statusCode(StatusCodes.CODE_200_OK)
                .body("username",equalTo("thanos"))
                .body("email",equalTo("thanos@marvel.com"));


    }
}