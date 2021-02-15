package today.vfu.hospitals;

public class Department {
    private String name;
    private int freebeds;
    private int occupied;
    private int total;
    private int type;
    private String hospital;

    public Department(String nam, int freeBeds, int occupied, int total,
                      String hospital, int type) {
        name = nam;
        this.freebeds = freeBeds;
        this.occupied = occupied;
        this.total = total;
        this.hospital = hospital;
        this.type = type;
    }

    public Department(String nam, int freeBeds, int occupied, int total) {
        name = nam;
        this.freebeds = freeBeds;
        this.occupied = occupied;
        this.total = total;
    }

    /**
     * Most used constructor as most of the time the
     * Department is pulled from the database
     */
    public Department() {
        name = "empty";
        freebeds = 0;
        occupied = 0;
        total = 0;
        hospital = "empty";
        type = 0;
    }

    /**
     * toString - mostly used for debugging
     */
    @Override
    public String toString() {
        return name + ": free - " + freebeds + " total " + total + " occupied" +
                " " + occupied + "belongs to " + hospital + "is a " +
                TypeIDReturn.getStringType(type) + " \n";
    }

    /**
     * @return the number of available beds in the department
     */
    public int getFreebeds() {
        return freebeds;
    }

    /**
     * @return the number of occupied beds in the department
     */
    public int getOccupied() {
        return occupied;
    }

    /**
     * @return number of all beds in the department
     */
    public int getTotal() {
        return total;
    }

    /**
     * Returns the name of the department.
     * Different from parent hospital unless
     * it's the only department.
     * Mostly used in the Audit.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the department's name. Different from parent hospital
     * unless it's the only department.
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * Returns the name of the parent hospital
     * Done so that departments may be grouped by parent hospital.
     */
    public String getHospital() {
        return hospital;
    }

    /**
     * Returns the type of department this one is.
     * A chart can be seen in TypeIDReturn
     */
    public int getType() {
        return type;
    }
}


