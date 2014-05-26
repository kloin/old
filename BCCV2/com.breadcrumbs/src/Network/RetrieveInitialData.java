package Network;

import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import com.breadcrumbs.client.R;
import com.google.gson.Gson;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

