package com.example.pollypress.dashboards;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.pollypress.*;
import com.example.pollypress.adapter.AdminPollAdapter;
import com.example.pollypress.adapter.AdminArticleAdapter;
import com.example.pollypress.model.ArticleModel;
import com.example.pollypress.model.PollModel;
import com.example.pollypress.content.EditArticleActivity;
import com.example.pollypress.content.EditPollActivity;
import com.example.pollypress.auth.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * AdminDashboardActivity - Admin's main dashboard for managing articles, polls, profile, and tickets.
 * Allows the admin to approve, edit, delete articles and polls, access tickets, and manage profile settings.
 */
public class AdminDashboardActivity extends AppCompatActivity {

    private static final String BASE_URL = VolleySingleton.BASE_URL;

    private RecyclerView recyclerViewArticles, recyclerViewPolls;
    private AdminArticleAdapter articleAdapter;
    private AdminPollAdapter pollAdapter;
    private ArrayList<ArticleModel> articleList;
    private ArrayList<PollModel> pollList;

    private DrawerLayout drawerLayout;
    private LinearLayout navigationDrawer;
    private ImageView btnMenu, profileImage;
    private TextView tvUsername;
    private Button btnShareApp, btnLogout, btnAdminTickets;

    /**
     * Called when the activity is first created.
     * Initializes drawer layout, recycler views, and buttons.
     * Fetches articles and polls from the server.
     *
     * @param savedInstanceState Saved instance state bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_dashboard);

        setupDrawer();
        setupRecyclerViews();
        setupButtons();

        fetchArticles();
        fetchPolls();
    }

    /**
     * Sets up the navigation drawer with menu button, profile image click, share and logout actions.
     */
    private void setupDrawer() {
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationDrawer = findViewById(R.id.navigationDrawer);
        btnMenu = findViewById(R.id.btnMenu);
        profileImage = findViewById(R.id.profileImage);
        tvUsername = findViewById(R.id.tvUsername);
        btnShareApp = findViewById(R.id.btnShareApp);
        btnLogout = findViewById(R.id.btnLogout);

        AdminSession session = new AdminSession(this);
        String username = session.getAdminUsername();
        tvUsername.setText("Hi, " + username);

        btnMenu.setOnClickListener(v -> drawerLayout.openDrawer(navigationDrawer));

        btnShareApp.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out PollyPress Admin App!");
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        });

        btnLogout.setOnClickListener(v -> {
            session.logout();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }

    /**
     * Initializes and binds the RecyclerViews for articles and polls.
     */
    private void setupRecyclerViews() {
        recyclerViewArticles = findViewById(R.id.recyclerViewAdminArticles);
        recyclerViewPolls = findViewById(R.id.recyclerViewAdminPolls);

        recyclerViewArticles.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPolls.setLayoutManager(new LinearLayoutManager(this));

        articleList = new ArrayList<>();
        pollList = new ArrayList<>();

        articleAdapter = new AdminArticleAdapter(articleList, new AdminArticleAdapter.OnArticleActionListener() {
            @Override public void onApproveClicked(int articleId) { approveArticle(articleId); }
            @Override public void onEditClicked(ArticleModel article) { openEditArticleActivity(article); }
            @Override public void onDeleteClicked(int articleId) {
                deleteArticle(articleId, AdminDashboardActivity.this::fetchArticles);
            }
        });


        pollAdapter = new AdminPollAdapter(pollList, new AdminPollAdapter.OnPollActionListener() {
            @Override public void onApproveClicked(int pollId) { approvePoll(pollId); }
            @Override public void onEditClicked(PollModel poll) { openEditPollActivity(poll); }
            @Override public void onDeleteClicked(int pollId) { deletePoll(pollId); }
        });

        recyclerViewArticles.setAdapter(articleAdapter);
        recyclerViewPolls.setAdapter(pollAdapter);
    }

    /**
     * Sets up other buttons, such as the ticket viewing button.
     */
    private void setupButtons() {
        btnAdminTickets = findViewById(R.id.btnAdminTickets);
        btnAdminTickets.setOnClickListener(v ->
                startActivity(new Intent(this, AdminTicketsActivity.class))
        );
    }

    /**
     * Fetches all unapproved articles from the server and updates the RecyclerView.
     */
    private void fetchArticles() {
        String url = BASE_URL + "articles";
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    articleList.clear();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);
                            if (!obj.optBoolean("approved", false)) {
                                articleList.add(new ArticleModel(
                                        obj.getInt("id"),
                                        obj.optString("title", "No Title"),
                                        obj.optString("content", "No Content"),
                                        obj.optString("author", "Unknown"),
                                        obj.optString("publication", "N/A"),
                                        obj.optString("publicationDate", "N/A"),
                                        "Unapproved"
                                ));
                            }
                        }
                        articleAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Toast.makeText(this, "Article parsing error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(this, "Failed to fetch articles", Toast.LENGTH_SHORT).show();
                    Log.e("AdminDashboard", "fetchArticles error", error);
                }
        );
        VolleySingleton.getInstance(this).addToRequestQueue(req);
    }

    /**
     * Fetches all polls from the server and updates the RecyclerView.
     */
    private void fetchPolls() {
        String url = BASE_URL + "polls";
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    pollList.clear();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);
                            pollList.add(new PollModel(
                                    (long) obj.getInt("id"),
                                    obj.optString("title", "No Title"),
                                    obj.optString("description", "No Description"),
                                    jsonArrayToList(obj.optJSONArray("options")),
                                    obj.optString("createdAt", "")
                            ));
                        }
                        pollAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Toast.makeText(this, "Poll parsing error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(this, "Failed to fetch polls", Toast.LENGTH_SHORT).show();
                    Log.e("AdminDashboard", "fetchPolls error", error);
                }
        );
        VolleySingleton.getInstance(this).addToRequestQueue(req);
    }

    /**
     * Converts a JSONArray to an ArrayList of Strings.
     *
     * @param array JSONArray to convert
     * @return ArrayList<String> containing all elements of the JSONArray
     * @throws JSONException if parsing fails
     */
    private ArrayList<String> jsonArrayToList(JSONArray array) throws JSONException {
        ArrayList<String> list = new ArrayList<>();
        if (array != null) {
            for (int i = 0; i < array.length(); i++) {
                list.add(array.getString(i));
            }
        }
        return list;
    }

    /**
     * Sends a request to approve an article.
     *
     * @param articleId ID of the article to approve
     */
    private void approveArticle(int articleId) {
        String url = BASE_URL + "articles/" + articleId + "/approve?adminId=" + new AdminSession(this).getAdminId();
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PUT, url, null,
                response -> {
                    Toast.makeText(this, "Article approved", Toast.LENGTH_SHORT).show();
                    fetchArticles();
                },
                error -> Toast.makeText(this, "Article approval failed", Toast.LENGTH_SHORT).show()
        );
        VolleySingleton.getInstance(this).addToRequestQueue(req);
    }

    /**
     * Sends a request to permanently delete an article.
     *
     * @param articleId ID of the article to delete
     */
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


    /**
     * Sends a request to approve a poll.
     *
     * @param pollId ID of the poll to approve
     */
    private void approvePoll(int pollId) {
        String url = BASE_URL + "polls/" + pollId + "/approve?adminId=" + new AdminSession(this).getAdminId();
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, null,
                response -> {
                    Toast.makeText(this, "Poll approved", Toast.LENGTH_SHORT).show();
                    fetchPolls();
                },
                error -> Toast.makeText(this, "Poll approval failed", Toast.LENGTH_SHORT).show()
        );
        VolleySingleton.getInstance(this).addToRequestQueue(req);
    }

    /**
     * Sends a request to delete a poll.
     *
     * @param pollId ID of the poll to delete
     */
    private void deletePoll(int pollId) {
        String url = BASE_URL + "polls/" + pollId;
        StringRequest req = new StringRequest(Request.Method.DELETE, url,
                response -> {
                    Toast.makeText(this, "Poll deleted", Toast.LENGTH_SHORT).show();
                    fetchPolls();
                },
                error -> Toast.makeText(this, "Poll deletion failed", Toast.LENGTH_SHORT).show()
        );
        VolleySingleton.getInstance(this).addToRequestQueue(req);
    }

    /**
     * Opens EditArticleActivity with the selected article's details.
     *
     * @param article ArticleModel containing article data
     */
    private void openEditArticleActivity(ArticleModel article) {
        Intent intent = new Intent(this, EditArticleActivity.class);
        intent.putExtra("articleId", article.getId());
        intent.putExtra("title", article.getTitle());
        intent.putExtra("content", article.getContent());
        intent.putExtra("author", article.getAuthor());
        intent.putExtra("publication", article.getPublication());
        intent.putExtra("publicationDate", article.getPublicationDate());
        startActivity(intent);
    }

    /**
     * Opens EditPollActivity with the selected poll's details.
     *
     * @param poll PollModel containing poll data
     */
    private void openEditPollActivity(PollModel poll) {
        Intent intent = new Intent(this, EditPollActivity.class);
        intent.putExtra("pollId", poll.getId());
        intent.putExtra("title", poll.getTitle());
        intent.putExtra("description", poll.getDescription());
        intent.putExtra("options", poll.getOptions().toString());
        startActivity(intent);
    }
}
