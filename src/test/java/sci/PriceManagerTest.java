package sci;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PriceManagerTest {

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss").withZone(ZoneOffset.UTC);

    private static Date dateFromString(final String stringValue) {
        return Date.from(LocalDateTime.parse(stringValue, dateFormatter).toInstant(ZoneOffset.UTC));
    }

    @Test
    public void testCaseFromTask() {
        // TimeZone.setDefault(TimeZone.getTimeZone("UTC")); // TODO Использовать для более читаемого вывода при отладке тестов.

        PriceDto template;

        final List<PriceDto> currentPrices = new ArrayList<>();

        template = new PriceDto();
        template.setProductCode("122856");
        template.setNumber(1);
        template.setDepart(1);
        template.setBegin(dateFromString("01.01.2013 00:00:00"));
        template.setEnd(dateFromString("31.01.2013 23:59:59"));
        template.setValue(11_000);
        currentPrices.add(template);

        template = new PriceDto();
        template.setProductCode("122856");
        template.setNumber(2);
        template.setDepart(1);
        template.setBegin(dateFromString("10.01.2013 00:00:00"));
        template.setEnd(dateFromString("20.01.2013 23:59:59"));
        template.setValue(99_000);
        currentPrices.add(template);

        template = new PriceDto();
        template.setProductCode("6654");
        template.setNumber(1);
        template.setDepart(2);
        template.setBegin(dateFromString("01.01.2013 00:00:00"));
        template.setEnd(dateFromString("31.01.2013 00:00:00"));
        template.setValue(5_000);
        currentPrices.add(template);

        final List<PriceDto> newPrices = new ArrayList<>();

        template = new PriceDto();
        template.setProductCode("122856");
        template.setNumber(1);
        template.setDepart(1);
        template.setBegin(dateFromString("20.01.2013 00:00:00"));
        template.setEnd(dateFromString("20.02.2013 23:59:59"));
        template.setValue(11_000);
        newPrices.add(template);

        template = new PriceDto();
        template.setProductCode("122856");
        template.setNumber(2);
        template.setDepart(1);
        template.setBegin(dateFromString("15.01.2013 00:00:00"));
        template.setEnd(dateFromString("25.01.2013 23:59:59"));
        template.setValue(92_000);
        newPrices.add(template);

        template = new PriceDto();
        template.setProductCode("6654");
        template.setNumber(1);
        template.setDepart(2);
        template.setBegin(dateFromString("12.01.2013 00:00:00"));
        template.setEnd(dateFromString("13.01.2013 00:00:00"));
        template.setValue(4_000);
        newPrices.add(template);

        final List<PriceDto> expectedResult = new ArrayList<>();

        template = new PriceDto();
        template.setProductCode("122856");
        template.setNumber(1);
        template.setDepart(1);
        template.setBegin(dateFromString("01.01.2013 00:00:00"));
        template.setEnd(dateFromString("20.02.2013 23:59:59"));
        template.setValue(11_000);
        expectedResult.add(template);

        template = new PriceDto();
        template.setProductCode("122856");
        template.setNumber(2);
        template.setDepart(1);
        template.setBegin(dateFromString("10.01.2013 00:00:00"));
        template.setEnd(dateFromString("15.01.2013 00:00:00"));
        template.setValue(99_000);
        expectedResult.add(template);

        template = new PriceDto();
        template.setProductCode("122856");
        template.setNumber(2);
        template.setDepart(1);
        template.setBegin(dateFromString("15.01.2013 00:00:00"));
        template.setEnd(dateFromString("25.01.2013 23:59:59"));
        template.setValue(92_000);
        expectedResult.add(template);

        template = new PriceDto();
        template.setProductCode("6654");
        template.setNumber(1);
        template.setDepart(2);
        template.setBegin(dateFromString("01.01.2013 00:00:00"));
        template.setEnd(dateFromString("12.01.2013 00:00:00"));
        template.setValue(5_000);
        expectedResult.add(template);

        template = new PriceDto();
        template.setProductCode("6654");
        template.setNumber(1);
        template.setDepart(2);
        template.setBegin(dateFromString("12.01.2013 00:00:00"));
        template.setEnd(dateFromString("13.01.2013 00:00:00"));
        template.setValue(4_000);
        expectedResult.add(template);

        template = new PriceDto();
        template.setProductCode("6654");
        template.setNumber(1);
        template.setDepart(2);
        template.setBegin(dateFromString("13.01.2013 00:00:00"));
        template.setEnd(dateFromString("31.01.2013 00:00:00"));
        template.setValue(5_000);
        expectedResult.add(template);

        final List<PriceDto> actualResult = PriceManager.mergePrices(currentPrices, newPrices);

        assertEquals(expectedResult.size(), actualResult.size());
        expectedResult.forEach(priceDto -> assertTrue(actualResult.contains(priceDto)));
    }
}