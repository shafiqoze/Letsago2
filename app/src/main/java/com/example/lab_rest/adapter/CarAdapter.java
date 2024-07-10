package com.example.lab_rest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.lab_rest.R;
import com.example.lab_rest.model.Car;

import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.ViewHolder> {

    /**
     * Create ViewHolder class to bind list item view
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvCarName;
        public TextView tvCarBrand;
        public TextView tvCarPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCarName = itemView.findViewById(R.id.tvCarName);
            tvCarBrand = itemView.findViewById(R.id.tvCarBrand);
            tvCarPrice = itemView.findViewById(R.id.tvCarPrice);
        }
    }



    //////////////////////////////////////////////////////////////////////
    // adapter class definitions

    private List<Car> carsListData;   // list of book objects
    private Context mContext;   // activity context
    private int currentPos;



    public CarAdapter(Context context, List<Car> listData) {
        carsListData = listData;
        mContext = context;
    }

    private Context getmContext() {
        return mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate layout using the single item layout
        View view = inflater.inflate(R.layout.car_list_item, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // bind data to the view holder instance
        Car m = carsListData.get(position);
        holder.tvCarName.setText(m.getCarName());
        holder.tvCarBrand.setText(m.getCarBrand());
        holder.tvCarPrice.setText(m.getCarPrice());
    }

    @Override
    public int getItemCount() {
        return carsListData.size();
    }

    /**
     * return car object for currently selected car (index already set by long press in viewholder)
     * @return
     */
    public Car getSelectedItem() {
        // return the car record if the current selected position/index is valid
        if(currentPos>=0 && carsListData !=null && currentPos<carsListData.size()) {
            return carsListData.get(currentPos);
        }
        return null;
    }
}