using System.Collections.Generic;

namespace proxy_csharp.Tools
{
    public class ParamContainer : Dictionary<string, object>
    {
        public static ParamContainer NewInstance(string key, object value)
        {
            ParamContainer container = new ParamContainer();
            if (!string.IsNullOrEmpty(key) && value != null)
            {
                container.Add(key, value);
            }
            return container;
        }

        public ParamContainer AddItem(string key, object value)
        {
            if (!string.IsNullOrEmpty(key) && value != null)
            {
                this.Add(key, value);
            }
            return this;
        }
    }
}
