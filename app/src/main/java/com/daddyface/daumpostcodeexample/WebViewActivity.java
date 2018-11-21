package com.daddyface.daumpostcodeexample;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.daddyface.daumpostcodeexample.R;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;
    private TextView result;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        result = (TextView) findViewById(R.id.result);

        // WebView 초기화
        init_webView();

        // 핸들러를 통한 JavaScript 이벤트 반응
        handler = new Handler();
    }

    public void init_webView() {
        // WebView 설정
        webView = (WebView) findViewById(R.id.webView);
        // JavaScript 허용
        webView.getSettings().setJavaScriptEnabled(true);
        // JavaScript의 window.open 허용
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌
        // 두 번째 파라미터는 사용될 php에도 동일하게 사용해야함
        webView.addJavascriptInterface(new AndroidBridge(), "TestApp");
        // web client 를 chrome 으로 설정
        webView.setWebChromeClient(new WebChromeClient());
        // webview url load
//        webView.loadUrl("file:///android_asset/search_address.html");
        webView.loadUrl("http://codeman77.ivyro.net/getAddress.php");

    }

    private class AndroidBridge {
        @JavascriptInterface
        public void setAddress(final String arg1, final String arg2, final String arg3) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(WebViewActivity.this, arg1+arg2+arg3, Toast.LENGTH_SHORT).show();
//                    Toast.makeText(WebViewActivity.this, arg1, Toast.LENGTH_SHORT).show();
//                    Toast.makeText(WebViewActivity.this, arg1, Toast.LENGTH_SHORT).show();

                    result.setText(String.format("(%s) %s %s", arg1, arg2, arg3));
                    // WebView를 초기화 하지않으면 재사용할 수 없음
                    init_webView();
                    //onPause();

                    Intent intent=new Intent(WebViewActivity.this, MainActivity.class);

                    String addr = arg1+arg2+arg3;
//                    Toast.makeText(WebViewActivity.this, addr, Toast.LENGTH_SHORT).show();
                    intent.putExtra("addr", addr);
                    startActivity(intent);
                    finish();
                }
            });

        }
    }

}

