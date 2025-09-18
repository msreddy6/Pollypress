package com.example.pollypress.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pollypress.R;
import com.example.pollypress.model.TicketModel;
import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.VH> {

    public interface OnTicketClickListener {
        void onTicketClick(TicketModel ticket);
    }

    private List<TicketModel> tickets;
    private OnTicketClickListener listener;

    public TicketAdapter(List<TicketModel> tickets, OnTicketClickListener listener) {
        this.tickets = tickets;
        this.listener = listener;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ticket, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(VH holder, int pos) {
        TicketModel t = tickets.get(pos);
        holder.tvTitle.setText("Ticket #" + t.getId());
        holder.tvInfo.setText(t.getReporterUsername() + " → " + t.getAdminUsername());
        holder.tvStatus.setText(t.isResolved() ? "Resolved at " + t.getResolvedAt() : "Pending");
        holder.itemView.setOnClickListener(v -> listener.onTicketClick(t));
    }

    @Override public int getItemCount() { return tickets.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvTitle, tvInfo, tvStatus;
        VH(View v) {
            super(v);
            tvTitle  = v.findViewById(R.id.tvTicketTitle);
            tvInfo   = v.findViewById(R.id.tvTicketInfo);
            tvStatus = v.findViewById(R.id.tvTicketStatus);
        }
    }
}
