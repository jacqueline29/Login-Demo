package utils;
import android.util.Log;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by DEVLON on 5/2/2015.
 */
public class ServiceHandler {
    static String response = null;
    public final static int GET = 1;
    public final static int POST = 2;

    //photo upload
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpeg");

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    String s=null;
    public String url = null;
    public String param = null;
    public String filepath = null;
    public String requestString=null;
    public String url1 = null;
    public String param1 = null;
    public String filepath1 = null;
    public String requestString1=null;
    public String filepath2 = null;
    public String requestString2=null;
    public String url2 = null;


    OkHttpClient client = new OkHttpClient();

    String doGetRequest(String url) throws IOException
    {
        Request request = new Request.Builder().url(url).build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public ServiceHandler() {

    }

    /*
     * Making service call
     * @url - url to make request
     * @method - http request method
     * */
    public String makeServiceCall(String url, int method) {
        return this.makeServiceCall(url, method, null);
    }

    /*
     * Making service call
     * @url - url to make request
     * @method - http request method
     * @params - http request params
     * */
    public String makeServiceCall(String url, int method,
                                  List<NameValuePair> params) {
        try {
            // http client
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;

            // Checking http request method type
            if (method == POST) {
                Log.e("in POST", "in POST");
                HttpPost httpPost = new HttpPost(url);
                // adding post params
                if (params != null) {
                    Log.e("in POST params", "in POST params");
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                }
                Log.e("url in post service", url);
                httpResponse = httpClient.execute(httpPost);

            } else if (method == GET) {
                // appending params to url
                Log.e("in GET", "in GET");
                if (params != null) {
                    Log.e("in GET params", "in GET params");
                    String paramString = URLEncodedUtils
                            .format(params, "utf-8");
                    url += "?" + paramString;
                }
                Log.e("url in get service", url);
                HttpGet httpGet = new HttpGet(url);

                httpResponse = httpClient.execute(httpGet);

            }
            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

         return response;

    }


}
