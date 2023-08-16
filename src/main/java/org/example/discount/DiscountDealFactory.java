package org.example.discount;

import org.example.discount.deal.BuyOne_Get50Off;
import org.example.discount.deal.BuyTen_GetOne;

public class DiscountDealFactory {

    public static DiscountDeal create(DiscountDealType type) {
        switch (type) {
            case BuyOne_Get50Off -> {
                return new BuyOne_Get50Off();
            }
            case BuyTen_GetOne -> {
                return new BuyTen_GetOne();
            }
        }
        return null;
    }


}
