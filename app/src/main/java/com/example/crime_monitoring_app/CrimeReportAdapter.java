package com.example.crime_monitoring_app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class CrimeReportAdapter extends ArrayAdapter<CrimeReport> {
    private Context context;
    private List<CrimeReport> crimeReports;

    public CrimeReportAdapter(Context context, List<CrimeReport> crimeReports) {
        super(context, 0, crimeReports);
        this.context = context;
        this.crimeReports = crimeReports;
    }

    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_crime_report, parent, false);
        }

        CrimeReport crimeReport = getItem(position);

        TextView descriptionTextView = convertView.findViewById(R.id.descriptionTextView);
        ImageView imageView = convertView.findViewById(R.id.imageView);

        if (crimeReport != null) {
            descriptionTextView.setText(crimeReport.getDescription());

            // Load image using BitmapFactory
            new Thread(() -> {
                try {
                    Bitmap bitmap = getBitmapFromURL(crimeReport.getFileUrl());
                    imageView.post(() -> imageView.setImageBitmap(bitmap));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        return convertView;
    }

    public void updateCrimeReports(List<CrimeReport> crimeReports) {
        this.crimeReports.clear();
        if (crimeReports != null) {
            this.crimeReports.addAll(crimeReports);
        }
        notifyDataSetChanged();
    }

    private Bitmap getBitmapFromURL(String src) throws IOException {
        URL url = new URL(src);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.connect();
        InputStream input = connection.getInputStream();
        return BitmapFactory.decodeStream(input);
    }
}
