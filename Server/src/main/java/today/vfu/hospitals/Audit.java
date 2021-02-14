package today.vfu.hospitals;

import java.util.Date;

public class Audit {

    private Department oldState;
    private Department newState;

    private Date dateOfChange;
    private Long dateInMils;

    private String userChanging;

    private String changedDepartament;

    Audit() {
        autoSetDates();
    }

    /**
     * Crates a new Audit with
     *
     * @param old  the old state of the department
     * @param niu  the new state of the department
     * @param user the person who made the change
     */
    public Audit(Department old, Department niu, String user) {
        autoSetDates();
        oldState = old;
        newState = niu;
        userChanging = user;
        changedDepartament = niu.getName();

    }

    public void hideUser() {
        this.userChanging = null;
    }

    public void autoSetDates() {
        dateOfChange = new Date();
        dateInMils = System.currentTimeMillis();
    }

    public Department getOldState() {
        return oldState;
    }

    public Department getNewState() {
        return newState;
    }

    public Date getDateOfChange() {
        return dateOfChange;
    }

    public Long getDateInMils() {
        return dateInMils;
    }

    public String getChangedDepartament() {
        return changedDepartament;
    }


}
