package sci;

import java.sql.Date;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.TreeMap;

/**
 * Describe product settings and price timeline.
 */
public class Product {
    private long id;
    private String code;
    private int number;
    private int department;

    private NavigableMap<Instant, Long> prices = new TreeMap<>();

    public Product() {
    }

    public static Product fromExternalFormat(final PriceCSI externalFormatPrice) { // TODO tests!
        final Product result = new Product();

        result.id = externalFormatPrice.getId();
        result.code = externalFormatPrice.getProductCode();
        result.number = externalFormatPrice.getNumber();
        result.department = externalFormatPrice.getDepart();

        return result;
    }

    public void addPriceRule(final Instant startTime, final Instant endTime, long value) {
        final Map.Entry<Instant, Long> prevEndPrice = prices.lowerEntry(endTime);
        if (prevEndPrice == null) {
            prices.put(endTime, null);
        } else if (prevEndPrice.getValue() == null || prevEndPrice.getValue() != value) {
            prices.put(endTime, prevEndPrice.getValue());
        }

        final Map.Entry<Instant, Long> prevStartPrice = prices.lowerEntry(startTime);
        if (prevStartPrice == null || prevStartPrice.getValue() == null || prevStartPrice.getValue() != value) {
            prices.put(startTime, value);
        }

        prices.subMap(startTime, false, endTime, false).forEach((k, v) -> prices.remove(k)); // Удаляются все записи, попадающие внутрь нового периода.
    }

    public List<PriceCSI> toExternalFormat() { // TODO tests!
        final List<PriceCSI> result = new LinkedList<>();

        PriceCSI prev = null;
        for (final Map.Entry<Instant, Long> entry : prices.entrySet()) {
            if (prev != null) {
                prev.setEnd(Date.from(entry.getKey()));

                result.add(prev);
                prev = null;
            }

            if(entry.getValue() == null) {
                continue;
            }

            prev = getExternalFormatTemplate();
            prev.setBegin(Date.from(entry.getKey()));
            prev.setValue(entry.getValue());
        }

        return result;
    }

    private PriceCSI getExternalFormatTemplate() {
        final PriceCSI result = new PriceCSI();

        result.setId(this.id);
        result.setProductCode(this.code);
        result.setNumber(this.number);
        result.setDepart(this.department);

        return result;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }

    public int getDepartment() {
        return department;
    }
    public void setDepartment(int department) {
        this.department = department;
    }

    public NavigableMap<Instant, Long> getPrices() {
        return prices;
    }
    public void setPrices(NavigableMap<Instant, Long> prices) {
        this.prices = prices;
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
