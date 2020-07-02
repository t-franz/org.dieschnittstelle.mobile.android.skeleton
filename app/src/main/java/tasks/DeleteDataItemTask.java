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


    public DeleteDataItemTask(IDataItemCRUDOperations crudOperations) {
        this.crudOperations = crudOperations;
    }

    @Override
    protected Boolean doInBackground(DataItem... dataItems) {
        Log.i("DeleteDataItemTask","longs[0]: " + dataItems[0]);
        return this.crudOperations.deleteDataItem(dataItems[0]);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        onDoneConsumer.accept(result);
//        if (progressBar != null) {
//            progressBar.setVisibility(View.INVISIBLE);
//            progressBar = null;
//        }
    }


    public void run(DataItem item, Consumer<Boolean> onDoneConsumer) {
        this.onDoneConsumer = onDoneConsumer;
        super.execute(item);
    }

}
