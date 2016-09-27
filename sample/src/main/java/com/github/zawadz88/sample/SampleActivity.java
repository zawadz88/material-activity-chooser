package com.github.zawadz88.sample;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;

import com.github.zawadz88.activitychooser.MaterialActivityChooserBuilder;
import com.github.zawadz88.sample.util.FileUtil;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SampleActivity extends AppCompatActivity {

    private static final String FILE_AUTHORITY = BuildConfig.APPLICATION_ID + ".FILE_PROVIDER";

    private static final String MIME_TYPE_APPLICATION_PDF = "application/pdf";

    private static final String PDF_FILE_NAME = "Latin-Lipsum.pdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.activity_sample_stock_share_text)
    public void onStockShareTextClicked() {
        Intent chooserIntent = Intent.createChooser(getDefaultShareIntent(), "Share via");
        startActivity(chooserIntent);
    }

    @OnClick(R.id.activity_sample_share_text)
    public void onShareTextClicked() {
        new MaterialActivityChooserBuilder(this)
                .withIntent(getDefaultShareIntent())
                .show();
    }

    @OnClick(R.id.activity_sample_share_text_with_title)
    public void onShareTextWithTitleClicked() {
        new MaterialActivityChooserBuilder(this)
                .withIntent(getDefaultShareIntent())
                .withTitle("Some custom title")
                .show();
    }

    @OnClick(R.id.activity_sample_share_text_with_title_from_resource)
    public void onShareTextWithTitleFromResourceClicked() {
        new MaterialActivityChooserBuilder(this)
                .withIntent(getDefaultShareIntent())
                .withTitle(R.string.custom_share_title)
                .show();
    }

    @OnClick(R.id.activity_sample_share_text_with_secondary_intents)
    public void onShareTextClickedWithSecondaryIntents() {
        new MaterialActivityChooserBuilder(this)
                .withIntent(getDefaultShareIntent())
                .withSecondaryIntent(getSecondaryShareIntent(),
                        "com.google.android.gm" /* GMail */,
                        "com.google.android.apps.inbox" /* Inbox */,
                        "com.microsoft.office.outlook" /* Microsoft Outlook */,
                        "com.google.android.email" /* Default mail app */)
                .withSecondaryIntent(getTertiaryShareIntent(), "com.facebook.katana" /* Facebook */)
                .show();
    }

    @OnClick(R.id.activity_sample_share_text_styled)
    public void onShareTextStyledClicked() {
        new MaterialActivityChooserBuilder(this)
                .withActivity(StyledActivityChooserActivity.class)
                .withIntent(getDefaultShareIntent())
                .show();
    }

    @OnClick(R.id.activity_sample_show_empty_default)
    public void onShowEmptyDefaultClicked() {
        new MaterialActivityChooserBuilder(this)
                .withIntent(getUnsupportedIntent())
                .show();
    }

    @OnClick(R.id.activity_sample_show_empty_custom_text)
    public void onShowEmptyCustomTextClicked() {
        new MaterialActivityChooserBuilder(this)
                .withIntent(getUnsupportedIntent())
                .withEmptyViewTitle("No activities found!")
                .show();
    }

    @OnClick(R.id.activity_sample_show_empty_custom_text_from_resource)
    public void onShowEmptyCustomTextFromResourceClicked() {
        new MaterialActivityChooserBuilder(this)
                .withIntent(getUnsupportedIntent())
                .withEmptyViewTitle(R.string.custom_empty_view_title)
                .show();
    }

    @OnClick(R.id.activity_sample_show_empty_with_custom_action)
    public void onShowEmptyCustomActionClicked() {
        new MaterialActivityChooserBuilder(this)
                .withIntent(getUnsupportedIntent())
                .withEmptyViewAction(getActionPendingIntent())
                .show();
    }

    @OnClick(R.id.activity_sample_show_empty_with_custom_action_text)
    public void onShowEmptyCustomActionTextClicked() {
        new MaterialActivityChooserBuilder(this)
                .withIntent(getUnsupportedIntent())
                .withEmptyViewAction("Click me!", getActionPendingIntent())
                .show();
    }

    @OnClick(R.id.activity_sample_show_empty_with_custom_action_text_from_resource)
    public void onShowEmptyCustomActiontextFromResourceClicked() {
        new MaterialActivityChooserBuilder(this)
                .withIntent(getUnsupportedIntent())
                .withEmptyViewTitle(R.string.custom_empty_view_title)
                .withEmptyViewAction(R.string.custom_empty_view_button_title, getActionPendingIntent())
                .show();
    }

    @OnClick(R.id.activity_sample_show_empty_with_custom_view)
    public void onShowEmptyWithCustomViewClicked() {
        new MaterialActivityChooserBuilder(this)
                .withIntent(getUnsupportedIntent())
                .withEmptyViewCustomView(R.layout.layout_custom_empty_view)
                .show();
    }

    @OnClick(R.id.activity_sample_track_clicked_items)
    public void onTrackClickedItemsClicked() {
        new MaterialActivityChooserBuilder(this)
                .withIntent(getDefaultShareIntent())
                .withActivity(TrackingActivityChooserActivity.class)
                .show();
    }

    @OnClick(R.id.activity_sample_preview_pdf)
    public void onPreviewPdfClicked() {
        File pdfFile = FileUtil.copyFileFromAssetsToCacheDirectory(this, PDF_FILE_NAME);
        Uri uri = FileProvider.getUriForFile(this, FILE_AUTHORITY, pdfFile);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String mimeType = MIME_TYPE_APPLICATION_PDF;
        intent.setDataAndType(uri, mimeType);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_GRANT_READ_URI_PERMISSION);

        new MaterialActivityChooserBuilder(this)
                .withIntent(intent)
                .withTitle("Preview PDF")
                .withEmptyViewTitle("No app found that can open a PDF file")
                .withEmptyViewAction("Find apps", getActionPendingIntent())
                .show();
    }

    @NonNull
    private PendingIntent getActionPendingIntent() {
        String playStoreUrl = "http://play.google.com/store/search?q=pdf&c=apps";
        return PendingIntent.getActivity(this, 0, new Intent(Intent.ACTION_VIEW, Uri.parse(playStoreUrl)), PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @NonNull
    private Intent getDefaultShareIntent() {
        Intent shareTextIntent = new Intent(Intent.ACTION_SEND);
        shareTextIntent.setType("text/plain");
        shareTextIntent.putExtra(Intent.EXTRA_TITLE, "Text to share");
        shareTextIntent.putExtra(Intent.EXTRA_TEXT, "Shared link: http://www.github.com");
        return shareTextIntent;
    }

    @NonNull
    private Intent getSecondaryShareIntent() {
        Intent shareTextIntent = new Intent(Intent.ACTION_SEND);
        shareTextIntent.setType("text/plain");
        shareTextIntent.putExtra(Intent.EXTRA_TITLE, "Secondary text to share");
        shareTextIntent.putExtra(Intent.EXTRA_TEXT, "Secondary shared link: http://www.google.com");
        return shareTextIntent;
    }

    @NonNull
    private Intent getTertiaryShareIntent() {
        Intent shareTextIntent = new Intent(Intent.ACTION_SEND);
        shareTextIntent.setType("text/plain");
        shareTextIntent.putExtra(Intent.EXTRA_TITLE, "Tertiary text to share");
        shareTextIntent.putExtra(Intent.EXTRA_TEXT, "Tertiary shared link: http://www.facebook.com");
        return shareTextIntent;
    }

    @NonNull
    private Intent getUnsupportedIntent() {
        return new Intent("some_unsupported_action");
    }

}
