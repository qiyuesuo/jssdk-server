using proxy_csharp.Http;

namespace proxy_csharp.Tools
{
    public interface IHttpRequest
    {
        HttpParamers GetHttpParamers();

        string GetRequestPath();
    }
}
