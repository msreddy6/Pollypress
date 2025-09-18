package com.example.pollypress.dashboards;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.pollypress.adapter.NewsUserArticlePagerAdapter;
import com.example.pollypress.adapter.NewsUserPollPagerAdapter;
import com.example.pollypress.NewsUserSession;
import com.example.pollypress.PollPercentageWebSocketManager;
import com.example.pollypress.R;
import com.example.pollypress.VolleySingleton;
import com.example.pollypress.model.ApprovedPollModel;
import com.example.pollypress.model.ArticleModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * NewsUserDashboardActivity displays approved articles and polls to news users.
 * It uses a ViewPager2 to switch between the two sections, supports pull-to-refresh for polls,
 * and connects to a WebSocket to receive live poll percentage updates.
 * @author Shivadhar Reddy Maddi
 */
public class NewsUserDashboardActivity extends AppCompatActivity {

    public static boolean isInTestMode;
    private DrawerLayout drawerLayout;
    private ViewPager2 viewPager;
    private SwipeRefreshLayout swipeRefreshPolls;
    private Button btnNewsArticles, btnPolls;
    private ImageView btnMenu;
    private TextView tvUsername;

    private NewsUserArticlePagerAdapter articlePagerAdapter;
    private NewsUserPollPagerAdapter pollPagerAdapter;
    private ArrayList<ArticleModel>     articleList;
    private ArrayList<ApprovedPollModel> pollList;

    private static final String BASE_URL = "http://coms-3090-003.class.las.iastate.edu:8080/";

    /**
     * Called when the activity is first created.
     * Initializes UI components, adapters, and event handlers, then fetches initial data.
     *
     * @param savedInstanceState the bundle containing the activity’s previously saved state, or null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_user_dashboard);

        // Bind views
        drawerLayout      = findViewById(R.id.drawerLayout);
        viewPager         = findViewById(R.id.viewPager);
        swipeRefreshPolls = findViewById(R.id.swipeRefreshPolls);
        btnNewsArticles   = findViewById(R.id.btnNewsArticles);
        btnPolls          = findViewById(R.id.btnPolls);
        btnMenu           = findViewById(R.id.btnMenu);
        tvUsername        = findViewById(R.id.tvUsername);

        // Display greeting
        String username = new NewsUserSession(this).getUsername();
        tvUsername.setText("Hi, " + username);

        // Profile menu navigation
        btnMenu.setOnClickListener(v ->
                startActivity(new Intent(NewsUserDashboardActivity.this, NewsUserProfileActivity.class))
        );

        // Prepare data lists and adapters
        articleList = new ArrayList<>();
        pollList    = new ArrayList<>();
        articlePagerAdapter = new NewsUserArticlePagerAdapter(this, articleList);
        pollPagerAdapter    = new NewsUserPollPagerAdapter(this, pollList);
        viewPager.setAdapter(articlePagerAdapter);

        // Bottom navigation toggles between articles and polls
        btnNewsArticles.setOnClickListener(v -> {
            viewPager.setAdapter(articlePagerAdapter);
            btnNewsArticles.setBackgroundTintList(getColorStateList(R.color.purple_500));
            btnPolls.setBackgroundTintList(getColorStateList(R.color.gray));
        });
        btnPolls.setOnClickListener(v -> {
            viewPager.setAdapter(pollPagerAdapter);
            btnPolls.setBackgroundTintList(getColorStateList(R.color.purple_500));
            btnNewsArticles.setBackgroundTintList(getColorStateList(R.color.gray));
        });

        // Pull-to-refresh for polls
        swipeRefreshPolls.setOnRefreshListener(this::fetchApprovedPolls);

        // Fetch content
        fetchApprovedArticles();
        fetchApprovedPolls();
    }

    /**
     * Retrieves the list of approved articles from the server
     * and updates the ViewPager adapter.
     */
    private void fetchApprovedArticles() {
        JsonArrayRequest req = new JsonArrayRequest(
                Request.Method.GET,
                BASE_URL + "approved-articles",
                null,
                response -> {
                    articleList.clear();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject o = response.getJSONObject(i);
                            articleList.add(new ArticleModel(
                                    o.getInt("id"),
                                    o.getString("title"),
                                    o.getString("content"),
                                    o.optString("author", "Unknown"),
                                    o.optString("publication", "N/A"),
                                    o.optString("publicationDate", "N/A"),
                                    "Approved"
                            ));
                        }
                        articlePagerAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Toast.makeText(this, "Article parsing error", Toast.LENGTH_SHORT).show();
                    }
                },
                err -> Toast.makeText(this, "Failed to fetch articles", Toast.LENGTH_SHORT).show()
        );
        VolleySingleton.getInstance(this).addToRequestQueue(req);
    }

    /**
     * Retrieves the list of approved polls from the server,
     * updates the ViewPager adapter, and stops the refresh animation.
     */
    private void fetchApprovedPolls() {
        swipeRefreshPolls.setRefreshing(true);
        JsonArrayRequest req = new JsonArrayRequest(
                Request.Method.GET,
                BASE_URL + "approved-polls",
                null,
                response -> {
                    pollList.clear();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject o = response.getJSONObject(i);
                            ApprovedPollModel p = new ApprovedPollModel();
                            p.setId(o.getLong("id"));
                            p.setTitle(o.optString("title", "No Title"));
                            p.setDescription(o.optString("description", "No Description"));

                            JSONArray opts = o.optJSONArray("options");
                            ArrayList<String> list = new ArrayList<>();
                            if (opts != null) {
                                for (int j = 0; j < opts.length(); j++) {
                                    list.add(opts.getString(j));
                                }
                            }
                            p.setOptions(list);
                            p.setApprovedAt(o.optString("approvedAt", ""));
                            pollList.add(p);
                        }
                        pollPagerAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Toast.makeText(this, "Poll parsing error", Toast.LENGTH_SHORT).show();
                    } finally {
                        swipeRefreshPolls.setRefreshing(false);
                    }
                },
                error -> {
                    Toast.makeText(this, "Failed to fetch polls", Toast.LENGTH_SHORT).show();
                    swipeRefreshPolls.setRefreshing(false);
                }
        );
        VolleySingleton.getInstance(this).addToRequestQueue(req);
    }

    /**
     * Closes the live poll percentage WebSocket when the activity is destroyed
     * to prevent resource leaks.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        PollPercentageWebSocketManager.getInstance().close();
    }
}
