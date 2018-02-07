package com.innovator.ipcserver.ContentProvider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 *
 * 服务端的 ContentProvider
 *
 * Created by innovator on 2018/1/30.
 */

public class BookProvider extends ContentProvider {

    private static final String TAG = "BookProvider";

    public static final String AUTHORITY = "com.innovator.ipcserver.provider";

    public static final Uri BOOK_CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/book");

    public static final Uri USER_CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/user");

    public static final int BOOK_URI_CODE = 0;

    public static final int USER_URI_CODE = 1;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY,"book",BOOK_URI_CODE);
        sUriMatcher.addURI(AUTHORITY,"user",USER_URI_CODE);
    }


    /**
     * 根据 Uri 来获取要访问哪个表
     * @param uri
     * @return
     */
    private String getTableName(Uri uri){
      String table = null;
      switch (sUriMatcher.match(uri)){
          case BOOK_URI_CODE:
              table = DbOpenHelper.BOOK_TABLE_NAME;
              break;
          case USER_URI_CODE:
              table = DbOpenHelper.USER_TABLE_NAME;
              break;
          default:
              break;
      }
      return table;
    }

    private Context mContext;
    private SQLiteDatabase mDb;


    @Override
    public boolean onCreate() {
        Log.i("TAG","onCreate，当前线程是："+Thread.currentThread().getName().toString());

        mContext = getContext();

        //另开线程初始化数据库
        new Thread(new Runnable() {
            @Override
            public void run() {
              initProviderData();
            }
        }).start();
        return true;
    }

    private void initProviderData(){
        mDb = new DbOpenHelper(mContext).getWritableDatabase();
        mDb.execSQL("delete from "+DbOpenHelper.BOOK_TABLE_NAME);
        mDb.execSQL("delete from "+DbOpenHelper.USER_TABLE_NAME);
        mDb.execSQL("insert into book values(3,'Android');");
        mDb.execSQL("insert into book values(4,'ios');");
        mDb.execSQL("insert into book values(5,'Html');");
        mDb.execSQL("insert into user values(1,'jake','1');");
        mDb.execSQL("insert into user values(2,'jasmine','0');");
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.i("TAG","query，当前线程是："+Thread.currentThread().getName().toString());

        String tableName = getTableName(uri);
        if(tableName == null){
            throw new IllegalArgumentException("Unsupported URI:"+uri);
        }

        return mDb.query(tableName,projection,selection,selectionArgs,null,null,sortOrder,null);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        Log.i("TAG","getType，当前线程是："+Thread.currentThread().getName().toString());
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Log.i("TAG","insert，当前线程是："+Thread.currentThread().getName().toString());

        String tableName = getTableName(uri);
        if(tableName == null){
            throw new IllegalArgumentException("Unsupported URI:"+uri);
        }
        mDb.insert(tableName,null,values);
        //通知外界当前的ContentProvider 数据已经发生改变
        mContext.getContentResolver().notifyChange(uri,null);
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.i("TAG","delete，当前线程是："+Thread.currentThread().getName().toString());

        String tableName = getTableName(uri);
        if(tableName == null){
            throw new IllegalArgumentException("Unsupported URI:"+uri);
        }

        int count = mDb.delete(tableName,selection,selectionArgs);
        if(count >0){
            //通知外界当前的ContentProvider 数据已经发生改变
            mContext.getContentResolver().notifyChange(uri,null);
        }
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.i("TAG","update，当前线程是："+Thread.currentThread().getName().toString());

        String tableName = getTableName(uri);
        if(tableName == null){
            throw new IllegalArgumentException("Unsupported URI:"+uri);
        }

        int row = mDb.update(tableName,values,selection,selectionArgs);
        if(row >0){
            //通知外界当前的ContentProvider 数据已经发生改变
            mContext.getContentResolver().notifyChange(uri,null);
        }
        return row;
    }
}
