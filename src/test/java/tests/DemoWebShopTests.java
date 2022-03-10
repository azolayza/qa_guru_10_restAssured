package tests;

import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class DemoWebShopTests {

    @Test
    void addToCartAsNewUserTest() {
        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .body("product_attribute_72_5_18=53" +
                        "&product_attribute_72_6_19=54" +
                        "&product_attribute_72_3_20=57" +
                        "&addtocart_72.EnteredQuantity=1")
                .when()
                .post("http://demowebshop.tricentis.com/addproducttocart/details/72/1")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", is(true))
                .body("message", is("The product has been added to your " +
                        "<a href=\"/cart\">shopping cart</a>"))
                .body("updatetopcartsectionhtml", is("(1)"));
    }

    @Test
    void addToCartWithCookieTest() {
        Integer cartSize = 0;

        ValidatableResponse response =
                given()
                        .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                        .cookie("Nop.customer=6b7ad687-aaa3-44ce-b30c-73011016a6d8; __utma=78382081.610137593.1646658735.1646658735.1646658735.1; __utmc=78382081; __utmz=78382081.1646658735.1.1.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=(not%20provided); __RequestVerificationToken=sklUCCp9xwEdGH9KKA-OtfDmjWwYwu2NbSXzRImlci03iDCgkPci3UIsOz96Ktyh87KO1Q4LpzB-PFJuoHO_5EMuMKe6J-uU9MaeZXijf4k1; NOPCOMMERCE.AUTH=6394359EEB01597015F32CF6EBF5652EFDDE70E54EF36F5874D4FC12E96937374EF2257492CB2E85520A3CDF63F5731268A77F101E1C0BF70D0933940DB3878DE0E8FEA38423D326530BC3A4235D4B2A23818697CCD4A103FB02B81643DBD3D7163C9BF0C118CCA793C9EAFE7A51486F63FA8D60A892912E2FBDF67662D709BC; nop.CompareProducts=CompareProductIds=24&CompareProductIds=44; __utmt=1; NopCommerce.RecentlyViewedProducts=RecentlyViewedProductIds=72&RecentlyViewedProductIds=31&RecentlyViewedProductIds=24&RecentlyViewedProductIds=75; __atuvc=16%7C10; __atuvs=62260604086d499500f; __utmb=78382081.61.10.1646658735")
                        .body("product_attribute_72_5_18=53" +
                                "&product_attribute_72_6_19=54" +
                                "&product_attribute_72_3_20=57" +
                                "&addtocart_72.EnteredQuantity=1")
                        .when()
                        .post("http://demowebshop.tricentis.com/addproducttocart/details/72/1")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .body("success", is(true))
                        .body("message", is("The product has been added to your " +
                                "<a href=\"/cart\">shopping cart</a>"));

//assertThat(response.extract().path("updatetopcartsectionhtml").toString())
// .body("updatetopcartsectionhtml", is("(43)"));*/

    }

    @Test //Добавление товара в лист стравнения неавторизованным пользователем
    void addToCompareListTest() {
        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .when()
                .get("http://demowebshop.tricentis.com/compareproducts/add/24")
                .then()
                .statusCode(200);
    }

    @Test //проверка неуспешного изменения параметров товара из корзины
    void negativeUpdateCartWithCookieTest() {
        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .cookie("Nop.customer=6b7ad687-aaa3-44ce-b30c-73011016a6d8; __utma=78382081.610137593.1646658735.1646658735.1646658735.1; __utmc=78382081; __utmz=78382081.1646658735.1.1.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=(not%20provided); __RequestVerificationToken=sklUCCp9xwEdGH9KKA-OtfDmjWwYwu2NbSXzRImlci03iDCgkPci3UIsOz96Ktyh87KO1Q4LpzB-PFJuoHO_5EMuMKe6J-uU9MaeZXijf4k1; NOPCOMMERCE.AUTH=6394359EEB01597015F32CF6EBF5652EFDDE70E54EF36F5874D4FC12E96937374EF2257492CB2E85520A3CDF63F5731268A77F101E1C0BF70D0933940DB3878DE0E8FEA38423D326530BC3A4235D4B2A23818697CCD4A103FB02B81643DBD3D7163C9BF0C118CCA793C9EAFE7A51486F63FA8D60A892912E2FBDF67662D709BC; nop.CompareProducts=CompareProductIds=24&CompareProductIds=44; __utmt=1; NopCommerce.RecentlyViewedProducts=RecentlyViewedProductIds=72&RecentlyViewedProductIds=31&RecentlyViewedProductIds=24&RecentlyViewedProductIds=75; __atuvc=16%7C10; __atuvs=62260604086d499500f; __utmb=78382081.61.10.1646658735")
                .body("product_attribute_72_5_18=52&product_attribute_72_6_19=91&" +
                        "product_attribute_72_3_20=57&product_attribute_72_8_30=94" +
                        "&addtocart_72.UpdatedShoppingCartItemId=2281230" + "" +
                        "&addtocart_72.EnteredQuantity=2")
                .when()
                .post("http://demowebshop.tricentis.com/addproducttocart/details/72/1")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", is(false))
                .body("message", is("No shopping cart item found to update"));

    }


    @Test //Переход на страницу обновления параметров выбранного товара
    void goToUpdateCardTitleTest() {
        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .cookie("Nop.customer=6b7ad687-aaa3-44ce-b30c-73011016a6d8; __utma=78382081.610137593.1646658735.1646658735.1646658735.1; __utmc=78382081; __utmz=78382081.1646658735.1.1.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=(not%20provided); __RequestVerificationToken=sklUCCp9xwEdGH9KKA-OtfDmjWwYwu2NbSXzRImlci03iDCgkPci3UIsOz96Ktyh87KO1Q4LpzB-PFJuoHO_5EMuMKe6J-uU9MaeZXijf4k1; NOPCOMMERCE.AUTH=6394359EEB01597015F32CF6EBF5652EFDDE70E54EF36F5874D4FC12E96937374EF2257492CB2E85520A3CDF63F5731268A77F101E1C0BF70D0933940DB3878DE0E8FEA38423D326530BC3A4235D4B2A23818697CCD4A103FB02B81643DBD3D7163C9BF0C118CCA793C9EAFE7A51486F63FA8D60A892912E2FBDF67662D709BC; nop.CompareProducts=CompareProductIds=24&CompareProductIds=44; __utmt=1; NopCommerce.RecentlyViewedProducts=RecentlyViewedProductIds=72&RecentlyViewedProductIds=31&RecentlyViewedProductIds=24&RecentlyViewedProductIds=75; __atuvc=16%7C10; __atuvs=62260604086d499500f; __utmb=78382081.61.10.1646658735")
                .body("updatecartitemid=2281302")
                .when()
                .get("http://demowebshop.tricentis.com/build-your-own-computer?updatecartitemid=2281302")
                .then()
                .statusCode(200);
    }

    @Test
    void successUpdateCardTitleTest() {
        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .cookie("Nop.customer=6b7ad687-aaa3-44ce-b30c-73011016a6d8; __utma=78382081.610137593.1646658735.1646658735.1646658735.1; __utmc=78382081; __utmz=78382081.1646658735.1.1.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=(not%20provided); __RequestVerificationToken=sklUCCp9xwEdGH9KKA-OtfDmjWwYwu2NbSXzRImlci03iDCgkPci3UIsOz96Ktyh87KO1Q4LpzB-PFJuoHO_5EMuMKe6J-uU9MaeZXijf4k1; NOPCOMMERCE.AUTH=6394359EEB01597015F32CF6EBF5652EFDDE70E54EF36F5874D4FC12E96937374EF2257492CB2E85520A3CDF63F5731268A77F101E1C0BF70D0933940DB3878DE0E8FEA38423D326530BC3A4235D4B2A23818697CCD4A103FB02B81643DBD3D7163C9BF0C118CCA793C9EAFE7A51486F63FA8D60A892912E2FBDF67662D709BC; nop.CompareProducts=CompareProductIds=24&CompareProductIds=44; __utmt=1; NopCommerce.RecentlyViewedProducts=RecentlyViewedProductIds=72&RecentlyViewedProductIds=31&RecentlyViewedProductIds=24&RecentlyViewedProductIds=75; __atuvc=16%7C10; __atuvs=62260604086d499500f; __utmb=78382081.61.10.1646658735")
                .body("product_attribute_72_5_18=53&product_attribute_72_6_19=91&product_attribute_72_3_20=57&addtocart_72.UpdatedShoppingCartItemId=2281324&addtocart_72.EnteredQuantity=1")
                .when()
                .post("http://demowebshop.tricentis.com/addproducttocart/details/72/1")
                .then()
                .statusCode(200)
                .body("success", is(true));
    }
}
