package sci;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;

@RunWith(JUnitParamsRunner.class)
public class PriceInfoTest {

    @Test
    @Parameters(method = "testsData")
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

    /**
     * "..." - описание промежутка между точками.
     * "M" - описание промежутка, модифицирующего существующие цены.
     * "A,B,C" - описание существующих цен.
     *
     * "(B-100" - описание начала промежутка со значением стоимости.
     * "B)" - описание окончания промежутка.
     *
     * FIXME Считаю, что тестировать отношения с большим количеством участников, чем 2 начальных и 1 модифицирующий, не имеет смысла. Т.к. Все случаи с большим количеством участников можно разложить на более малые.
     */
    private Object testsData() {
        return new Object[]{
                new Object[]{"...(M-100...M)...",
                        Arrays.asList(),
                        Arrays.asList(PriceRule.of(100L, 200L, 100L)),
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(200L, null))},
                new Object[]{"...(A-100...A)...(M-500...M)...",
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(200L, null)),
                        Arrays.asList(PriceRule.of(300L, 400L, 500L)),
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(200L, null), Pair.of(300L, 500L), Pair.of(400L, null))},
                new Object[]{"...(M-500...M)...(A-100...A)...",
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(200L, null)),
                        Arrays.asList(PriceRule.of(50L, 90L, 500L)),
                        Arrays.asList(Pair.of(50L, 500L), Pair.of(90L, null), Pair.of(100L, 100L), Pair.of(200L, null))},
                new Object[]{"...(M-500...(A-100...M)...A)...",
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(200L, null)),
                        Arrays.asList(PriceRule.of(50L, 150L, 500L)),
                        Arrays.asList(Pair.of(50L, 500L), Pair.of(150L, 100L), Pair.of(200L, null))},
                new Object[]{"...(A-100...(M-500...A)...M)...",
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(200L, null)),
                        Arrays.asList(PriceRule.of(150L, 250L, 500L)),
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(150L, 500L), Pair.of(250L, null))},
                new Object[]{"...(M-100...(A-100...M)...A)...",
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(200L, null)),
                        Arrays.asList(PriceRule.of(50L, 150L, 100L)),
                        Arrays.asList(Pair.of(50L, 100L), Pair.of(200L, null))},
                new Object[]{"...(A-100...(M-100...A)...M)...",
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(200L, null)),
                        Arrays.asList(PriceRule.of(150L, 250L, 100L)),
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(250L, null))},
                new Object[]{"...(A-100...(M-100...M)...A)...",
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(200L, null)),
                        Arrays.asList(PriceRule.of(150L, 180L, 100L)),
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(200L, null))},
                new Object[]{"...(M-100...(A-100...A)...M)...",
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(200L, null)),
                        Arrays.asList(PriceRule.of(150L, 180L, 100L)),
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(200L, null))},
                new Object[]{"...(A-100...A)...(M-150...M)...(B-200...B)...",
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(200L, null), Pair.of(300L, 200L), Pair.of(400L, null)),
                        Arrays.asList(PriceRule.of(220L, 280L, 150L)),
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(200L, null), Pair.of(220L, 150L), Pair.of(280L, null), Pair.of(300L, 200L), Pair.of(400L, null))},
                new Object[]{"...(A-100...(M-150...A)...(B-200...M)...B)...",
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(200L, null), Pair.of(300L, 200L), Pair.of(400L, null)),
                        Arrays.asList(PriceRule.of(150L, 350L, 150L)),
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(150L, 150L), Pair.of(350L, 200L), Pair.of(400L, null))},
                new Object[]{"...(A-100...(M-100...A)...(B-100...M)...B)...",
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(200L, null), Pair.of(300L, 100L), Pair.of(400L, null)),
                        Arrays.asList(PriceRule.of(150L, 350L, 100L)),
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(400L, null))},
                new Object[]{"...(A-100...(M-100...A)(B-100...M)...B)...",
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(200L, 200L), Pair.of(300L, null)),
                        Arrays.asList(PriceRule.of(150L, 250L, 150L)),
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(150L, 150L), Pair.of(250L, 200L), Pair.of(300L, null))},
                new Object[]{"...(A-100...(M-150...A)(B-100...B)...M)...",
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(200L, 200L), Pair.of(300L, null)),
                        Arrays.asList(PriceRule.of(150L, 500L, 150L)),
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(150L, 150L), Pair.of(500L, null))},
                new Object[]{"...(A-100...(M-100...A)(B-100...M)B)...",
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(200L, null), Pair.of(300L, 100L), Pair.of(400L, null)),
                        Arrays.asList(PriceRule.of(150L, 400L, 100L)),
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(400L, null))},
                new Object[]{"...(A-100(M-150...A)(B-200...M)B)...",
                        Arrays.asList(Pair.of(100L, 100L), Pair.of(200L, null), Pair.of(300L, 200L), Pair.of(400L, null)),
                        Arrays.asList(PriceRule.of(100L, 400L, 150L)),
                        Arrays.asList(Pair.of(100L, 150L), Pair.of(400L, null))},
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