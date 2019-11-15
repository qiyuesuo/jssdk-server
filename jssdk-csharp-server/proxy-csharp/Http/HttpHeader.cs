namespace proxy_csharp.Http
{
    class HttpHeader
    {
        private string contentType;
        private double timestamp;
        private string accessToken;
        private string signature;
        private string version;

        public string ContentType { get => contentType; set => contentType = value; }
        public double Timestamp { get => timestamp; set => timestamp = value; }
        public string AccessToken { get => accessToken; set => accessToken = value; }
        public string Signature { get => signature; set => signature = value; }
        public string Version { get => version; set => version = value; }


        public HttpHeader(string accessToken, double timestamp, string signature, string version)
        {
            this.AccessToken = accessToken;
            this.Timestamp = timestamp;
            this.Signature = signature;
            this.Version = version;
        }

    }
}
