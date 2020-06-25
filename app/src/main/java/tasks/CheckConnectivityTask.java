package tasks;

import android.os.AsyncTask;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Consumer;

public class CheckConnectivityTask extends AsyncTask<Void,Void,Boolean> {

    private Consumer<Boolean> onDone;

    public CheckConnectivityTask(Consumer<Boolean> onDone) {
        this.onDone = onDone;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL("http://10.0.2.2:8080/")
                    .openConnection();
            conn.setConnectTimeout(1000);
            conn.setReadTimeout(1000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            conn.connect();
            conn.getInputStream();

            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        this.onDone.accept(result);
    }
}
