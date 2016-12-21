package DBAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shrikantbadwaik.needahelp.R;

import java.util.ArrayList;

import DBClasses.PhoneDirectory;

/**
 * Created by Deadpool on 15/07/2016.
 */
public class DirectoryAdapter extends ArrayAdapter<String> {

    Context context;
    ArrayList<PhoneDirectory>directoryList;

    TextView textSrNo,textLocation,textContactNo;
    ImageButton btnMakeCall;

    public DirectoryAdapter(Context context,ArrayList<PhoneDirectory> directoryList) {
        super(context, 0);
        this.context = context;
        this.directoryList = directoryList;
    }

    @Override
    public int getCount() {
        return directoryList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout layout = null;
        if(convertView==null)
        {
            LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout=(LinearLayout)layoutInflater.inflate(R.layout.activity_adapter_directory,null);
        }
        else
        {
            layout = (LinearLayout) convertView;
        }

        PhoneDirectory directory = directoryList.get(position);

        textSrNo=(TextView)layout.findViewById(R.id.textSrNum);
        textSrNo.setText(""+directory.getSrno());

        textLocation=(TextView)layout.findViewById(R.id.textLocation);
        textLocation.setText(directory.getLocationName());

        textContactNo=(TextView)layout.findViewById(R.id.textContactNo);
        textContactNo.setText(directory.getContactNumber());

        btnMakeCall=(ImageButton)layout.findViewById(R.id.btnMakeCall);

        return layout;
    }
}
