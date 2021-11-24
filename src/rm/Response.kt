package rm

import java.io.Serializable

data class Response(val id: Int, val data: Serializable) : Serializable;