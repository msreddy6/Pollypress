package com.example.pollypress.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pollypress.R;
import com.example.pollypress.model.ReporterPollModel;

import java.util.ArrayList;

public class ReporterPollAdapter extends RecyclerView.Adapter<ReporterPollAdapter.ViewHolder> {

    private ArrayList<ReporterPollModel> pollList;
    private OnPollActionListener onPollActionListener;
    private Context context;

    public interface OnPollActionListener {
        void onEditClicked(ReporterPollModel poll);
        void onDeleteClicked(long pollId);
    }

    public ReporterPollAdapter(ArrayList<ReporterPollModel> pollList, OnPollActionListener onPollActionListener) {
        this.pollList = pollList;
        this.onPollActionListener = onPollActionListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_reporter_poll, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ReporterPollModel poll = pollList.get(position);
        holder.tvPollTitle.setText(poll.getTitle());
        holder.tvPollDescription.setText(poll.getDescription());
        holder.tvPollStatus.setText(poll.getStatus());
        holder.tvPollStatus.setTextColor(poll.getStatus().equals("Approved") ? context.getResources().getColor(android.R.color.holo_green_dark) : context.getResources().getColor(android.R.color.holo_orange_dark));

        // Handle the Edit button click
        holder.btnEditPoll.setOnClickListener(v -> onPollActionListener.onEditClicked(poll));

        // Handle the Delete button click
        holder.btnDeletePoll.setOnClickListener(v -> onPollActionListener.onDeleteClicked(poll.getId()));
    }

    @Override
    public int getItemCount() {
        return pollList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvPollTitle, tvPollDescription, tvPollStatus;
        private Button btnEditPoll, btnDeletePoll;

        public ViewHolder(View itemView) {
            super(itemView);

            tvPollTitle = itemView.findViewById(R.id.tvReporterPollTitle);
            tvPollDescription = itemView.findViewById(R.id.tvReporterPollDescription);
            tvPollStatus = itemView.findViewById(R.id.tvReporterPollStatus);
            btnEditPoll = itemView.findViewById(R.id.btnEditReporterPoll);
            btnDeletePoll = itemView.findViewById(R.id.btnDeleteReporterPoll);
        }
    }
}
