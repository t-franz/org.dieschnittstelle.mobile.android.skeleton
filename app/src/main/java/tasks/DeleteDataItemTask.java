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


    public DeleteDataItemTask(IDataItemCRUDOperations crudOperations, Consumer<Boolean> onDoneConsumer) {
        Log.i("Delete","DeleteDataItemTask : DeleteDataItemTask ");
        this.crudOperations = crudOperations;
        this.onDoneConsumer = onDoneConsumer;
    }

    @Override
    protected Boolean doInBackground(DataItem... dataItems) {
        Log.i("Delete","DeleteDataItemTask : doInBackground ");
        return this.crudOperations.deleteDataItem(dataItems[0]);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        onDoneConsumer.accept(result);
    }
}
