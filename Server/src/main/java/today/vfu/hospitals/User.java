package today.vfu.hospitals;


public class User {

    private String id;

    private String email;

    private String hospital;

    /**
     * Constructor:
     * used when generating new user from backend.
     * In most other cases the user is pulled from the database.
     */
    public User(String email, String hospital, String id) {
        this.email = email;
        this.hospital = hospital;
        this.id = id;

    }

    /**
     * Overloaded constructor:
     * avoids crashes, though scarcely used.
     */
    public User() {

    }


    /**
     * toString primarily for debugging
     */
    @Override
    public String toString() {
        return email + " belongs to " + hospital;
    }

    /**
     * Email is used as primary identification.
     * Since we use Google authentication we are
     * guaranteed that no collisions will happen.
     *
     * @return
     */
    public String getEmail() {
        return email;
    }


    /**
     * Gives the hospital the user is assigned to
     * important when dealing with permissions and access
     */
    public String getHospital() {
        return hospital;
    }

    String getId() {
        return id;
    }
}
