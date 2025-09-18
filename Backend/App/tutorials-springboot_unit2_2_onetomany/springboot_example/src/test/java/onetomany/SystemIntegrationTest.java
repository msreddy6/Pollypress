//package onetomany;
//
//import io.restassured.RestAssured;
//import io.restassured.response.Response;
//import org.json.JSONArray;
//import org.json.JSONObject;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import static io.restassured.RestAssured.*;
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.*;
//
////io.restassured.RestAssured.*;
////io.restassured.matcher.RestAssuredMatchers.*;
////org.hamcrest.Matchers.*;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ExtendWith(SpringExtension.class)
//public class SystemIntegrationTests {
//
//    @LocalServerPort
//    int port;
//
//    @BeforeEach
//    void setup() {
//        RestAssured.port = port;
//        RestAssured.baseURI = "http://localhost";
//    }
//
//    @Test
//    void createAndFetchReporter() {
//        String rptBody = """
//          { "username":"sysTestRep", "email":"sys@test.com", "password":"pw" }
//          """;
//
//        long rid = given()
//                .contentType("application/json")
//                .body(rptBody)
//                .when()
//                .post("/reporters")
//                .then()
//                .statusCode(200)
//                .body("id", greaterThan(0))
//                .body("username", equalTo("sysTestRep"))
//                .extract().jsonPath().getLong("id");
//
//        when()
//                .get("/reporters/" + rid)
//                .then()
//                .statusCode(200)
//                .body("id", equalTo((int)rid))
//                .body("username", equalTo("sysTestRep"))
//                .body("email",    equalTo("sys@test.com"));
//    }
//
//    @Test
//    void createAndFetchArticle() {
//        long repId = given()
//                .contentType("application/json")
//                .body("""
//              { "username":"artRep", "email":"art@test.com", "password":"pw" }
//              """)
//                .when().post("/reporters")
//                .then().statusCode(200)
//                .extract().jsonPath().getLong("id");
//
//        String artBody = """
//          {
//            "title":"sysTitle","content":"sysContent",
//            "author":"sysAuth","publication":"sysPub",
//            "publicationDate":"2025-01-01",
//            "reporter": { "id": %d }
//          }
//          """.formatted(repId);
//
//        long aid = given()
//                .contentType("application/json")
//                .body(artBody)
//                .when().post("/articles")
//                .then().statusCode(201)
//                .body("title", equalTo("sysTitle"))
//                .extract().jsonPath().getLong("id");
//
//        when()
//                .get("/articles/id/" + aid)
//                .then()
//                .statusCode(200)
//                .body("content", equalTo("sysContent"))
//                .body("publication", equalTo("sysPub"));
//    }
//
//    @Test
//    void pollApprovalFlow() {
//        long adminId = given()
//                .contentType("application/json")
//                .body("""
//              { "username":"admTest","email":"adm@test.com","password":"pw" }
//              """)
//                .when().post("/admins")
//                .then().statusCode(200)
//                .extract().jsonPath().getLong("id");
//
//        long repId = given()
//                .contentType("application/json")
//                .body("""
//              { "username":"pollRep","email":"poll@test.com","password":"pw" }
//              """)
//                .when().post("/reporters")
//                .then().statusCode(200)
//                .extract().jsonPath().getLong("id");
//
//        String pollBody = """
//          {
//            "title":"sysPoll","description":"desc",
//            "options":["A","B","C"],
//            "reporter": { "id": %d }
//          }
//          """.formatted(repId);
//
//        long pollId = given()
//                .contentType("application/json")
//                .body(pollBody)
//                .when().post("/polls")
//                .then().statusCode(201)
//                .extract().jsonPath().getLong("id");
//
//        given()
//                .when().post("/polls/" + pollId + "/approve?adminId=" + adminId)
//                .then().statusCode(200)
//                .body("options", hasItems("A","B","C"));
//
//        when()
//                .get("/approved-polls/" + pollId)
//                .then()
//                .statusCode(200)
//                .body("approvedBy.id", equalTo((int)adminId));
//    }
//
//    @Test
//    void ticketCreateAndResolveFlow() {
//        long repId = given()
//                .contentType("application/json")
//                .body("""
//              { "username":"ticRep","email":"tic@test.com","password":"pw" }
//              """)
//                .when().post("/reporters")
//                .then().statusCode(200)
//                .extract().jsonPath().getLong("id");
//
//        long adminId = given()
//                .contentType("application/json")
//                .body("""
//              { "username":"ticAdm","email":"ticadm@test.com","password":"pw" }
//              """)
//                .when().post("/admins")
//                .then().statusCode(200)
//                .extract().jsonPath().getLong("id");
//
//        long tid = given()
//                .contentType("application/json")
//                .body("""
//              {
//                "reporter": { "id": %d },
//                "admin":    { "id": %d }
//              }
//              """.formatted(repId, adminId))
//                .when().post("/tickets")
//                .then().statusCode(200)
//                .extract().jsonPath().getLong("id");
//
//        when().get("/tickets/" + tid)
//                .then().body("resolved", equalTo(false));
//
//        when().put("/tickets/" + tid + "/resolve?adminId=" + adminId)
//                .then().body("resolved", equalTo(true));
//    }
//}


