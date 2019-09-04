using Microsoft.AspNetCore.Mvc;

namespace cardinal.Controllers
{
    [Route("[controller].jsp")]
    [ApiController]
    public class ReturnController : Controller
    {

        [HttpPost]
        public ActionResult AuthCallback()
        {
            var TransactionId = HttpContext.Request.Form["TransactionId"];
            var Response = HttpContext.Request.Form["Response"];
            var MD = HttpContext.Request.Form["MD"];
            ViewBag.TransactionId = TransactionId;
            ViewBag.Resp = Response;
            ViewBag.MD = MD;
            return View();
        }

    }

}
