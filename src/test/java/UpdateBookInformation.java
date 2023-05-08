import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

public class UpdateBookInformation extends AbstractTest{

    @BeforeAll
    static void setUp(){
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void UpdateBookInfo(){
        String id = given()
                .body( "{\n"
                        +"\"name\": \"Неострые козырьки\",\n"
                        +"\"author\": \"ШОМАС ТЕЛБИ\",\n"
                        +"\"isElectronicBook\": false,\n"
                        +"\"year\": 2017"
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
                .body( "{\n"
                        +"\"name\": \"Острые козырьки\",\n"
                        +"\"author\": \"Томас Шелби\",\n"
                        +"\"isElectronicBook\": true,\n"
                        +"\"year\": 2016"
                        + "}")
                .when()
                .contentType(ContentType.JSON)
                .put("http://localhost:5000/api/books/"+id)

                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .statusLine("HTTP/1.1 200 OK")
                .statusLine(containsString("OK"))
                .body("book.name", Matchers.is("Острые козырьки"))
                .body("book.id", Matchers.is(Matchers.notNullValue()))
                .body("book.author", Matchers.is("Томас Шелби"))
                .body("book.isElectronicBook", Matchers.is(true))
                .body("book.year", Matchers.is(2016));
    }

    @Test
    void UpdateBookInvalidId(){
        given()
                .body( "{\n"
                        +"\"name\": \"Король лев\",\n"
                        +"\"author\": \"Тимон и пумба\",\n"
                        +"\"isElectronicBook\": true,\n"
                        +"\"year\": 1995"
                        + "}")
                .when()
                .contentType(ContentType.JSON)
                .put("http://localhost:5000/api/books/9999999")

                .then()
                .log().all()
                .assertThat()
                .statusCode(404)
                .statusLine("HTTP/1.1 404 NOT FOUND")
                .statusLine(containsString("NOT FOUND"))
                .body("error", Matchers.is("Book with id 9999999 not found"));
    }


    @Test
    void UpdateBookNameFieldWithSpaces(){
        String id = given()
                .body( "{\n"
                        +"\"name\": \"Король лев\",\n"
                        +"\"author\": \"Тимон\",\n"
                        +"\"isElectronicBook\": false,\n"
                        +"\"year\": 1996"
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
                .body( "{\n"
                        +"\"name\": \"   \",\n"
                        +"\"author\": \"Тимон и пумба\",\n"
                        +"\"isElectronicBook\": true,\n"
                        +"\"year\": 1947"
                        + "}")
                .when()
                .contentType(ContentType.JSON)
                .put("http://localhost:5000/api/books/"+id)

                .then()
                .log().all()
                .assertThat()
                .statusCode(400)
                .statusLine("HTTP/1.1 400 BAD REQUEST")
                .statusLine(containsString("BAD REQUEST"))
                .body("error", Matchers.is("Required name field is not filled"));

    }



    @Test
    void UpdateBookNameNotFilled(){
        String id = given()
                .body( "{\n"
                        +"\"name\": \"Сказка про Шрека\",\n"
                        +"\"author\": \"Осел\",\n"
                        +"\"isElectronicBook\": false,\n"
                        +"\"year\": 2000"
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
                .body( "{\n"
                        +"\"name\": \"\",\n"
                        +"\"author\": \"Фиона\",\n"
                        +"\"isElectronicBook\": true,\n"
                        +"\"year\": 2001"
                        + "}")
                .when()
                .contentType(ContentType.JSON)
                .put("http://localhost:5000/api/books/"+id)

                .then()
                .log().all()
                .assertThat()
                .statusCode(400)
                .statusLine("HTTP/1.1 400 BAD REQUEST")
                .statusLine(containsString("BAD REQUEST"))
                .body("error", Matchers.is("Required name field is not filled"));
    }

}
