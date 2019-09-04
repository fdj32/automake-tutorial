using System.Net.Http;
using Microsoft.AspNetCore.Mvc;

namespace cardinal.Controllers
{
    [Route("[controller].jsp")]
    [ApiController]
    public class AmsController : ControllerBase
    {
        // GET ams.jsp
        [HttpGet]
        public ActionResult<Result> Get()
        {
            var amsURL = "https://mstest.active.com/MS/MSServer";
            var fullURL = amsURL + Request.QueryString.ToString();
            HttpClient client = new HttpClient();
            var result = client.GetStringAsync(fullURL).Result;
            result = result.Replace("\n", "");
            System.Console.WriteLine(">>>" + result);
            return new Result(result);
        }
    }

    public class Result
    {
        public string result;

        public Result(string _result)
        {
            result = _result;
        }
    }
}
