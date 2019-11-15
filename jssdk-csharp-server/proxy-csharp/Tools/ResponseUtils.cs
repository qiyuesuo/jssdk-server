using System;
using System.Text.RegularExpressions;

namespace proxy_csharp.Tools
{
    public class ResponseUtils
    {
        public static int GetResponseCode(string responseTxt)
        {
            return GetResponseNumber(responseTxt, "code");
        }

        public static string GetResponseMessage(string responseTxt)
        {
            return GetResponseString(responseTxt, "message");
        }

        public static int GetResponseNumber(string responseTxt, string mark)
        {
            string flag = "\"" + mark + "\":";
            int start = responseTxt.IndexOf(flag);
            if (start < 0)
                throw new Exception("远程服务器返回失败，" + responseTxt);

            int end = responseTxt.IndexOf(",", start);
            if (end < 0)
                end = responseTxt.Length - 1;

            start += flag.Length;
            string strCode = responseTxt.Substring(start, end - start);
            int code = 0;

            try
            {
                code = int.Parse(strCode);
            }
            catch (Exception e)
            {
                throw new Exception("远程服务器返回失败，原因：" + e.Message + " " + responseTxt);
            }
            return code;
        }

        public static string GetResponseString(string responseTxt, string mark)
        {
            string flag = "\"" + mark + "\":\"";
            int start = responseTxt.IndexOf(flag);
            if (start < 0)
            {
                return String.Empty;
            }

            start += flag.Length;
            int end = responseTxt.IndexOf("\"", start);
            if (end < 0)
                end = responseTxt.Length - 1;

            string message = responseTxt.Substring(start, end - start);
            if (string.IsNullOrEmpty(message))
                return message;
            return Regex.Unescape(message);
        }

        public static string GetResponseObjString(string responseTxt, string mark)
        {
            string flag = "\"" + mark + "\":";
            int start = responseTxt.IndexOf(flag);
            if (start < 0)
            {
                return String.Empty;
            }

            start += flag.Length;
            int end = responseTxt.IndexOf("}", start);
            if (end < 0)
                end = responseTxt.Length - 1;
            else
                end++;

            string message = responseTxt.Substring(start, end - start);

            return message;
        }

        public static string GetResponseOneObjString(string responseTxt, string mark)
        {
            string flag = "\"" + mark + "\":";
            int start = responseTxt.IndexOf(flag);
            if (start < 0)
                throw new Exception("远程服务器返回失败，" + responseTxt);

            start += flag.Length;
            int end = responseTxt.LastIndexOf("},");
            if (end < 0)
                end = responseTxt.Length - 1;
            else
                end++;

            string message = responseTxt.Substring(start, end - start);

            return message;
        }

        public static string GetResponseJsonString(string responseTxt, string mark)
        {
            string flag = "\"" + mark + "\":\"";
            int start = responseTxt.IndexOf(flag);
            if (start < 0)
                throw new Exception("远程服务器返回失败，" + responseTxt);

            start += flag.Length;
            int end = responseTxt.LastIndexOf("}\",");
            if (end < 0)
                end = responseTxt.Length - 1;
            else
                end++;

            string message = responseTxt.Substring(start, end - start);

            message = message.Replace("\\\"", "\"");

            return message;
        }

        public static string GetResponseListString(string responseTxt, string mark)
        {
            string flag = "\"" + mark + "\":";
            int start = responseTxt.IndexOf(flag);
            if (start < 0)
                throw new Exception("远程服务器返回失败，" + responseTxt);

            start += flag.Length;
            int end = responseTxt.LastIndexOf("]");
            if (end < 0)
                end = responseTxt.Length - 1;
            else
                end++;

            string message = responseTxt.Substring(start, end - start);

            return message;
        }

        public static string GetResponse(string responseTxt, string mark)
        {
            if (String.IsNullOrEmpty(responseTxt))
            {
                throw new Exception("服务器返回的相应报文为空!");
            }

            if (GetResponseCode(responseTxt) != 0)
            {
                throw new Exception("远程服务器返回失败，失败原因：" + GetResponseMessage(responseTxt));
            }

            string flag = "\"" + mark + "\":\"";
            int start = responseTxt.IndexOf(flag);
            if (start < 0)
                throw new Exception("远程服务器返回失败，" + responseTxt);
            start += flag.Length;
            int end = responseTxt.IndexOf("\"", start);
            if (end < 0)
                end = responseTxt.Length;

            string Data = responseTxt.Substring(start, end - start);

            return Data;
        }
    }
}
