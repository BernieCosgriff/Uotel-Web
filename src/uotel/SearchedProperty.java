package uotel;

import java.math.BigDecimal;
import java.sql.Date;

public class SearchedProperty extends Property {
    public Date startDate;
    public Date endDate;
    public BigDecimal pricePerNight;
    public String avgRating;

    public SearchedProperty(int id,
                            String name,
                            int ownerId,
                            Address address,
                            Category category,
                            String phoneNumber,
                            String url,
                            String yearBuilt,
                            Date startDate,
                            Date endDate,
                            BigDecimal pricePerNight,
                            String avgRating) {
        super(id, name, ownerId, address, category, phoneNumber, url, yearBuilt);
        this.startDate = startDate;
        this.endDate = endDate;
        this.pricePerNight = pricePerNight;
        this.avgRating = avgRating;
    }

    @Override
    public String toString() {
        String avgRatingStr = avgRating != null ? avgRating : "This property has no feedback yet.";
        String s = super.toString();
        s += "Listing/Availability Start Date: " + startDate + "\n";
        s += "Listing/Availability End Date: " + endDate + "\n";
        s += "Listing Price Per Night: " + pricePerNight + "\n";
        s += "Average Feedback Rating: " + avgRatingStr + "\n";
        return s;
    }
}
