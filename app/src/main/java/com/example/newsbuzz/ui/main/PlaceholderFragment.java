package com.example.newsbuzz.ui.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.content.res.TypedArrayUtils;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsbuzz.Api.ApiClient;
import com.example.newsbuzz.Api.ApiInterface;
import com.example.newsbuzz.NewsItem;
import com.example.newsbuzz.R;
import com.example.newsbuzz.WebActivity;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.newsbuzz.ui.main.SectionsPagerAdapter.TAB_TITLES;

public class PlaceholderFragment extends Fragment implements RecyclerAdapter.OnClickListener{

    private static final String ARG_SECTION_NUMBER = "section_number";
    private String API_KEY = "8c1b72bd9c72457ba75709bea098ed1a";
    ApiInterface apiInterface;
    RecyclerView recyclerView;
    LinearLayoutManager manager;
    RecyclerAdapter adapter;
    ProgressBar progressBar;
    int tabIndex;
    int pageNo;
    Context context;


    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabIndex = 0;
        pageNo = 0;
        if (getArguments() != null) {
            tabIndex = getArguments().getInt(ARG_SECTION_NUMBER);
        }
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        context = container.getContext();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialise(context);
        setPagination();
        getNews();
    }

    private void setPagination() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!recyclerView.canScrollVertically(1)){
                    getNews();
                }
            }
        });
    }

    private void initialise(Context context){
        manager = new LinearLayoutManager(context);
        recyclerView = getView().findViewById(R.id.recycler_view);
        progressBar = getView().findViewById(R.id.progress_bar);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        adapter = new RecyclerAdapter(context,this);
        recyclerView.setAdapter(adapter);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
    }

    private void getNews(){
        ++pageNo;
        Call<JsonObject> call = sendRequest();
        if(call == null) return;
        progressBar.setVisibility(ProgressBar.VISIBLE);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d(getTag(), "onResponse: "+response);
                if(!response.isSuccessful()) return;
                progressBar.setVisibility(ProgressBar.INVISIBLE);
                JsonArray array = response.body().getAsJsonArray("articles");
                for(int i=0;i<array.size();++i){
                    JsonObject object = array.get(i).getAsJsonObject();
                    Log.d(getTag(), "onResponse: " + object);
                    NewsItem newsItem = new NewsItem();
                    newsItem.setTitle(getString(object.get("title")));
                    newsItem.setDescription(getString(object.get("description")));
                    newsItem.setAuthor(getString(object.get("author")));
                    newsItem.setPublishedAt(getString(object.get("publishedAt")));
                    newsItem.setUrlToImage(getString(object.get("urlToImage")));
                    newsItem.setUrl(getString(object.get("url")));
                    adapter.newsList.add(newsItem);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(getTag(), "onFailure: ", t.fillInStackTrace() );
                progressBar.setVisibility(ProgressBar.INVISIBLE);
                Toast.makeText(getContext(), "Error Fetching News", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getString(JsonElement title) {
        if(title.isJsonNull()) return "";
        return title.getAsString();
    }

    private Call<JsonObject> sendRequest(){
        String COUNTRY = "us";
        if(tabIndex == 0){
            return apiInterface.getTopHeadlines(API_KEY, "popularity", pageNo, COUNTRY);
        }
        return apiInterface.getOtherNews(API_KEY, "relevancy", pageNo, context.getResources().getString(TAB_TITLES[tabIndex]).toLowerCase());
    }

    @Override
    public void onNewsClick(int position) {
        Intent intent = new Intent(getActivity(), WebActivity.class);
        String url = adapter.newsList.get(position).getUrl();
        if(url.equals("")){
            Toast.makeText(context, "No URL available", Toast.LENGTH_SHORT).show();
            return;
        }
        intent.putExtra("URL",url);
        startActivity(intent);
    }
}