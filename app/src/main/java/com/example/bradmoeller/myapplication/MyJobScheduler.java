package com.example.bradmoeller.myapplication;

import android.app.job.JobParameters;
import android.app.job.JobService;

/**
 * Created by bradmoeller on 10/20/17.
 */

public class MyJobScheduler extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {

        return false;
    }

}
