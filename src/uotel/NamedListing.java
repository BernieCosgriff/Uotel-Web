package uotel;

import java.math.BigDecimal;
import java.sql.Date;

public class NamedListing extends Listing {
    public final String propertyName;

    public NamedListing(int propertyId,
                        String propertyName,
                        Date startDate,
                        Date endDate,
                        BigDecimal pricePerNight) {
        super(propertyId, startDate, endDate, pricePerNight);
        this.propertyName = propertyName;
    }

    @Override
    public String toString() {
        String s = "Property ID: " + propertyId + "\n";
        s += "Property Name: " + propertyName + "\n";
        s += "Start Date: " + startDate.toString() + "\n";
        s += "End Date : " + endDate.toString() + "\n";
        s += "Price-per-Night: " + pricePerNight.toString() + "\n";
        return s;
    }
}
