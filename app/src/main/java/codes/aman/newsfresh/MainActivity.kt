package codes.aman.newsfresh

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONArray
import org.json.JSONObject


class MainActivity : AppCompatActivity(), NewsItemClicked {

    private lateinit var mAdapter: NewsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchData()
        mAdapter = NewsListAdapter( this)

        recyclerView.adapter = mAdapter

    }

    private fun fetchData() {
        val url = "https://saurav.tech/NewsAPI/everything/cnn.json"

        val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET, url, null,
                { response ->
                    val newsJsonArray = response.getJSONArray("articles")
                    val newsArray = ArrayList<News>()

                    for (i in 0 until newsJsonArray.length()){
                        val newsJsonObject = newsJsonArray.getJSONObject(i)
                        val news = News(
                                newsJsonObject.getString("title"),
                                newsJsonObject.getString("author"),
                                newsJsonObject.getString("url"),
                                newsJsonObject.getString("urlToImage")
                        )
                        newsArray.add(news)
                    }

                    mAdapter.updateNews(newsArray)
                },
                { error ->
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
                }
        )

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

    }

    override fun onItemClicked(item: News) {

        val builder = CustomTabsIntent.Builder();
        val customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(item.url));
    }
}