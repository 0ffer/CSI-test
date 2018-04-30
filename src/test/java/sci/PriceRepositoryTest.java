package sci;

import org.junit.Test;

import java.time.Instant;
import java.util.Date;

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
        expected.setId(1);
        expected.setCode("122856");
        expected.setNumber(2);
        expected.setDepartment(3);
        expected.getTimeRules().put(Instant.ofEpochMilli(10_000), 11000L);
        expected.getTimeRules().put(Instant.ofEpochMilli(20_000), null);

        assertEquals(1, repo.getPrices().size());
        assertEquals(1, repo.getPrices().get(0).getId());
        assertEquals("122856", repo.getPrices().get(0).getCode());
        assertEquals(2, repo.getPrices().get(0).getNumber());
        assertEquals(3, repo.getPrices().get(0).getDepartment());
        assertEquals(expected.getTimeRules(), repo.getPrices().get(0).getTimeRules());
    }

    @Test
    public void toExternalSimple() {
        final PriceRepository repo = new PriceRepository();

        // TODO Проработать тест!

        final PriceInfo priceInfo = new PriceInfo();
        priceInfo.setId(1);
        priceInfo.setCode("122856");
        priceInfo.setNumber(2);
        priceInfo.setDepartment(3);
        priceInfo.getTimeRules().put(Instant.ofEpochMilli(10_000), 11000L);
        priceInfo.getTimeRules().put(Instant.ofEpochMilli(20_000), null);
//        repo.getPrices().put()

        final PriceDto expected = new PriceDto();
        expected.setId(1);
        expected.setProductCode("122856");
        expected.setNumber(2);
        expected.setDepart(3);
        expected.setBegin(new Date(10_000));
        expected.setEnd(new Date(20_000));
        expected.setValue(11000);

//        assertEquals(1, priceInfo.toExternalFormat().size());
//        assertEquals(expected, priceInfo.toExternalFormat().get(0));
    }

}