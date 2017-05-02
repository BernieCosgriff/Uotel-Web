package uotel;

public class UsefulFeedback extends Feedback {

    double rating;

    UsefulFeedback(int id,
                   int userId,
                   String userLogin,
                   Property property,
                   java.sql.Date date,
                   int score,
                   String comment,
                   double rating) {
        super(id, userId, userLogin, property, date, score, comment);
        this.rating = rating;
    }

    @Override
    public String toString() {
        String s = "Average Rating: " + rating + '\n';
        s += super.toString();
        return s;
    }
}
