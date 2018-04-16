package dingshi.com.hibook.retrofit.api;


import java.util.HashMap;

import dingshi.com.hibook.BuildConfig;
import dingshi.com.hibook.bean.BookCase;
import dingshi.com.hibook.bean.BookDetails;
import dingshi.com.hibook.bean.BookPerson;
import dingshi.com.hibook.bean.Borrows;
import dingshi.com.hibook.bean.Case2Book;
import dingshi.com.hibook.bean.BookTalent;
import dingshi.com.hibook.bean.Captcha;
import dingshi.com.hibook.bean.CommGradeAdd;
import dingshi.com.hibook.bean.CommInfoAdd;
import dingshi.com.hibook.bean.CommList;
import dingshi.com.hibook.bean.CommentGrade;
import dingshi.com.hibook.bean.CommentInfo;
import dingshi.com.hibook.bean.Coupon;
import dingshi.com.hibook.bean.Home;
import dingshi.com.hibook.bean.MoneyDetails;
import dingshi.com.hibook.bean.Notice;
import dingshi.com.hibook.bean.Order;
import dingshi.com.hibook.bean.OrderDetails;
import dingshi.com.hibook.bean.Payment;
import dingshi.com.hibook.bean.RallySearch;
import dingshi.com.hibook.bean.Result;
import dingshi.com.hibook.bean.BookList;
import dingshi.com.hibook.bean.User;
import dingshi.com.hibook.bean.UserCenter;
import dingshi.com.hibook.bean.Zxing;
import dingshi.com.hibook.bean.card.CardDetails;
import dingshi.com.hibook.bean.card.CardList;
import dingshi.com.hibook.bean.lib.ClubList;
import dingshi.com.hibook.bean.lib.LibCreate;
import dingshi.com.hibook.bean.lib.LibDisc;
import dingshi.com.hibook.bean.lib.LibHome;
import dingshi.com.hibook.bean.lib.LibIntro;
import dingshi.com.hibook.bean.lib.LibList;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;

/**
 * @author wangqi
 * @since 2017/11/24 12:21
 */

public interface ApiService {

    /**
     * 阿里校验模块
     *
     * @return
     */
    @GET("aliopen/signature")
    Observable<ResponseBody> test();

    /**
     * 登录模块
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("user/signin")
    Observable<User> login(@FieldMap HashMap<String, String> map);

    /**
     * 微信登录模块
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("wxopen/signin")
    Observable<User> loginWx(@FieldMap HashMap<String, String> map);

    /**
     * 阿里校验模块
     *
     * @param map
     * @return
     */
    @GET("aliopen/signature")
    Observable<Payment> signatureAli(@QueryMap HashMap<String, String> map);

    /**
     * 阿里登录模块
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("aliopen/signin")
    Observable<User> loginAli(@FieldMap HashMap<String, String> map);

    /**
     * 退出登录模块
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("user/signout")
    Observable<Result> signOut(@FieldMap HashMap<String, String> map);

    /**
     * 注册模块
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("user/register")
    Observable<User> register(@FieldMap HashMap<String, String> map);

    /**
     * 获取验证码模块
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("user/captcha")
    Observable<Captcha> captcha(@FieldMap HashMap<String, String> map);


    /********************************** 用户信息模块 *****************************************************/


    /**
     * 所有通知
     *
     * @param map uid : Number
     * @return
     */
    @GET("information/all")
    Observable<Notice> showNotice(@QueryMap HashMap<String, String> map);


    /**
     * 用户信息查询
     *
     * @param map uid : Number
     * @return
     */
    @GET("user/show")
    Observable<User> userShow(@QueryMap HashMap<String, String> map);


    /**
     * 用户信息更改模块
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("user/update")
    Observable<User> update(@FieldMap HashMap<String, String> map);

    /**
     * 更新用户头像
     *
     * @param map   请求参数
     * @param photo 图片
     * @return
     */
    @Multipart
    @POST("user/upavatar")
    Observable<User> updateAvatar(@QueryMap HashMap<String, String> map, @Part MultipartBody.Part photo);

    /**
     * 书房图书的列表
     *
     * @param map
     * @return
     */
    @GET("bookrack/personal")
    Observable<BookList> personalBook(@QueryMap HashMap<String, String> map);

    /**
     * 获取用户钱包明细 user_id  genre：1.消费，2.充值，3.退款进度  page
     *
     * @param map
     * @return
     */
    @GET("finance/receipt")
    Observable<MoneyDetails> receipt(@QueryMap HashMap<String, String> map);

