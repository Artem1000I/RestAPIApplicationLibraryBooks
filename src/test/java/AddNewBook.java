import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

public class  AddNewBook extends AbstractTest{


    @BeforeAll
    static void setUp(){

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void AddBook(){
        given()
                .body( "{\n"
                        +"\"name\": \"Selenium Python\",\n"
                        +"\"author\": \"Томас Кормен, Чарльз Лейзерсон\",\n"
                        +"\"isElectronicBook\": true,\n"
                        +"\"year\": 2022"
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
                .body("book.name", Matchers.is("Selenium Python"))
                .body("book.id", Matchers.is(Matchers.notNullValue()))
                .body("book.author", Matchers.is("Томас Кормен, Чарльз Лейзерсон"))
                .body("book.isElectronicBook", Matchers.is(true))
                .body("book.year", Matchers.is(2022));
    }


    @Test
    void AddBookFillingOnlyName(){
        given()
                .body( "{\n"
                        +"\"name\": \"Selenium Java\""
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
                .body("book.name", Matchers.is("Selenium Java"))
                .body("book.id", Matchers.is(Matchers.notNullValue()))
                .body("book.author", Matchers.is(""))
                .body("book.isElectronicBook", Matchers.is(false))
                .body("book.year", Matchers.is(0));
    }


    @Test
    void AddBookBigName(){
        given()
                .body( "{\n"
                        +"\"name\": \"Рассвет полночи, или Созерцание славы, торжества и мудрости порфироносных, браноносных и мирных гениев России с последованием дидактических, эротических и других разного рода в стихах и прозе опытов Семена Боброва\",\n"
                        +"\"author\": \"Бобров Семён Сергеевич\",\n"
                        +"\"isElectronicBook\": true,\n"
                        +"\"year\": 1763"
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
                .body("book.name", Matchers.is("Рассвет полночи, или Созерцание славы, торжества и мудрости порфироносных, браноносных и мирных гениев России с последованием дидактических, эротических и других разного рода в стихах и прозе опытов Семена Боброва"))
                .body("book.id", Matchers.is(Matchers.notNullValue()))
                .body("book.author", Matchers.is("Бобров Семён Сергеевич"))
                .body("book.isElectronicBook", Matchers.is(true))
                .body("book.year", Matchers.is(1763));
    }

    @Test
    void AddBookSpecialCharactersInName(){
        given()
                .body( "{\n"
                        +"\"name\": \"€ Энциклопедия символов ¥\""
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
                .body("book.name", Matchers.is("€ Энциклопедия символов ¥"))
                .body("book.id", Matchers.is(Matchers.notNullValue()))
                .body("book.author", Matchers.is(""))
                .body("book.isElectronicBook", Matchers.is(false))
                .body("book.year", Matchers.is(0));
    }

    @Test
    void AddBookNameOfNumbers(){
        given()
                .body( "{\n"
                        +"\"name\": \"1945\""
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
                .body("book.name", Matchers.is("1945"))
                .body("book.id", Matchers.is(Matchers.notNullValue()))
                .body("book.author", Matchers.is(""))
                .body("book.isElectronicBook", Matchers.is(false))
                .body("book.year", Matchers.is(0));
    }

    @Test
    void AddBookYearOfFiveCharacters(){
        given()
                .body( "{\n"
                        +"\"name\": \"Год 2024 еще не настал\",\n"
                        +"\"year\": 2024"
                        + "}")
                .when()
                .contentType(ContentType.JSON)
                .post("http://localhost:5000/api/books")

                .then()
                .log().all()
                .assertThat()
                .statusCode(400)
                .statusLine("HTTP/1.1 400 CREATED")
                .statusLine(containsString("BAD REQUEST"))
                .body("error", Matchers.is("Invalid date format"));

    }
    @Test
    void AddBookInvalidDate(){
        given()
                .body( "{\n"
                        +"\"name\": \"1945\",\n"
                        +"\"year\": -1"
                        + "}")
                .when()
                .contentType(ContentType.JSON)
                .post("http://localhost:5000/api/books")

                .then()
                .log().all()
                .assertThat()
                .statusCode(400)
                .statusLine("HTTP/1.1 400 BAD REQUEST")
                .statusLine(containsString("BAD REQUEST"))
                .body("error", Matchers.is("Invalid date format"));
    }

    @Test
    void AddBookNameFieldWithSpaces(){
        given()
                .body( "{\n"
                        +"\"name\": \"   \",\n"
                        +"\"author\": \"Томас Кормен\",\n"
                        +"\"isElectronicBook\": true,\n"
                        +"\"year\": 2022"
                        + "}")
                .when()
                .contentType(ContentType.JSON)
                .post("http://localhost:5000/api/books")

                .then()
                .log().all()
                .assertThat()
                .statusCode(400)
                .statusLine("HTTP/1.1 400 BAD REQUEST")
                .statusLine(containsString("BAD REQUEST"))
                .body("error", Matchers.is("Required name field is not filled"));
    }

    @Test
    void AddBookNameNotFilled(){
        given()
                .body( "{\n"
                        +"\"name\": \"\",\n"
                        +"\"author\": \"Кирил Многоруков\",\n"
                        +"\"isElectronicBook\": true,\n"
                        +"\"year\": 2012"
                        + "}")
                .when()
                .contentType(ContentType.JSON)
                .post("http://localhost:5000/api/books")

                .then()
                .log().all()
                .assertThat()
                .statusCode(400)
                .statusLine("HTTP/1.1 400 BAD REQUEST")
                .statusLine(containsString("BAD REQUEST"))
                .body("error", Matchers.is("Required name field is not filled"));
    }


}
