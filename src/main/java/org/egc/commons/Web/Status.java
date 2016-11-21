package org.egc.commons.Web;

/**
 * TODO
 * http://baike.baidu.com/link?url=mSF49R9JHE_KssAMdFb5Fdu2Rvmcc-qo7Y1KspZ0jz8Sh0U3yc5FimTI_1aG14Qn3xnajrcu0OtQvyZDy6OO4uXYUFcOCfpgF0pkXDXy1GGGosSlX1gfCnz2D1rV486c
 *
 * @author houzhiwei
 * @date 2016/11/21 22:23
 */
public class Status
{
    //成功:这一类型的状态码，代表请求已成功被服务器接收、理解、并接受。
    public static final int OK = 200;
    /**
     * 请求已经被实现，而且有一个新的资源已经依据请求的需要而建立，且其 URI 已经随Location 头信息返回。
     */
    public static final int Created = 201;
    /**
     * 服务器已接受请求，但尚未处理。在接受请求处理并返回202状态码的响应应当在返回的实体中包含一些指示处理当前状态的信息，
     * 以及指向处理状态监视器或状态预测的指针，以便用户能够估计操作是否已经完成。
     */
    public static final int ACCEPTED = 202;
    /**
     * 服务器已成功处理了请求，但返回的实体头部元信息不是在原始服务器上有效的确定集合，
     * 而是来自本地或者第三方的拷贝。
     */
    public static final int NonAuthoritativeInformation = 203;
    /**
     * 服务器成功处理了请求，但不需要返回任何实体内容，并且希望返回更新了的元信息。
     */
    public static final int NO_CONTENT = 204;
    /**
     * 服务器成功处理了请求，且没有返回任何内容。该响应主要是被用于接受用户输入后，
     * 立即重置表单，以便用户能够轻松地开始另一次输入。
     */
    public static final int ResetContent = 205;
    /**
     * 服务器已经成功处理了部分 GET 请求。
     */
    public static final int PartialContent = 206;
    /**
     * 被请求的资源有一系列可供选择的回馈信息，每个都有自己特定的地址和浏览器驱动的商议信息。
     * 用户或浏览器能够自行选择一个首选的地址进行重定向。
     */
    public static final int MultipleChoices = 300;
    /**
     * 被请求的资源已永久移动到新位置，并且将来任何对此资源的引用都应该使用本响应返回的若干个 URI 之一。
     */
    public static final int Moved_Permanently = 301;
    /**
     * 请求的资源临时从不同的 URI响应请求。
     */
    public static final int Move_temporarily = 302;
    /**
     * 对应当前请求的响应可以在另一个 URI 上被找到，而且客户端应当采用 GET 的方式访问那个资源。
     */
    public static final int SeeOther = 303;
    /**
     *
     */
    public static final int NOT_MODIFIED = 304;
    /**
     * 被请求的资源必须通过指定的代理才能被访问。
     */
    public static final int UseProxy = 200;
    /**
     * 请求的资源临时从不同的URI 响应请求。
     */
    public static final int TemporaryRedirect = 307;
    //请求错误

    public static final int BAD_REQUEST = 400;
    public static final int ERROR = BAD_REQUEST;
    /**
     * 当前请求需要用户验证。
     */
    public static final int UNAUTHORIZED = 401;
    /**
     * 服务器已经理解请求，但是拒绝执行它。
     */
    public static final int FORBIDDEN = 403;
    /**
     * 请求失败，请求所希望得到的资源未被在服务器上发现。
     */
    public static final int NOT_FOUND = 404;
    /**
     * 请求行中指定的请求方法不能被用于请求相应的资源。
     */
    public static final int MethodNotAllowed = 405;
    /**
     * 请求的资源的内容特性无法满足请求头中的条件，因而无法生成响应实体。
     */
    public static final int NOT_ACCEPTABLE = 406;
    /**
     * 请求超时。
     */
    public static final int REQUEST_TIMEOUT = 408;
    /**
     * 与401响应类似，只不过客户端必须在代理服务器上进行身份验证。
     */
    public static final int Proxy_Authentication_Required = 407;
    /**
     * 由于和被请求的资源的当前状态之间存在冲突，请求无法完成。
     */
    public static final int CONFLICT = 409;
    /**
     * 被请求的资源在服务器上已经不再可用，而且没有任何已知的转发地址。这样的状况应当被认为是永久性的。
     * 主要是帮助网站管理员维护网站，通知用户该资源已经不再可用，
     * 并且服务器拥有者希望所有指向这个资源的远端连接也被删除。
     */
    public static final int GONE = 410;
    /**
     * 请求的URI 长度超过了服务器能够解释的长度，因此服务器拒绝对该请求提供服务
     */
    public static final int RequestURI_Too_Long = 414;
    /**
     * 服务器拒绝处理当前请求，因为该请求提交的实体数据大小超过了服务器愿意或者能够处理的范围。
     */
    public static final int Request_Entity_Too_Large = 413;
    /**
     * 对于当前请求的方法和所请求的资源，请求中提交的实体并不是服务器中所支持的格式，因此请求被拒绝。
     */
    public static final int Unsupported_Media_Type = 415;
    /**
     * 当前资源被锁定。
     */
    public static final int LOCKED = 423;
    /**
     * 请求格式正确，但是由于含有语义错误，无法响应。
     */
    public static final int Unprocessable_Entity = 422;
    //服务器错误
    /**
     * 服务器遇到了一个未曾预料的状况，导致了它无法完成对请求的处理
     */
    public static final int INTERNAL_SERVER_ERROR = 500;
    /**
     * 由于临时的服务器维护或者过载，服务器当前无法处理请求。
     */
    public static final int SERVICE_UNAVAILABLE = 503;
}
