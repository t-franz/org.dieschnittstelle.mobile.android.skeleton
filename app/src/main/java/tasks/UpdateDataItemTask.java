package tasks;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import java.util.function.Consumer;

import model.DataItem;
import model.IDataItemCRUDOperations;

public class UpdateDataItemTask extends AsyncTask<DataItem,Void,Boolean> {

    private ProgressBar progressBar;
    private IDataItemCRUDOperations crudOperations;
    private Consumer<Boolean> onDoneConsumer;

    @Override
    protected void onPreExecute() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public UpdateDataItemTask(ProgressBar progressBar, IDataItemCRUDOperations crudOperations, Consumer<Boolean> onDoneConsumer) {
        this.crudOperations = crudOperations;
        this.progressBar = progressBar;
        this.onDoneConsumer = onDoneConsumer;
    }

    @Override
    protected Boolean doInBackground(DataItem... dataItems) {
        return crudOperations.updateDataItem(dataItems[0]);
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
