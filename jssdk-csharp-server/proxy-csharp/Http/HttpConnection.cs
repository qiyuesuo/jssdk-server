using System.Net;
using System.Net.Security;
using System.Security.Cryptography.X509Certificates;

namespace proxy_csharp.Http
{
    class HttpConnection
    {
        public static HttpWebRequest GetRequest(string url, HttpMethod method, HttpHeader header)
        {
            HttpWebRequest request = null;

            ServicePointManager.ServerCertificateValidationCallback = new RemoteCertificateValidationCallback(CheckValidationResult);
            request = (HttpWebRequest)WebRequest.Create(url);

            request.Method = method.ToString();
            request.Accept = "text/plain,application/json";
            request.UserAgent = "qiyuesuo-csharp-sdk";

            request.Headers.Set("Accept-Encoding", "gzip,deflate");
            request.Headers.Add("x-qys-open-timestamp", header.Timestamp.ToString());
            request.Headers.Add("x-qys-open-signature", header.Signature.ToLower());
            request.Headers.Add("x-qys-open-accesstoken", header.AccessToken);
            request.Headers.Add("version", header.Version);

            return request;
        }

        private static bool CheckValidationResult(object sender, X509Certificate certificate, X509Chain chain, SslPolicyErrors errors)
        {
            return true; //总是接受     
        }
    }
}
