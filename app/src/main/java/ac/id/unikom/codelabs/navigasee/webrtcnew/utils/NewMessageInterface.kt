package ac.id.unikom.codelabs.navigasee.webrtcnew.utils

import ac.id.unikom.codelabs.navigasee.webrtcnew.model.MessageModel


interface NewMessageInterface {
    fun onNewMessage(message: MessageModel)
}