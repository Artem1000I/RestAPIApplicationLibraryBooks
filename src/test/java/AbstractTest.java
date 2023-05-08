import io.cucumber.java.BeforeAll;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AbstractTest{
    static Properties prop = new Properties();
    private static InputStream configFile;
    private static String baseUrl; //Урл сайта или ресурса

    @BeforeAll //Будет применяться для всех тестов
    static void initTest() throws IOException {
        configFile = new FileInputStream("src/main/resources/my.properties");// cчитываем конфигурационный файл
        prop.load(configFile); //загружаем его в обьект пропертис prop


        baseUrl= prop.getProperty("base_url");//достаем значение которое будем использовать
    }

    public static String getBaseUrl() {
        return baseUrl;
    }
}
