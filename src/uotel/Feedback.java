package uotel;

import java.sql.Date;

public class Feedback {
    public final int id;
    public final int userId;
    public final String userLogin;
    public final Property property;
    public final Date date;
    public final int score;
    public final String comment;

    public Feedback(int id,
                    int userId,
                    String userLogin,
                    Property property,
                    Date date,
                    int score,
                    String comment) {
        this.id = id;
        this.userId = userId;
        this.userLogin = userLogin;
        this.property = property;
        this.date = date;
        this.score = score;
        this.comment = comment;
    }

    @Override
    public String toString() {
        String s = "ID: " + id + "\n";
        s += "Rater User ID: " + userId + "\n";
        s += "Rater User Login: " + userLogin + "\n";
        s += "Property ID: " + property.id + "\n";
        s += "Property Name: " + property.name + "\n";
        s += "Property Address: " + property.address + "\n";
        s += "Date: " + date + "\n";
        s += "Score: " + score + "\n";
        s += "Comment: " + comment;
        return s;
    }
}
