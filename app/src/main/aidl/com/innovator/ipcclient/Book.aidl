// Book.aidl
//定义非基本类型的数据类型，定义数据
//引入了一个序列化对象 Book 供其他的AIDL文件使用
//注意：Book.aidl与Book.java的包名应当是一样的

//移植客户端的AIDL文件必须保证吧包名和客户端的一致
package com.innovator.ipcclient;

//注意parcelable是小写
parcelable Book;
