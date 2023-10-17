package onetomany.Users;

public class Event {

    //event id
    private int eId;

    //name of event
    private String name;

    //type of event (ex. course, study, sleep, activity)
    private String eType;

    //start time
    private double start;

    //end time
    private double end;
/*<================================ Constructor ================================>*/

    public Event(int eId, String name, String eType, double start, double end) {
        this.eId = eId;
        this.name = name;
        this.eType = eType;
        this.start = start;
        this.end = end;
    }
    public Event(){};

/*<================================ Getters and Setters ================================>*/

    public int geteId() {
        return eId;
    }

    public void seteId(int eId) {
        this.eId = eId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String geteType() {
        return eType;
    }

    public void seteType(String eType) {
        this.eType = eType;
    }

    public double getStart() {
        return start;
    }

    public void setStart(double start) {
        this.start = start;
    }

    public double getEnd() {
        return end;
    }

    public void setEnd(double end) {
        this.end = end;
    }
}
