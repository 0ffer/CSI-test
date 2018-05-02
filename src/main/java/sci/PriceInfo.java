package sci;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Описание цены.
 *
 * Для простоты описательной модели не произведена декомпозиция на отдельные сущности отдела, продукта и ценовых правил.
 */
final class PriceInfo {
    private String code;
    private int number;
    private int department;

    /**
     * Представление ценовых правил в виде временной линии.
     *
     * Начало определенной ценовой политики указывается как точка времени со значением новой цены.
     * Начала неопределенного ценового периода обозначается как точка времени с отсутствующей ценой.
     */
    private NavigableMap<Instant, Long> timeRules = new TreeMap<>();

    /**
     * Обработка нового ценового правила.
     *
     * Производится с цчетов предыдущих правил.
     *
     * @param startTime Начало периода действия новой цены.
     * @param endTime Окончание действия новой цены.
     * @param value Значение новой цены.
     */
    public void addPriceRule(final Instant startTime, final Instant endTime, long value) {

        /*
         * Точка окончания нового периода ставится с оглядкой на начало предыдущего перед ней периода.
         *
         * Окончание ставится, если:
         * 1. предыдущего периода не было;
         * 2. цена предыдущего перода не определена;
         * 3. цена предыдущего периода не равна цене нового периода.
         *
         * Окончание не ставится, если:
         * 1. цена предыдущего периода равна цене нового периода.
         */
        final Map.Entry<Instant, Long> endPrevPrice = timeRules.floorEntry(endTime);
        if (endPrevPrice == null) {
            timeRules.put(endTime, null);
        } else if (endPrevPrice.getValue() == null || endPrevPrice.getValue() != value) {
            timeRules.put(endTime, endPrevPrice.getValue());
        }

        /*
         * Точка начала периода ставится с оглядкой на начало предыдущего перед ней периода.
         *
         * Начало ставится, если:
         * 1. предыдущего перода не было;
         * 2. цена предыдущего периода не определена;
         * 3. цена предыдущего периода не равна цене нового.
         *
         * Начало не ставится, если:
         * 1. цена предыдущего периода равна цене нового периода.
         */
        final Map.Entry<Instant, Long> startPrevPrice = timeRules.floorEntry(startTime);
        if (startPrevPrice == null || startPrevPrice.getValue() == null || startPrevPrice.getValue() != value) {
            timeRules.put(startTime, value);
        }

        // Все промежуточные периоды всегда затираются.
        List<Instant> keysToDelete = new ArrayList<>(timeRules.subMap(startTime, false, endTime, false).keySet());
        keysToDelete.forEach(time -> timeRules.remove(time));
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

    public NavigableMap<Instant, Long> getTimeRules() {
        return timeRules;
    }
    public void setTimeRules(NavigableMap<Instant, Long> timeRules) {
        this.timeRules = timeRules;
    }
}
