package com.example.bradmoeller.myapplication;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bradmoeller on 10/20/17.
 */

public class MyJobScheduler extends JobService {

    public static final int JOB_ID = 9164;

    @Override
    public boolean onStartJob(JobParameters params) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        //SharedPrefHelper.putTime(getContext(), dateFormat.format(date));
        writeToFile(this, dateFormat.format(date));
        jobFinished(params, false);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {

        return false;
    }

    private void writeToFile(Context context, String newDate) {
        BufferedWriter bw = null;
        FileWriter fw = null;

        try {
            File path = context.getExternalFilesDir(null);
            File file = new File(path, "jobscheduler1hour.txt");

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            // true = append file
            fw = new FileWriter(file.getAbsoluteFile(), true);
            bw = new BufferedWriter(fw);
            bw.write(newDate + "\n");

            System.out.println("Done");

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (bw != null)
                    bw.close();

                if (fw != null)
                    fw.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }
        }
    }

}
