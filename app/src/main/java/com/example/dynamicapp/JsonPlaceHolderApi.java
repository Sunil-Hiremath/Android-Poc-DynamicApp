package com.example.dynamicapp;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface JsonPlaceHolderApi {

    // https://jsonplaceholder.typicode.com/posts

    //posts is the relative URL or END point see above and behind it is the base url which is used later
    @GET("posts")//retrofit take care of the body of the below method that bring all the posts through annotation
    Call<List<Post>> getPosts(
            @Query("userId") Integer[] userId,//Integer because by this we can pass null
      /*      @Query("userId") Integer userId2,*/
            @Query("_sort") String sort,
            @Query("_order") String order


    ); //An invocation of a Retrofit method that sends a request to a webserver and returns a response.
    //@Query("userId") int userId for these  (/comments?postId=1) type of endpoints

    @GET("posts")
    Call<List<Post>> getPosts(
            //when we want to decide the param when we call this method

            @QueryMap Map<String,String> parameters
     );


    @GET("/posts/{id}/comments")
    Call<List<Comment>> getComments(@Path("id") int postId);//the value in  postId will be linked to id through @Path

    @GET
    Call<List<Comment>> getComments(@Url String url);

    @POST("posts ")
    Call<Post> createPost(@Body Post post);

    //@Body putting the obj into the Body
    @FormUrlEncoded
    @POST("posts")
    Call<Post> createPost(
            @Field("userId") int userId,
            @Field("title") String title,
            @Field("body") String text
    );

    @Headers({"Static-Header1: 123","Static-Header2: 456"})
    @PUT("posts/{id}")
    Call<Post> PutPost(@Header("Dynamic-Header") String header ,
                       @Path("id") int id,
                       @Body Post post); //replace the whole obj(Edit)

    @PATCH("posts/{id}")
    Call<Post> patchPost(@HeaderMap Map<String,String> headers,
                         @Path("id") int id,
                         @Body Post post);// replace the fields

    @DELETE("posts/{id}")
    Call<Void> deletePost(@Path("id") int id);



}