    /**
     * 订单 user_id payment_status:0.所有，1.已支付，2.未支付   page
     */
    @GET("order/all")
    Observable<Order> orderAll(@QueryMap HashMap<String, String> map);

    /**
     * 订单查询
     */
    @GET("order/show")
    Observable<OrderDetails> orderShow(@QueryMap HashMap<String, String> map);


    /**
     * 优惠券
     */
    @GET("coupon/all")
    Observable<Coupon> couponAll(@QueryMap HashMap<String, String> map);


    /**
     * 我的借阅
     *
     * @param map
     * @return
     */
    @GET("user/borrows")
    Observable<Borrows> borrows(@QueryMap HashMap<String, String> map);


    /**
     * 附近书柜
     *
     * @param map
     * @return
     */
    @GET("user/nearby_case")
    Observable<BookCase> bookCase(@QueryMap HashMap<String, String> map);

    /**
     * 书柜还书
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("user/returned")
    Observable<Result> returnedBook(@FieldMap HashMap<String, String> map);

    /**
     * 打开书柜
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("user/pickup")
    Observable<Result> openBookCase(@FieldMap HashMap<String, String> map);

    /**
     * 用户退款申请  1.退押金，2.提现
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("user/refund_apply")
    Observable<Result> returnMoney(@FieldMap HashMap<String, String> map);

    /**
     * 确认还书
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("user/revert")
    Observable<Result> returnPerson(@FieldMap HashMap<String, String> map);

    /**
     * 同意借书
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("user/give")
    Observable<Result> agreeBorrow(@FieldMap HashMap<String, String> map);

    /**
     * 用户确定取书
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("user/take_book")
    Observable<Result> receiveBook(@FieldMap HashMap<String, String> map);


    /********************************** 书城 *****************************************************/
    /**
     * 首页书城模块
     *
     * @param map
     * @return
     */
    @GET("bookstore/home")
    Observable<Home> home(@QueryMap HashMap<String, String> map);

    /**
     * 图书详情数据模块
     *
     * @param map
     * @return
     */
    @GET("book/show")
    Observable<BookDetails> bookShow(@QueryMap HashMap<String, String> map);


    /**
     * 附近书柜
     *
     * @param map
     * @return
     */
    @GET("book/nearby_case")
    Observable<BookCase> nearbycase(@QueryMap HashMap<String, String> map);

    /**
     * 附近书柜列表
     *
     * @param map
     * @return
     */
    @GET("bookcase/list")
    Observable<BookCase> bookCaseList(@QueryMap HashMap<String, String> map);

    /**
     * 共读者数据
     *
     * @param map
     * @return
     */
    @GET("book/common_reader")
    Observable<BookTalent> commonReader(@QueryMap HashMap<String, String> map);

    /**
     * 图书所有者数据
     *
     * @param map
     * @return
     */
    @GET("book/users")
    Observable<BookPerson> bookPerson(@QueryMap HashMap<String, String> map);

    /**
     * 用户评论/评分点赞
     *
     * @param map
     * @return
     */
    @GET("comment/personal")
    Observable<CommList> commonPerson(@QueryMap HashMap<String, String> map);


    /**
     * 图书评分数据
     *
     * @param map
     * @return
     */
    @GET("comment/grade")
    Observable<CommentGrade> commonGrade(@QueryMap HashMap<String, String> map);


    /**
     * 图书评论数据
     *
     * @param map
     * @return
     */
    @GET("comment/info")
    Observable<CommentInfo> commonInfo(@QueryMap HashMap<String, String> map);


    /**
     * 添加图书评分
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("comment/add_grade")
    Observable<CommGradeAdd> addCommonGrade(@FieldMap HashMap<String, String> map);

    /**
     * 添加图书交流评论
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("comment/add_info")
    Observable<CommInfoAdd> addCommonInfo(@FieldMap HashMap<String, String> map);

    /**
     * 评论的点赞
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("comment/praise")
    Observable<Result> commentPraise(@FieldMap HashMap<String, String> map);


    /**
     * 支付
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("order/payment")
    Observable<Payment> payment(@FieldMap HashMap<String, String> map);


    /**
     * 书柜里面的图书列表
     *
     * @param map
     * @return
     */
    @GET("bookcase/show")
    Observable<Case2Book> bookList(@QueryMap HashMap<String, String> map);


    /**
     * 共享书籍
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("bookrack/upstate")
    Observable<Result> bookShare(@FieldMap HashMap<String, String> map);


    /**
     * 用户中心配置
     *
     * @param map
     * @return
     */
    @GET("user/configure")
    Observable<UserCenter> configure(@QueryMap HashMap<String, String> map);


