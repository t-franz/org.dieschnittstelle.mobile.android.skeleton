package tasks;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import java.util.function.Consumer;

import model.DataItem;
import model.IDataItemCRUDOperations;

public class CreateDataItemTask extends AsyncTask<DataItem,Void,DataItem> {

    private ProgressBar progressBar;
    private IDataItemCRUDOperations crudOperations;
    private Consumer<DataItem> onDoneConsumer;

    @Override
    protected void onPreExecute() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public CreateDataItemTask(ProgressBar progressBar, IDataItemCRUDOperations crudOperations, Consumer<DataItem> onDoneConsumer) {
        this.crudOperations = crudOperations;
        this.progressBar = progressBar;
        this.onDoneConsumer = onDoneConsumer;
    }

    @Override
    protected DataItem doInBackground(DataItem... dataItems) {
        return crudOperations.createDataItem(dataItems[0]);
    }

    @Override
    protected void onPostExecute(DataItem dataItem) {
        onDoneConsumer.accept(dataItem);
        if (progressBar != null) {
            progressBar.setVisibility(View.INVISIBLE);
            progressBar = null;
        }
    }
}
