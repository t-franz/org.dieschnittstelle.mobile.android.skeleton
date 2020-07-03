package tasks;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import java.util.function.Consumer;

import model.IDataItemCRUDOperations;

public class DeleteAllTask extends AsyncTask<Void,Void,Boolean> {

    private IDataItemCRUDOperations crudOperations;
    private Consumer<Boolean> onDoneConsumer;
    private ProgressBar progressBar;


    @Override
    protected Boolean doInBackground(Void... voids) {
        return crudOperations.deleteAll();
    }

    @Override
    protected void onPreExecute() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public DeleteAllTask(ProgressBar progressBar, IDataItemCRUDOperations crudOperations, Consumer<Boolean> onDoneConsumer) {
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
