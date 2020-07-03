package tasks;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import java.util.function.Consumer;

import model.IDataItemCRUDOperations;

public class DeleteAllLocalsTask extends AsyncTask<Void,Void,Boolean> {

    private IDataItemCRUDOperations crudOperations;
    private Consumer<Boolean> onDoneConsumer;
    private ProgressBar progressBar;


    @Override
    protected Boolean doInBackground(Void... voids) {
        return crudOperations.deleteAllLocal();
    }

    @Override
    protected void onPreExecute() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public DeleteAllLocalsTask(ProgressBar progressBar, IDataItemCRUDOperations crudOperations, Consumer<Boolean> onDoneConsumer) {
        this.progressBar = progressBar;
        this.crudOperations = crudOperations;
        this.onDoneConsumer = onDoneConsumer;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        onDoneConsumer.accept(result);
        if (progressBar != null) {
            progressBar.setVisibility(View.INVISIBLE);
            progressBar = null;
        }
    }
}
