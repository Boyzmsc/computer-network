# Simple Protocol

## Sender 

Bit-stuffing 결과를 MLT-3 알고리즘의 입력값으로 넣어 결과를 출력

1) Data-link layer에서 보내고자 하는 Frame : 11111111

2) Bit-stuffing 결과 : 111110111

3) Physical layer에서 MLT-3 처리 결과 : +0-0++0-0

## Receiver 

#### Physical layer

Sender가 보낸 Bit-stream을 MLT-3 알고리즘을 이용하여 0과 1의 Bit-stream으로 변환

#### Data-link layer

Bit-unstuffing 적용
