package com.innovator.ipcserver.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.innovator.ipcclient.Book;
import com.innovator.ipcclient.BookManager;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 服务端的AIDLService.java
 *
 * 在服务端实现AIDL中定义的方法接口的具体逻辑，
 * 然后在客户端调用这些方法接口，从而达到跨进程通信的目的。
 * Created by innovator on 2018/1/18.
 */

public class AIDLService extends Service{


    //包含Book的List
    private List<Book> mBooks = new ArrayList<>();


    //由AIDL生成的BookManager类的代理类
    private final BookManager.Stub mBookManager = new BookManager.Stub() {
        @Override
        public List<Book> getBooks() throws RemoteException {
            synchronized (this){
                Log.i("TAG","服务端发送数据给客户端");
                if(mBooks != null){
                    return mBooks;
                }

                return new ArrayList<>();
            }
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            Log.i("TAG","服务端接收客户端的数据");
            synchronized (this) {
                if (mBooks == null) {
                    mBooks = new ArrayList<>();
                }
                if (book == null) {
                    Log.e("TAG", "客户端传了一个空的Book对象");
                    book = new Book();
                }
                //因为 getBook 的参数的 tag 是 inout，所以服务端修改了book的参数，客户端的应该也会修改
                book.setPrice(6666);
                if (!mBooks.contains(book)) {
                    mBooks.add(book);
                }
                //打印mBooks列表，观察客户端传过来的值
                Log.e("TAG", "服务端的 addBooks() 方法被调用 , 打印服务端收到的数据 : " + mBooks.toString());
            }
        }
    };

    @Override
    public void onCreate() {
        Book book = new Book();
        book.setName("在复杂的世界做个明白人");
        book.setPrice(2222);
        mBooks.add(book);
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("TAG", String.format("服务端连接上了客户端,连接的 ntent 是 %s", intent.toString()));
        return mBookManager;
    }
}
