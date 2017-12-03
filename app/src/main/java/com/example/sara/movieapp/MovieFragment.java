package com.example.sara.movieapp;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.sara.movieapp.Adapters.MovieAdapter;
import com.example.sara.movieapp.Models.Movie;
import com.example.sara.movieapp.database.MovieDBController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MovieFragment extends Fragment {

    MovieAdapter movieAdapter ;
    GridView gridView;

    public interface Callback {
        public void onItemSelected(Movie movie);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.settings , menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.popular){
            Update();
        }
        if (id == R.id.top_rated){
            FetchMovie fetch = new FetchMovie();
            fetch.execute("top_rated");
        }
        if (id == R.id.favourite){
            MovieDBController movie_db = new MovieDBController(getActivity());
            ArrayList<Movie> fav = movie_db.getAllMovies();
            if (fav != null){
                movieAdapter.clear();
                for (Movie movieFav : fav){
                    movieAdapter.add(movieFav);
                }
                movieAdapter.notifyDataSetChanged();
            }else {}

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        Update();
    }

    public void Update(){
        FetchMovie movie = new FetchMovie();
        movie.execute("popular");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie, container , false);

        movieAdapter = new MovieAdapter(getContext());
        gridView = view.findViewById(R.id.poster_fragment);
        gridView.setAdapter(movieAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Movie model = movieAdapter.getItem(position);
                ((Callback) getActivity()).onItemSelected(model);
            }
        });
        return view;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public class FetchMovie extends AsyncTask<String ,Void ,ArrayList<Movie>>{

        @Override
        protected ArrayList<Movie> doInBackground(String... strings) {
            HttpURLConnection urlConnection = null;
            BufferedReader bufferedReader = null;
            String jsonSt = null ;

            try {
                String baseUrl = "https://api.themoviedb.org/3/movie/" + strings[0];
                String API_STRINGS = "api_key";
                String API_Value = "e78b2940f343f66faef64fa2fe876545";

                Uri uri = Uri.parse(baseUrl).buildUpon()
                        .appendQueryParameter(API_STRINGS, API_Value).build();
                Log.e("doinback" ,uri.toString());

                URL url = new URL(uri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream input = urlConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(input));
                StringBuffer stringBuffer = new StringBuffer();
                String line = null;
                while ((line = bufferedReader.readLine()) != null){
                    stringBuffer.append(line + "\n");
                }
                jsonSt = stringBuffer.toString();
                Log.e("jsonst" , jsonSt);

            }catch (Exception e){
                Log.e("Main Fragment" , "Error" , e);
            }
            finally {
                if (urlConnection != null){
                    urlConnection.disconnect();
                }
                if (bufferedReader != null){
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        Log.e("AsyncTask" , "Cann't close buffer reader");
                    }
                }
            }
            return GetDataFromJson(jsonSt);
        }

        // Parsing Data
        public ArrayList<Movie> GetDataFromJson(String result){
            Log.e("Parsing" , result);
            ArrayList<Movie> movieData = new ArrayList<Movie>();

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray array = (JSONArray) jsonObject.get("results");
                for (int i = 0 ; i <array.length() ; i++){
                    JSONObject object = array.getJSONObject(i);
                    String data_title = object.getString("original_title");
                    String data_overview = object.getString("overview");
                    String data_rate = object.getString("vote_average");
                    String data_date = object.getString("release_date");
                    String data_image = object.getString("poster_path");
                    String data_id = object.getString("id");

                    Movie data = new Movie(data_title ,data_overview , data_rate ,data_date ,data_image , data_id);
                    movieData.add(data);
                }
            }catch (Exception e){
                Log.e("Parsing", e.getMessage(), e);
                e.printStackTrace();
            }
            return movieData;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movieModels) {
            movieAdapter.clear();
            for (Movie movie : movieModels){
                movieAdapter.add(movie);
            }
            movieAdapter.notifyDataSetChanged();
        }
    }

}
