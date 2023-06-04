package ac.id.unikom.codelabs.navigasee.utilities.base

import ac.id.unikom.codelabs.navigasee.utilities.helper.Preferences

open class BaseRepository {
    val token = Preferences.getInstance().getToken()

}