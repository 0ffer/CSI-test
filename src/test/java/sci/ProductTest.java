package sci;

import org.junit.Test;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.*;

public class ProductTest {

    @Test
    public void fromExternal() {
        final PriceCSI exFormat = new PriceCSI();
        exFormat.setId(1);
        exFormat.setProductCode("122856");
        exFormat.setNumber(2);
        exFormat.setDepart(3);
        exFormat.setBegin(new Date(10_000));
        exFormat.setEnd(new Date(20_000));
        exFormat.setValue(11000);

        final Product expected = new Product();
        expected.setId(1);
        expected.setCode("122856");
        expected.setNumber(2);
        expected.setDepartment(3);
        expected.getPrices().put(Instant.ofEpochMilli(10_000), 11000L);
        expected.getPrices().put(Instant.ofEpochMilli(20_000), null);

        assertEquals(expected, Product.fromExternalFormat(exFormat));
        assertEquals(expected.getPrices(), Product.fromExternalFormat(exFormat).getPrices());
    }

    @Test
    public void toExternalSimple() {
        final Product product = new Product();
        product.setId(1);
        product.setCode("122856");
        product.setNumber(2);
        product.setDepartment(3);
        product.getPrices().put(Instant.ofEpochMilli(10_000), 11000L);
        product.getPrices().put(Instant.ofEpochMilli(20_000), null);

        final PriceCSI expected = new PriceCSI();
        expected.setId(1);
        expected.setProductCode("122856");
        expected.setNumber(2);
        expected.setDepart(3);
        expected.setBegin(new Date(10_000));
        expected.setEnd(new Date(20_000));
        expected.setValue(11000);

        assertEquals(1, product.toExternalFormat().size());
        assertEquals(expected, product.toExternalFormat().get(0));
    }

    @Test
    public void mergeTwoPricesNotIntersectsAfterEnd() {
        final Product testProduct = new Product();

        testProduct.getPrices().put(Instant.ofEpochSecond(100), 100L);
        testProduct.getPrices().put(Instant.ofEpochSecond(200), null);

        testProduct.addPriceRule(Instant.ofEpochSecond(300), Instant.ofEpochSecond(400), 500L);

        final Map<Instant, Long> expected = new TreeMap<>();
        expected.put(Instant.ofEpochSecond(100), 100L);
        expected.put(Instant.ofEpochSecond(200), null);
        expected.put(Instant.ofEpochSecond(300), 500L);
        expected.put(Instant.ofEpochSecond(400), null);

        assertEquals(expected, testProduct.getPrices());
    }

    @Test
    public void mergeTwoPricesNotIntersectsBeforeStart() {
        fail();
    }

    @Test
    public void mergeTwoPricesIntersectsBeforeEnd() {
        fail();
    }

    @Test
    public void mergeTwoPricesIntersectsAfterStart() {
        fail();
    }

    @Test
    public void mergeThreePricesIntersectsTwoInMiddle() {
        fail();
    }

    @Test
    public void mergeTwoEqualsPricesOneNested() {
        fail();
    }

    @Test
    public void mergeTwoEqualsPricesOneExtendsFromEnd() {
        fail();
    }

    @Test
    public void mergeTwoEqualsPricesOneExtendsBeforeStart() {
        fail();
    }
}