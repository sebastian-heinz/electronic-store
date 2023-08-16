package org.example.discount.deal;

import org.example.discount.DiscountDeal;
import org.example.discount.DiscountDealType;
import org.example.entity.BillEntry;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class BuyTen_GetOne implements DiscountDeal {

    @Override
    public DiscountDealType getType() {
        return DiscountDealType.BuyTen_GetOne;
    }

    @Override
    public Optional<BillEntry> apply(BillEntry billEntry, List<BillEntry> allEntries) {

        if (billEntry.getQuantity() >= 10) {
            BillEntry discount = new BillEntry(
                    billEntry.getProductId(),
                    billEntry.getName() + " Deal (buy 10, get one)",
                    1,
                    BigDecimal.ZERO
            );
            return Optional.of(discount);
        }
        return Optional.empty();
    }
}