    /**
     * 用户更新邀请码
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("user/upinvitecode")
    Observable<Result> alterInvite(@FieldMap HashMap<String, String> map);


    /**
     * 用户发送邀请码
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("user/invite")
    Observable<Result> userInvite(@FieldMap HashMap<String, String> map);

/**
 * *************************************** 搜索 ****************************************************8
 */
    /**
     * 搜索返回的图书列表
     *
     * @param map
     * @return
     */
    @GET("book/search")
    Observable<BookList> search(@QueryMap HashMap<String, String> map);

/**
 * *************************************** 扫码 ****************************************************8
 */
    /**
     * 扫码加书
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("book/scan")
    Observable<ResponseBody> bookScan(@FieldMap HashMap<String, String> map);


    /**
     * 扫码提交书单
     *
     * @param map
     * @return
     */
    @POST("book/add_all")
    Observable<ResponseBody> bookaddAll(@QueryMap HashMap<String, String> map, @Body String body);


    /**
     * 用户自己创建图书
     *
     * @param map
     * @param photo
     * @return
     */
    @Multipart
    @POST("book/add")
    Observable<Result> createBook(@QueryMap HashMap<String, String> map, @Part MultipartBody.Part photo);

/**
 * *************************************** 卡片 ****************************************************8
 */
    /**
     * 我的卡片列表
     *
     * @param map
     * @return
     */
    @GET("card/list")
    Observable<CardList> cardList(@QueryMap HashMap<String, String> map);

    /**
     * 创建名片
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("card/add")
    Observable<Result> cardAdd(@FieldMap HashMap<String, String> map);

    /**
     * 删除名片
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("card/delete")
    Observable<Result> cardDelete(@FieldMap HashMap<String, String> map);

    /**
     * 删除图书
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("bookrack/delete")
    Observable<Result> bookDelete(@FieldMap HashMap<String, String> map);



    /**
     * 创建名片
     *
     * @param map
     * @return
     */
    @Multipart
    @POST("card/add")
    Observable<Result> cardAdd(@QueryMap HashMap<String, String> map, @Part MultipartBody.Part photo);


    /**
     * 修改名片头像
     *
     * @param map
     * @return
     */
    @Multipart
    @POST("card/upavatar")
    Observable<Result> cardAvatar(@QueryMap HashMap<String, String> map, @Part MultipartBody.Part photo);


    /**
     * 修改名片
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("card/edit")
    Observable<Result> cardEdit(@FieldMap HashMap<String, String> map);


    /**
     * 名片详情
     *
     * @param map
     * @return
     */
    @GET("card/show")
    Observable<CardDetails> cardShow(@QueryMap HashMap<String, String> map);


    /**
     * 名片认证申请
     *
     * @param map
     * @return
     */
    @Multipart
    @POST("card/upident")
    Observable<Result> identApply(@QueryMap HashMap<String, String> map, @Part MultipartBody.Part photo);


    /**
     * 名片交换申请
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("card/apply")
    Observable<CardDetails> cardApply(@FieldMap HashMap<String, String> map);


    /**
     * 设置主名片
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("card/set_mian")
    Observable<Result> setMainCard(@FieldMap HashMap<String, String> map);


    /**
     * 审核名片交换申请
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("card/change_apply")
    Observable<Result> applyCard(@FieldMap HashMap<String, String> map);


    /**
     * 通讯录
     *
     * @param map
     * @return
     */
    @GET("card/follow_list")
    Observable<LibIntro> addressBook(@QueryMap HashMap<String, String> map);


/** *************************************** 图书馆  ****************************************************8
 */
    /**
     * 创建图书目录
     *
     * @param map   请求参数
     * @param photo 图片
     * @return
     */
    @Multipart
    @POST("catalog/add")
    Observable<LibCreate> libCreate(@QueryMap HashMap<String, String> map, @Part MultipartBody.Part photo);


    /**
     * 更改图书目录的图片
     *
     * @param map   请求参数
     * @param photo 图片
     * @return
     */
    @Multipart
    @POST("catalog/upicon")
    Observable<Result> libAvatar(@QueryMap HashMap<String, String> map, @Part MultipartBody.Part photo);

    /**
     * 编辑图书目录
     *
     * @param map 请求参数
     * @return
     */
    @FormUrlEncoded
    @POST("catalog/edit")
    Observable<LibCreate> libEdit(@FieldMap HashMap<String, String> map);

