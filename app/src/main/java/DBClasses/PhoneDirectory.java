package DBClasses;

/**
 * Created by Deadpool on 15/07/2016.
 */
public class PhoneDirectory {
    private int srno;
    private String locationName;
    private String contactNumber;

    public PhoneDirectory() {
    }

    public PhoneDirectory(int srno, String locationName, String contactNumber) {
        this.srno = srno;
        this.locationName = locationName;
        this.contactNumber = contactNumber;
    }

    public int getSrno() {
        return srno;
    }

    public void setSrno(int srno) {
        this.srno = srno;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}
