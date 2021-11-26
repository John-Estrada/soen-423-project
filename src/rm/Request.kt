package rm

import java.io.Serializable

data class Request(val id: Int, val method: Method) : Serializable;