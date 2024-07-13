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
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {

        public TextView tvCarName;
        public TextView tvCarBrand;
        public TextView tvCarPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCarName = itemView.findViewById(R.id.tvCarName);
            tvCarBrand = itemView.findViewById(R.id.tvCarBrand);
            tvCarPrice = itemView.findViewById(R.id.tvCarPrice);

            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }
        @Override
        public boolean onLongClick(View v) {
            currentPos = getAdapterPosition();
            return false;
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.onItemClick(getAdapterPosition());
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    //////////////////////////////////////////////////////////////////////
    // adapter class definitions

    private List<Car> carsListData;   // list of book objects
    private Context mContext;   // activity context
    private int currentPos;
    private OnItemClickListener clickListener;

    public CarAdapter(Context context, List<Car> listData, OnItemClickListener listener) {
        carsListData = listData;
        mContext = context;
        clickListener = listener;
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