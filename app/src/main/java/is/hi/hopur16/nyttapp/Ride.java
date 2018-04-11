package is.hi.hopur16.nyttapp;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by atliharaldsson on 14/02/2018.
 */

public class Ride implements Serializable {

    public String username;
    public String phone;
    public String email;
    public String ridefrom;
    public String rideto;
    public String date;
    public String deptime;
    public int seatsavailable;
    public int cost;
    public int id;

    public String getRidefrom() {
        return ridefrom;
    }

    public void setRidefrom(String rideFrom) {
        this.ridefrom = rideFrom;
    }

    public String getRideto() {
        return rideto;
    }

    public void setRideTo(String rideTo) {
        this.rideto = rideTo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDeptime() {
        return deptime;
    }

    public void setDeptime(String depTime) {
        this.deptime = depTime;
    }

    public int getSeatsavailable() {
        return seatsavailable;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setSeatsavailable(int seatsAvailable) {
        this.seatsavailable = seatsAvailable;
    }

    public int getCost() {
        return cost;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRideto(String rideto) {
        this.rideto = rideto;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public Ride(String rideFrom, String rideTo, String date, String depTime, int seatsAvailable, int cost,
                String username, String phone, String email) {
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.ridefrom = rideFrom;
        this.rideto = rideTo;
        this.date = date;
        this.deptime = depTime;
        this.seatsavailable = seatsAvailable;
        this.cost = cost;
    }

    public Ride(int id, String rideFrom, String rideTo, String date, String depTime, int seatsAvailable, int cost,
                String username, String phone, String email) {
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.ridefrom = rideFrom;
        this.rideto = rideTo;
        this.date = date;
        this.deptime = depTime;
        this.seatsavailable = seatsAvailable;
        this.cost = cost;
        this.id = id;
    }

}
