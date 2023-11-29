// api/auth.js
import axios from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';

export const signIn = async (email, password) => {
  try {
    const response = await axios.post(
      "http://10.100.203.31:8099/api/app/member/login",
      { email, password },
      { 
        headers: { "Content-Type": "application/json" },
        timeout: 5000 // milliseconds (5 seconds)
      }
    );
    console.log(response);
    console.log(response.data); // 서버에서 받은 데이터
    console.log("~!!@#@E");


    if (response.data !== null && response.data !== "") {
      console.log('signIn 함수 호출');
      console.log('이메일:', email);
      console.log('비밀번호:', password);

      // 토큰을 AsyncStorage에 저장
      await AsyncStorage.setItem('token', response.data);

     
      const storedToken = await AsyncStorage.getItem('token');
      console.log('Stored Token:', storedToken);
      
      return Promise.resolve({
        email: email,
        // 다른 사용자 정보를 추가하려면 여기에 추가
      });
    } else {
      console.log("로그인 실패", "아이디나 비밀번호를 확인하세요.");
      // 로그인 실패 시에는 Promise를 통해 에러 메시지 반환
      return Promise.reject(new Error("아이디나 비밀번호를 확인하세요.~~~~~~~~"));
    }
  } catch (error) {
    if (axios.isCancel(error)) {
      // Timeout 에러인 경우
      console.log("서버 응답이 지연되었습니다. 잠시 후 다시 시도하세요.");
      return Promise.reject(new Error("서버 응답이 지연되었습니다. 잠시 후 다시 시도하세요."));
    } else if (error.response && error.response.status === 401) {
      // 401 에러는 인증 실패를 의미하므로 로그인 실패 메시지 반환
      console.log("로그인 실패", "아이디나 비밀번호를 확인하세요.");
      return Promise.reject(new Error("아이디나 비밀번호를 확인하세요."));
    } else {
      // 그 외의 경우에는 서버와의 통신 중 오류 메시지 반환
      console.log(`Error Message: ${error}`);
      return Promise.reject(new Error('서버와의 통신 중 오류가 발생했습니다.'));
    }
  }
};