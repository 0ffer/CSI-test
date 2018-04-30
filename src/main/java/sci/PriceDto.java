package sci;

import java.util.Date;
import java.util.Objects;

/**
 * Модель внешнего представления ценовых правил.
 */
public class PriceDto {
    private long id;
    private String productCode;
    private int number;
    private int depart;
    private Date begin;
    private Date end;
    private long value;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getProductCode() {
        return productCode;
    }
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }

    public int getDepart() {
        return depart;
    }
    public void setDepart(int depart) {
        this.depart = depart;
    }

    public Date getBegin() {
        return begin;
    }
    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }
    public void setEnd(Date end) {
        this.end = end;
    }

    public long getValue() {
        return value;
    }
    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PriceDto)) return false;
        PriceDto priceDto = (PriceDto) o;
        return id == priceDto.id &&
                number == priceDto.number &&
                depart == priceDto.depart &&
                value == priceDto.value &&
                Objects.equals(productCode, priceDto.productCode) &&
                Objects.equals(begin, priceDto.begin) &&
                Objects.equals(end, priceDto.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productCode, number, depart, begin, end, value);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PriceDto{");
        sb.append("id=").append(id);
        sb.append(", productCode='").append(productCode).append('\'');
        sb.append(", number=").append(number);
        sb.append(", depart=").append(depart);
        sb.append(", begin=").append(begin);
        sb.append(", end=").append(end);
        sb.append(", value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}
