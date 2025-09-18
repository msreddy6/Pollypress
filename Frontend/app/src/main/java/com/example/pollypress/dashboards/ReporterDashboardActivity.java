package com.example.pollypress.dashboards;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.pollypress.R;
import com.example.pollypress.adapter.ReporterArticleAdapter;
import com.example.pollypress.adapter.ReporterPollAdapter;
import com.example.pollypress.content.CreateArticleActivity;
import com.example.pollypress.content.CreatePollActivity;
import com.example.pollypress.content.EditArticleActivity;
import com.example.pollypress.content.EditPollActivity;
import com.example.pollypress.dashboards.ReporterTicketsActivity;
import com.example.pollypress.model.ArticleModel;
import com.example.pollypress.model.ReporterPollModel;
import com.example.pollypress.ReporterSession;
import com.example.pollypress.VolleySingleton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReporterDashboardActivity extends AppCompatActivity {

    private static final String BASE_URL = VolleySingleton.BASE_URL;

    private RecyclerView recyclerViewReporterArticles, recyclerViewReporterPolls;
    private ReporterArticleAdapter articleAdapter;
    private ReporterPollAdapter pollAdapter;
    private ArrayList<ArticleModel> articleList;
    private ArrayList<ReporterPollModel> pollList;

    private EditText searchInput;
    private Button btnSearch;
    private Button btnCreateArticle, btnCreatePoll;
    private Button btnNavArticles, btnNavPolls;
    private FloatingActionButton btnMyTickets;

    private String reporterName;
    private int reporterId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reporter_dashboard);

        ImageView btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(v ->
                startActivity(new Intent(this, ReporterProfileActivity.class))
        );

        ReporterSession session = new ReporterSession(this);
        reporterName = session.getReporterUsername();
        reporterId   = session.getReporterId();
        if (reporterName == null || reporterName.isEmpty()) {
            Toast.makeText(this, "Reporter name not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        searchInput            = findViewById(R.id.searchInput);
        btnSearch              = findViewById(R.id.btnSearch);
        btnCreateArticle       = findViewById(R.id.btnCreateArticle);
        btnCreatePoll          = findViewById(R.id.btnCreatePoll);
        btnNavArticles         = findViewById(R.id.btnNavArticles);
        btnNavPolls            = findViewById(R.id.btnNavPolls);
        btnMyTickets           = findViewById(R.id.btnMyTickets);

        recyclerViewReporterArticles = findViewById(R.id.recyclerViewReporterArticles);
        recyclerViewReporterPolls    = findViewById(R.id.recyclerViewReporterPolls);
        recyclerViewReporterArticles.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewReporterPolls   .setLayoutManager(new LinearLayoutManager(this));

        articleList = new ArrayList<>();
        pollList    = new ArrayList<>();

        articleAdapter = new ReporterArticleAdapter(articleList, new ReporterArticleAdapter.OnArticleActionListener() {
            @Override
            public void onEditClicked(ArticleModel article) {
                Intent intent = new Intent(ReporterDashboardActivity.this, EditArticleActivity.class);
                intent.putExtra("articleId", article.getId());
                intent.putExtra("title", article.getTitle());
                intent.putExtra("content", article.getContent());
                intent.putExtra("author", article.getAuthor());
                intent.putExtra("publication", article.getPublication());
                intent.putExtra("publicationDate", article.getPublicationDate());
                startActivity(intent);
            }
            @Override
            public void onDeleteClicked(int articleId) {
                deleteArticle(articleId, ReporterDashboardActivity.this::fetchArticles);
            }
        });

        pollAdapter = new ReporterPollAdapter(pollList, new ReporterPollAdapter.OnPollActionListener() {
            @Override
            public void onEditClicked(ReporterPollModel poll) {
                Intent intent = new Intent(ReporterDashboardActivity.this, EditPollActivity.class);
                intent.putExtra("pollId", poll.getId());
                intent.putExtra("title", poll.getTitle());
                intent.putExtra("description", poll.getDescription());
                intent.putExtra("options", new JSONArray(poll.getOptions()).toString());
                startActivity(intent);
            }
            @Override
            public void onDeleteClicked(long pollId) {
                deletePoll(pollId);
            }
        });

        recyclerViewReporterArticles.setAdapter(articleAdapter);
        recyclerViewReporterPolls   .setAdapter(pollAdapter);

        btnSearch.setOnClickListener(v -> {
            String keyword = searchInput.getText().toString().trim();
            if (!keyword.isEmpty()) {
                searchArticles(keyword);
                searchPolls(keyword);
            } else {
                fetchArticles();
                fetchPolls();
            }
        });

        btnCreateArticle.setOnClickListener(v ->
                startActivity(new Intent(this, CreateArticleActivity.class))
        );
        btnCreatePoll.setOnClickListener(v ->
                startActivity(new Intent(this, CreatePollActivity.class))
        );

        btnNavArticles.setOnClickListener(v -> showSection(true));
        btnNavPolls   .setOnClickListener(v -> showSection(false));

        btnMyTickets.setOnClickListener(v ->
                startActivity(new Intent(this, ReporterTicketsActivity.class))
        );

        fetchArticles();
        fetchPolls();
        showSection(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchArticles();
        fetchPolls();
    }

    /** Toggle between Articles vs Polls and tint the nav buttons */
    private void showSection(boolean showArticles) {
        recyclerViewReporterArticles.setVisibility(showArticles ? View.VISIBLE : View.GONE);
        recyclerViewReporterPolls   .setVisibility(showArticles ? View.GONE   : View.VISIBLE);

        int activeColor   = ContextCompat.getColor(this, R.color.purple_500);
        int inactiveColor = ContextCompat.getColor(this, android.R.color.darker_gray);

        btnNavArticles.getBackground().setColorFilter(
                showArticles ? activeColor : inactiveColor, PorterDuff.Mode.MULTIPLY
        );
        btnNavPolls.getBackground().setColorFilter(
                showArticles ? inactiveColor : activeColor, PorterDuff.Mode.MULTIPLY
        );
    }

    private void fetchArticles() {
        String url = BASE_URL + "articles/reporter/" + reporterName;
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    articleList.clear();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);
                            boolean approved = obj.optBoolean("approved", false);
                            String status = approved ? "Approved" : "Pending Approval";
                            articleList.add(new ArticleModel(
                                    obj.getInt("id"),
                                    obj.optString("title","No Title"),
                                    obj.optString("content","No Content"),
                                    obj.optString("author",""),
                                    obj.optString("publication","N/A"),
                                    obj.optString("publicationDate","N/A"),
                                    status
                            ));
                        }
                        articleAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Toast.makeText(this, "Parsing error in articles", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Failed to load articles", Toast.LENGTH_SHORT).show()
        );
        VolleySingleton.getInstance(this).addToRequestQueue(req);
    }

    private void fetchPolls() {
        String url = BASE_URL + "polls/reporterName/" + reporterName;
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    pollList.clear();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);
                            boolean approved = obj.optBoolean("approved", false);
                            String status = approved ? "Approved" : "Pending Approval";
                            JSONArray opts = obj.optJSONArray("options");
                            List<String> options = new ArrayList<>();
                            if (opts != null) {
                                for (int j = 0; j < opts.length(); j++) {
                                    options.add(opts.getString(j));
                                }
                            }
                            pollList.add(new ReporterPollModel(
                                    obj.getLong("id"),
                                    obj.optString("title","No Title"),
                                    obj.optString("description","No Description"),
                                    options,
                                    obj.optString("createdAt","Unknown Date"),
                                    status
                            ));
                        }
                        pollAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Toast.makeText(this, "Parsing error in polls", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Failed to load polls", Toast.LENGTH_SHORT).show()
        );
        VolleySingleton.getInstance(this).addToRequestQueue(req);
    }

    private void deleteArticle(int articleId, Runnable refreshCallback) {
        String markUrl = BASE_URL + "articles/" + articleId;
        StringRequest markReq = new StringRequest(Request.Method.DELETE, markUrl,
                response -> {
                    String finUrl = BASE_URL + "articles/" + articleId + "/finalizeDeletion";
                    StringRequest finReq = new StringRequest(Request.Method.DELETE, finUrl,
                            finResp -> {
                                Toast.makeText(this, "Article permanently deleted", Toast.LENGTH_SHORT).show();
                                refreshCallback.run();
                            },
                            err -> Toast.makeText(this, "Finalize failed", Toast.LENGTH_SHORT).show()
                    );
                    VolleySingleton.getInstance(this).addToRequestQueue(finReq);
                },
                error -> Toast.makeText(this, "Failed to mark article for deletion", Toast.LENGTH_SHORT).show()
        );
        VolleySingleton.getInstance(this).addToRequestQueue(markReq);
    }

    private void deletePoll(long pollId) {
        String url = BASE_URL + "polls/" + pollId;
        StringRequest req = new StringRequest(Request.Method.DELETE, url,
                response -> {
                    Toast.makeText(this, "Poll deleted", Toast.LENGTH_SHORT).show();
                    fetchPolls();
                },
                error -> Toast.makeText(this, "Failed to delete poll", Toast.LENGTH_SHORT).show()
        );
        VolleySingleton.getInstance(this).addToRequestQueue(req);
    }

    private void searchArticles(String keyword) {
        String url = BASE_URL + "articles/search?keyword=" + keyword;
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    articleList.clear();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);
                            JSONObject rep = obj.optJSONObject("reporter");
                            if (rep != null && rep.optString("username","")
                                    .equalsIgnoreCase(reporterName)) {
                                boolean approved = obj.optBoolean("approved", false);
                                String status = approved ? "Approved" : "Pending Approval";
                                articleList.add(new ArticleModel(
                                        obj.getInt("id"),
                                        obj.optString("title","No Title"),
                                        obj.optString("content","No Content"),
                                        obj.optString("author",""),
                                        obj.optString("publication","N/A"),
                                        obj.optString("publicationDate","N/A"),
                                        status
                                ));
                            }
                        }
                        articleAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Toast.makeText(this, "Search parsing error in articles", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Article search failed", Toast.LENGTH_SHORT).show()
        );
        VolleySingleton.getInstance(this).addToRequestQueue(req);
    }

    private void searchPolls(String keyword) {
        String url = BASE_URL + "polls/search?keyword=" + keyword;
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    pollList.clear();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);
                            JSONObject rep = obj.optJSONObject("reporter");
                            if (rep != null && rep.optString("username","")
                                    .equalsIgnoreCase(reporterName)) {
                                boolean approved = obj.optBoolean("approved", false);
                                String status = approved ? "Approved" : "Pending Approval";
                                JSONArray opts = obj.optJSONArray("options");
                                List<String> options = new ArrayList<>();
                                if (opts != null) {
                                    for (int j = 0; j < opts.length(); j++) {
                                        options.add(opts.getString(j));
                                    }
                                }
                                pollList.add(new ReporterPollModel(
                                        obj.getLong("id"),
                                        obj.optString("title","No Title"),
                                        obj.optString("description","No Description"),
                                        options,
                                        obj.optString("createdAt","Unknown Date"),
                                        status
                                ));
                            }
                        }
                        pollAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Toast.makeText(this, "Search parsing error in polls", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Poll search failed", Toast.LENGTH_SHORT).show()
        );
        VolleySingleton.getInstance(this).addToRequestQueue(req);
    }
}