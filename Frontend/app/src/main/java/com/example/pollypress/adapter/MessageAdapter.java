package com.example.pollypress.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pollypress.R;
import com.example.pollypress.model.TicketMessageModel;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.VH> {
    private List<TicketMessageModel> msgs;

    public MessageAdapter(List<TicketMessageModel> msgs) {
        this.msgs = msgs;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(VH holder, int pos) {
        TicketMessageModel m = msgs.get(pos);
        holder.tvSender.setText(m.getSender());
        holder.tvBody.setText(m.getMessage());
        holder.tvTime.setText(m.getSentAt());
    }

    @Override public int getItemCount() { return msgs.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvSender, tvBody, tvTime;
        VH(View v) {
            super(v);
            tvSender = v.findViewById(R.id.tvMsgSender);
            tvBody   = v.findViewById(R.id.tvMsgBody);
            tvTime   = v.findViewById(R.id.tvMsgTime);
        }
    }
}
