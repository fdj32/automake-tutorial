using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;

namespace cardinal.Controllers
{
    [Route("[controller].jsp")]
    [ApiController]
    public class AmsController : ControllerBase
    {
        // GET ams.jsp
        [HttpGet]
        public ActionResult<string> Get()
        {
            return Request.QueryString.ToString();
        }
    }
}
