package beautician.com.sapplication.Utils;


import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
Created by amaresh
 */

public class APIManager {

    APIManagerInterface listener;
    private RequestQueue requestQueue;
    String jsonString = null;
    int server_status;
    String server_message;

    public interface APIManagerInterface {
        public void onSuccess(Object resultObj);

        public void onError(String error);
    }
    public void ModifyAPI(String url, final String resobjName, JSONObject params, final Context context, final APIManagerInterface listener) {
        // if (ConnectionManager.Connection(context)) {
        // resobjName is the object name which is coming from the response. We know it before so we pass is while calling this function to make it dynamic
        requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (response.getJSONObject(resobjName)!=null) {
                                JSONObject j_obj = response.getJSONObject(resobjName);
                                server_status = j_obj.getInt("status");
                                if(server_status==1) {
                                    Object json = server_status;
                                    if(listener != null){
                                        listener.onSuccess(json);
                                    }
                                    //  String jsonString = response.getString("id");
                                    // JSONObject object = new JSONObject(jsonString);
                                }
                                else{
                                    if (listener != null) {
                                        listener.onError("Error while update");
                                        // listener.onError(response.getString("message"));
                                    }
                                }
                            } else {
                                if (listener != null) {
                                    listener.onError("Error while update ");
                                    // listener.onError(response.getString("message"));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            if (listener != null) {
                                try {
                                    listener.onError(response.getString("message"));
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        } catch (Exception e) {
                            if (listener != null) {
                                try {
                                    listener.onError(response.getString("message"));
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                            }
//                                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (listener != null) {
                    listener.onError(error.toString());
                }
            }
        });

        requestQueue.add(mJsonObjectRequest);
        mJsonObjectRequest.setShouldCache(false);
        mJsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        /*} else {
            if (listener != null) {
//                Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                listener.onError("Please check your internet connection");
            }
        }*/
    }

    public void PostAPI(String url, final String resobjName, JSONObject params, final Class classType, final Context context, final APIManagerInterface listener) {
       // if (ConnectionManager.Connection(context)) {
        // resobjName is the object name which is coming from the response. We know it before so we pass is while calling this function to make it dynamic
            requestQueue = Volley.newRequestQueue(context);

            JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {

                                if (response.getJSONObject(resobjName)!=null) {
                                    JSONObject j_obj = response.getJSONObject(resobjName);
                                    server_status = j_obj.getInt("status");
                                    if(server_status==1) {
                                        Gson gson = new Gson();
                                        Object json = null;
                                      //  String jsonString = response.getString("id");
                                       // JSONObject object = new JSONObject(jsonString);
                                        Object model = gson.fromJson(j_obj.toString(), classType);
                                        if (listener != null) {
                                            listener.onSuccess(model);
                                        }
                                    }
                                    else{
                                        if (listener != null) {
                                            listener.onError("Invalid Mobile or Pin");
                                            // listener.onError(response.getString("message"));
                                        }
                                    }
                                } else {
                                    if (listener != null) {
                                        listener.onError("Invalid Mobile or Pin");
                                       // listener.onError(response.getString("message"));
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                if (listener != null) {
                                    try {
                                        listener.onError(response.getString("message"));
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            } catch (Exception e) {
                                if (listener != null) {
                                    try {
                                        listener.onError(response.getString("message"));
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }
                                }
//                                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (listener != null) {
                        listener.onError(error.toString());
                    }
                }
            });

            requestQueue.add(mJsonObjectRequest);
            mJsonObjectRequest.setShouldCache(false);
            mJsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    3000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        /*} else {
            if (listener != null) {
//                Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                listener.onError("Please check your internet connection");
            }
        }*/
    }


    public void postJSONArrayAPI(String url, final String resobjName, JSONObject params, final Class classType, final Context context, final APIManagerInterface listener) {
       // if (ConnectionManager.Connection(context)) {
            requestQueue = Volley.newRequestQueue(context);

            JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("Array",response.toString());

                            try {
                                if (response.getJSONArray(resobjName)!=null) {
                                    Gson gson = new Gson();
                                    Object json = null;
                                    JSONArray jsonArray = response.getJSONArray(resobjName);
                                    ArrayList arrList = new ArrayList();
                                    if(jsonArray.length()<=0){
                                        if (listener != null) {
                                            listener.onError("No Data Found ");
                                        }
                                    }
                                    else {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject object = jsonArray.getJSONObject(i);
                                            Object model = gson.fromJson(object.toString(), classType);
                                            arrList.add(model);
                                        }
                                        if (listener != null) {
                                            listener.onSuccess(arrList);
                                        }

                                      //  JSONObject object = new JSONObject(jsonString);
                                       /* Object model = gson.fromJson(response.toString(), classType);

                                        if (listener != null) {
                                            listener.onSuccess(model);
                                        }*/
                                    }
                                } else {
                                    if (listener != null) {
                                        listener.onError(response.getString("No Data Found"));
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                if (listener != null) {
                                    try {
                                        listener.onError(response.getString("message"));
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            } catch (Exception e) {
                                if (listener != null) {
                                    try {
                                        listener.onError(response.getString("message"));
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }
                                }
//                                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (listener != null) {
                        listener.onError(error.toString());
                    }
                }
            });

            requestQueue.add(mJsonObjectRequest);
            mJsonObjectRequest.setShouldCache(false);
            mJsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    3000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        /*} else {
            if (listener != null) {
//                Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                listener.onError("Please check your internet connection");
            }
        }*/
    }


    public void postGalleryJSONArrayAPI(String url, JSONObject params, final Class classType, final Context context, final APIManagerInterface listener) {
       // if (ConnectionManager.Connection(context)) {
            requestQueue = Volley.newRequestQueue(context);

            JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getString("status").equalsIgnoreCase("SUCCESS")) {
                                    Gson gson = new Gson();
                                    Object json = null;

                                    String jsonString = response.getString("response");

                                    String largeImgURL = response.getString("large_image");


                                    JSONArray jsonArray = new JSONArray(jsonString);
                                    ArrayList arrList = new ArrayList();

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        Object model = gson.fromJson(object.toString(), classType);
                                        /*GalleryModel temp = (GalleryModel) model;
                                        temp.setLarge_image(largeImgURL);
                                        arrList.add(temp);*/
                                    }

                                    if (listener != null) {
                                        listener.onSuccess(arrList);
                                    }

                                    JSONObject object = new JSONObject(jsonString);
                                    Object model = gson.fromJson(object.toString(), classType);

                                    if (listener != null) {
                                        listener.onSuccess(model);
                                    }
                                } else {
                                    if (listener != null) {
                                        listener.onError(response.getString("message"));
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                if (listener != null) {
                                    try {
                                        listener.onError(response.getString("message"));
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            } catch (Exception e) {
                                if (listener != null) {
                                    try {
                                        listener.onError(response.getString("message"));
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }
                                }
//                                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (listener != null) {
                        listener.onError(error.toString());
                    }
                }
            });

            requestQueue.add(mJsonObjectRequest);
            mJsonObjectRequest.setShouldCache(false);
            mJsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    3000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        /*} else {
            if (listener != null) {
//                Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                listener.onError("Please check your internet connection");
            }
        }*/
    }

}


