package com.example.android.recyclerviewexample.screen.wordList;

import android.content.ContentValues;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.android.recyclerviewexample.R;
import com.example.android.recyclerviewexample.data.word.Word;
import com.example.android.recyclerviewexample.data.word.WordProvider;
import com.example.android.recyclerviewexample.data.word.WordRepository;
import com.example.android.recyclerviewexample.data.word.local.WordLocalDatasource;
import com.example.android.recyclerviewexample.databinding.FragmentWordListBinding;
import com.example.android.recyclerviewexample.screen.BaseFragment;
import java.util.ArrayList;

/**
 * WordList Screen.
 */
public class WordListFragment extends BaseFragment {
    private static final String TAG = WordListFragment.class.getSimpleName();
    private WordListContract.ViewModel mViewModel;
    private WordListAdapter mWordListAdapter;
    private WordRepository mWordRepository;

    public WordListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<Word> words = new ArrayList<>();
        mWordRepository = WordRepository.getInstance(new WordLocalDatasource(getActivity()));
        mWordRepository.deleteAll();
        mWordRepository.addWord(new Word("ahihi"));
        mWordRepository.addWord(new Word("ahehe"));
        mWordRepository.addWord(new Word("ahoho"));
        String URL = "content://com.example.android.recyclerviewexample.data.word.WordProvider";
        Uri wordsUri = Uri.parse(URL);
        Cursor c = getActivity().managedQuery(wordsUri, null, null, null, "name");

        if (c.moveToFirst()) {
            do{
                words.add(new Word(c.getInt(c.getColumnIndex(WordProvider.ID)),
                        c.getString(c.getColumnIndex(WordProvider.NAME))));
            } while (c.moveToNext());
        }
        Log.d(TAG, "words size: " + words.size());
        
        mWordListAdapter = new WordListAdapter(getActivity(), words);
        mViewModel = new WordListViewModel(mWordListAdapter);
        WordListContract.Presenter presenter = new WordListPresenter(mViewModel, mWordRepository);
        mViewModel.setPresenter(presenter);

        ContentValues values = new ContentValues();
       

       

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        FragmentWordListBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_word_list, container, false);
        binding.setViewModel((WordListViewModel) mViewModel);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        mViewModel.onStart();
    }

    @Override
    public void onStop() {
        mViewModel.onStop();
        super.onStop();
    }
}
