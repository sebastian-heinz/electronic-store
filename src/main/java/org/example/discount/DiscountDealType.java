package org.example.discount;

public enum DiscountDealType {
    BuyOne_Get50Off("BuyOne_Get50Off"),
    BuyTen_GetOne("BuyTen_GetOne"),
    ;

    private final String stringValue;

    DiscountDealType(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public static DiscountDealType getType(String stringValue) {
        for (DiscountDealType i : DiscountDealType.values()) {
            if (i.getStringValue().equals(stringValue)) {
                return i;
            }
        }
        return null;
    }
}
