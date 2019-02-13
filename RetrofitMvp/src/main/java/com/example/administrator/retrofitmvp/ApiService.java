package com.example.administrator.retrofitmvp;


import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * 开源答题项目与后台交互的 api 定义
 * Created by wenyiming on 22/01/2018.
 */

// Android 开发中一个比较典型的例子是点击监听器 OnClickListener。
//          对设置 OnClickListener 来说，
//          View 是被观察者， OnClickListener 是观察者，二者通过 setOnClickListener() 方法达成订阅关系。
//          订阅之后用户点击按钮的瞬间，Android Framework 就会将点击事件发送给已经注册的 OnClickListener 。
//          采取这样被动的观察方式，既省去了反复检索状态的资源消耗，也能够得到最高的反馈速度。

// RxJava 有四个基本概念：
//          Observable (可观察者，即被观察者)、 Observer (观察者)、 subscribe (订阅)、事件。Observable 和 Observer 通过 subscribe() 方法实现订阅关系，
//          从而 Observable 可以在需要的时候发出事件来通知 Observer。


public interface ApiService {


    /**
     * 用户登录接口
     */
    @FormUrlEncoded
    @POST("login")
    Observable<UserBean> login(@Field("username") String username,
                               @Field("password") String password
    );

    /**
     * 获取品牌图片/视频 getShopBrandAdver
     */
    @GET("getShopBrandAdver")
    Observable<ShopBrandAdverBean> getShopBrandAdver(

    );

}

/*

类型           注解名称          解释                       作用域
---------------
网络请求方法

@GET              所有方法分别对应                          网络请求接口的方法
@POST             Http中的网络请求方法
@PUT              都接收一个网络地址URL
@DELETE           （也可以不指定，通过
@PATH             @Http设置）
@HEAD
@OPTIONS
	       ----------------------------------------
@HTTP             用于替换以上7个注解的
                  作用及更多功能拓展
---------------
标记类
@FormUrlEncoded   表示请求体是一个Form表单

@Multipart        表示请求体是一个支持文件
                  上传的Form表单

@Streaming        表示返回的数据以流的形式
                  返回；
                  适用于返回数据较大的场景；
                  （如果没有使用该注解，默
                  认把数据全部载入内存；之
                  后获取数据也是从内存中读
                  取）
-------------------------------------------------------------------------------
网络请求参数

@Headers--------> 添加请求头                               网络请求接口的方法的参数
                                                         (Call<> getCall(*)中的*)
@Header           添加不固定值的Header

@Body-----------> 用于非表单请求体

@Field            向Post表单传入键值对
@FieldMap

@Part   --------> 用于表单字段；适用于有
@PartMap          文件上传的情况

@Query            用于表单字段；
@QueryMap         功能同 @Field @FieldMap；
                  区别在于 @Query @QueryMap
                  的数据体现在URL上， @Field
@FieldMap         的数据体现在请求
                  体上；但生成的数据是一致的

@Path   --------> URL缺省值
@URL              URL设置

*/