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
    public void setUp(){
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        baseUrl = "http://localhost:" + port;
        driver.get(baseUrl);

    }

    @After
    void tearDown(){
        //driver.quit();
    }

    @Given("the user is on webpage {string}")
    public void theUserIsOnWebpage(String localhost) {
        String getUrl = driver.getCurrentUrl();
        assertEquals("http://localhost:8080/", getUrl);
    }


    @When("user clicks the {string} button")
    public void userClicksTheButton(String buttonText) {
        // Write code here that turns the phrase above into concrete actions
        WebElement btn = driver.findElement(By.xpath
                ("//button[contains(text(), '" + buttonText + "')]"));
        btn.click();
    }

    @When("a {string} has been entered into the field")
    public void aNameHasBeenEnteredIntoTheField(String name) {
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

    // TINA - - - - - - - - - - - - - - - - - - -  - - - - - - - - -
    @And("the name field in Add competitor is selected")
    public void theNameFieldInAddCompetitorIsSelected() {
        WebElement nameField = wait.until(ExpectedConditions.visibilityOfElementLocated
                (By.cssSelector("[data-testid='competitorNameInput']")));
        nameField.click();

        WebElement focusedField = driver.switchTo().activeElement();

        assertEquals(nameField, focusedField);
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


    // PHYLLIS - - - - - - - - - - - - - - - - - - -  - - - - - - - - -

    @Given("the user is on the calculator page")
    public void theUserIsOnTheCalculatorPage() {
        driver.get(baseUrl);

    }

    @And("{string} is selected from the event dropdown")
    public void isSelectedFromTheEventDropdown(String eventLabel) {
        WebElement dropdown = driver.findElement(By.name("disciplineBox"));
        dropdown.sendKeys(eventLabel);
    }

    @And("the result {string} is entered in the result field")
    public void theResultIsEnteredInTheResultField(String score) {
        WebElement resField = driver.findElement(By.name("resultField"));
        resField.clear();
        resField.sendKeys(score);
    }


    @Then("the message area should show {string}")
    public void theMessageAreaShouldShow(String expectedMessage) {
        WebElement messageArea = driver.findElement(By.id("message")); // Antaget ID
        String actualMessage = messageArea.getText();
        assertEquals(expectedMessage, actualMessage);
    }

    @And("{string} is entered in the result name field")
    public void isEnteredInTheResultNameField(String name) {
        WebElement nameField = driver.findElement(By.id("name2"));
        nameField.clear();
        nameField.sendKeys(name);
    }

    @And("a non-numeric value {string} is entered in the result field")
    public void aNonNumericValueIsEnteredInTheResulField(String value) {
        WebElement field = driver.findElement(By.id("score"));
        field.clear();
        field.sendKeys(value);
    }

    @Then("an error message {string} should be displayed")
    public void anErrorMessageShouldBeDisplayed(String errorMessage) {
        WebElement errorElement = driver.findElement(By.id("error")); // Antaget ID
        assertTrue(errorElement.getText().contains(errorMessage));
    }


    // ANTON - - - - - - - - - - - - - - - - - - -  - - - - - - - - - -

    @Then("a user friendly message of rejection is shown")
    public void aUserFriendlyMessageOfRejectionIsShown() {
        // Write code here that turns the phrase above into concrete actions

    }

    @And("an unreasonable {string} has been entered into the name field")
    public void anUnreasonableHasBeenEnteredIntoTheNameField(String name) {

        driver.findElement(By.cssSelector("[data-testid='competitorNameInput']"))
                .sendKeys(name);
        WebElement addBtn = driver.findElement(By.cssSelector("[data-testid='addCompetitorBtn']"));
        addBtn.click();
        addBtn.sendKeys(DOWN);
        WebElement table = driver.findElement(By.cssSelector("[data-testid='standingsTable']"));
        String tableText = table.getText();
        assertTrue(tableText.contains(name));

    }
    // KIM - - - - - - - - - - - - - - - - - - -  - - - - - - - - - - -

    // OSKAR - - - - - - - - - - - - - - - - - - -  - - - - - - - - - -

   

    // SAM - - - - - - - - - - - - - - - - - - -  - - - - - - - - - - -
}
