package ac.id.unikom.codelabs.navigasee.webrtcnew.model

data class MessageModel(
     val type: String,
     val name: String? = null,
     val target: String? = null,
     val data:Any?=null
)