//!!!!!!!!!!!!!!
//package onetomany;
//
//import io.restassured.RestAssured;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import static io.restassured.RestAssured.*;
//import static org.hamcrest.Matchers.*;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ExtendWith(SpringExtension.class)
//class SystemIntegrationTest {
//
//    @LocalServerPort
//    int port;
//
//    @BeforeEach
//    void setup() {
//        RestAssured.baseURI = "http://localhost";
//        RestAssured.port     = port;
//    }
//
//    @Test
//    void createAndFetchReporter() {
//        String body = """
//                { "username":"sysTestRep","email":"sys@test.com","password":"pw" }
//                """;
//
//        long rid = given().contentType("application/json").body(body)
//                .when().post("/reporters")
//                .then().statusCode(200)
//                .body("username", equalTo("sysTestRep"))
//                .extract().jsonPath().getLong("id");
//
//        when().get("/reporters/{id}", rid)
//                .then().statusCode(200)
//                .body("email", equalTo("sys@test.com"));
//    }
//
//    @Test
//    void createAndFetchArticle() {
//        long repId = given().contentType("application/json")
//                .body("""
//                                   { "username":"artRep","email":"art@test.com","password":"pw" }
//                                   """)
//                .when().post("/reporters")
//                .then().extract().jsonPath().getLong("id");
//
//        String art = """
//                {
//                  "title":"sysTitle","content":"sysContent",
//                  "author":"sysAuth","publication":"sysPub",
//                  "publicationDate":"2025-01-01",
//                  "reporter": { "id": %d }
//                }
//                """.formatted(repId);
//
//        long aid = given().contentType("application/json").body(art)
//                .when().post("/articles")
//                .then().statusCode(201)
//                .extract().jsonPath().getLong("id");
//
//        when().get("/articles/id/{id}", aid)
//                .then().statusCode(200)
//                .body("title", equalTo("sysTitle"))
//                .body("publication", equalTo("sysPub"));
//    }
//
//    @Test
//    void pollApprovalFlow() {
//        long admin = given().contentType("application/json")
//                .body("""{ "username":"adm","email":"adm@test.com","password":"pw" }""")
//                .when().post("/admins")
//                .then().extract().jsonPath().getLong("id");
//
//        long rep   = given().contentType("application/json")
//                .body("""{ "username":"pollRep","email":"poll@test.com","password":"pw" }""")
//                .when().post("/reporters")
//                .then().extract().jsonPath().getLong("id");
//
//        String poll = """
//                {
//                  "title":"sysPoll","description":"desc",
//                  "options":["A","B","C"],
//                  "reporter": { "id": %d }
//                }
//                """.formatted(rep);
//
//        long pid = given().contentType("application/json").body(poll)
//                .when().post("/polls")
//                .then().extract().jsonPath().getLong("id");
//
//        given().post("/polls/{id}/approve?adminId={adm}", pid, admin)
//                .then().statusCode(200)
//                .body("options", hasItems("A","B","C"));
//    }
//
//    @Test
//    void ticketCreateAndResolveFlow() {
//        long rep   = given().contentType("application/json")
//                .body("""{ "username":"tRep","email":"trep@test.com","password":"pw" }""")
//                .when().post("/reporters")
//                .then().extract().jsonPath().getLong("id");
//
//        long admin = given().contentType("application/json")
//                .body("""{ "username":"tAdm","email":"tadm@test.com","password":"pw" }""")
//                .when().post("/admins")
//                .then().extract().jsonPath().getLong("id");
//
//        long tid = given().contentType("application/json")
//                .body("""
//                                { "reporter":{ "id":%d }, "admin":{ "id":%d } }
//                                """.formatted(rep, admin))
//                .when().post("/tickets")
//                .then().extract().jsonPath().getLong("id");
//
//        when().put("/tickets/{id}/resolve?adminId={adm}", tid, admin)
//                .then().statusCode(200)
//                .body("resolved", equalTo(true));
//    }
//}

