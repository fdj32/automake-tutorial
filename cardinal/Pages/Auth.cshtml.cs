using Microsoft.AspNetCore.Mvc.RazorPages;

namespace cardinal.Pages
{
    public class AuthModel : PageModel
    {
        public string postUrl { get; set; }
        public string jwt { get; set; }
        public string md { get; set; }
        public void OnGet()
        {
            var qs = Request.QueryString.ToString();
            qs = qs.Replace("\n", "");
            qs = qs.Substring(1);
            string[] kvs = qs.Split("&");
            foreach (string kv in kvs)
            {
                if (kv.ToUpper().StartsWith("POSTURL"))
                {
                    postUrl = kv.Split("=")[1];
                }
                if (kv.ToUpper().StartsWith("JWT"))
                {
                    jwt = kv.Split("=")[1];
                }
                if (kv.ToUpper().StartsWith("MD"))
                {
                    md = kv.Split("=")[1];
                }
            }
        }
    }
}