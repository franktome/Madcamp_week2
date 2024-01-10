앱 기능 요약
=
- 설정 시간의 잔여석 현황 확인
- 잔여석 예약 
- 예약 변경(자리 및 시간)
- 예약 취소
- 예약 내역 확인

앱 상세 설명
=
개발 스택
-
- Android Studio(java)
- Flask
- MySQL

로그인 화면
- 
- 진입 화면에 "카카오 로그인" 버튼이 있습니다. 기기에 카카오톡 앱이 설치되어 있을 때는 카카오톡 앱으로 연동이 되고, 카카오톡이 설치되지 않았다면 '카카오 계정으로 로그인'창이 열립니다.

![KakaoTalk_20240110_183534719_011](https://github.com/franktome/madcamp_week2/assets/102137004/6cd840a2-fd16-439f-a96d-b56dd7ab07f7)



예약 및 변경 탭
- 
- 시간을 설정하고 열람실 별 잔여석 현황을 확인하고 예약을 할 수 있습니다.
- 시작 시간이 종료 시간보다 뒤로 설정할 경우, "시간을 정확히 입력하세요."라는 경고창과 함께 현황 확인/예약이 불가능합니다.

![KakaoTalk_20240110_183534719_01](https://github.com/franktome/madcamp_week2/assets/102137004/10458acd-8c3d-494e-be84-7b3baf8f68a0) ![KakaoTalk_20240110_183534719_09](https://github.com/franktome/madcamp_week2/assets/102137004/989fe50c-76c2-4090-9dd7-194cba7b2c4c)


- 열람실 버튼을 클릭하면 좌석표가 나타납니다. 예약이 불가능한 좌석들은 회색으로 표시되며, 선택이 불가합니다.
- 설정 시간이 화면에 표시됩니다.
- 선택된 좌석은 파랑색으로 변합니다.
- 예약하기 버튼을 누르면 "예약이 완료되었습니다."라는 알림창이 뜹니다.
- 이전화면 버튼을 누르면 좌석 현황 확인 페이지로 돌아갑니다.

![KakaoTalk_20240110_183534719_08](https://github.com/franktome/madcamp_week2/assets/102137004/4aaef8cb-104b-4313-b9a3-20d0ca8eadc1) ![KakaoTalk_20240110_183534719_02](https://github.com/franktome/madcamp_week2/assets/102137004/5e897624-1c9a-4163-8057-14a3404482fb)

- 예약내역이 있을 때는 해당 좌석이 초록색으로 표시됩니다.
- 현재 이용 중인 좌석을 선택해 이용시간을 변경하거나, 다른 좌석으로 이동을 할 수 있습니다.
- 예약을 변경하면 "예약 변경되었습니다."라는 알림창이 나타납니다.

![KakaoTalk_20240110_183534719_06](https://github.com/franktome/madcamp_week2/assets/102137004/79d9243a-3c43-400a-b2c2-6308c83a39bd) ![KakaoTalk_20240110_183534719_05](https://github.com/franktome/madcamp_week2/assets/102137004/1c621d69-e007-406d-a74b-5e8dc4bdee3c)



마이페이지 탭
-
- 예약 내역을 확인할 수 있습니다. 예약 내역이 존재하지 않을 때는 "해당값이 없음"이라고 표시됩니다.
- 예약 취소하기 버튼을 누르면 "예약이 취소되었습니다." 알림창이 뜨며 예약이 취소되고, 예약 현황이 "해당값이 없음"으로 변경됩니다. 

![KakaoTalk_20240110_183534719_03](https://github.com/franktome/madcamp_week2/assets/102137004/7b056946-6a3d-4e25-89f4-50099ad7c334) ![KakaoTalk_20240110_183534719_04](https://github.com/franktome/madcamp_week2/assets/102137004/76687d33-7f96-4c0e-ac49-fcb7c7a63457)


## 데이터베이스(MySQL)

![users_table](https://github.com/franktome/madcamp_week2/assets/154505487/7ab191c0-6ca5-4035-ad42-96adcb8d0fde)
![occupied_seatsA,B,C](https://github.com/franktome/madcamp_week2/assets/154505487/40d25aed-95e0-4253-a6b8-689fb446dd23)
![occupied_users_tables](https://github.com/franktome/madcamp_week2/assets/154505487/f790c761-79fb-4e89-98c4-5b68f59dd172)




  
