package model;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public class RetrofitDataItemCRUDOperationsImpl implements IDataItemCRUDOperations {

    public static interface TodoWebAPI {

        @POST("api/todos")
        public Call<DataItem> createItem(@Body DataItem item);

        @GET("api/todos")
        public Call<List<DataItem>> readAllItems();

        @PUT("api/todos/{id}")
        public Call<DataItem> updateDataItem(@Path("id") long id, @Body DataItem item);

        @DELETE("/api/todos/{id}")
        Call<Boolean> deleteDataItem(@Path("id") long id);
    }

    private TodoWebAPI webAPI;

    public RetrofitDataItemCRUDOperationsImpl(){
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        Retrofit apiRoot = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        webAPI = apiRoot.create(TodoWebAPI.class);
    }

    @Override
    public DataItem createDataItem(DataItem item) {
        try {
            Log.i("RetrofitCRUD", "createItem(): " + item);
            return webAPI.createItem(item).execute().body();
        } catch (Exception e) {
            Log.e("RetrofitCRUD","got exception: " + e);
            return null;
        }
    }

    @Override
    public List<DataItem> readAllDataItems() {
        try {
            return webAPI.readAllItems().execute().body();
        } catch (Exception e) {
            Log.e("RetrofitCRUD","got exception: " + e);
            return null;
        }
    }

    @Override
    public DataItem readDataItem(long id) {
        return null;
    }

    @Override
    public boolean updateDataItem(DataItem item) {
        try {
            if (webAPI.updateDataItem(item.getId(), item).execute().body() != null) {
                return true;
            }
            return false;
        } catch (Exception e) {
            Log.e("RetrofitCRUD","got exception: " + e);
            return false;
        }
    }

    @Override
    public boolean deleteDataItem(DataItem item) {

        try {
            if(webAPI.deleteDataItem(item.getId()).execute().body()!=null){
                return true;
            }
        } catch (Exception e) {
            Log.e("RetrofitCRUD","got exception: " + e);
            return false;
        }
        return false;

    }
}
