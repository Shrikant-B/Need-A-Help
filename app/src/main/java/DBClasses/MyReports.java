package DBClasses;

/**
 * Created by Deadpool on 26/07/2016.
 */
public class MyReports
{
    private String reportname;
    private String reportdescription;
    private String latitude;
    private String longitude;
    private String reportstatus;

    public MyReports() {
    }

    public MyReports(String reportname, String reportdescription, String latitude, String longitude, String reportstatus) {
        this.reportname = reportname;
        this.reportdescription = reportdescription;
        this.latitude = latitude;
        this.longitude = longitude;
        this.reportstatus = reportstatus;
    }

    public String getReportname() {
        return reportname;
    }

    public void setReportname(String reportname) {
        this.reportname = reportname;
    }

    public String getReportdescription() {
        return reportdescription;
    }

    public void setReportdescription(String reportdescription) {
        this.reportdescription = reportdescription;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getReportstatus() {
        return reportstatus;
    }

    public void setReportstatus(String reportstatus) {
        this.reportstatus = reportstatus;
    }
}
