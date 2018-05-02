package sci;

import org.junit.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class PriceRepositoryTest {

    @Test
    public void fromExternal() {
        final PriceRepository repo = new PriceRepository();

        final PriceDto exFormat = new PriceDto();
        exFormat.setId(1);
        exFormat.setProductCode("122856");
        exFormat.setNumber(2);
        exFormat.setDepart(3);
        exFormat.setBegin(new Date(10_000));
        exFormat.setEnd(new Date(20_000));
        exFormat.setValue(11000);
        repo.processPriceDto(exFormat);

        final PriceInfo expected = new PriceInfo();
        expected.setCode("122856");
        expected.setNumber(2);
        expected.setDepartment(3);
        expected.getTimeRules().put(Instant.ofEpochMilli(10_000), 11000L);
        expected.getTimeRules().put(Instant.ofEpochMilli(20_000), null);

        assertEquals(1, repo.getPrices().size());
        PriceInfo priceInfo = repo.getPrices().values().stream().findFirst().get();
        assertEquals("122856", priceInfo.getCode());
        assertEquals(2, priceInfo.getNumber());
        assertEquals(3, priceInfo.getDepartment());
        assertEquals(expected.getTimeRules(), priceInfo.getTimeRules());
    }

    @Test
    public void toExternalSimple() {
        final PriceRepository repo = new PriceRepository();

        PriceInfo priceInfo = new PriceInfo();
        priceInfo.setCode("122856");
        priceInfo.setNumber(2);
        priceInfo.setDepartment(3);
        priceInfo.getTimeRules().put(Instant.ofEpochSecond(10), 11000L);
        priceInfo.getTimeRules().put(Instant.ofEpochSecond(20), null);
        priceInfo.getTimeRules().put(Instant.ofEpochSecond(30), 99000L);
        priceInfo.getTimeRules().put(Instant.ofEpochSecond(40), null);
        repo.getPrices().put(1, priceInfo);

        priceInfo = new PriceInfo();
        priceInfo.setCode("6654");
        priceInfo.setNumber(1);
        priceInfo.setDepartment(1);
        priceInfo.getTimeRules().put(Instant.ofEpochSecond(11), 11300L);
        priceInfo.getTimeRules().put(Instant.ofEpochSecond(33), 99100L);
        priceInfo.getTimeRules().put(Instant.ofEpochSecond(44), null);
        repo.getPrices().put(2, priceInfo);

        final List<PriceDto> expectedResult = new ArrayList<>();
        PriceDto expectedPriceDto = new PriceDto();
        expectedPriceDto.setProductCode("122856");
        expectedPriceDto.setNumber(2);
        expectedPriceDto.setDepart(3);
        expectedPriceDto.setBegin(new Date(10_000));
        expectedPriceDto.setEnd(new Date(20_000));
        expectedPriceDto.setValue(11000);
        expectedResult.add(expectedPriceDto);

        expectedPriceDto = new PriceDto();
        expectedPriceDto.setProductCode("122856");
        expectedPriceDto.setNumber(2);
        expectedPriceDto.setDepart(3);
        expectedPriceDto.setBegin(new Date(30_000));
        expectedPriceDto.setEnd(new Date(40_000));
        expectedPriceDto.setValue(99000);
        expectedResult.add(expectedPriceDto);

        expectedPriceDto = new PriceDto();
        expectedPriceDto.setProductCode("6654");
        expectedPriceDto.setNumber(1);
        expectedPriceDto.setDepart(1);
        expectedPriceDto.setBegin(new Date(11_000));
        expectedPriceDto.setEnd(new Date(33_000));
        expectedPriceDto.setValue(11300);
        expectedResult.add(expectedPriceDto);

        expectedPriceDto = new PriceDto();
        expectedPriceDto.setProductCode("6654");
        expectedPriceDto.setNumber(1);
        expectedPriceDto.setDepart(1);
        expectedPriceDto.setBegin(new Date(33_000));
        expectedPriceDto.setEnd(new Date(44_000));
        expectedPriceDto.setValue(99100);
        expectedResult.add(expectedPriceDto);

        final List<PriceDto> actualResult = repo.toDto();

        assertEquals(expectedResult.size(), actualResult.size());
        expectedResult.forEach(priceDto -> assertTrue(actualResult.contains(priceDto)));
    }

}