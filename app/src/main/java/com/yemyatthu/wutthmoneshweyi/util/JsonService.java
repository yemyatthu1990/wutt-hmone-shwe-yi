package com.yemyatthu.wutthmoneshweyi.util;

import android.content.Context;
import android.util.Log;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.SearchResultSnippet;
import com.google.api.services.youtube.model.Thumbnail;
import com.google.api.services.youtube.model.ThumbnailDetails;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yemyatthu on 5/5/15.
 */
public class JsonService {
  public static String convertToJson(List<SearchResult> searchResults) {
    List<Map<String,String>> results = new ArrayList<>();
    for(SearchResult searchResult:searchResults){
      Map<String,String> tempStore = new HashMap<>();
      tempStore.put("kind","youtube#video");
      tempStore.put("videoId",searchResult.getId().getVideoId());
      tempStore.put("title",searchResult.getSnippet().getTitle());
      tempStore.put("thumbnails", searchResult.getSnippet().getThumbnails().getMedium().getUrl());
      results.add(tempStore);
    }
    Gson gson = new Gson();
    return gson.toJson(results);
  }

  public static List<SearchResult> convertToJava(String jsonString) {
    List<SearchResult> searchResults = new ArrayList<>();
    GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson = gsonBuilder.create();
    Type type = new TypeToken<List<Map<String,String>>>() {
    }.getType();
    List<Map<String,String>> results = gson.fromJson(jsonString, type);
    for(Map<String,String> tempStore: results){
      SearchResult searchResult = new SearchResult();
      ResourceId resourceId = new ResourceId();
      resourceId.setKind(tempStore.get("kind"));
      resourceId.setVideoId(tempStore.get("videoId"));
      searchResult.setId(resourceId);
      SearchResultSnippet snippet = new SearchResultSnippet();
      snippet.setTitle(tempStore.get("title"));
      snippet.setThumbnails(
          new ThumbnailDetails().setMedium(new Thumbnail().setUrl(tempStore.get("thumbnails"))));
      searchResult.setSnippet(snippet);
      searchResults.add(searchResult);
    }
    return searchResults;
  }

  //Write Json to Local Storage
  public static void saveData(Context context, String jsonString, String fileName) {
    OutputStream outputStream = null;
    try {
      outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
      outputStream.write(jsonString.getBytes());
      outputStream.close();
    } catch (FileNotFoundException exception) {
      Log.d("FileNotFound", "File not found Error");
    } catch (IOException ioException) {
      Log.d("IOError", "IO Error");
    }
  }

  //Load Json from Local Storage
  public static String loadData(Context context, String fileName) {
    StringBuilder builder = null;
    InputStream inputStream = null;
    try {
      inputStream = context.openFileInput(fileName);
      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
      builder = new StringBuilder();
      String line = null;
      while ((line = reader.readLine()) != null) {
        builder.append(line);
      }
      reader.close();
    } catch (FileNotFoundException fileNotFoundExcepiton) {
      Log.d("FileNotFound", "No File Found");
    } catch (IOException ioException) {
      Log.d("IO Exception", "IO Exception");
    }
    assert builder != null;
    return builder.toString();
  }

}
