package android.balaji.tech.mylibrary.util;

import android.balaji.tech.mylibrary.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Balaji SB on 3/2/18.
 */

public class BaseActivity extends AppCompatActivity {

    protected Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=BaseActivity.this;
    }

    public void showCustomSnackBar(View containerLayout, Bitmap image, String text) {
        // Create the Snackbar
        final Snackbar snackbar = Snackbar.make(containerLayout, "", Snackbar.LENGTH_LONG);
        // Get the Snackbar's layout view
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        // Hide the text
        TextView textView = (TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);
        // Inflate our custom view
        View snackView = LayoutInflater.from(mContext).inflate(R.layout.custom_snackbar, null);
        // Configure the view
        ImageView snackbarCloseImg = snackView.findViewById(R.id.snackbarCloseImg);
        ImageView snackbarImg = snackView.findViewById(R.id.snackbarImg);
        TextView textViewTop = snackView.findViewById(R.id.snackbarText);

        snackbarCloseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbarImg.setImageBitmap(image);
        textViewTop.setText(text);
        textViewTop.setTextColor(Color.WHITE);

        // Add the view to the Snackbar's layout
        layout.addView(snackView, 0);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackView.getLayoutParams();
        params.gravity = Gravity.TOP;
        snackView.setLayoutParams(params);
        // Show the Snackbar
        snackbar.show();
    }
}
