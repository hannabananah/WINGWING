package com.ssafy.shieldroneapp.services

import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.ssafy.shieldroneapp.viewmodels.SensorViewModel

class WearableService : WearableListenerService() {
    companion object {
        const val PATH_EMERGENCY_ALERT = "/emergency_alert"
        private var sensorViewModel: SensorViewModel? = null

        fun setSensorViewModel(viewModel: SensorViewModel?) {
            sensorViewModel = viewModel
        }

        fun getSensorViewModel(): SensorViewModel? {
            return sensorViewModel
        }
    }

//    override fun onMessageReceived(messageEvent: MessageEvent) {
//        super.onMessageReceived(messageEvent)
//
//        when (messageEvent.path) {
//            PATH_EMERGENCY_ALERT -> {
//                CoroutineScope(Dispatchers.Main).launch {
//                    sensorViewModel?.showAlert()
//                }
//            }
//        }
//    }
}