package com.example.lab_rest.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.lab_rest.R;
import com.example.lab_rest.UpdateRentalActivity;
import com.example.lab_rest.model.Bookings;

import java.util.ArrayList;
import java.util.List;

public class RentalAdapter extends RecyclerView.Adapter<RentalAdapter.ViewHolder> {

    private List<Bookings> bookingsListData = new ArrayList<>();
    private Context mContext;
    private OnBookingClickListener onBookingClickListener;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public RentalAdapter(Context context, List<Bookings> listData, OnBookingClickListener listener) {
        bookingsListData = listData != null ? listData : new ArrayList<>();
        mContext = context;
        onBookingClickListener = listener;
    }

    public void setData(List<Bookings> bookings) {
        bookingsListData = bookings != null ? bookings : new ArrayList<>();
        notifyDataSetChanged();
    }

    public interface OnBookingClickListener {
        void onUpdateClick(Bookings booking);
        void onDeleteClick(Bookings booking);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.rental_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Bookings m = bookingsListData.get(position);
        holder.tvCustomerName.setText(m.getCustName());
        holder.tvCarid.setText(String.valueOf(m.getCarId()));
        holder.tvPickupdate.setText(m.getPickupDate());
        holder.tvReturndate.setText(m.getReturnDate());
        holder.tvRemarks.setText(m.getRemarks());

        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = holder.getAdapterPosition();
                Intent intent = new Intent(mContext, UpdateRentalActivity.class);
                intent.putExtra("booking_id", m.getId());
                intent.putExtra("customer_name", m.getCustName());
                intent.putExtra("car_id", m.getCarId());
                intent.putExtra("pickup_date", m.getPickupDate());
                intent.putExtra("return_date", m.getReturnDate());
                intent.putExtra("remarks", m.getRemarks());
                intent.putExtra("status", m.getStatus());
                mContext.startActivity(intent);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onBookingClickListener != null) {
                    onBookingClickListener.onDeleteClick(m);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookingsListData.size();
    }

    public Bookings getItem(int position) {
        if (position < 0 || position >= bookingsListData.size()) {
            return null;
        }
        return bookingsListData.get(position);
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvCustomerName;
        public TextView tvCarid;
        public TextView tvPickupdate;
        public TextView tvReturndate;
        public TextView tvRemarks;
        public Button btnUpdate;
        public Button btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvCarid = itemView.findViewById(R.id.tvCarid);
            tvPickupdate = itemView.findViewById(R.id.tvPickupdate);
            tvReturndate = itemView.findViewById(R.id.tvReturndate);
            tvRemarks = itemView.findViewById(R.id.tvRemarks);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
