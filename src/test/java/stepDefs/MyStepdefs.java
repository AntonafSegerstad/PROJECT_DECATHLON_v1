package stepDefs;

import com.example.decathlon.Application;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.openqa.selenium.Keys.*;

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
    public void setUp(){
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        baseUrl = "http://localhost:" + port;
        driver.get(baseUrl);
    }

    @After
    void tearDown(){
        driver.quit();
    }

    @Given("the user is on webpage {string}")
    public void theUserIsOnWebpage(String localhost) {
        String getUrl = driver.getCurrentUrl();
        assertEquals("http://localhost:8080/", getUrl);
    }

    // TINA - - - - - - - - - - - - - - - - - - -  - - - - - - - - -
    @And("the name field in Add competitor is selected")
    public void theNameFieldInAddCompetitorIsSelected() {
        WebElement nameField = wait.until(ExpectedConditions.visibilityOfElementLocated
                (By.cssSelector("[data-testid='competitorNameInput']")));
        nameField.click();

        WebElement focusedField = driver.switchTo().activeElement();

        assertEquals(nameField, focusedField);
    }

    @When("a {string} has been entered into the field")
    public void aHasBeenEnteredIntoTheField(String name) {
        name = "Chuck Norris";
        driver.findElement(By.cssSelector("[data-testid='competitorNameInput']"))
                .sendKeys(name);
        WebElement addBtn = driver.findElement(By.cssSelector("[data-testid='addCompetitorBtn']"));
        addBtn.click();
        addBtn.sendKeys(DOWN);
        WebElement table = driver.findElement(By.cssSelector("[data-testid='standingsTable']"));
        String tableText = table.getText();
        assertTrue(tableText.contains(name));
    }

    @And("user clicks the {string} button")
    public void userClicksTheButton(String arg0) {

    }

    @Then("the name should be visible in the Standings section")
    public void theNameShouldBeVisibleInTheStandingsSection() {
        tearDown();
    }

    @And("a {string} has been entered into the Name field")
    public void aHasBeenEnteredIntoTheNameField(String arg0) {

    }

    @And("a {string} has been entered into the Result field")
    public void aHasBeenEnteredIntoTheResultField(String arg0) {

    }

    @Then("the name and score is visible in Standings")
    public void theNameAndScoreIsVisibleInStandings() {
        tearDown();
    }

    @When("user has entered a {string} and {string}")
    public void userHasEnteredAAnd(String arg0, String arg1) {

    }

    @Then("the result file is exported")
    public void theResultFileIsExported() {

    }

    @And("available to download result file")
    public void availableToDownloadResultFile() {
        tearDown();
    }

    // ANTON - - - - - - - - - - - - - - - - - - -  - - - - - - - - - -

    // KIM - - - - - - - - - - - - - - - - - - -  - - - - - - - - - - -

    // OSKAR - - - - - - - - - - - - - - - - - - -  - - - - - - - - - -

    // PHYLLIS - - - - - - - - - - - - - - - - - - -  - - - - - - - - -

    // SAM - - - - - - - - - - - - - - - - - - -  - - - - - - - - - - -
}
