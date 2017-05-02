package uotel;

import java.sql.Date;

public class Reservation {
    public final int id;
    public final int userId;
    public final Property property;
    public final Date startDate;
    public final Date endDate;
    public final int guests;

    public Reservation(int id,
                       int userId,
                       Property property,
                       Date startDate,
                       Date endDate,
                       int guests) {
        this.id = id;
        this.userId = userId;
        this.property = property;
        this.startDate = startDate;
        this.endDate = endDate;
        this.guests = guests;
    }

    @Override
    public String toString() {
        String s = "ID: " + id + "\n";
        s += "Property Name: " + property.name + "\n";
        s += "Start Date: " + startDate + "\n";
        s += "End Date: " + endDate + "\n";
        s += "Guests: " + guests + "\n";
        return s;
    }

    public static class Order {
        public int userId;
        public int propertyId;
        public Date startDate;
        public Date endDate;
        public int guests;

        @Override
        public String toString() {
            String s = "Property ID: " + propertyId + "\n";
            s += "Start Date: " + startDate + "\n";
            s += "End Date: " + endDate + "\n";
            s += "Guests: " + guests + "\n";
            return s;
        }
    }

}
