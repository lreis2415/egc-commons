package org.egc.commons.exception;

/**
 * TODO
 * 业务异常处理类
 *
 * @author houzhiwei
 * @date 2016/12/8 14:43
 * @link http://blog.csdn.net/king87130/article/details/8011843
 */
public class BusinessException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public BusinessException(String friendlyErrMsg)
    {
        super(createFriendlyErrMsg(friendlyErrMsg));
    }

    public BusinessException(Throwable throwable)
    {
        super(throwable);
    }

    public BusinessException(Throwable throwable, String friendlyErrMsg)
    {
        super(friendlyErrMsg, throwable);
    }

    /**
     * 友好的错误提示
     *
     * @param msgBody
     * @return
     */
    private static String createFriendlyErrMsg(String msgBody)
    {
        String prefixStr = ":( Sorry, ";
        String suffixStr = " Try again later or report the error to us! :)";

        StringBuffer friendlyErrMsg = new StringBuffer("");

        friendlyErrMsg.append(prefixStr);

        friendlyErrMsg.append(msgBody);

        friendlyErrMsg.append(suffixStr);

        return friendlyErrMsg.toString();
    }
}
