import { StyleSheet, View } from 'react-native';
import Button, { ButtonTypes } from '../components/Button';
import { useUserContext } from '../contexts/UserContext';
import AsyncStorage from '@react-native-async-storage/async-storage';

const W2003_Working = () => {
  const { setUser } = useUserContext();

  const handleLogout = async () => {
    try {
      // AsyncStorage에서 토큰 제거
      await AsyncStorage.removeItem('accessToken');
      await AsyncStorage.removeItem('refreshToken');
    
      // 사용자 정보를 null로 설정하여 로그아웃 상태로 변경
      setUser(null);
    } catch (error) {
      console.error('로그아웃 중 에러 발생:', error.message); // 에러 메시지 출력
    }
  };
  
  return (
    <View style={styles.container}>
      <Button
        title="로그아웃"
        onPress={handleLogout}
        buttonType={ButtonTypes.DANGER}
      />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    paddingHorizontal: 20,
  },
});

export default W2003_Working;
