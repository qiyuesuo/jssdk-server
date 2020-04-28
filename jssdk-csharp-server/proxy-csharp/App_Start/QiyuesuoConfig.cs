namespace proxy_csharp.App_Start
{
    /// <summary>
    /// 契约锁转发服务配置
    /// </summary>
    public class QiyuesuoConfig
    {
        private static string sdkroot = "/uniformapi";// 默认接口转发地址
        public static int CONNECT_TIMEOUT = 15000;
        public static int READ_TIMEOUT = 90000;

        private string accessToken;// 开放平台App Token
        private string accessSecret;// 开放平台App Secret
        private string accessUrl;//开放平台请求地址

        /// <summary>
        /// 修改下方配置为您个人配置
        /// accessToken：开放平台 App Token
        /// accessSecret：开放平台 App Secret
        /// accessUrl:开放平台请求地址
        /// </summary>
        public QiyuesuoConfig()
        {
            this.accessToken = "替换为申请的App Token";
            this.accessSecret = "替换为申请的App Secret";
            this.accessUrl = "替换为开放平台请求地址";
        }

        public string AccessSecret { get => accessSecret; }
        public string AccessUrl { get => accessUrl + sdkroot; }
        public string AccessToken { get => accessToken; }
    }
}
