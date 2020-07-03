package tasks;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.common.primitives.Longs;

import java.util.function.Consumer;

import model.DataItem;
import model.IDataItemCRUDOperations;

public class DeleteDataItemTask extends AsyncTask<DataItem,Void,Boolean> {

    private IDataItemCRUDOperations crudOperations;
    private Consumer<Boolean> onDoneConsumer;
    private ProgressBar progressBar;

    @Override
    protected void onPreExecute() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public DeleteDataItemTask(ProgressBar progressBar, IDataItemCRUDOperations crudOperations, Consumer<Boolean> onDoneConsumer) {
        this.progressBar = progressBar;
        this.crudOperations = crudOperations;
        this.onDoneConsumer = onDoneConsumer;
    }

    @Override
    protected Boolean doInBackground(DataItem... dataItems) {
        return this.crudOperations.deleteDataItem(dataItems[0]);
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
