import { useState } from 'react';
import { Text, View, TouchableOpacity, StyleSheet } from 'react-native';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import QRCode from 'react-native-qrcode-svg';
import AsyncStorage from '@react-native-async-storage/async-storage';

const W2002_QRcode = () => {
  const { bottom } = useSafeAreaInsets();
  const [qrData, setQrData] = useState(null);
  const [showButton, setShowButton] = useState(true);

  const handlePress = async () => {
    try {
      // AsyncStorage에서 토큰을 가져옴
      const accessToken = await AsyncStorage.getItem('accessToken');
      const refreshToken = await AsyncStorage.getItem('refreshToken');

      if (accessToken || refreshToken) {
        // 사용자 정보와 AsyncStorage에 저장된 토큰을 이용하여 QR 코드 생성
        setQrData(`${accessToken || refreshToken}`);
        setShowButton(false);

        // 30초 후에 qrData를 초기화하여 QR 코드를 만료
        setTimeout(() => {
          setQrData(null);
          setShowButton(true);
        }, 30000);

        // 콘솔에 정보 출력
        console.log('Token:', accessToken || refreshToken);
      } else {
        console.warn('사용자 정보 또는 토큰이 없습니다. 로그인 후 다시 시도하세요.');
      }
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <View style={[styles.container, { paddingBottom: bottom }]}>
      {showButton && (
        <TouchableOpacity onPress={handlePress} style={styles.button}>
          <Text style={styles.buttonText}>Qr 코드 생성</Text>
        </TouchableOpacity>
      )}
      {qrData && <QRCode value={qrData} />}
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  button: {
    backgroundColor: 'blue', // 필요에 따라 색상 조절
    padding: 10,
    borderRadius: 5,
    marginTop: 20,
  },
  buttonText: {
    color: 'white', // 필요에 따라 색상 조절
    fontSize: 16,
  },
});

export default W2002_QRcode;