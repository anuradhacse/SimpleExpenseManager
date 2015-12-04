package lk.ac.mrt.cse.dbs.simpleexpensemanager;

import android.app.Application;
import android.content.Context;

/**
 * Created by Anuradha Niroshan on 04/12/2015.
 */
public class MyContext extends Application {
    private static Context context;
    public void onCreate(){
        context=getApplicationContext();
    }

    public static Context getCustomAppContext(){
        return context;
    }
}
