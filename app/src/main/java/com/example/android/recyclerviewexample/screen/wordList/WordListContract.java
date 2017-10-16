package com.example.android.recyclerviewexample.screen.wordList;

import com.example.android.recyclerviewexample.data.word.Word;
import com.example.android.recyclerviewexample.screen.BasePresenter;
import com.example.android.recyclerviewexample.screen.BaseViewModel;
import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
interface WordListContract {
    /**
     * View.
     */
    interface ViewModel extends BaseViewModel<Presenter> {
        public void changeDataSet(List<Word> words);
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {
        void onItemWordListClicked(Word word);
    }
}
