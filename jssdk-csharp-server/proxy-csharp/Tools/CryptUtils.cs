using System;
using System.IO;
using System.Security.Cryptography;
using System.Text;

namespace proxy_csharp.Tools
{
    /// <summary>
    /// AES 工具类用于解密回调数据
    /// </summary>
    public class CryptUtils
    {
        /// <summary>
        /// AES解密
        /// </summary>
        /// <param name="encrypt">解密原文</param>
        /// <param name="secret">密钥</param>
        /// <returns></returns>
        public static string AESDecrypt(string encrypt, string secret)
        {
            try
            {
                byte[] encryptBytes = Convert.FromBase64String(Encoding.UTF8.GetString(Encoding.UTF8.GetBytes(encrypt)));
                SymmetricAlgorithm aes = Rijndael.Create();
                aes.Mode = CipherMode.ECB;
                aes.Padding = PaddingMode.Zeros;
                aes.Key = Encoding.UTF8.GetBytes(secret);
                byte[] decryptBytes = new byte[encryptBytes.Length];
                MemoryStream memoryStream = new MemoryStream(encryptBytes);
                CryptoStream cryptoStream = new CryptoStream(memoryStream, aes.CreateDecryptor(), CryptoStreamMode.Read);
                cryptoStream.Read(decryptBytes, 0, decryptBytes.Length);
                cryptoStream.Close();
                memoryStream.Close();
                aes.Clear();
                return Encoding.UTF8.GetString(decryptBytes);
            }
            catch (Exception e)
            {
                throw e;
            }
        }

        /// <summary>
        /// MD5
        /// </summary>
        /// <param name="unEncryted"></param>
        /// <returns></returns>
        public static string Md5(string unEncryted)
        {
            MD5 md5 = MD5.Create();
            byte[] bufstr = Encoding.GetEncoding("GBK").GetBytes(unEncryted);
            byte[] hashstr = md5.ComputeHash(bufstr);
            string md5Str = string.Empty;
            for (int i = 0; i < hashstr.Length; i++)
            {
                md5Str += hashstr[i].ToString("X");
            }
            return md5Str.ToLower();
        }
    }
}
