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
    private Context mContext;       // activity context

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
        return new ViewHolder(view);
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
}