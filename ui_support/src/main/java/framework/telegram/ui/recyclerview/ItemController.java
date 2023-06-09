package framework.telegram.ui.recyclerview;

import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;

public class ItemController {

    private RecyclerViewController mRecyclerViewController;

    ItemController(@NonNull RecyclerViewController recyclerViewController) {
        mRecyclerViewController = recyclerViewController;
    }

    public void setNewData(List<?> data) {
        mRecyclerViewController.getAdapter().setNewData(data);
        mRecyclerViewController.getAdapter().disableLoadMoreIfNotFullPage();
    }

    public void addData(Collection<?> data) {
        mRecyclerViewController.getAdapter().addData(data);
    }

    public void addData(Object data) {
        mRecyclerViewController.getAdapter().addData(data);
    }

    public void addData(int position, List<?> data) {
        mRecyclerViewController.getAdapter().addData(position, data);
    }

    public void addData(int position, Object data) {
        mRecyclerViewController.getAdapter().addData(position, data);
    }

    public void replaceData( List<?> data) {
        mRecyclerViewController.getAdapter().replaceData( data);
    }

}
