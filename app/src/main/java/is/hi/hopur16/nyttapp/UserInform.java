package is.hi.hopur16.nyttapp;

/**
 * Created by atliharaldsson on 04/04/2018.
 */

public class UserInform {

    public User currUser = null;

    public void setCurrUser(User user) {
        currUser = user;
    }

    public User getCurrUser() {
        return currUser;
    }

    public Boolean isCurrUser() {
        if (currUser != null) {
            return true;
        } else {
            return false;
        }
    }
}