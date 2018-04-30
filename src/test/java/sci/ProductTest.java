package sci;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.Instant;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(JUnitParamsRunner.class)
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
    @Parameters(method = "testCases")
    @TestCaseName("test {0}")
    public void paramsTest(String testDesc, List<Pair> initial, List<PriceRule> modificationData, List<Pair> expected) {
        final Product testProduct = new Product();
        for (final Pair pair: initial)
            testProduct.getPrices().put(pair.time, pair.value);

        for (final PriceRule rule: modificationData)
            testProduct.addPriceRule(rule.startTime, rule.endTime, rule.value);

        final Map<Instant, Long> expectedPrices = new TreeMap<>();
        for (final Pair pair: expected)
            expectedPrices.put(pair.time, pair.value);

        assertEquals(expectedPrices, testProduct.getPrices());
    }

    private Object testCases() {
        return new Object[]{
                new Object[]{"fromEmpty",
                        Arrays.asList(),
                        Arrays.asList(PriceRule.of(100L, 200L, 100L)),
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(200L, null))},
                new Object[]{"mergeTwoPricesNotIntersectsAfterEnd",
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(200L, null)),
                        Arrays.asList(PriceRule.of(300L, 400L, 500L)),
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(200L, null), Pair.of(300L, 500L), Pair.of(400L, null))},
                new Object[]{"mergeTwoPricesNotIntersectsBeforeStart",
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(200L, null)),
                        Arrays.asList(PriceRule.of(50L, 90L, 500L)),
                        Arrays.asList(Pair.of(50L, 500L), Pair.of(90L, null), Pair.of(100L, 100L), Pair.of(200L, null))},
                new Object[]{"mergeTwoPricesIntersectsBeforeEnd",
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(200L, null)),
                        Arrays.asList(PriceRule.of(50L, 150L, 500L)),
                        Arrays.asList(Pair.of(50L, 500L), Pair.of(150L, 100L), Pair.of(200L, null))},
                new Object[]{"mergeTwoPricesIntersectsAfterStart",
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(200L, null)),
                        Arrays.asList(PriceRule.of(150L, 250L, 500L)),
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(150L, 500L), Pair.of(250L, null))},
                new Object[]{"mergeTwoEqualsPricesIntersectsBeforeEnd",
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(200L, null)),
                        Arrays.asList(PriceRule.of(50L, 150L, 100L)),
                        Arrays.asList(Pair.of(50L, 100L), Pair.of(200L, null))},
                new Object[]{"mergeTwoEqualsPricesIntersectsAfterStart",
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(200L, null)),
                        Arrays.asList(PriceRule.of(150L, 250L, 100L)),
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(250L, null))},
                new Object[]{"mergeTwoEqualsPricesOneNested",
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(200L, null)),
                        Arrays.asList(PriceRule.of(150L, 180L, 100L)),
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(200L, null))},
                new Object[]{"mergeTwoEqualsPricesOneOverlaps",
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(200L, null)),
                        Arrays.asList(PriceRule.of(150L, 180L, 100L)),
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(200L, null))},
        };
    }

    static class Pair {
        Instant time;
        Long value;

        static Pair of(Long seconds, Long value) {
            Pair res = new Pair();
            res.time = Instant.ofEpochSecond(seconds);
            res.value = value;
            return res;
        }
    }

    static class PriceRule {
        Instant startTime;
        Instant endTime;
        Long value;

        static PriceRule of(Long startSeconds, Long endSeconds, Long value) {
            PriceRule res = new PriceRule();
            res.startTime = Instant.ofEpochSecond(startSeconds);
            res.endTime = Instant.ofEpochSecond(endSeconds);
            res.value = value;
            return res;
        }
    }
}