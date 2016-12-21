package DBAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shrikantbadwaik.needahelp.R;

import java.util.ArrayList;

import DBClasses.MyReports;

/**
 * Created by Deadpool on 26/07/2016.
 */
public class ReportsAdapter extends ArrayAdapter<MyReports> {

    Context context;
    ArrayList<MyReports> myReportsArrayList = new ArrayList<>();
    MyReports myReports;
    private TextView getTitle, getDesc, getLatitude, getLongitude, getStatus;

    public ReportsAdapter(Context context, ArrayList<MyReports> myReportsArrayList) {
        super(context, 0);

        this.context = context;
        this.myReportsArrayList = myReportsArrayList;
    }

    @Override
    public int getCount() {
        return myReportsArrayList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout layout = null;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (LinearLayout) layoutInflater.inflate(R.layout.activity_adapter_reports, null);
        } else {
            layout = (LinearLayout) convertView;
        }

        myReports=myReportsArrayList.get(position);

        getTitle = (TextView) layout.findViewById(R.id.getTitle);
        getTitle.setText(myReports.getReportname());

        getDesc = (TextView) layout.findViewById(R.id.getDesc);
        getDesc.setText(myReports.getReportdescription());

        getLatitude = (TextView) layout.findViewById(R.id.getLatitude);
        getLatitude.setText(myReports.getLatitude());

        getLongitude = (TextView) layout.findViewById(R.id.getLongitude);
        getLongitude.setText(myReports.getLongitude());

        getStatus = (TextView) layout.findViewById(R.id.getStatus);
        getStatus.setText(myReports.getReportstatus());

        if ((position % 2) == 0) {
            layout.setBackgroundColor(Color.parseColor("#BEE9F7"));
        } else {
            layout.setBackgroundColor(Color.parseColor("#fbfbfb"));
        }

        return layout;
    }
}
