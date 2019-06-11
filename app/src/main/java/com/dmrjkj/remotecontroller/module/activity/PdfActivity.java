package com.dmrjkj.remotecontroller.module.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmrjkj.remotecontroller.R;
import com.dmrjkj.remotecontroller.module.base.BaseActivity;
import com.github.barteksc.pdfviewer.PDFView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xinchen on 19-5-29.
 */

@SuppressLint("SetTextI18n")
public class PdfActivity extends BaseActivity {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.search_iv_search)
    ImageView searchIvSearch;
    @BindView(R.id.pdfView)
    PDFView pdfview;

    public static void intentActivity(Activity activity, String manual) {
        activity.startActivity(new Intent(activity, PdfActivity.class).putExtra("lan", manual));
    }

    @Override
    protected void initAndBindConfig() {
        super.initAndBindConfig();

        toolbarTitle.setText("Instructions");
        searchIvSearch.setVisibility(View.GONE);

        PDFView.Configurator configurator = pdfview.fromAsset("instruction.pdf").swipeHorizontal(false).enableSwipe(true);
        String manual = getIntent().getStringExtra("lan");
        if ("en".equals(manual)) {
            configurator.pages(new int[]{0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20}).load();
        } else if ("fr".equals(manual)) {
            configurator.pages(new int[]{21, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41}).load();
        } else if ("nl".equals(manual)) {
            configurator.pages(new int[]{42, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62}).load();
        }
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_pdf;
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        onBackPressed();
    }
}
