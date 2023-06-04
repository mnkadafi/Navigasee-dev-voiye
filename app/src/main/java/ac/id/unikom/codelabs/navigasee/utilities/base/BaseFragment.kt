package ac.id.unikom.codelabs.navigasee.utilities.base

import ac.id.unikom.codelabs.navigasee.utilities.extensions.*
import ac.id.unikom.codelabs.navigasee.utilities.helper.EventObserver
import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

abstract class BaseFragment<T : BaseViewModel> : Fragment() {

    lateinit var mParentVM: T
    private var mMessageType = MESSAGE_TYPE_SNACK
    private lateinit var progressDialog: ProgressDialog

    override fun onViewCreated(paramView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(paramView, savedInstanceState)
        mParentVM.apply {
            showMessage.observe(this@BaseFragment, EventObserver {
                if (!it.isEmpty()) {
                    when (mMessageType) {
                        MESSAGE_TYPE_SNACK_CUSTOM -> {
                            view?.showCustomSnackBar(it)
                        }
                        MESSAGE_TYPE_SNACK -> {
                            view?.showSnackBar(it)
                        }
                        else -> {
                            requireContext().showToast(it)
                        }
                    }
                }
            })

            showMessageRes.observe(this@BaseFragment, EventObserver {
                if (it != 0) {
                    when (mMessageType) {
                        MESSAGE_TYPE_SNACK_CUSTOM -> {
                            view?.showCustomSnackBarRes(it)
                        }
                        MESSAGE_TYPE_SNACK -> {
                            view?.showSnackBarRes(it)
                        }
                        else -> {
                            requireContext().showToast(it)
                        }
                    }
                }
            })

            isRequesting.observe(this@BaseFragment, EventObserver {
                if (it == true) {
                    progressDialog.show()
                } else {
                    progressDialog.dismiss()
                }
            })
        }
        onCreateObserver(mParentVM)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setContentData()

        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("")
        progressDialog.setMessage("Loading...")
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog.setCancelable(false)

        mMessageType = setMessageType()
    }


    abstract fun onCreateObserver(viewModel: T)
    abstract fun setContentData()
    abstract fun setMessageType(): String

    companion object {
        const val MESSAGE_TYPE_TOAST = "TOAST_TYPE"
        const val MESSAGE_TYPE_SNACK = "SNACK_TYPE"
        const val MESSAGE_TYPE_SNACK_CUSTOM = "SNACK_CUSTOM_TYPE"
    }

}