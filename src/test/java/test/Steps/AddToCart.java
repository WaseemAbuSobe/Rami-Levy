package test.Steps;
import Infrastructure.DriverSetup;
import Infrastructure.WrapApiResponse;
import Utils.DateTimeFormat;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import logic.*;
import org.junit.Assert;
import io.cucumber.datatable.DataTable;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddToCart {
    static HashMap<String,String> items;
    static DriverSetup driverSetup;

    //api values
    static ApiCalls apiCalls;
    static WrapApiResponse<ApiResponse> result;
//    @BeforeAll
//    public static void setUP() throws InterruptedException {
//        driverSetup = new DriverSetup();
//        driverSetup.setupDriver("chrome");
//        driverSetup.getDriver().get("https://www.rami-levy.co.il/he");
//        driverSetup.getDriver().manage().window().maximize();
//        MainPage mainPage = new MainPage(driverSetup.getDriver());
//        mainPage.flowPersonalArea("ashraf.egbaria@gmail.com","Ashrafadel152");
//        items = new HashMap<>();
//        // init api
//        apiCalls=new ApiCalls();
//        result=null;
//    }
//
//    @AfterAll
//    public static void tearDown(){
//        driverSetup.closeDriver();
//        items=null;
//        apiCalls=null;
//        result=null;
//    }

    @When("Add To Cart Item")
    public void addItem(DataTable dataTable) throws IOException {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> row : rows) {
            String item = row.get("item");
            String quantity = row.get("quantity");
            items.put(item, quantity);
        }
        String store="331";
        int inClub=0;
        String supplyAt= DateTimeFormat.getCurrentDateTime();
        ItemBodyRequest jsonBody=new ItemBodyRequest(store,inClub,supplyAt,items,null);
        result=ApiCalls.addNewProduct(jsonBody.toString());
    }
    @Then("Check The quantity")
    public static void checkTheQuantity() throws InterruptedException {
        CartMenu cartMenu = new CartMenu(driverSetup.getDriver());
        int sumQuantity = 0;
        for(Map.Entry<String, String> entry : items.entrySet()){
            float floatValue =  Float.parseFloat(entry.getValue());
            sumQuantity+= (int) floatValue ;
        }
        Assert.assertEquals(sumQuantity,cartMenu.countItems());
    }

}