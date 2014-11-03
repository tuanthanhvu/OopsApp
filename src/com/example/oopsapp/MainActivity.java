package com.example.oopsapp;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;

import com.example.oopsapp.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	private EditText txtUserName, txtPassWord;
	final String NAMESPACE = "http://vn.gnc.com";
	final String METHOD_NAME = "TuanVT2";
	Context context=this;
	// SOAP_ACTIOn là NAMESPACE + METHOD_NAME
	final String SOAP_ACTION = "http://vn.gnc.com/TuanVT2";
	// URL là địa chỉ của webservice khi run lên webpage
	final String URL = "http://10.0.2.2:8080/OopsAppService/services/UserService?wsdl";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
		txtUserName = (EditText) findViewById(R.id.txtUser);
		txtPassWord = (EditText) findViewById(R.id.txtPass);
		Button btn = (Button) findViewById(R.id.login);
		View.OnClickListener btOnClick = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				new RegisterTask().execute();
			}
		};

		// Set sự kiên cho Button
		btn.setOnClickListener(btOnClick);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
    private class RegisterTask extends AsyncTask<String,String, String>
    {
        ProgressDialog progressDialog;
        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
			String result = doGetData();
			return result;
        }

        protected void onPostExecute(String result) {
        	progressDialog.dismiss();
        	txtUserName.setText(result);
        }
        @Override
        protected void onPreExecute() {
            // tiền sử lý giao diện trước đăng nhập
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Đang xu...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }	
    public String doGetData(){
		SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		// Nếu webservice viết bằng dotNet.Nếu ko thì =false
		envelope.dotNet = false;
		envelope.encodingStyle = SoapSerializationEnvelope.ENC;
		envelope.setOutputSoapObject(Request);
		AndroidHttpTransport aht = new AndroidHttpTransport(URL);
		try {
			aht.call(SOAP_ACTION, envelope);
			SoapObject result = (SoapObject) envelope.bodyIn;
			try {
				if (result.getPropertyCount() > 0) {
					String res=result.getProperty(0).toString();
					return res;
				}else{
					return "NULL";
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.i("*********", e.getMessage());
				return "ERR";
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.i("*********", e.getMessage());
			return "ERR";
		}
    }
}
