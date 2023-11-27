// api/auth.js
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8099'; // Spring Boot API의 기본 URL로 변경

export const signIn = async (email, password) => {
  try {
    const response = await axios.post(
      `${API_BASE_URL}/api/members/login`,
      { email, password }
    );
  
    console.log(response); // 서버 응답을 콘솔에 출력
  
    // 서버에서 반환한 데이터를 기반으로 로그인 성공 여부를 판단
    if (response.data === '로그인 성공') {
      return email;
    } else {
      throw new Error('이메일 혹은 비밀번호가 올바르지 않습니다.');
    }
  } catch (error) {
    throw new Error('서버와의 통신 중 오류가 발생했습니다.');
  }
};