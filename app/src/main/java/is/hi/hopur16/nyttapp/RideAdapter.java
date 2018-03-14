package is.hi.hopur16.nyttapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by atliharaldsson on 14/03/2018.
 */

public class RideAdapter extends ArrayAdapter {
    List list = new ArrayList();
    public RideAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }




    public void add(Ride object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row;
        row = convertView;
        RideHolder rideHolder;
        if(row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.row_layout,parent,false);
            rideHolder = new RideHolder();
            rideHolder.tx_from = (TextView) row.findViewById(R.id.tx_from);
            rideHolder.tx_to = (TextView) row.findViewById(R.id.tx_to);
            rideHolder.tx_date = (TextView) row.findViewById(R.id.tx_date);
            rideHolder.tx_deptime = (TextView) row.findViewById(R.id.tx_deptime);
            rideHolder.tx_cost = (TextView) row.findViewById(R.id.tx_cost);
            rideHolder.tx_seats = (TextView) row.findViewById(R.id.tx_seats);
            row.setTag(rideHolder);
        }
        else {
            rideHolder = (RideHolder) row.getTag();
        }
        Ride ride = (Ride) this.getItem(position);
        rideHolder.tx_from.setText(ride.getRidefrom());
        rideHolder.tx_to.setText(ride.getRideto());
        rideHolder.tx_date.setText(ride.getDate());
        rideHolder.tx_deptime.setText(ride.getDeptime());
        rideHolder.tx_cost.setText(String.valueOf(ride.getCost()));
        rideHolder.tx_seats.setText(String.valueOf(ride.getSeatsavailable()));

        return row;
    }

    static class RideHolder {
        TextView tx_from, tx_to,tx_date,tx_cost,tx_seats,tx_deptime;
    }
}
