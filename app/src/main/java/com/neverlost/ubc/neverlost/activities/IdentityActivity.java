package com.neverlost.ubc.neverlost.activities;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.facebook.Profile;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.neverlost.ubc.neverlost.R;

public class IdentityActivity extends AppCompatActivity {

    private final static String TAG = "IDENTITY";
    private final static int QR_CODE_DIMENSION = 1000;

    private ImageView qrCodeImageView;
    private ProgressBar qrCodeProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identity);

        qrCodeProgressBar = (ProgressBar) findViewById(R.id.identity_qr_progress);
        qrCodeImageView = (ImageView) findViewById(R.id.identity_qr_code);

        new IdentityAsyncTask().execute();
    }

    private class IdentityAsyncTask extends AsyncTask<Void, Integer, Bitmap> {

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            qrCodeProgressBar.setVisibility(View.INVISIBLE);
            qrCodeImageView.setImageBitmap(result);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            qrCodeProgressBar.setProgress(values[0]);
        }

        @Override
        protected Bitmap doInBackground(Void... params) {

            JsonObject jsonIdentity = new JsonObject();
            jsonIdentity.addProperty("name", Profile.getCurrentProfile().getName());
            jsonIdentity.addProperty("firebase_id", FirebaseInstanceId.getInstance().getToken());

            BitMatrix bitMatrix;
            try {
                bitMatrix = new MultiFormatWriter().encode(
                        jsonIdentity.toString(),
                        BarcodeFormat.DATA_MATRIX.QR_CODE,
                        QR_CODE_DIMENSION, QR_CODE_DIMENSION, null
                );

            } catch (Exception exception) {
                return null;
            }

            int bitMatrixWidth = bitMatrix.getWidth();
            int bitMatrixHeight = bitMatrix.getHeight();

            int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

            // Better cache this otherwise performance WILL drop
            int colorCodeBlack = ContextCompat.getColor(IdentityActivity.this, R.color.black);
            int colorCodeWhite = ContextCompat.getColor(IdentityActivity.this, R.color.white);

            for (int y = 0; y < bitMatrixHeight; y++) {
                for (int x = 0; x < bitMatrixWidth; x++) {
                    pixels[y * bitMatrixWidth + x] = bitMatrix.get(x, y) ?
                            colorCodeBlack : colorCodeWhite;
                }
                publishProgress((Integer) (y * 100 / bitMatrixHeight));
            }

            Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);
            bitmap.setPixels(pixels, 0, bitMatrixWidth, 0, 0, bitMatrixWidth, bitMatrixHeight);

            return bitmap;
        }
    }
}
