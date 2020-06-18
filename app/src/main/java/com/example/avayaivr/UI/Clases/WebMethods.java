package com.example.avayaivr.UI.Clases;

import android.print.PageRange;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;

//import org.apache.http.HttpResponse;
//import org.apache.http.HttpStatus;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLHandshakeException;

public class WebMethods {

    public static synchronized String getProfiles(){
        String resp = "-1";
        //String postParameters = createQueryStringForParameters(parameters);

        HttpURLConnection urlConnection = null;
        try {
            // create connection
            URL urlToRequest = new URL("https://breeze2-196.collaboratory.avaya.com/services/AAADEVAvayaOmnichannel/Home/js/presets-android.json");
            urlConnection = (HttpURLConnection) urlToRequest.openConnection();
            urlConnection.setConnectTimeout(15000);
            urlConnection.setReadTimeout(15000);
            // handle POST parameters

            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestMethod("GET");
            //urlConnection.setFixedLengthStreamingMode(postParameters.getBytes().length);
            //urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
            urlConnection.setRequestProperty("Accept", "*/*");
            //send the POST out
            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
            //out.print(values);
            out.close();


            // handle issues
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                // throw some exception
            }

            InputStream is = urlConnection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                //response.append("\n");
            }
            rd.close();
            Log.i("makeservicekall", "response:" + response);
            resp = response.toString();


        } catch (MalformedURLException e) {
            Log.e("EXCEPTION malformedurl", "error:"+e);

        } catch (SocketTimeoutException e) {
            // hadle timeout
            Log.e("EXCEPTION SOcket", "error:"+e);

        } catch (IOException e) {
            // handle I/0
            Log.e("makesercall EXCEPTION", "error:"+e);

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

        }


        return resp;
    }

    ///**
     //* Metodo para consumir el endpoint de Zang
     //* @param url
     //* @param from
     //* @param to
     //* @param urlPram
     //* @return
     //*/
    /*public static String  postDataZang(String url, String from, String to, String urlPram){
        String resp = "-1";

        try {

            String urltorequest = url + "?From="+from+"&To="+to+"&Url="+urlPram;
            String[] urlSplitdotdot = url.split(":");
            String User = urlSplitdotdot[1].substring(2);
            String Pwd = urlSplitdotdot[2].substring(0,urlSplitdotdot[2].indexOf("@"));

            HttpPost request = new HttpPost(urltorequest);
            String auth = User+":"+Pwd;
            byte[] encodedAuth = Base64.encode(auth.getBytes(),Base64.NO_WRAP);
            String authHeader = "Basic " + new String(encodedAuth);
            request.setHeader("Authorization", authHeader);

            HttpClient client = new DefaultHttpClient();
            HttpResponse response = null;
            response = client.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();

        resp = ""+statusCode;
        } catch (IOException e) {
            e.printStackTrace();
        }



        return resp;
    }*/

    public synchronized static JSONArray getJsonPOSTmethod(String url, HashMap<String, String> parameters){
        JSONArray respJsonArray = null;
        String postParameters = createQueryStringForParameters(parameters);

        HttpURLConnection urlConnection = null;
        try {
            // create connection
            URL urlToRequest = new URL(url);
	             /*if (android.os.Build.VERSION.SDK_INT<=16){
	                 Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.server.url", 8080));// this needs to be hard coded to make it work in 2.3
	                 urlConnection = (HttpURLConnection) urlToRequest.openConnection(proxy);
	                 Log.d("make_servicecall", "Andoird ginger o anterior");
	 	         } else{*/
            urlConnection = (HttpURLConnection) urlToRequest.openConnection();
            //}

            urlConnection.setConnectTimeout(20000);
            urlConnection.setReadTimeout(20000);
            // handle POST parameters
            if (postParameters != null) {
                Log.i("postParameters not NULL", "POST parameters: " + postParameters + " URL: "+ url);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                urlConnection.setRequestMethod("POST");
                urlConnection.setFixedLengthStreamingMode(postParameters.getBytes().length);
                //urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                //urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
                //urlConnection.setRequestProperty("Accept", "*/*");
                //send the POST out
                PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                out.print(postParameters);
                out.close();
            }


            // handle issues
            int statusCode = urlConnection.getResponseCode();
            Log.e("Makesefv icecall", "statuscode: "+statusCode);
            if (statusCode != HttpURLConnection.HTTP_OK) {
                // throw some exception

            }

            InputStream is = urlConnection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                //response.append("\n");
            }
            rd.close();
            Log.i("makeservicekall", "response:" + response);
            try {
                respJsonArray = new JSONArray(response.toString());
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                try {
                    respJsonArray= new JSONArray("[{"+'"'+"Exception"+'"'+":"+'"'+"malformedURL"+'"'+"}]");
                } catch (JSONException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
            return respJsonArray;


        } catch (MalformedURLException e) {
            Log.e("EXCEPTION malformedurl", "error:"+e);
            try {
                respJsonArray = new JSONArray("[{"+'"'+"Exception"+'"'+":"+'"'+"malformedURL"+'"'+"}]");
            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            return respJsonArray;
        } catch (SocketTimeoutException e) {
            // hadle timeout
            Log.e("EXCEPTION SOcket", "error:"+e);
            try {
                respJsonArray = new JSONArray("[{"+'"'+"Exception"+'"'+":"+'"'+"socket_error"+'"'+"}]");
            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            return respJsonArray;
        } catch (IOException e) {
            // handle I/0
            Log.e("makesercall EXCEPTION", "error:"+e);
            try {
                respJsonArray = new JSONArray("[{"+'"'+"Exception"+'"'+":"+'"'+"error"+'"'+"}]");
            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            return respJsonArray;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

        }
    }



    //@SuppressWarnings("deprecation")
    public synchronized static String createQueryStringForParameters(HashMap<String, String> parameters) {
        final char PARAMETER_DELIMITER = '&';
        final char PARAMETER_EQUALS_CHAR = '=';
        StringBuilder parametersAsQueryString = new StringBuilder();
        if (parameters != null) {
            boolean firstParameter = true;

            for (String parameterName : parameters.keySet()) {
                if (!firstParameter) {
                    parametersAsQueryString.append(PARAMETER_DELIMITER);
                }

                parametersAsQueryString.append(parameterName)
                        .append(PARAMETER_EQUALS_CHAR)
                        .append(java.net.URLEncoder.encode(parameters.get(parameterName)));

                firstParameter = false;
            }
        }
        return parametersAsQueryString.toString();
    }
}
