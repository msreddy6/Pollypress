package com.example.pollypress.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pollypress.model.PollModel;
import com.example.pollypress.R;

import java.util.ArrayList;

public class AdminPollAdapter extends RecyclerView.Adapter<AdminPollAdapter.PollViewHolder> {
    private ArrayList<PollModel> polls;
    private OnPollActionListener listener;

    public interface OnPollActionListener {
        void onApproveClicked(int pollId);
        void onEditClicked(PollModel poll);
        void onDeleteClicked(int pollId);
    }

    public AdminPollAdapter(ArrayList<PollModel> polls, OnPollActionListener listener) {
        this.polls = polls;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PollViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_poll, parent, false);
        return new PollViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PollViewHolder holder, int position) {
        PollModel poll = polls.get(position);
        holder.bind(poll, listener);
    }

    @Override
    public int getItemCount() {
        return polls.size();
    }

    public static class PollViewHolder extends RecyclerView.ViewHolder {
        public TextView tvPollTitle;
        public TextView tvPollDescription;
        public TextView tvPollOptions;
        public Button btnApprove;
        public Button btnEdit;
        public Button btnDelete;

        public PollViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPollTitle = itemView.findViewById(R.id.tvPollTitle);
            tvPollDescription = itemView.findViewById(R.id.tvPollDescription);
            tvPollOptions = itemView.findViewById(R.id.tvPollOptions);
            btnApprove = itemView.findViewById(R.id.btnApprovePoll);
            btnEdit = itemView.findViewById(R.id.btnEditPoll);
            btnDelete = itemView.findViewById(R.id.btnDeletePoll);
        }

        public void bind(PollModel poll, OnPollActionListener listener) {
            tvPollTitle.setText(poll.getTitle());
            tvPollDescription.setText(poll.getDescription());
            String optionsText = "Options: " + String.join(", ", poll.getOptions());
            tvPollOptions.setText(optionsText);
            btnApprove.setOnClickListener(v -> listener.onApproveClicked(poll.getId().intValue()));
            btnEdit.setOnClickListener(v -> listener.onEditClicked(poll));
            btnDelete.setOnClickListener(v -> listener.onDeleteClicked(poll.getId().intValue()));
        }
    }
}
