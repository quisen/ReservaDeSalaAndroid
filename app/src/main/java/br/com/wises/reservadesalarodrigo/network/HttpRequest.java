package br.com.wises.reservadesalarodrigo.network;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import br.com.wises.reservadesalarodrigo.models.Event;
import br.com.wises.reservadesalarodrigo.utils.Constants;

public class HttpRequest {

	Context context;
	Map<String, String> headers;
	String url;
	int reqMethod;
	String eventName;

	String ip = "http://172.30.248.56:8080/ReservaDeSala/rest/";

	public HttpRequest(Context c, Map<String, String> h, String u, String reqMethod, String eventName) {
		this.context = c;
		this.headers = h;
		this.url = u;
		this.eventName = eventName;
		switch (reqMethod) {
			case "GET":
				this.reqMethod = Request.Method.GET;
				break;
			case "POST":
				this.reqMethod = Request.Method.POST;
				break;
			case "PUT":
				this.reqMethod = Request.Method.PUT;
				break;
			case "DELETE":
				this.reqMethod = Request.Method.DELETE;
				break;
		}
	}

	public void doRequest() {
		try {
			// Formulate the request and handle the response.
			StringRequest stringRequest = new StringRequest(reqMethod, ip+url,
					new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
							EventBus.getDefault().post(new Event(eventName + Constants.eventSuccessLabel, response));
						}
					},
					new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							try {
								if (error.networkResponse != null) {
									String errorResult = new String(error.networkResponse.data, "UTF-8");
									EventBus.getDefault().post(new Event(eventName + Constants.eventErrorLabel, errorResult));
								} else {
									EventBus.getDefault().post(new Event(eventName + Constants.eventErrorLabel, "Sem resposta do servidor"));
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}) {
				@Override
				public Map<String, String> getHeaders() throws AuthFailureError {
					return headers;
				}
			};

			stringRequest.setRetryPolicy(new DefaultRetryPolicy(
					5000,
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}