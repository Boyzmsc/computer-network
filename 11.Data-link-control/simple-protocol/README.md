# Simple-Protocol 

## Sender 

* bit-stuffing 결과를 MLT-3 알고리즘의 입력값으로 넣어 결과를 출력하시오

1) Data-link layer에서 보내고자 하는 frame  : 11111111

2) bit-stuffing 결과 : 111110111

3) Physical layer에서 MLT-3 처리 결과 :  +0-0++0-0



## Receiver 

* Physical layer

Sender가 보낸 bit-stream (예 : +0-0++0-0 )을 MLT-3 알고리즘을 이용하여 0과 1의 bit stream으로 변환 (예 : 111110111)

* Data-link layer

bit-unstuffing 적용 (예 : 111110111 -> 11111111)