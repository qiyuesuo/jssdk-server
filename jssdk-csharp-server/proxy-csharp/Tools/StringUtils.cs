using System;
using System.Text;

namespace proxy_csharp.Tools
{
    class StringUtils
    {
        public static string RANDOM_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        public static string ByteToString(byte[] InBytes, int len)
        {
            string StringOut = "";
            for (int i = 0; i < len; i++)
            {
                StringOut = StringOut + String.Format("{0:X2}", InBytes[i]);
            }
            return StringOut;
        }


        //随机数
        public static string Random(int length)
        {
            if (length < 1)
            {
                return null;
            }
            Random random = new Random();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < length; i++)
            {
                int number = random.Next(RANDOM_CHARACTERS.Length);
                sb.Append(RANDOM_CHARACTERS.ToCharArray()[number]);
            }
            return sb.ToString();
        }
    }
}
