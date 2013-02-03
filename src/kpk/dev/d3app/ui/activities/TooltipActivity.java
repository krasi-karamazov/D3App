package kpk.dev.d3app.ui.activities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import kpk.dev.d3app.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class TooltipActivity extends Activity {
	private WebView webView;
	private String mToolTipUrl;
	private StringBuilder strBuilder = new StringBuilder();
	public static final String TOOLTIP_URL_KEY = "tooltip_url_key";
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_background);
		mToolTipUrl = getIntent().getExtras().getString(TOOLTIP_URL_KEY);
		webView = (WebView)findViewById(R.id.details_view);
		LayoutParams params = webView.getLayoutParams();
		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay();
		params.height = (int) (0.70 * display.getHeight());
		webView.setLayoutParams(params); // NOTE: Temporary fix
		webView.setWebViewClient(new D3ToolTipClient());
		webView.setBackgroundColor(Color.BLACK);
		new Thread(getHtmlRunnable).start();
		
	}
	
	private class D3ToolTipClient extends WebViewClient{
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return (false);
		}
	}
	
	@SuppressWarnings("deprecation")
	private int getInitialScale() {
		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		int displayWidth = display.getWidth();
		Double scaleValue = Double.valueOf(displayWidth) / Double.valueOf(400);
		scaleValue = scaleValue * Double.valueOf(100);
		return scaleValue.intValue();
	}
	private Runnable getHtmlRunnable = new Runnable() {
		
		@Override
		public void run() {
			try{
				HttpClient client = new DefaultHttpClient();
				HttpGet method = new HttpGet(mToolTipUrl);
				HttpResponse response = client.execute(method);
				if(response.getStatusLine().getStatusCode() == 200){
					InputStream is = response.getEntity().getContent();
					BufferedReader isr = new BufferedReader(new InputStreamReader(is, "UTF-8"));
					int c;
					while((c = isr.read()) != -1){
						
						strBuilder.append((char)c);
					}
					strBuilder.toString().replace("\t", "");
					
					final String strbString = strBuilder.toString();
					strbString.replace("-", "00");
					
					TooltipActivity.this.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							final String html = "<html>"
									+ "<link rel=\"stylesheet\" type=\"text/css\" href=\""
									+ "http://eu.battle.net/d3/static/css/tooltips.css"
									+ "\" />"
									+ "<style type=\"text/css\">"
									+ ".ui-tooltip { background: black; padding: 1px; border: 1px solid #322a20; opacity: 0.95; max-width: 355px;"
									+ "-moz-border-radius: 2px; -webkit-border-radius: 2px; border-radius: 2px;"
									+ "-moz-box-shadow: 0 0 10px #000; -webkit-box-shadow: 0 0 10px #000; box-shadow: 0 0 10px #000;"
									+ "width: 355px;}"
									+ ".ui-tooltip .tooltip-content { background: black; padding: 10px; color: #CFB991; font-size: 12px; }"
									+ ".ui-tooltip-d3 .tooltip-content { padding: 0; }"
									+ ".ui-tooltip .subheader { font-size: 18px; color: #F3E6D0; font-weight: normal; margin-bottom: 4px; } "
									+ "</style>" + "<body><div class=\"ui-tooltip\">" + strbString.toString()
									+ "</div></body></html>";
							webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
							webView.setInitialScale(getInitialScale());
						}
					});
				}
			}catch(IOException e){
				e.printStackTrace();
			}
			
		}
	};
}
