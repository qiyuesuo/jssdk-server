using proxy_csharp.Tools;
using System;
using System.Collections.Generic;
using System.IO;
using System.IO.Compression;
using System.Net;
using System.Text;
using System.Text.RegularExpressions;

namespace proxy_csharp.Http
{
    class HttpClient
    {
        public static string CONTENT_FORM = "application/x-www-form-urlencoded;charset=UTF-8";
        public static string CONTENT_MULTIPART = "multipart/form-data;charset=UTF-8; boundary=";
        public static string JSON_CONTENT_FORM = "application/json;charset=UTF-8";
        public static string DEFAULT_CHARSET = "UTF-8";
        public static string CONTENT_ENCODING_GZIP = "gzip";

        public static string DoService(string url, HttpParamers paramers, HttpHeader header, int connectTimeout, int readTimeout)
        {
            HttpMethod method = paramers.Method;
            try
            {
                switch (method)
                {
                    case HttpMethod.POST:
                        return DoPost(url, paramers, header, connectTimeout, readTimeout);
                    case HttpMethod.GET:
                        return DoGet(url, paramers, header, connectTimeout, readTimeout);
                    default:
                        throw new Exception("不支持的HTTP方法类型");
                }
            }
            catch (Exception e)
            {
                throw e;
            }


            throw new Exception("尝试访问服务器时异常！");
        }

        public static void DoDownload(string url, HttpParamers paramers, HttpHeader header, int connectTimeout, int readTimeout, ref Stream stream)
        {
            HttpWebRequest webRequest = null;
            try
            {
                string queryString = paramers.GetQueryString();
                string getURL = BuildGetUrl(url, queryString);

                webRequest = HttpConnection.GetRequest(getURL, HttpMethod.GET, header);
                webRequest.ReadWriteTimeout = readTimeout;
                webRequest.Timeout = connectTimeout;
                GetResponseAsOutputStream(webRequest, ref stream);
            }
            catch (Exception e)
            {
                throw e;
            }
        }

        public static string DoPost(string url, HttpParamers paramers, HttpHeader header, int connectTimeout, int readTimeout)
        {
            System.IO.Stream stream = null;

            HttpWebRequest webRequest = null;

            try
            {
                webRequest = HttpConnection.GetRequest(url, HttpMethod.POST, header);

                webRequest.ReadWriteTimeout = readTimeout;
                webRequest.Timeout = connectTimeout;
                if (paramers.IsMultipart())
                {
                    string boundary = "----sdkboundary" + StringUtils.Random(6); // 随机分隔线
                    webRequest.ContentType = CONTENT_MULTIPART + boundary;

                    Stream memStream = new MemoryStream();

                    WriteMutiContent(boundary, paramers, ref memStream);

                    webRequest.ContentLength = memStream.Length;

                    Stream requestStream = webRequest.GetRequestStream();

                    memStream.Position = 0;
                    byte[] tempBuffer = new byte[memStream.Length];
                    memStream.Read(tempBuffer, 0, tempBuffer.Length);
                    memStream.Close();

                    requestStream.Write(tempBuffer, 0, tempBuffer.Length);
                    requestStream.Close();
                }
                else if (paramers.IsJsonApplication())
                {
                    string jsonString = paramers.JsonParamer;
                    byte[] jsonBytes = System.Text.Encoding.UTF8.GetBytes(jsonString);
                    webRequest.ContentType = JSON_CONTENT_FORM;
                    stream = webRequest.GetRequestStream();
                    stream.Write(jsonBytes, 0, jsonBytes.Length);
                    stream.Close();
                }
                else
                {
                    string queryString = paramers.GetQueryString();
                    if (!String.IsNullOrEmpty(queryString))
                    {
                        byte[] queryBytes = System.Text.Encoding.UTF8.GetBytes(queryString);
                        webRequest.ContentType = CONTENT_FORM;
                        stream = webRequest.GetRequestStream();
                        stream.Write(queryBytes, 0, queryBytes.Length);
                        stream.Close();
                    }

                }

                return GetResponseAsString(webRequest);
            }
            catch (Exception e)
            {
                if (stream != null)
                    stream.Close();
                throw e;
            }

        }

        public static string DoGet(string url, HttpParamers paramers, HttpHeader header, int connectTimeout, int readTimeout)
        {
            HttpWebRequest webRequest = null;

            try
            {
                string queryString = paramers.GetQueryString();
                string getURL = BuildGetUrl(url, queryString);

                webRequest = HttpConnection.GetRequest(getURL, HttpMethod.GET, header);
                webRequest.ReadWriteTimeout = readTimeout;
                webRequest.Timeout = connectTimeout;

                return GetResponseAsString(webRequest);

            }
            catch (Exception e)
            {
                throw e;
            }
        }

        private static string BuildGetUrl(String url, String query)
        {
            if (String.IsNullOrEmpty(query))
                return url;

            bool hasQuery = url.Contains("?");
            bool hasPrepend = url.EndsWith("?") || url.EndsWith("&");

            string newUrl = url;

            if (!hasPrepend)
            {
                if (hasQuery)
                {
                    newUrl += "&";
                }
                else
                {
                    newUrl += "?";
                }
            }

            newUrl += query;
            return newUrl;
        }

