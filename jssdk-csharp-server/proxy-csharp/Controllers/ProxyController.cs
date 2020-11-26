using proxy_csharp.App_Start;
using proxy_csharp.Http;
using proxy_csharp.Tools;
using System;
using System.Collections.Generic;
using System.IO;
using System.Net.Http;
using System.Security.Cryptography;
using System.Web.Http;

namespace proxy_csharp.Controllers
{
    public class ProxyController : ApiController
    {
        private QiyuesuoConfig config = new QiyuesuoConfig();

        /// <summary>
        /// Get请求
        /// </summary>
        /// <param name="requestData"></param>
        /// <returns></returns>
        [Route("proxy")]
        [HttpGet]
        public HttpResponseMessage Get(string requestData)
        {
            try
            {
                if (string.IsNullOrEmpty(requestData))
                {
                    return Request.CreateResponse(System.Net.HttpStatusCode.OK);
                }
                HttpParamers paramers = HttpParamers.GetParamers();
                paramers.AddParamer("requestData", requestData);
                Stream stream = new MemoryStream();
                Http.HttpClient.DoDownload(config.AccessUrl, paramers, GetHttpHeader(), QiyuesuoConfig.CONNECT_TIMEOUT, QiyuesuoConfig.READ_TIMEOUT, ref stream);
                return new HttpResponseMessage { Content = new ByteArrayContent(((MemoryStream)stream).ToArray()) };
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
                return new HttpResponseMessage { StatusCode = System.Net.HttpStatusCode.InternalServerError, Content = new StringContent(e.Message) };
            }

        }

        /// <summary>
        /// Post请求
        /// </summary>
        /// <returns></returns>
        [Route("proxy")]
        [HttpPost]
        public async System.Threading.Tasks.Task<HttpResponseMessage> Post()
        {
            Stream fileStream = new MemoryStream();
            try
            {
                string requestData = null;
                Dictionary<string, string> dic = new Dictionary<string, string>();
                // multipart/form-data 格式请求 解析文件流和参数内容
                if (Request.Content.IsMimeMultipartContent())
                {
                    var multipartMemoryStreamProvider = await Request.Content.ReadAsMultipartAsync();
                    foreach (var content in multipartMemoryStreamProvider.Contents)
                    {
                        // 获取 file 对应的文件流 保存到 fileStream
                        if (!string.IsNullOrEmpty(content.Headers.ContentDisposition.FileName) && content.Headers.ContentDisposition.Name.Equals("\"file\""))
                        {
                            using (Stream stream = await content.ReadAsStreamAsync())
                            {
                                byte[] bytes = new byte[stream.Length];
                                stream.Read(bytes, 0, bytes.Length);
                                fileStream = new MemoryStream(bytes);
                                fileStream.Position = 0;
                            }
                        }
                        else
                        {
                            string val = await content.ReadAsStringAsync();
                            dic.Add(content.Headers.ContentDisposition.Name.Replace("\"", ""), System.Web.HttpUtility.UrlDecode(val));
                        }
                    }
                    // 获取 requestData值
                    dic.TryGetValue("requestData", out requestData);
                }
                // 非mulitpart/form-data 格式请求：application/x-www-form-urlencoded
                else
                {
                    // 获取 requestData值
                    string provider = await Request.Content.ReadAsStringAsync();
                    requestData = System.Web.HttpUtility.UrlDecode(provider.Substring(12));
                }
                // 组装请求请求到远程服务器
                HttpParamers paramers = HttpParamers.GetParamers();
                paramers.AddParamer("requestData", requestData);
                if (fileStream != null && fileStream.Length != 0)
                {
                    paramers.AddFile("file", new StreamFile(fileStream));
                }
                String response = Http.HttpClient.DoPost(config.AccessUrl, paramers, GetHttpHeader(), QiyuesuoConfig.CONNECT_TIMEOUT, QiyuesuoConfig.READ_TIMEOUT);
                return new HttpResponseMessage { Content = new StringContent(response) };
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
                return new HttpResponseMessage { StatusCode = System.Net.HttpStatusCode.InternalServerError, Content = new StringContent(e.Message) };
            }
            finally
            {
                fileStream.Close();
            }
        }

        /// <summary>
        /// 生成请求头
        /// </summary>
        /// <returns></returns>
        private HttpHeader GetHttpHeader()
        {
            long timestamp = (long)(DateTime.Now.ToUniversalTime() - DateTime.Parse("1970-1-1")).TotalMilliseconds;
            MD5 md5 = new MD5CryptoServiceProvider();
            string nonce = Guid.NewGuid().ToString();
            byte[] bSignature = md5.ComputeHash(System.Text.Encoding.UTF8.GetBytes(config.AccessToken + config.AccessSecret + timestamp.ToString() + nonce));
            string signature = StringUtils.ByteToString(bSignature, bSignature.Length);
            HttpHeader header = new HttpHeader(config.AccessToken, timestamp, signature, "proxy-sharp", nonce);
            return header;
        }

    }
}
