using System;
using System.IO;

namespace proxy_csharp.Tools
{
    public class StreamFile : IFileItem
    {
        /** 默认媒体类型 **/
        public static string MIME_TYPE_DEFAULT = "application/octet-stream";
        /** 默认流式读取缓冲区大小 **/
        public static int READ_BUFFER_SIZE = 1024 * 4;

        private string fileName;
        private Stream stream;
        private string mimeType;

        private static string DEFAULT_FILE_NAME = "streamFile";

        public StreamFile(string fileName, Stream stream, string mimeType)
        {
            this.fileName = fileName;
            this.stream = stream;
            this.mimeType = mimeType;
        }

        public StreamFile(string fileName, Stream stream)
        {
            this.fileName = fileName;
            this.stream = stream;
            this.mimeType = MIME_TYPE_DEFAULT;
        }

        public StreamFile(Stream stream)
        {
            this.fileName = DEFAULT_FILE_NAME;
            this.stream = stream;
            this.mimeType = MIME_TYPE_DEFAULT;
        }

        public long GetFileLength()
        {
            return 0L;
        }

        public string GetFileName()
        {
            return this.fileName;
        }

        public string GetMimeType()
        {
            if (this.mimeType == null)
            {
                return MIME_TYPE_DEFAULT;
            }
            else
            {
                return this.mimeType;
            }
        }

        public bool IsValid()
        {
            return this.stream != null && !String.IsNullOrEmpty(this.fileName);
        }

        public void Write(ref Stream output)
        {
            try
            {
                long sLen = stream.Length;
                byte[] buffer = new byte[READ_BUFFER_SIZE];
                int n = 0;
                while (sLen > 0)
                {
                    n = stream.Read(buffer, 0, READ_BUFFER_SIZE);
                    sLen -= n;
                    output.Write(buffer, 0, n);
                }
            }
            finally
            {
                if (stream != null)
                {
                    stream.Close();
                }
            }
        }
    }
}
