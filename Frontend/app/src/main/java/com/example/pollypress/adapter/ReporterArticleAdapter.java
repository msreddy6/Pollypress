package com.example.pollypress.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pollypress.R;
import com.example.pollypress.model.ArticleModel;

import java.util.ArrayList;

public class ReporterArticleAdapter extends RecyclerView.Adapter<ReporterArticleAdapter.ReporterViewHolder> {

    private ArrayList<ArticleModel> articles;
    private OnArticleActionListener listener;

    public interface OnArticleActionListener {
        void onEditClicked(ArticleModel article);
        void onDeleteClicked(int articleId);  // Delete article method trigger
    }

    public ReporterArticleAdapter(ArrayList<ArticleModel> articles, OnArticleActionListener listener) {
        this.articles = articles;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReporterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reporter_article, parent, false);
        return new ReporterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReporterViewHolder holder, int position) {
        ArticleModel article = articles.get(position);
        holder.bind(article, listener);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    static class ReporterViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProfile;
        TextView tvTitle, tvContent, tvStatus, tvDate;
        Button btnEdit, btnDelete;

        public ReporterViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProfile = itemView.findViewById(R.id.imgProfile);
            tvTitle = itemView.findViewById(R.id.tvReporterArticleTitle);
            tvContent = itemView.findViewById(R.id.tvReporterArticleContent);
            tvStatus = itemView.findViewById(R.id.tvReporterArticleStatus);
            tvDate = itemView.findViewById(R.id.tvReporterArticleDate);
            btnEdit = itemView.findViewById(R.id.btnEditReporterArticle);
            btnDelete = itemView.findViewById(R.id.btnDeleteReporterArticle);
        }

        public void bind(ArticleModel article, OnArticleActionListener listener) {
            imgProfile.setImageResource(R.drawable.profile);
            tvTitle.setText(article.getTitle());
            tvContent.setText(article.getContent());
            tvStatus.setText("Status: " + article.getStatus());
            tvDate.setText("Date: " + article.getPublicationDate());

            // Handle the click on the edit button
            btnEdit.setOnClickListener(v -> listener.onEditClicked(article));

            // Handle the click on the delete button
            btnDelete.setOnClickListener(v -> {
                // Call the delete function
                listener.onDeleteClicked(article.getId());
            });
        }
    }
}
