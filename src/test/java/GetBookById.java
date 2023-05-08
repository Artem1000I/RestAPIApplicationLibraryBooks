import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

public class  GetBookById extends AbstractTest{

    @BeforeAll
    static void setUp(){
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
    @Test
    void GetBookIdValid(){
        String id = given()
                .body( "{\n"
                        +"\"name\": \"Java\",\n"
                        +"\"author\": \"Katy Era\",\n"
                        +"\"isElectronicBook\": true,\n"
                        +"\"year\": 2019"
                        + "}")
                .when()
                .contentType(ContentType.JSON)
                .post("http://localhost:5000/api/books")

                .then()
                .log().all()
                .assertThat()
                .statusCode(201)
                .statusLine("HTTP/1.1 201 CREATED")
                .statusLine(containsString("CREATED"))
                .extract()
                .jsonPath()
                .get("book.id")
                .toString();

             given()

                .when()
                .contentType(ContentType.JSON)
                .get("http://localhost:5000/api/books/"+id)

                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .statusLine("HTTP/1.1 200 OK")
                .statusLine(containsString("OK"))
                .body("book.id", Matchers.is(Matchers.notNullValue()))
                .body("book.name", Matchers.is("Java"));


    }


    @Test
    void GetBookWithWrongId(){
        given()

                .when()
                .contentType(ContentType.JSON)
                .get("http://localhost:5000/api/books/9999999")

                .then()
                .log().all()
                .assertThat()
                .statusCode(404)
                .statusLine("HTTP/1.1 404 NOT FOUND")
                .statusLine(containsString("NOT FOUND"))
                .body("error", Matchers.is("Book with id 9999999 not found"));

    }

}
