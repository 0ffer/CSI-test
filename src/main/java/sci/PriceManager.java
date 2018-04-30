package sci;

import java.util.*;

/**
 * Методы для работы с ценами.
 */
public final class PriceManager {

    /**
     * Объединение ценовых правил.
     *
     * @param currentPrices Текущие ценовые правила.
     * @param newPrices Новые ценовые правила.
     * @return Список объединенных ценовых правил.
     */
    private static List<PriceDto> mergePrices(final List<PriceDto> currentPrices, final List<PriceDto> newPrices) {
        PriceRepository repo = new PriceRepository();

        currentPrices.forEach(repo::processPriceDto);
        newPrices.forEach(repo::processPriceDto);

        return repo.toDto();
    }

}
