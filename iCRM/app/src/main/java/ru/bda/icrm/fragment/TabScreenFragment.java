package ru.bda.icrm.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import ru.bda.icrm.R;
import ru.bda.icrm.enums.Constants;
import ru.bda.icrm.model.Score;

/**
 * Created by User on 26.10.2016.
 */

public class TabScreenFragment extends Fragment {

    private WebView mWebView;
    private String mLink;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_screen, null);
        initContent(view);
        return view;
    }

    private void initContent(View view) {
        mWebView = (WebView) view.findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.loadUrl(Constants.TEST_LINK_SCORE + mLink);
    }

    public void setUrl(Score score) {
        mLink = score.getLinkUrl();
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
