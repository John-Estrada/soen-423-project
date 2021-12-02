package rm

import java.io.Serializable

data class Request(val requestId: Int, val method: Method) : Serializable;
