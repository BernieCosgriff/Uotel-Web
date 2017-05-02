package uotel;

import java.math.BigDecimal;

public class Stay {
    public final Reservation reservation;
    public final BigDecimal totalCost;

    public Stay(Reservation reservation,
                BigDecimal totalCost) {
        this.reservation = reservation;
        this.totalCost = totalCost;
    }

    @Override
    public String toString() {
        String s = "Property ID: " + reservation.property.id + "\n";
        s += "Property Name: " + reservation.property.name + "\n";
        s += "Property Address: " + reservation.property.address + "\n";
        s += "Start Date: " + reservation.startDate + "\n";
        s += "End Date: " + reservation.endDate + "\n";
        s += "Total Cost: " + totalCost + "\n";
        return s;
    }

    public static class Order {
        public int reservationId;
        public BigDecimal totalCost;

        @Override
        public String toString() {
            String s = "Reservation ID: " + reservationId + "\n";
            s += "Total Cost: " + totalCost + "\n";
            return s;
        }
    }
}