    /**
     * 图书目录列表（我的目录）
     *
     * @param map
     * @return
     */
    @GET("catalog/list")
    Observable<LibList> libList(@QueryMap HashMap<String, String> map);

    /**
     * 图书目录介绍也的用户列表
     * 获取图书目录用户
     *
     * @param map
     * @return
     */
    @GET("catalog/users")
    Observable<LibIntro> libUser(@QueryMap HashMap<String, String> map);


    /**
     * 踢出目录
     *
     * @param map 请求参数
     * @return
     */
    @FormUrlEncoded
    @POST("catalog/kicked")
    Observable<Result> libKicked(@FieldMap HashMap<String, String> map);

    /**
     * 图书目录介绍
     * 获取图书目录用户
     *
     * @param map
     * @return
     */
    @GET("catalog/show")
    Observable<LibCreate> libIntro(@QueryMap HashMap<String, String> map);


    /**
     * 图书馆详情的图书列表
     *
     * @param map 请求参数
     * @return
     */
    @FormUrlEncoded
    @POST("catalog/books")
    Observable<BookList> libBookList(@FieldMap HashMap<String, String> map);

    /**
     * 删除图书馆
     *
     * @param map 请求参数
     * @return
     */
    @FormUrlEncoded
    @POST("catalog/delete")
    Observable<Result> libDelete(@FieldMap HashMap<String, String> map);

    /**
     * 导出名单
     *
     * @param map 请求参数
     * @return
     */
    @FormUrlEncoded
    @POST("catalog/export_user")
    Observable<Result> exportUser(@FieldMap HashMap<String, String> map);


    /**
     * 图书馆首页
     *
     * @param map 请求参数
     * @return
     */
    @FormUrlEncoded
    @POST("catalog/home")
    Observable<LibHome> libHome(@FieldMap HashMap<String, String> map);

    /**
     * 图书馆首页发现入口
     * <p>
     * 可以作为搜索接口用
     *
     * @param map 请求参数
     * @return
     */
    @GET("catalog/multitude")
    Observable<LibDisc> libOpenList(@QueryMap HashMap<String, String> map);

    /**
     * 加入图书馆
     *
     * @param map 请求参数
     * @return
     */
    @FormUrlEncoded
    @POST("catalog/join")
    Observable<Result> libJoin(@FieldMap HashMap<String, String> map);


    /**
     * 虚拟人数设置
     *
     * @param map 请求参数
     * @return
     */
    @FormUrlEncoded
    @POST("catalog/set_fake_user")
    Observable<Result> libFakePerson(@FieldMap HashMap<String, String> map);

    /**
     * 人数上限设置
     *
     * @param map 请求参数
     * @return
     */
    @FormUrlEncoded
    @POST("catalog/set_user_limit")
    Observable<Result> libLimitPerson(@FieldMap HashMap<String, String> map);


    /**
     * 图书馆允许加入设置
     * 图书馆允许加入设置 0：不允许加入 1：允许加入，默认为1
     *
     * @param map 请求参数
     * @return
     */
    @FormUrlEncoded
    @POST("catalog/join_allow")
    Observable<Result> libAllow(@FieldMap HashMap<String, String> map);


    /**
     * 允许用户咨询设置 1：公开 2：不公开 默认为1
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("catalog/set_consult")
    Observable<Result> libConsult(@FieldMap HashMap<String, String> map);

    /**
     * 通知提醒设置
     * 通知提醒状态 1：加入后消息提醒 2：加入后不提醒 默认为1
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("catalog/message_remind")
    Observable<Result> libNotice(@FieldMap HashMap<String, String> map);


    /**
     * 公开到广场 1：公开 2：不公开 默认为1
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("catalog/set_open")
    Observable<Result> libOpen(@FieldMap HashMap<String, String> map);

    /**
     * 书友会列表
     *
     * @param map 请求参数
     * @return
     */
    @GET("clubs/home")
    Observable<ClubList> clubList(@QueryMap HashMap<String, String> map);


    /**
     * 书友会搜索列表
     *
     * @param map 请求参数
     * @return
     */
    @GET("clubs/list")
    Observable<RallySearch> clubLists(@QueryMap HashMap<String, String> map);

    /**
     * 书友会搜索
     *
     * @param map 请求参数  6.城市书友会，7.大学书友会，8.老乡书友会
     * @return
     */
    @GET("clubs/search")
    Observable<RallySearch> clubSearch(@QueryMap HashMap<String, String> map);

}