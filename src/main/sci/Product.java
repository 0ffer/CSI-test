package sci;

import java.time.LocalDateTime;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.TreeMap;

public class Product {
    String code;
    int number;
    int department;

    NavigableMap<LocalDateTime, Integer> prices = new TreeMap<>();

    public void takeRule(final LocalDateTime startTime, final LocalDateTime endTime, long value) {
        // TODO Здесь вообще все тестовое задание должно быть!
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;

        Product product = (Product) o;
        return number == product.number &&
                department == product.department &&
                Objects.equals(code, product.code) &&
                Objects.equals(prices, product.prices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, number, department);
    }
}
