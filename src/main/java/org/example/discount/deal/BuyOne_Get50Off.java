package org.example.discount.deal;

import org.example.discount.DiscountDeal;
import org.example.discount.DiscountDealType;
import org.example.entity.BillEntry;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class BuyOne_Get50Off implements DiscountDeal {

    @Override
    public DiscountDealType getType() {
        return DiscountDealType.BuyOne_Get50Off;
    }

    @Override
    public Optional<BillEntry> apply(BillEntry billEntry, List<BillEntry> allEntries) {

        if (billEntry.getQuantity() > 1) {
            BigDecimal discountPrice = billEntry.getPrice().divide(new BigDecimal(2)).negate();
            BillEntry discount = new BillEntry(
                    billEntry.getProductId(),
                    billEntry.getName() + " Deal (buy 1, get 50% off next)",
                    1,
                    discountPrice
            );
            return Optional.of(discount);
        }
        return Optional.empty();
    }
}
