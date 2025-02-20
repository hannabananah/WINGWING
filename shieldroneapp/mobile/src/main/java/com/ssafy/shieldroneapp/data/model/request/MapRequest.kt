package com.ssafy.shieldroneapp.data.model.request

/**
 * 출발지(드론 정류장) 검색을 위한 요청 모델
 *
 * @property keyword 사용자가 입력한 검색 키워드
 */
data class HiveSearchRequest(
    val keyword: String
)

/**
 * 도착지(집) 검색을 위한 카카오 API 요청 모델
 */
data class KakaoSearchRequest(
    val keyword: String,
    val page: Int = 1,
    val size: Int = 15,
    val sort: String = "accuracy"  // "accuracy" or "distance"
)

/**
 * 기본 도착지(집) 설정 요청 모델
 *
 * @property homeAddress 도로명 주소
 * @property lat 위도
 * @property lng 경도
 */
data class HomeLocationRequest(
    val homeAddress: String,
    val lat: Double,
    val lng: Double,
)

/**
 * 드론 배정 요청 모델
 *
 * @property hiveId 출발 정류장 ID
 * @property endLocation 도착지 위치 정보
 */
data class DroneRouteRequest(
    val hiveId: Int,
    val endLocation: EndLocation,
)

data class EndLocation(
    val lat: Double,
    val lng: Double,
)

/**
 * 드론 배정 취소 요청 모델
 * 10분 내 코드 미입력 시 사용
 */
data class DroneCancelRequest(
    val droneId: Int
)

/**
 * 드론 매칭 요청 모델
 * 드론에 표시된 코드 입력 시 사용
 */
data class DroneMatchRequest(
    val droneId: Int,
    val droneCode: Int
)