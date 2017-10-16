package com.example.android.recyclerviewexample.data.word;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.example.android.recyclerviewexample.data.word.local.WordLocalDatasource;
import java.util.HashMap;

/**
 * Created by VinhTL on 16/10/2017.
 */

public class WordProvider extends ContentProvider {
    private SQLiteDatabase db;
    static final String PROVIDER_NAME =
            "com.example.android.recyclerviewexample.data.word.WordProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/words";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    public static final String ID = "id";
    public static final String NAME = "name";

    private static HashMap<String, String> WORDS_PROJECTION_MAP;

    static final int WORDS = 1;
    static final int WORD_ID = 2;

    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "words", WORDS);
        uriMatcher.addURI(PROVIDER_NAME, "word/#", WORD_ID);
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        WordLocalDatasource wordLocalDatasource = new WordLocalDatasource(context);
        db = wordLocalDatasource.getWritableDatabase();
        return db != null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
            @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(WordLocalDatasource.TABLE_WORDS);

        switch (uriMatcher.match(uri)) {
            case WORDS:
                qb.setProjectionMap(WORDS_PROJECTION_MAP);
                break;

            case WORD_ID:
                qb.appendWhere(ID + "=" + uri.getPathSegments().get(1));
                break;

            default:
        }
        if (sortOrder == null || sortOrder == "") {
            sortOrder = ID;
        }
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)){
            case WORDS:
                return "vnd.android.cursor.dir/vnd.example.students";
            case WORD_ID:
                return "vnd.android.cursor.item/vnd.example.students";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
            @Nullable String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case WORDS:
                count = db.delete(WordLocalDatasource.TABLE_WORDS, selection, selectionArgs);
                break;
            case WORD_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete(WordLocalDatasource.TABLE_WORDS, ID + " = " + id, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
            @Nullable String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case WORDS:
                count = db.update(WordLocalDatasource.TABLE_WORDS, values, selection, selectionArgs);
                break;

            case WORD_ID:
                count = db.update(WordLocalDatasource.TABLE_WORDS, values,
                        ID + " = " + uri.getPathSegments().get(1) + (!TextUtils.isEmpty(selection)
                                ? "  AND(" +selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
