package uotel;

import java.math.BigDecimal;
import java.sql.Date;

public class Listing {
    public final int propertyId;
    public final Date startDate;
    public final Date endDate;
    public final BigDecimal pricePerNight;

    public Listing(int propertyId,
                   Date startDate,
                   Date endDate,
                   BigDecimal pricePerNight) {
        this.propertyId = propertyId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.pricePerNight = pricePerNight;
    }

    @Override
    public String toString() {
        String s = "Start Date: " + startDate.toString() + "\n";
        s += "End Date : " + endDate.toString() + "\n";
        s += "Price-per-Night: " + pricePerNight.toString() + "\n";
        return s;
    }
}
