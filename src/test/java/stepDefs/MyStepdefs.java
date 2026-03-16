package stepDefs;

import com.example.decathlon.Application;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@CucumberContextConfiguration
@SpringBootTest(
        classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)

public class MyStepdefs {
    private WebDriver driver;

    @LocalServerPort
    public int port = 8080;
    public String baseUrl;

    @Before
    void setUp(){
        driver = new ChromeDriver();
        baseUrl = "localhost:"+port;
        driver.get(baseUrl);
    }

    @After
    void tearDown(){
        driver.quit();
    }

    @Given("the user is on webpage {string}")
    public void theUserIsOnWebpage(String localhost) {
        setUp();
    }

    @And("the name field in Add competitor is selected")
    public void theNameFieldInAddCompetitorIsSelected() {

    }

    @When("a {string} has been entered into the field")
    public void aHasBeenEnteredIntoTheField(String arg0) {

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
}
