package sci;

import java.sql.Date;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс для хранения и конвертации цен внутреннего и внешнего форматов.
 */
final class PriceRepository {
    private final Map<Integer, PriceInfo> prices = new HashMap<>();

    /**
     * Первичная конвертация цены внешнего формата в цену внутреннего формата.
     *
     * @param priceDto Ценовое правило внешнего формата.
     * @return Представление цены во внутреннем формате.
     */
    private static PriceInfo getPriceInfoFromDto(PriceDto priceDto) {
        final PriceInfo priceInfo = new PriceInfo();
        priceInfo.setId(priceDto.getId());
        priceInfo.setCode(priceDto.getProductCode());
        priceInfo.setDepartment(priceDto.getDepart());
        priceInfo.setNumber(priceDto.getNumber());
        return priceInfo;
    }

    /**
     * Получение уникального указателя на цену.
     *
     * @param priceDto Ценовое правило внешнего формата.
     * @return Числовой указатель на цену.
     */
    private static int priceInfoHash(final PriceDto priceDto) {
        return Objects.hash(priceDto.getId(), priceDto.getProductCode(), priceDto.getDepart(), priceDto.getNumber());
    }

    /**
     * Конвертация внутреннего представления одной цены в список ценовых правил внешнего формата.
     *
     * @param priceInfo Объект цены внутреннего формата.
     * @return Список ценовых правил внешнего формата.
     */
    private static List<PriceDto> priceInfoToDto(final PriceInfo priceInfo) {
        final List<PriceDto> result = new LinkedList<>();

        PriceDto dtoWithoutEndTime = null;
        for (final Map.Entry<Instant, Long> entry : priceInfo.getTimeRules().entrySet()) {
            if (dtoWithoutEndTime != null) {
                dtoWithoutEndTime.setEnd(java.sql.Date.from(entry.getKey()));

                result.add(dtoWithoutEndTime);
                dtoWithoutEndTime = null;
            }

            if(entry.getValue() == null) {
                continue;
            }

            dtoWithoutEndTime = new PriceDto();
            dtoWithoutEndTime.setId(priceInfo.getId());
            dtoWithoutEndTime.setProductCode(priceInfo.getCode());
            dtoWithoutEndTime.setNumber(priceInfo.getNumber());
            dtoWithoutEndTime.setDepart(priceInfo.getDepartment());
            dtoWithoutEndTime.setBegin(Date.from(entry.getKey()));
            dtoWithoutEndTime.setValue(entry.getValue());
        }

        return result;
    }

    /**
     * Учет единичного ценового правила внешнего формата.
     *
     * @param priceDto Ценовое правило внешнего формата.
     */
    void processPriceDto(final PriceDto priceDto) {
        prices.getOrDefault(priceInfoHash(priceDto), getPriceInfoFromDto(priceDto)).
                addPriceRule(priceDto.getBegin().toInstant(), priceDto.getEnd().toInstant(), priceDto.getValue());
    }

    /**
     * Конвертация накопленных цен во внешнее представление.
     *
     * @return Список ценовых правил во внешнем формате.
     */
    List<PriceDto> toDto() {
        return prices.values().stream().map(PriceRepository::priceInfoToDto)
                .flatMap(Collection::stream).collect(Collectors.toList());
    }

    public Map<Integer, PriceInfo> getPrices() {
        return prices;
    }
}
