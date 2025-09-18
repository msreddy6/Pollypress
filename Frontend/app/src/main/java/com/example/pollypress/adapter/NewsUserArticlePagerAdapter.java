package com.example.pollypress.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pollypress.R;
import com.example.pollypress.model.ArticleModel;

import java.util.ArrayList;
import java.util.List;

public class NewsUserArticlePagerAdapter extends RecyclerView.Adapter<NewsUserArticlePagerAdapter.ArticleViewHolder> {

    public static List<ArticleModel> TEST_ARTICLES = new ArrayList<>();

    private Context context;
    private ArrayList<ArticleModel> articleList;

    public NewsUserArticlePagerAdapter(Context context, ArrayList<ArticleModel> articleList) {
        this.context = context;
        this.articleList = articleList;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_newsuser_article, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        ArticleModel article = articleList.get(position);

        holder.title.setText(article.getTitle());
        holder.content.setText(article.getContent());
        holder.meta.setText("By " + article.getAuthor() + " · " + article.getPublicationDate());
        // holder.image.setImageResource(R.drawable.sample_image); // already static in XML
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    static class ArticleViewHolder extends RecyclerView.ViewHolder {
        TextView title, content, meta;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvArticleTitle);
            content = itemView.findViewById(R.id.tvArticleContent);
            meta = itemView.findViewById(R.id.tvArticleMeta);
        }
    }
}
