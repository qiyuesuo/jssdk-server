using proxy_csharp.Tools;
using System;
using System.Collections.Generic;
using System.Web;

namespace proxy_csharp.Http
{
    public class HttpParamers
    {
        private string jsonParamer;
        private Dictionary<string, string> paramers = new Dictionary<string, string>();
        private Dictionary<String, IFileItem> fileStreams = new Dictionary<string, IFileItem>();
        private Dictionary<String, List<IFileItem>> files = new Dictionary<string, List<IFileItem>>();


        public HttpParamers(HttpMethod method)
        {
            this.Method = method;
        }

        public static HttpParamers PostParamers()
        {
            return new HttpParamers(HttpMethod.POST);
        }

        public static HttpParamers PostJsonParamers(string json)
        {
            HttpParamers paramers = new HttpParamers(HttpMethod.POST);
            paramers.JsonParamer = json;
            return paramers;
        }

        public static HttpParamers GetParamers()
        {
            return new HttpParamers(HttpMethod.GET);
        }

        public HttpParamers AddParamer(string key, string value)
        {
            Paramers.Add(key, value);
            return this;
        }

        public HttpParamers AddFile(string key, IFileItem stream)
        {
            FileStreams.Add(key, stream);
            return this;
        }

        public HttpParamers AddFiles(string key, List<IFileItem> items)
        {
            Files.Add(key, items);
            return this;
        }


        public bool IsMultipart()
        {
            return FileStreams.Count != 0 || Files.Count != 0;
        }

        public bool IsJsonApplication()
        {
            return JsonParamer != null && JsonParamer.Length != 0;
        }

        public string GetQueryString()
        {
            if (Paramers == null || Paramers.Count == 0)
                return null;

            string queryString = "";
            foreach (KeyValuePair<string, string> paramer in Paramers)
            {
                String name = paramer.Key;
                String value = paramer.Value;
                // 忽略参数名或参数值为空的参数
                if (!String.IsNullOrEmpty(name) && !String.IsNullOrEmpty(value))
                {
                    queryString += "&";
                    queryString += name;
                    queryString += "=";

                    queryString += HttpUtility.UrlEncode(value, System.Text.Encoding.UTF8); //编码 UrlEncode(value);
                }
            }
            return queryString.Substring(1);
        }

        internal HttpMethod Method { get; set; }
        public Dictionary<string, string> Paramers { get => paramers; set => paramers = value; }
        internal Dictionary<string, IFileItem> FileStreams { get => fileStreams; set => fileStreams = value; }
        internal Dictionary<string, List<IFileItem>> Files { get => files; set => files = value; }
        public string JsonParamer { get => jsonParamer; set => jsonParamer = value; }
    }
}
