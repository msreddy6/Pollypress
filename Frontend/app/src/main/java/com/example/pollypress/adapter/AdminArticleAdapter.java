package com.example.pollypress.adapter;

import android.accessibilityservice.TouchInteractionController;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pollypress.model.ArticleModel;
import com.example.pollypress.R;

import java.text.BreakIterator;
import java.util.ArrayList;

public class AdminArticleAdapter extends RecyclerView.Adapter<AdminArticleAdapter.AdminViewHolder> {

    private ArrayList<ArticleModel> articles;
    private OnArticleActionListener listener;

    public interface OnArticleActionListener {
        void onApproveClicked(int articleId);
        void onEditClicked(ArticleModel article);
        void onDeleteClicked(int articleId);
    }

    public AdminArticleAdapter(ArrayList<ArticleModel> articles, OnArticleActionListener listener) {
        this.articles = articles;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_article, parent, false);
        return new AdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminViewHolder holder, int position) {
        ArticleModel article = articles.get(position);
        holder.bind(article, listener);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    static class AdminViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProfile;
        TextView tvTitle, tvStatus, tvDate;
        Button btnApprove, btnEdit, btnDelete;

        public AdminViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProfile = itemView.findViewById(R.id.imgProfile);
            tvTitle = itemView.findViewById(R.id.tvArticleTitle);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvDate = itemView.findViewById(R.id.tvDate);
            btnApprove = itemView.findViewById(R.id.btnApprove);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        public void bind(ArticleModel article, OnArticleActionListener listener) {
            imgProfile.setImageResource(R.drawable.profile);
            tvTitle.setText(article.getTitle());
            tvStatus.setText("Status: " + article.getStatus());
            tvDate.setText("Date: " + article.getPublicationDate());
            btnApprove.setOnClickListener(v -> listener.onApproveClicked(article.getId()));
            btnEdit.setOnClickListener(v -> listener.onEditClicked(article));
            btnDelete.setOnClickListener(v -> listener.onDeleteClicked(article.getId()));
        }
    }

    public static class ArticleViewHolder {
        public TouchInteractionController btnApprove;
        public TouchInteractionController btnEdit;
        public TouchInteractionController btnDelete;
        public BreakIterator tvArticleTitle;
        public BreakIterator tvStatus;
        public BreakIterator tvDate;

        public ArticleViewHolder(View itemView) {
        }

        public void bind(ArticleModel article, OnArticleActionListener artListener) {
        }
    }
}
