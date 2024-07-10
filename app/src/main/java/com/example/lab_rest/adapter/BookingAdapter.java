package com.example.lab_rest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.lab_rest.R;
import com.example.lab_rest.model.Booking;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {

    /**
     * Create ViewHolder class to bind list item view
     */
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        public TextView tvBookingID;
        public TextView tvPrice;
        public TextView tvPickupDate;
        public TextView tvReturnDate;
        public TextView tvStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            tvBookingID = itemView.findViewById(R.id.tvBookingID);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvPickupDate = itemView.findViewById(R.id.tvPickupDate);
            tvReturnDate = itemView.findViewById(R.id.tvReturnDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);

            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            currentPos = getAdapterPosition(); //key point, record the position here
            return false;
        }
    } // close ViewHolder class

    //////////////////////////////////////////////////////////////////////
    // adapter class definitions

    private List<Booking> bookingListData;   // list of book objects
    private Context mContext;   // activity context
    private int currentPos;

    public BookingAdapter(Context context, List<Booking> listData) {
        bookingListData = listData;
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
        View view = inflater.inflate(R.layout.booking_list_item, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // bind data to the view holder instance
        Booking m = bookingListData.get(position);
        holder.tvBookingID.setText(m.getBookingID());
        holder.tvPrice.setText(m.getPrice());
        holder.tvPickupDate.setText(m.getPickupDate());
        holder.tvReturnDate.setText(m.getReturnDate());
        holder.tvStatus.setText(m.getStatus());
    }

    @Override
    public int getItemCount() {
        return bookingListData.size();
    }

    /**
     * return book object for currently selected book (index already set by long press in viewholder)
     * @return
     */
    public Booking getSelectedItem() {
        // return the book record if the current selected position/index is valid
        if(currentPos>=0 && bookingListData !=null && currentPos<bookingListData.size()) {
            return bookingListData.get(currentPos);
        }
        return null;
    }

}