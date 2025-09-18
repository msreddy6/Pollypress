package com.example.pollypress.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pollypress.NewsUserSession;
import com.example.pollypress.PollPercentageWebSocketManager;
import com.example.pollypress.R;
import com.example.pollypress.model.ApprovedPollModel;

import java.util.ArrayList;
import java.util.List;

public class NewsUserPollPagerAdapter extends RecyclerView.Adapter<NewsUserPollPagerAdapter.PollViewHolder> {

    public static List<ApprovedPollModel> TEST_POLLS = new ArrayList<>();
    private final Context context;
    private final ArrayList<ApprovedPollModel> pollList;

    public NewsUserPollPagerAdapter(Context context, ArrayList<ApprovedPollModel> pollList) {
        this.context = context;
        this.pollList = pollList;
    }

    @NonNull
    @Override
    public PollViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.item_newsuser_poll, parent, false);
        return new PollViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PollViewHolder holder, int pos) {
        ApprovedPollModel poll = pollList.get(pos);

        holder.title.setText(poll.getTitle());
        holder.meta.setText("By Pollypress · " + poll.getApprovedAt());

        holder.radioGroup.removeAllViews();
        for (String option : poll.getOptions()) {
            RadioButton rb = new RadioButton(context);
            rb.setText(option);
            holder.radioGroup.addView(rb);
        }

        holder.btnVote.setOnClickListener(v -> {
            int selId = holder.radioGroup.getCheckedRadioButtonId();
            if (selId != -1) {
                String choice = ((RadioButton)holder.radioGroup.findViewById(selId)).getText().toString();
                PollPercentageWebSocketManager.getInstance().sendVote(choice);
                Toast.makeText(context, "Voted: " + choice, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Please select an option", Toast.LENGTH_SHORT).show();
            }
        });

        // connect for live percentages
        String username = new NewsUserSession(context).getUsername();
        PollPercentageWebSocketManager.getInstance()
                .connect(username, poll.getId(), new PollPercentageWebSocketManager.PercentageListener() {
                    @Override
                    public void onUpdate(String opt, double yourShare, int total, int optVotes) {
                        holder.stats.post(() -> {
                            holder.stats.setText(
                                    "Your share: " + String.format("%.1f", yourShare) + "%   " +
                                            "Total votes: " + total
                            );
                            holder.stats.setVisibility(View.VISIBLE);
                        });
                    }
                    @Override
                    public void onError(String msg) {
                        // optionally log or toast
                    }
                });
    }

    @Override
    public int getItemCount() { return pollList.size(); }

    static class PollViewHolder extends RecyclerView.ViewHolder {
        TextView title, meta, stats;
        RadioGroup radioGroup;
        Button btnVote;

        public PollViewHolder(@NonNull View itemView) {
            super(itemView);
            title      = itemView.findViewById(R.id.tvNewsPollTitle);
            meta       = itemView.findViewById(R.id.tvPollMeta);
            stats      = itemView.findViewById(R.id.tvVoteStats);
            radioGroup = itemView.findViewById(R.id.radioGroupOptions);
            btnVote    = itemView.findViewById(R.id.btnVote);
        }
    }
}
