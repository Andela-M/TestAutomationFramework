package saucedemotests.web;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
import saucedemotests.core.SauceDemoBaseWebTest;
import saucedemotests.enums.TestData;

import java.util.List;

public class ProductsTests extends SauceDemoBaseWebTest {
    private final String BACKPACK_TITLE = "Sauce Labs Backpack";
    private final String SHIRT_TITLE = "Sauce Labs Bolt T-Shirt";
    private final String FIRST_NAME = "Andela";
    private final String LAST_NAME = "Micic";
    private final String POSTAL_CODE = "12345";

    @BeforeEach
    public void beforeTest(){
        loginPage.navigate();
        authenticateWithUser(TestData.STANDARD_USER_USERNAME.getValue(),
                TestData.STANDARD_USER_PASSWORD.getValue());

    }

    @Test
    public void productAddedToShoppingCart_when_addToCart(){
        inventoryPage.addProductsByTitle(BACKPACK_TITLE, SHIRT_TITLE);
        inventoryPage.clickShoppingCartLink();

        Assertions.assertEquals(2, inventoryPage.getShoppingCartItemsNumber());
        List<String> itemsInCart = shoppingCartPage.getShoppingCartItems().stream().
                map(WebElement::getText).toList();
        Assertions.assertTrue(itemsInCart.contains(BACKPACK_TITLE), "Backpack "
                + BACKPACK_TITLE + " should be displayed in shopping cart.");
        Assertions.assertTrue(itemsInCart.contains(SHIRT_TITLE), "T-Shirt "
                + SHIRT_TITLE + " should be displayed in shopping cart.");
    }

    @Test
    public void userDetailsAdded_when_checkoutWithValidInformation(){
        inventoryPage.addProductsByTitle(BACKPACK_TITLE, SHIRT_TITLE);
        inventoryPage.clickShoppingCartLink();
        shoppingCartPage.clickCheckout();
        checkoutYourInformationPage.fillShippingDetails(FIRST_NAME, LAST_NAME, POSTAL_CODE);
        checkoutYourInformationPage.clickContinue();


        Assertions.assertEquals(2, inventoryPage.getShoppingCartItemsNumber(),
                "Shopping cart should contain 2 items.");

        String expectedTotalCost = checkoutOverviewPage.getTotalLabelText();
        List<String> itemsInCheckout = checkoutOverviewPage.getShoppingCartItems()
                .stream().map(WebElement::getText).toList();
        Assertions.assertTrue(itemsInCheckout.contains(BACKPACK_TITLE),
                "Backpack " + BACKPACK_TITLE + " should be displayed in checkout page.");
        Assertions.assertTrue(itemsInCheckout.contains(SHIRT_TITLE),
                "T-Shirt " + SHIRT_TITLE + " should be displayed in checkout page.");
        Assertions.assertEquals(expectedTotalCost, checkoutOverviewPage.getTotalLabelText(),
                "The total cost should match. Expected: " + expectedTotalCost + ", Actual: " + checkoutOverviewPage.getTotalLabelText());

    }

    @Test
    public void orderCompleted_when_addProduct_and_checkout_withConfirm(){
        inventoryPage.addProductsByTitle(BACKPACK_TITLE, SHIRT_TITLE);
        inventoryPage.clickShoppingCartLink();
        shoppingCartPage.clickCheckout();
        checkoutYourInformationPage.fillShippingDetails(FIRST_NAME, LAST_NAME, POSTAL_CODE);
        checkoutYourInformationPage.clickContinue();
        checkoutOverviewPage.clickFinish();
        checkoutCompletePage.assertNavigated();
        shoppingCartPage.navigate();

        Assertions.assertEquals(0, inventoryPage.getShoppingCartItemsNumber(),
                "Shopping cart is empty.");
    }
}