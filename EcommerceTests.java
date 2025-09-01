import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class EcommerceTests {

    static WebDriver driver;

    // Setup
    public static void setUp() {
    	    ChromeOptions options = new ChromeOptions();
    	    options.addArguments("--incognito");
    	    options.addArguments("--disable-save-password-bubble");
    	    options.addArguments("--disable-infobars");
    	    options.addArguments("--disable-notifications");

    	    driver = new ChromeDriver(options);
    	    driver.manage().window().maximize();
    	    driver.get("https://www.saucedemo.com/");
    	}


    // Login Helper
    public static void login(String username, String password) {
        driver.findElement(By.id("user-name")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("login-button")).click();
    }

    // TC_01: Valid Login
    public static void validLogin() {
        setUp();
        login("standard_user", "secret_sauce");
        System.out.println("Title after login: " + driver.getTitle());
        driver.quit();
    }

    // TC_02: Invalid Login
    public static void invalidLogin() {
        setUp();
        login("standard_user", "wrong_pass");
        String error = driver.findElement(By.cssSelector("h3[data-test='error']")).getText();
        System.out.println("Error: " + error);
        driver.quit();
    }

    // TC_03: Empty Fields
    public static void emptyFields() {
        setUp();
        login("", "");
        String error = driver.findElement(By.cssSelector("h3[data-test='error']")).getText();
        System.out.println("Error: " + error);
        driver.quit();
    }

    // TC_04: Locked Out User
    public static void lockedOutUser() {
        setUp();
        login("locked_out_user", "secret_sauce");
        String error = driver.findElement(By.cssSelector("h3[data-test='error']")).getText();
        System.out.println("Error: " + error);
        driver.quit();
    }

    // TC_05: Add Item to Cart
    public static void addItemToCart() {
        setUp();
        login("standard_user", "secret_sauce");
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        String count = driver.findElement(By.className("shopping_cart_badge")).getText();
        System.out.println("Cart Count: " + count);
        driver.quit();
    }

    // TC_06: Remove Item from Cart
    public static void removeItemFromCart() {
        setUp();
        login("standard_user", "secret_sauce");
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        driver.findElement(By.id("remove-sauce-labs-backpack")).click();
        List<WebElement> badges = driver.findElements(By.className("shopping_cart_badge"));
        if (badges.size() == 0) {
            System.out.println("Cart badge visible? false");
        } else {
            String count = badges.get(0).getText();
            if (count.isEmpty()) {
                System.out.println("Cart badge visible? false");
            } else {
                System.out.println("Cart badge visible? true with count " + count);
            }
        }

        driver.quit();
    }

    // TC_07: Multiple Items
    public static void multipleItems() {
        setUp();
        login("standard_user", "secret_sauce");
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        driver.findElement(By.id("add-to-cart-sauce-labs-bike-light")).click();
        driver.findElement(By.id("add-to-cart-sauce-labs-bolt-t-shirt")).click();
        String count = driver.findElement(By.className("shopping_cart_badge")).getText();
        System.out.println("Cart Count: " + count);
        driver.quit();
    }

    // TC_08: Cart Persistence (not persistent in SauceDemo, will reset after logout)
    public static void cartPersistence() throws Exception {
        setUp();
        login("standard_user", "secret_sauce");
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        driver.findElement(By.id("react-burger-menu-btn")).click();
     // Wait for logout link to become visible
     Thread.sleep(1000); // quick fix for demo (better: WebDriverWait)

     driver.findElement(By.id("logout_sidebar_link")).click();

        login("standard_user", "secret_sauce");
        boolean present = driver.findElements(By.className("shopping_cart_badge")).size() > 0;
        System.out.println("Cart retained? " + present);
        driver.quit();
    }

    // TC_09: Checkout Valid Info
    public static void checkoutValid() {
        setUp();
        login("standard_user", "secret_sauce");
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        driver.findElement(By.className("shopping_cart_link")).click();
        driver.findElement(By.id("checkout")).click();
        driver.findElement(By.id("first-name")).sendKeys("John");
        driver.findElement(By.id("last-name")).sendKeys("Doe");
        driver.findElement(By.id("postal-code")).sendKeys("12345");
        driver.findElement(By.id("continue")).click();
        driver.findElement(By.id("finish")).click();
        String msg = driver.findElement(By.className("complete-header")).getText();
        System.out.println("Checkout Message: " + msg);
        driver.quit();
    }

    // TC_10: Checkout Invalid Info
    public static void checkoutInvalid() {
        setUp();
        login("standard_user", "secret_sauce");
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        driver.findElement(By.className("shopping_cart_link")).click();
        driver.findElement(By.id("checkout")).click();
        driver.findElement(By.id("continue")).click();
        String error = driver.findElement(By.cssSelector("h3[data-test='error']")).getText();
        System.out.println("Error: " + error);
        driver.quit();
    }

    // TC_11: Page Title Verification
    public static void verifyTitle() {
        setUp();
        login("standard_user", "secret_sauce");
        System.out.println("Title: " + driver.getTitle());
        driver.quit();
    }

    // TC_12: URL Verification
    public static void verifyURL() {
        setUp();
        login("standard_user", "secret_sauce");
        String url = driver.getCurrentUrl();
        System.out.println("Current URL: " + url);
        driver.quit();
    }

    // Main Method to Run Tests
    public static void main(String[] args) throws Throwable {
        validLogin();
        invalidLogin();
        emptyFields();
        lockedOutUser();
        addItemToCart();
        removeItemFromCart();
        multipleItems();
        cartPersistence();
        checkoutValid();
        checkoutInvalid();
        verifyTitle();
        verifyURL();
    }
}