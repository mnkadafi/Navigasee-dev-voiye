package ac.id.unikom.codelabs.navigasee.mvvm.video_call

import ac.id.unikom.codelabs.navigasee.R
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_video_call.*

class VideoCallFragment : Fragment() {

    private var callEvents: OnCallEvents? = null

    interface OnCallEvents {
        fun onCallHangUp()
        fun onCameraSwitch()
        fun onToggleMic(): Boolean
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_video_call, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        flDisconnect.setOnClickListener { callEvents?.onCallHangUp() }
        flSwitchCamera.setOnClickListener { callEvents?.onCameraSwitch() }
        flMic.setOnClickListener {
            val isMute = callEvents?.onToggleMic()
            flMic.alpha = if (!isMute!!) 1F else 0.3F
        }
    }

    @SuppressWarnings("deprecation")
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        callEvents = activity as OnCallEvents
    }
}
