package network;
/* 네트워크란 데이터를 주고받을 수 있는 경로
 * 데이터를 주고받는 방법은 여러가지가 있음
 * -OSI7 Layer, TCP/IP같은게 있음
 * 보내는 사람과 받는 사람의 주소가 필요 => IP
 * IP는 각각 한 바이트씩 4바이트를 가짐(IPv4) =>32비트
 * 한사람당사용하는 아이피주소가 하나가 아니기 때문에
 * IPv4로는 감당이 안됨 =>IPv6 (더복잡해짐)
 * 주소가 복잡해서 찾아가는데 어려움을 겪음 
 * -> 인간에게 익숙한 주소체계로 표현해줌 =>'도메인네임'
 * 도메인네임은 아이피주소와 연결되어있음 
 * 도메인네임을 여러개로 표현할 수 있음
 * 
 * 
 * <Port & Protocol>
 * Port: 단말기 내부에도 영역이 나뉘어있다. 그 영역의 주소를 포트라고 한다. 예)숭실대(IP)의 형남관(Port)
 * Protocol:통신규약->누구를 통해서 어떤형식으로 데이터를 전송할지
 * 
 * ##프로토콜##
 * < TCP & UDP >
 * TCP(신뢰할수있는프로토콜) : 데이터는 전기적신호이기때문에 손실이 일어날 수 있음 .그래서 TCP는 제대로 전달이 될 때까지 계속 다시 보내준다.(like 등기)
 * UDP(신뢰할수없는프로토콜): 중간에 소실되든 말든 그냥 계속 보낼거 보내는 것. 제대로보내졌는지확인 X(like 편지) ->데이터가많고 끊겨도 상관없는 경우에 유용함(예.동영상)
 * 
 * 
 *1대다통신은 HTTP
 *1대1통신은 소켓연결해서 사용
 *
 *
 * */
public class A_Network_Intro {

}
