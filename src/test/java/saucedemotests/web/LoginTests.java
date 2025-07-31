package saucedemotests.web;

import org.junit.jupiter.api.Test;
import saucedemotests.core.SauceDemoBaseWebTest;
import saucedemotests.enums.TestData;

public class LoginTests extends SauceDemoBaseWebTest {

    @Test
    public void userAuthenticated_when_validCredentialsProvided(){
        loginPage.navigate();
        loginPage.submitLoginForm(TestData.STANDARD_USER_USERNAME.getValue(), TestData.STANDARD_USER_PASSWORD.getValue());
        inventoryPage.waitForPageTitle();
        inventoryPage.assertNavigated();
    }
}