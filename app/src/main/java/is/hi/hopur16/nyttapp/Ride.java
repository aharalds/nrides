package is.hi.hopur16.nyttapp;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by atliharaldsson on 14/02/2018.
 */

public class Ride implements Serializable {

    public String rideFrom;
    public String rideTo;
    public String date;
    public String depTime;
    public int seatsAvailable;
    public int cost;

    public String getRideFrom() {
        return rideFrom;
    }

    public void setRideFrom(String rideFrom) {
        this.rideFrom = rideFrom;
    }

    public String getRideTo() {
        return rideTo;
    }

    public void setRideTo(String rideTo) {
        this.rideTo = rideTo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDepTime() {
        return depTime;
    }

    public void setDepTime(String depTime) {
        this.depTime = depTime;
    }

    public int getSeatsAvailable() {
        return seatsAvailable;
    }

    public void setSeatsAvailable(int seatsAvailable) {
        this.seatsAvailable = seatsAvailable;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public Ride(String rideFrom, String rideTo, String date, String depTime, int seatsAvailable, int cost) {
        this.rideFrom = rideFrom;
        this.rideTo = rideTo;
        this.date = date;
        this.depTime = depTime;
        this.seatsAvailable = seatsAvailable;
        this.cost = cost;
    }


}
