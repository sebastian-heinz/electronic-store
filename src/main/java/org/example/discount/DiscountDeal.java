package org.example.discount;

import org.example.entity.BillEntry;

import java.util.List;
import java.util.Optional;

public interface DiscountDeal {
    DiscountDealType getType();

    Optional<BillEntry> apply(BillEntry billEntry, List<BillEntry> allEntries);
}
