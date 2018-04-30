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
public class PriceInfoTest {

    @Test
    @Parameters(method = "fromSingle, twoWithBreak, twoWithoutBreak")
    @TestCaseName("{0}")
    public void paramsTest(String testDesc, List<Pair> initial, List<PriceRule> modificationData, List<Pair> expected) {
        final PriceInfo testPriceInfo = new PriceInfo();
        for (final Pair pair: initial)
            testPriceInfo.getTimeRules().put(pair.time, pair.value);

        for (final PriceRule rule: modificationData)
            testPriceInfo.addPriceRule(rule.startTime, rule.endTime, rule.value);

        final Map<Instant, Long> expectedPrices = new TreeMap<>();
        for (final Pair pair: expected)
            expectedPrices.put(pair.time, pair.value);

        assertEquals(expectedPrices, testPriceInfo.getTimeRules());
    }

    private Object fromSingle() {
        return new Object[]{
                new Object[]{"Добавление правила в пустой список",
                        Arrays.asList(),
                        Arrays.asList(PriceRule.of(100L, 200L, 100L)),
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(200L, null))},
                new Object[]{"Добавление одного правила, не пересекается справа",
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(200L, null)),
                        Arrays.asList(PriceRule.of(300L, 400L, 500L)),
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(200L, null), Pair.of(300L, 500L), Pair.of(400L, null))},
                new Object[]{"Добавление одного правила, не пересекается слева",
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(200L, null)),
                        Arrays.asList(PriceRule.of(50L, 90L, 500L)),
                        Arrays.asList(Pair.of(50L, 500L), Pair.of(90L, null), Pair.of(100L, 100L), Pair.of(200L, null))},
                new Object[]{"Добавление одного правила, пересекается в начале",
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(200L, null)),
                        Arrays.asList(PriceRule.of(50L, 150L, 500L)),
                        Arrays.asList(Pair.of(50L, 500L), Pair.of(150L, 100L), Pair.of(200L, null))},
                new Object[]{"Добавление одного правила, пересекается в конце",
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(200L, null)),
                        Arrays.asList(PriceRule.of(150L, 250L, 500L)),
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(150L, 500L), Pair.of(250L, null))},
                new Object[]{"Добавление одного равного по цене правила, пересекается в начале",
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(200L, null)),
                        Arrays.asList(PriceRule.of(50L, 150L, 100L)),
                        Arrays.asList(Pair.of(50L, 100L), Pair.of(200L, null))},
                new Object[]{"Добавление одного равного по цене правила, пересекается в конце",
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(200L, null)),
                        Arrays.asList(PriceRule.of(150L, 250L, 100L)),
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(250L, null))},
                new Object[]{"Добавление одного равного по цене правила, полностью входит в существующее",
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(200L, null)),
                        Arrays.asList(PriceRule.of(150L, 180L, 100L)),
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(200L, null))},
                new Object[]{"Добавление одного равного по цене правила, полностью поглощает существующее",
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(200L, null)),
                        Arrays.asList(PriceRule.of(150L, 180L, 100L)),
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(200L, null))},
        };
    }

    private Object twoWithBreak() {
        return new Object[]{
                new Object[]{"Добавление правила в промежуток",
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(200L, null), Pair.of(300L, 200L), Pair.of(400L, null)),
                        Arrays.asList(PriceRule.of(220L, 280L, 150L)),
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(200L, null), Pair.of(220L, 150L), Pair.of(280L, null), Pair.of(300L, 200L), Pair.of(400L, null))},
        };
    }

    private Object twoWithoutBreak() {
        return new Object[]{
                new Object[]{"Добавление правила с разбиением на три",
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(200L, 200L), Pair.of(300L, null)),
                        Arrays.asList(PriceRule.of(150L, 250L, 150L)),
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(150L, 150L), Pair.of(250L, 200L), Pair.of(300L, null))},
        };
    }

    /**
     * Класс для определения правил внутреннего представления.
     */
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

    /**
     * Класс для определения правил внешнего представления.
     */
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