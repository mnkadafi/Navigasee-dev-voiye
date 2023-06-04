package ac.id.unikom.codelabs.navigasee.mvvm.login

import ac.id.unikom.codelabs.navigasee.utilities.base.BaseUserActionListener

interface LoginUserActionListener : BaseUserActionListener {
    fun login(role: String)
}