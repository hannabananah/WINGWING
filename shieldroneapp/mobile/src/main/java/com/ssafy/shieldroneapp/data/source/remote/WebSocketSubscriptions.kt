package com.ssafy.shieldroneapp.data.source.remote

/**
 * WebSocket을 통해 서버로부터 수신하는 알림 및 메시지를 구독하고 처리하는 클래스.
 *
 * 주로 위험 상황 알림을 단계별로 처리하며, 각 단계에 맞는 UI 동작을 수행
 *
 * 주요 메서드
 * - subscribeToDangerAlerts(): 위험 상황 알림을 구독하고 처리
 *
 * 알림 처리 방식:
 * - 1단계, 2단계: 알림을 표시하고 5초 후 자동으로 닫힘
 * - 3단계: 알림을 표시하며, 사용자가 '괜찮습니다. 위험하지 않습니다.'를 선택할 경우,
 *   서버에 응답을 전송하는 로직이 필요 (이 부분은 WebSocketMessageSender에서 처리)
 *
 * 이 클래스는 WebSocketService에 의해 import되어 사용됩니다.
 * 또한 WebSocketMessageParser를 import하여 수신한 메시지를 파싱합니다.
 */


import android.content.Context
import android.util.Log
import com.google.android.gms.wearable.Wearable
import com.google.gson.Gson
import com.ssafy.shieldroneapp.data.model.AlertData
import com.ssafy.shieldroneapp.data.repository.AlertRepository
import com.ssafy.shieldroneapp.services.alert.AlertService
import com.ssafy.shieldroneapp.ui.map.AlertHandler
import com.ssafy.shieldroneapp.utils.await
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class WebSocketSubscriptions @Inject constructor(
    private val messageParser: WebSocketMessageParser,
    private val alertRepository: AlertRepository,
    private val alertHandler: AlertHandler,
    private val alertService: AlertService,
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val TAG = "모바일: 웹소켓 구독"
        private const val PATH_DANGER_ALERT = "/danger_alert"
    }

    private val messageClient = Wearable.getMessageClient(context)
    private val subscriptionScope = CoroutineScope(Dispatchers.IO)
    private val gson = Gson()

    fun handleIncomingMessage(message: String) {
        subscriptionScope.launch {
            try {
                val messageType = messageParser.getMessageType(message)

                when (messageType) {
                    "sendWarning" -> {
                        val warningData = messageParser.parseWarningMessage(message)
                        if (warningData != null) {
                            // UI 업데이트
                            alertHandler.handleWarningBeep(warningData.warningFlag)
                            // 데이터 상태 관리
                            alertRepository.updateAlertState(warningData.warningFlag)
                            // 시스템 알림 처리 (소리, 진동 등)
                            alertService.handleWarningBeep(warningData.warningFlag)
                            // 워치로 알림 전송
                            sendDangerAlertToWatch(warningData.warningFlag)
                        }
                    }
                    null -> Log.e(TAG, "메시지 타입을 찾을 수 없음")
                    else -> Log.d(TAG, "처리되지 않은 메시지 타입: $messageType")
                }
            } catch (e: Exception) {
                Log.e(TAG, "메시지 처리 중 오류 발생", e)
            }
        }
    }

    private suspend fun sendDangerAlertToWatch(warningFlag: Boolean) {
        try {
            val nodes = Wearable.getNodeClient(context).connectedNodes.await(5000)
            if (nodes.isEmpty()) {
                Log.e(TAG, "연결된 워치가 없음")
                return
            }

            val alertData = AlertData(
                warningFlag = warningFlag,
                timestamp = System.currentTimeMillis()
            )

            val alertJson = gson.toJson(alertData)

            nodes.forEach { node ->
                try {
                    messageClient.sendMessage(
                        node.id,
                        PATH_DANGER_ALERT,
                        alertJson.toByteArray()
                    ).await(5000)
                    Log.d(TAG, "워치로 위험 알림 전송 성공 - node: ${node.displayName}, warningFlag: $warningFlag")
                } catch (e: Exception) {
                    Log.e(TAG, "워치로 위험 알림 전송 실패 - node: ${node.displayName}", e)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "워치 알림 전송 중 오류 발생", e)
            e.printStackTrace()
        }
    }
}