package sci;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.TreeMap;

/**
 * Describe product settings and price timeline.
 */
public class Product {
    long id;
    String code;
    int number;
    int department;

    NavigableMap<Instant, Long> prices = new TreeMap<>();

    public static Product fromExternalFormat(final PriceCSI externalFormatPrice) {
        final Product result = new Product();

        result.id = externalFormatPrice.getId();
        result.code = externalFormatPrice.getProductCode();
        result.number = externalFormatPrice.getNumber();
        result.department = externalFormatPrice.getDepart();

        result.prices.put(externalFormatPrice.getBegin().toInstant(), externalFormatPrice.getValue());
        result.prices.put(externalFormatPrice.getEnd().toInstant(), null);

        return result;
    }

    public void addPriceRule(final Instant startTime, final Instant endTime, long value) {
        // TODO Здесь вообще все тестовое задание должно быть!
    }

    public List<PriceCSI> toExternalFormat() {

        // TODO realize!

        throw new RuntimeException();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;

        Product product = (Product) o;
        return id == product.id &&
                number == product.number &&
                department == product.department &&
                Objects.equals(code, product.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, number, department);
    }
}
