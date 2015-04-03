package app.emcc_selfcontrol_android.UI;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MyBindService extends Service {
    private final static String TAG = "main";
    private int count;
    private boolean quit;

    private Thread thread;
    private MyBinder binder=new MyBinder();


    public class MyBinder extends Binder
    {
        public int getCount(){
            return count;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        thread=new Thread(new Runnable() {
            @Override
            public void run() {
                while(!quit){
                    try{
                        Thread.sleep(1000);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                    count++;
                }
            }
        });
        thread.start();
    }

    @Override
    public boolean onUnbind(Intent intent) {

        return true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        this.quit=true;

    }

    @Override
    public IBinder onBind(Intent intent) {

        return binder;
    }
}
