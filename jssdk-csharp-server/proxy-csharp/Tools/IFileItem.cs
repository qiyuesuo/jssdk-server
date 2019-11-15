using System.IO;

namespace proxy_csharp.Tools
{
    public interface IFileItem
    {
        bool IsValid();

        string GetFileName();

        string GetMimeType();

        long GetFileLength();

        void Write(ref Stream output);
    }
}
