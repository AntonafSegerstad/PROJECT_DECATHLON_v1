package stepDefs;

import com.example.decathlon.Application;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.File;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.openqa.selenium.Keys.DOWN;

@CucumberContextConfiguration
@SpringBootTest(
        classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)

public class MyStepdefs {
    private WebDriver driver;
    private WebDriverWait wait;

    @LocalServerPort
    private int port;
    private String baseUrl;

    @Before
    public void setUp() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        baseUrl = "http://localhost:" + port;
        driver.get(baseUrl);

    }

    @After
    void tearDown() {
        driver.quit();
    }

    @Given("the user is on webpage {string}")
    public void theUserIsOnWebpage(String localhost) {
        String getUrl = driver.getCurrentUrl();
        assertEquals("http://localhost:8080/", getUrl);
    }


    // TINA - - - - - - - - - - - - - - - - - - -  - - - - - - - - -

    @When("the valid name {string} has been entered into the name field")
    public void aNameHasBeenEnteredIntoTheField(String name) {
        WebElement nameField = driver.findElement(By.cssSelector("#name"));
        nameField.clear();
        nameField.sendKeys(name);
    }

    @When("user clicks the add competitor button")
    public void userClicksTheAddCompetitorButton() {
        WebElement addButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("#add"))
        );
        addButton.click();
    }

    @Then("the name {string} should be visible in the Standings section")
    public void theShouldBeVisibleInTheStandingsSection(String name) {
        wait.until(
                ExpectedConditions.textToBePresentInElementLocated(
                        By.cssSelector("#standings"),
                        name
                )
        );
        String tableText = driver.findElement(By.cssSelector("#standings")).getText();
        Assertions.assertTrue(tableText.contains(name));
    }

    @And("the name field in Add competitor is selected")
    public void theNameFieldInAddCompetitorIsSelected() {
        driver.findElement(By.cssSelector("#name")).click();
    }


    @And("the result {string} has been entered into the Result field")
    public void aHasBeenEnteredIntoTheResultField(String result) {
        WebElement resultField = driver.findElement(By.cssSelector("#raw"));
        resultField.clear();
        resultField.sendKeys(result);

    }

    @And("the event {string} has been selected in the event field")
    public void theEventMHasBeenSelectedInTheEventField(String event) {
        Select dropdown = new Select(driver.findElement(By.cssSelector("#event")));
        dropdown.selectByValue(event);

    }

    @When("user clicks the Save score button")
    public void userClicksSaveScoreButton() {
        driver.findElement(By.cssSelector("#save")).click();
    }

    @Then("score {string} is visible in Standings")
    public void scoreIsVisibleInStandings(String points) {
        By standingsSelector = By.cssSelector("#standings");
        wait.until( ExpectedConditions.textToBePresentInElementLocated(standingsSelector, points) );
        String tableText = driver.findElement(standingsSelector).getText(); Assertions.assertTrue(tableText.contains(points));

            }

    @And("user clicks the {string} button")
    public void userClicksTheButton(String arg0) {
        WebElement exportButton = wait.until( ExpectedConditions.elementToBeClickable(By.cssSelector("#export")) );
        exportButton.click();
    }

    @Then("the CSV file is exported and downloaded")
    public void theCSVFileIsExportedAndDownloaded() {
        File downloadDir = new File(System.getProperty("user.home") + "/Downloads");
        File[] files = downloadDir.listFiles((dir, name) -> name.startsWith("results") && name.endsWith(".csv") );
        Assertions.assertNotNull(files); Assertions.assertTrue(files.length > 0);

    }




    // PHYLLIS - - - - - - - - - - - - - - - - - - -  - - - - - - - - -
    @Given("the user is on the calculator page")
    public void theUserIsOnTheCalculatorPage() {
        driver.get(baseUrl);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h1")));
    }

    @And("{string} is selected from the event dropdown")
    public void isSelectedFromTheEventDropdown(String eventLabel) {
        WebElement dropdown = driver.findElement(By.id("event"));
        //Jag skickar in 100m (s) till fältet
        dropdown.sendKeys(eventLabel);
    }

    @And("the result {string} is entered in the result field")
    public void theResultIsEnteredInTheResultField(String name) {
        // app.js använder id="name2" för namnet i "Enter result"-sektionen
        WebElement nameField = driver.findElement(By.id("name2"));
        nameField.clear();
        nameField.sendKeys(name);
    }
    

    @Then("the message area should show {string}")
    public void theMessageAreaShouldShow(String expectedMessage) {
        // 1. Vänta på att meddelandet dyker upp
        WebElement messageArea = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("msg")));
        String actualMessage = messageArea.getText();
        assertTrue(actualMessage.contains(expectedMessage),
                "Hittade inte förväntat meddelande! Skulle vara: " + expectedMessage + " men var: " + actualMessage);
    }

    @And("{string} is entered in the result name field")
    public void isEnteredInTheResultNameField(String name) {
        WebElement nameField = driver.findElement(By.cssSelector("[data-testid='competitorNameInput']"));
        nameField.clear();
        nameField.sendKeys(name);
    }

    @And("a non-numeric value {string} is entered in the result field")
    public void aNonNumericValueIsEnteredInTheResulField(String value) {
        WebElement field = driver.findElement(By.cssSelector("[data-testid='rawInput']"));
        field.clear();
        field.sendKeys(value);
    }

    @Then("an error message {string} should be displayed")
    public void anErrorMessageShouldBeDisplayed(String errorMessage) {
        WebElement errorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("msg")));
        String actualMessage = errorElement.getText();
        assertTrue(actualMessage.contains(errorMessage),
                "Förväntade felmeddelande '" + errorMessage + "' men hittade: '" + actualMessage + "'");
    }


    // ANTON - - - - - - - - - - - - - - - - - - -  - - - - - - - - - -

    @And("a {string} has been selected from the dropdown")
    public void aHasBeenSelectedFromTheDropdown(String event) {

        Select dropdown = new Select(driver.findElement(By.cssSelector("#event")));
        dropdown.selectByValue(event);
    }

    @Then("the correct {string} are presented for each event")
    public void theCorrectArePresentedForEachEvent(String points) {
        By standingsSelector = By.cssSelector("#standings");

        wait.until(
                ExpectedConditions.textToBePresentInElementLocated(standingsSelector, points)
        );

        String tableText = driver.findElement(standingsSelector).getText();
        Assertions.assertTrue(tableText.contains(points));
    }




    // KIM - - - - - - - - - - - - - - - - - - -  - - - - - - - - - - -

    // OSKAR - - - - - - - - - - - - - - - - - - -  - - - - - - - - - -


    // SAM - - - - - - - - - - - - - - - - - - -  - - - - - - - - - - -
}