package onetomany;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class SystemIntegrationTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port     = port;
    }

    @Test
    void createAndFetchReporter() {
        String reporterJson = """
                { "username":"sysTestRep", "email":"sys@test.com", "password":"pw" }
                """;

        int id = given()
                .contentType("application/json")
                .body(reporterJson)
                .when()
                .post("/reporters")
                .then()
                .statusCode(200)
                .body("username", equalTo("sysTestRep"))
                .extract().path("id");
        when()
                .get("/reporters/{id}", id)
                .then()
                .statusCode(200)
                .body("email", equalTo("sys@test.com"))
                .body("username", equalTo("sysTestRep"));
    }

    @Test
    void createAndFetchArticle() {
        String reporterJson = """
                { "username":"artRep", "email":"art@test.com", "password":"pw" }
                """;
        int repId = given()
                .contentType("application/json")
                .body(reporterJson)
                .when()
                .post("/reporters")
                .then()
                .statusCode(200)
                .extract().path("id");

        String articleJson = """
                {
                  "title":"sysTitle",
                  "content":"sysContent",
                  "author":"sysAuth",
                  "publication":"sysPub",
                  "publicationDate":"2025-01-01",
                  "reporter": { "id": %d }
                }
                """.formatted(repId);

        int artId = given()
                .contentType("application/json")
                .body(articleJson)
                .when()
                .post("/articles")
                .then()
                .statusCode(201)
                .body("title", equalTo("sysTitle"))
                .extract().path("id");

        when()
                .get("/articles/id/{id}", artId)
                .then()
                .statusCode(200)
                .body("publication", equalTo("sysPub"))
                .body("content", equalTo("sysContent"));
    }


    @Test
    void pollApprovalFlow() {
        String adminJson = """
            { "username":"admTest", "email":"adm@test.com", "password":"pw" }
            """;

        int adminId = given()
                .contentType("application/json")
                .body(adminJson)
                .when()
                .post("/admins")
                .then()
                .statusCode(200)
                .extract().path("id");

        String repJson = """
            { "username":"pollRep", "email":"poll@test.com", "password":"pw" }
            """;

        int repId = given()
                .contentType("application/json")
                .body(repJson)
                .when()
                .post("/reporters")
                .then()
                .statusCode(200)
                .extract().path("id");

        String pollJson = """
            {
              "title":"sysPoll",
              "description":"desc",
              "options":["A","B","C"],
              "reporter": { "id": %d }
            }
            """.formatted(repId);

        int pollId = given()
                .contentType("application/json")
                .body(pollJson)
                .when()
                .post("/polls")
                .then()
                .statusCode(201)
                .extract().path("id");

        int approvedPollId = given()
                .when()
                .post("/polls/{id}/approve?adminId={adm}", pollId, adminId)
                .then()
                .statusCode(200)
                .body("options", hasItems("A","B","C"))
                .extract().path("id");

        given()
                .when()
                .get("/approved-polls/{id}", approvedPollId)
                .then()
                .statusCode(200)
                .body("approvedBy.id", equalTo(adminId));
    }
    @Test
    void ticketCreateAndResolveFlow() {
        String repJson = """
                { "username":"tRep", "email":"trep@test.com", "password":"pw" }
                """;
        int repId = given()
                .contentType("application/json")
                .body(repJson)
                .when()
                .post("/reporters")
                .then()
                .statusCode(200)
                .extract().path("id");

        String adminJson = """
                { "username":"tAdm", "email":"tadm@test.com", "password":"pw" }
                """;
        int adminId = given()
                .contentType("application/json")
                .body(adminJson)
                .when()
                .post("/admins")
                .then().statusCode(200)
                .extract().path("id");

        String ticketJson = """
                {
                  "reporter": { "id": %d },
                  "admin":    { "id": %d }
                }
                """.formatted(repId, adminId);

//        int ticketId = given()
//                .contentType("application/json")
//                .body(ticketJson)
//                .when()
//                .post("/tickets")
//                .then().statusCode(200)
//                .extract().path("id");

        int ticketId = given()
                .contentType("application/json")
                .body(ticketJson)
                .when()
                .post("/tickets")
                .then().statusCode(201)
                .extract().path("id");

        when()
                .get("/tickets/{id}", ticketId)
                .then()
                .statusCode(200)
                .body("resolved", equalTo(false));

        when()
                .put("/tickets/{id}/resolve?adminId={adm}", ticketId, adminId)
                .then()
                .statusCode(200)
                .body("resolved", equalTo(true));
    }
}


