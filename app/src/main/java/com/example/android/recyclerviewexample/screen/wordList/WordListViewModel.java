package com.example.android.recyclerviewexample.screen.wordList;

import android.view.View;
import com.example.android.recyclerviewexample.data.word.Word;
import java.util.List;

/**
 * Exposes the data to be used in the WordList screen.
 */

public class WordListViewModel implements WordListContract.ViewModel, WordListAdapter.OnRecyclerViewItemClickListener {

    private WordListContract.Presenter mPresenter;
    private WordListAdapter mAdapter;

    public WordListViewModel(WordListAdapter adapter) {
        mAdapter = adapter;
        mAdapter.setItemClickListener(this);
    }

    public WordListAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(WordListAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public void changeDataSet(List<Word> words){
        mAdapter.changeDataSet(words);
    }

    @Override
    public void onStart() {
        mPresenter.onStart();
    }

    @Override
    public void onStop() {
        mPresenter.onStop();
    }

    @Override
    public void setPresenter(WordListContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onItemRecyclerViewClick(View view, Object item) {
        mPresenter.onItemWordListClicked((Word)item);
    }
}
