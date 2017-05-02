package uotel;

public class SuggestedProperty extends Property {

    // Visit count by users who also stayed in a reserved property.
    public int visitCount;

    // The reservation this suggested property is based on.
    public int reservationPropId;

    public SuggestedProperty(int id,
                             String name,
                             int ownerId,
                             Address address,
                             Category category,
                             String phoneNumber,
                             String url,
                             String yearBuilt,
                             int visitCount,
                             int reservationPropId) {
        super(id, name, ownerId, address, category, phoneNumber, url, yearBuilt);
        this.visitCount = visitCount;
        this.reservationPropId = reservationPropId;
    }

    @Override
    public String toString() {
        String s = super.toString();
        s += "Visit Count (by users who also stayed in " + reservationPropId + "): " + visitCount + "\n";
        return s;
    }
}
