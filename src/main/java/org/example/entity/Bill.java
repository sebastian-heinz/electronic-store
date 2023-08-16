package org.example.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Bill {

    private final List<BillEntry> entries;

    private final BigDecimal totalPrice;


    public Bill(List<BillEntry> entries) {
        this.entries = new ArrayList<>();

        BigDecimal tmpTotalPrice = BigDecimal.ZERO;
        for (BillEntry entry : entries) {
            this.entries.add(entry);
            BigDecimal entryTotalPrice = calcEntryTotalPrice(entry);
            tmpTotalPrice = tmpTotalPrice.add(entryTotalPrice);
        }
        totalPrice = tmpTotalPrice;
    }

    public List<BillEntry> getEntries() {
        return entries;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    private BigDecimal calcEntryTotalPrice(BillEntry entry) {
        BigDecimal entryTotalPrice = entry.getPrice().multiply(new BigDecimal(entry.getQuantity()));
        return entryTotalPrice;
    }

    public String print() {

        StringBuilder sb = new StringBuilder();
        sb.append("Product, Amount, Item Price, Total Price\r\n");
        sb.append("# --- --- --- --- ----\r\n");
        for (BillEntry entry : entries) {
            sb.append(entry.getName());
            sb.append(", ");
            sb.append(entry.getQuantity());
            sb.append(", ");
            sb.append(entry.getPrice());
            sb.append(", ");
            sb.append(calcEntryTotalPrice(entry));
            sb.append(", ");
            sb.append("\r\n");
        }
        sb.append("# --- --- --- --- ----\r\n");
        sb.append("# Total: ");
        sb.append(totalPrice);
        sb.append("\r\n");

        return sb.toString();
    }

}
