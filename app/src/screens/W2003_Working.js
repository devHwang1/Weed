import { StyleSheet, View } from 'react-native';
import Button, { ButtonTypes } from '../components/Button';
import { useUserContext } from '../contexts/UserContext';
import AsyncStorage from '@react-native-async-storage/async-storage';

const W2003_Working = () => {
  const { setUser } = useUserContext();

  const handleLogout = async () => {
    // AsyncStorage에서 토큰 제거
    await AsyncStorage.removeItem('token');
    setUser(null);
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
