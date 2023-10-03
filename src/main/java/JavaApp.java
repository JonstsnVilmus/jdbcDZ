import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class JavaApp {
    static final String dataBase = "test";
    static final String table = "Travel";
    public static void main(String[] args) {
        ConnectionJdbc connectionJdbc = new ConnectionJdbc().getInstance();
        Map<String, String> data = connectionJdbc.getData(dataBase,table);
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(4000));
        driver.get("https://travel.agileway.net/login");
        driver.findElement(By.name("username")).sendKeys("test");
        driver.findElement(By.name("password")).sendKeys("test");
        driver.findElement(By.name("commit")).click();
        // можем проверить текст ошибки на странице таким образом | ради примера буду делать вывод в консоль
        List<WebElement> elementList = driver.findElements(By.xpath("//*[text()='Invalid email or password']"));
        isCorrect(elementList.size()==0);
        // другой способ, просто проверить что элемент есть на странице
        isCorrect(driver.findElements(By.id("flash_alert")).size()==0);

        // вводим корректные данные
        driver.findElement(By.name("username")).sendKeys(data.get("login"));
        driver.findElement(By.name("password")).sendKeys(data.get("password"));
        driver.findElement(By.name("commit")).click();
        //проверяем что у нас есть сообщение и любой элемент на форме так как сообщение не гарантия того что форма открылась
        isCorrect(driver.findElements(By.xpath("//*[text()='Invalid email or password']")).size()==0);
        isCorrect(driver.findElements(By.name("toPort")).size()==0);
        driver.quit();

    }
    private static void isCorrect(boolean isCorrect){
        if(isCorrect){
            System.out.println("No element");
        }
    }

    private static void addDataInTable() {
        ConnectionJdbc connectionJdbc = new ConnectionJdbc().getInstance();
        Map<String, String> data = new HashMap<>();
        data.put("login", "agileway");
        data.put("password", "testwise");
        connectionJdbc.updateTable(dataBase, table, data);
    }
}