        public static string GetResponseAsString(HttpWebRequest request)
        {
            string charset = GetResponseCharset(request.ContentType);
            string responseTxt = "";
            HttpWebResponse response = null;
            try
            {
                response = (HttpWebResponse)request.GetResponse();
                if ((int)response.StatusCode < 400)
                {
                    Stream st = response.GetResponseStream();
                    if (!String.IsNullOrEmpty(response.ContentEncoding))
                    {
                        if (response.ContentEncoding.ToLower().Contains("gzip"))
                        {
                            st = new GZipStream(st, CompressionMode.Decompress);
                        }
                    }

                    StreamReader sr = new StreamReader(st, Encoding.GetEncoding(charset));
                    responseTxt = sr.ReadToEnd();
                }
                else
                {
                    responseTxt = response.StatusDescription;
                }
            }
            catch (Exception e)
            {
                throw e;
            }
            finally
            {
                if (response != null)
                    response.Close();
            }
            if (string.IsNullOrEmpty(responseTxt))
                return responseTxt;
            return Regex.Unescape(responseTxt);

        }

        public static string GetResponseCharset(string ctype)
        {
            String charset = DEFAULT_CHARSET;

            if (!String.IsNullOrEmpty(ctype))
            {
                string[] paramers = ctype.Split(';');

                for (int i = 0; i < paramers.Length; i++)
                {
                    string paramer = paramers[i];
                    if (paramer.StartsWith("charset"))
                    {
                        string[] pair = paramer.Split(new char[] { '=' }, 2);
                        if (pair.Length == 2)
                        {
                            if (!String.IsNullOrEmpty(pair[1]))
                            {
                                charset = pair[1];
                            }
                        }
                        break;
                    }
                }
            }

            return charset;
        }

        /**
        * 写入multipart参数
        * 
        * @param boundary
        * @param paramers
        * @param out
        * @throws IOException
        */
        private static void WriteMutiContent(string boundary, HttpParamers paramers, ref Stream stream)
        {

            byte[] boundaryBytes = System.Text.Encoding.ASCII.GetBytes("\r\n--" + boundary + "\r\n");

            // 写入文本请求参数
            foreach (KeyValuePair<string, string> paramer in paramers.Paramers)
            {
                byte[] textBytes = GetTextEntry(paramer.Key, paramer.Value);
                stream.Write(boundaryBytes, 0, boundaryBytes.Length);
                stream.Write(textBytes, 0, textBytes.Length);
            }

            // 写入文件请求参数
            if (paramers.FileStreams.Count > 0)
            {
                foreach (KeyValuePair<string, IFileItem> file in paramers.FileStreams)
                {
                    IFileItem fileItem = file.Value;
                    if (!fileItem.IsValid())
                        throw new Exception("无效的文件流");

                    byte[] fileBytes = GetFileEntry(file.Key, fileItem.GetFileName(), fileItem.GetMimeType());
                    stream.Write(boundaryBytes, 0, boundaryBytes.Length);
                    stream.Write(fileBytes, 0, fileBytes.Length);
                    fileItem.Write(ref stream);
                }
            }

            //写入批量文件请求
            if (paramers.Files.Count > 0)
            {
                foreach (KeyValuePair<string, List<IFileItem>> listEntry in paramers.Files)
                {
                    List<IFileItem> items = listEntry.Value;
                    foreach (IFileItem item in items)
                    {
                        if (!item.IsValid())
                            throw new Exception("无效的文件流");

                        byte[] fileBytes = GetFileEntry(listEntry.Key, item.GetFileName(), item.GetMimeType());
                        stream.Write(boundaryBytes, 0, boundaryBytes.Length);
                        stream.Write(fileBytes, 0, fileBytes.Length);
                        item.Write(ref stream);
                    }
                }

            }


            // 添加请求结束标志
            byte[] endBoundary = System.Text.Encoding.ASCII.GetBytes("\r\n--" + boundary + "--\r\n");
            stream.Write(endBoundary, 0, endBoundary.Length);
        }

        private static byte[] GetTextEntry(string fieldName, string fieldValue)
        {
            StringBuilder entry = new StringBuilder();
            entry.Append("Content-Disposition:form-data;name=\"");
            entry.Append(fieldName);
            entry.Append("\"\r\nContent-Type:text/plain\r\n\r\n");
            entry.Append(fieldValue);
            return System.Text.Encoding.UTF8.GetBytes(entry.ToString());
        }

        private static byte[] GetFileEntry(string fieldName, string fileName, string mimeType)
        {
            StringBuilder entry = new StringBuilder();
            entry.Append("Content-Disposition:form-data;name=\"");
            entry.Append(fieldName);
            entry.Append("\";filename=\"");
            entry.Append(fileName);
            entry.Append("\"\r\nContent-Type:");
            entry.Append(mimeType);
            entry.Append("\r\n\r\n");
            return System.Text.Encoding.UTF8.GetBytes(entry.ToString());
        }

        public static string getStreamAsString(System.IO.Stream stream, string charset)
        {
            using (System.IO.StreamReader reader = new System.IO.StreamReader(stream, Encoding.GetEncoding(charset)))
            {
                return reader.ReadToEnd();
            }
        }

        private static void GetResponseAsOutputStream(WebRequest request, ref Stream outputStream)
        {
            HttpWebResponse response = (HttpWebResponse)request.GetResponse();
            if ((int)response.StatusCode < 400)
            {
                Stream responseStream = response.GetResponseStream();
                byte[] temp_t = new byte[8192];
                int n;
                while ((n = responseStream.Read(temp_t, 0, temp_t.Length)) > 0)
                {
                    outputStream.Write(temp_t, 0, n);
                }

            }
            else
            {// Client Error 4xx and Server Error 5xx
                response.Close();
                throw new IOException(response.StatusCode.ToString() + " " + response.StatusDescription);
            }

            response.Close();
        }
    }
}
