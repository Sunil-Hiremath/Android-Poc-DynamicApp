package com.example.dynamicapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class MainActivity extends AppCompatActivity {


    //serialization-converting an object into a format that can be easily stored, transmitted, or reconstructed later.
    private TextView text_view_result;

    private JsonPlaceHolderApi jsonPlaceHolderApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text_view_result = findViewById(R.id.text_view_result);

        Gson gson=new GsonBuilder().serializeNulls().create();// to pass null values to json


        // For Logging
        HttpLoggingInterceptor loggingInterceptor=new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                //adding headers to methods
                .addInterceptor(new Interceptor() {
                    @NonNull
                    @Override
                    public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {
                        Request originalRequest=chain.request();

                        Request newRequest=originalRequest.newBuilder()
                                .header("Interceptor-Header","xyz")
                                .build();

                        return chain.proceed(newRequest);
                    }
                })
                .addInterceptor(loggingInterceptor)
                .build();



        //executing the GET request
        //retrofit instance
        Retrofit retrofit = new Retrofit.Builder()  //represents HTTP request
                .baseUrl("https://jsonplaceholder.typicode.com")
                 .addConverterFactory(GsonConverterFactory.create(gson))//Gson is used for serialization and deserialization and also to convert the req nad resp bodies into Json format
                .client(okHttpClient)
                .build();

        //API instance
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);//retrofit automatically gives the body to the method created in JsonPlaceHolderApi interface

      getPosts();

        //getComments();
       // createPost();
       // updatePost();
        //deletePost();

    }




    private void createPost() {
        Post post=new Post(23,"New Title","New Text");

       // Call<Post> call= jsonPlaceHolderApi.createPost(post);
        Call<Post> call= jsonPlaceHolderApi.createPost(23,"New Title","New Text");

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {

                if(!response.isSuccessful()){
                    text_view_result.setText("code: "+ response.code());//prints response code i.e, 200 or 404 etc
                    return;
                }
                Post postResponse=response.body();

                String content="";

                content +="Code: "+response.code()+"\n";
                content +="ID: "+postResponse.getId()+"\n";
                content +="User ID: "+postResponse.getUserId()+"\n";
                content +="Title: "+postResponse.getTitle()+"\n";
                content +="Text: "+postResponse.getText()+"\n\n";

                text_view_result.setText(content);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                text_view_result.setText(t.getMessage());

            }
        });
    }


    private void getPosts(){
        Map<String,String> parameters=new HashMap<>();
        parameters.put("userId","1");
        parameters.put("_sort","id");
        parameters.put("_order","desc");

        Call<List<Post>> call= jsonPlaceHolderApi.getPosts(parameters);
/*
        Call<List<Post>> call= jsonPlaceHolderApi.getPosts(new Integer[]{2,3,6},"id","desc"); API call
*/

        //call.enqueue ,used to run parallely with the main thread i.e, asynchronous execution
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {

                // for 404 error
                if(!response.isSuccessful()){
                    text_view_result.setText("code: "+ response.code());//prints response code i.e, 200 or 404 etc
                    return;
                }
                List<Post> posts=response.body();

                for (Post post: posts) {

                    String content="";

                    content +="ID: "+post.getId()+"\n";
                    content +="User ID: "+post.getUserId()+"\n";
                    content +="Title: "+post.getTitle()+"\n";
                    content +="Text: "+post.getText()+"\n\n";

                    text_view_result.append(content);



                }
                // to log in logcat
                /*Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String json = gson.toJson(response.body());
                Log.d("API_Response", "Response body:\n" + json);
*/

            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                text_view_result.setText(t.getMessage());

            }
        });

    }

    private void getComments() {
        Call<List<Comment>> call= jsonPlaceHolderApi.getComments("posts/3/comments");
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if(!response.isSuccessful()){
                    text_view_result.setText("Code: "+response.code());
                    return;
                }
                List<Comment> comments=response.body();

                for (Comment comment:comments) {
                    String content="";

                    content+="ID: "+comment.getId()+"\n";
                    content+="Post ID: "+comment.getPostId()+"\n";
                    content+="Name: "+comment.getName()+"\n";
                    content+="Email: "+comment.getEmail()+"\n";
                    content+="Text: "+comment.getText()+"\n\n";

                    text_view_result.append(content);





                    
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                text_view_result.setText(t.getMessage());

            }
        });



    }


    //put and patch are used in the same below method just replace it with put/patch in jsonPlaceHolderApi.PutPost
    private void updatePost() {
        Post post=new Post(12,null,"Next Text");

        Map<String,String> headers=new HashMap<>();
        headers.put("Map-Header1","def");
        headers.put("Map-Header2","ghi");

        Call<Post> call= jsonPlaceHolderApi.PutPost("abc",5,post);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {

                if(!response.isSuccessful()){
                    text_view_result.setText("code: "+ response.code());//prints response code i.e, 200 or 404 etc
                    return;
                }
                Post postResponse=response.body();

                String content="";

                content +="Code: "+response.code()+"\n";
                content +="ID: "+postResponse.getId()+"\n";
                content +="User ID: "+postResponse.getUserId()+"\n";
                content +="Title: "+postResponse.getTitle()+"\n";
                content +="Text: "+postResponse.getText()+"\n\n";

                text_view_result.setText(content);

            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                text_view_result.setText(t.getMessage());

            }
        });
    }
    private void deletePost(){
        Call<Void> call=jsonPlaceHolderApi.deletePost(5);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                text_view_result.setText("code: "+response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                text_view_result.setText(t.getMessage());

            }
        });
    }

}