import { useState } from 'react';
import { Text, View, TouchableOpacity, StyleSheet } from 'react-native';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import QRCode from 'react-native-qrcode-svg';
import AsyncStorage from '@react-native-async-storage/async-storage';

const W2002_QRcode = () => {
  const { bottom } = useSafeAreaInsets();
  const [qrData, setQrData] = useState(null);
  const [showButton, setShowButton] = useState(true);
  // const [scannedData, setScannedData] = useState(null);

  const handlePress = async () => {
    try {
      // AsyncStorage에서 토큰을 가져옴
      const accessToken = await AsyncStorage.getItem('accessToken');
      

      if (accessToken) {
        // 사용자 정보와 AsyncStorage에 저장된 토큰을 이용하여 QR 코드 생성
        setQrData(`${accessToken}`);
        setShowButton(false);

        // 30초 후에 qrData를 초기화하여 QR 코드를 만료
        setTimeout(() => {
          setQrData(null);
          setShowButton(true);
        }, 30000);

        // 콘솔에 정보 출력
        console.log('Token:', accessToken);
      } else {
        console.warn(
          '사용자 정보 또는 토큰이 없습니다. 로그인 후 다시 시도하세요.'
        );
      }
    } catch (error) {
      console.error(error);
    }
  };
  

  return (
    <View style={[styles.container, { paddingBottom: bottom }]}>
      <Text style={styles.title}>전자출입명부</Text>
      <Text style={styles.content}>출퇴근 등록을 위해 QR코드를 찍어주세요</Text>
      {showButton && (
        <TouchableOpacity onPress={handlePress} style={styles.button}>
          <Text style={styles.buttonText}>QR 코드 생성</Text>
        </TouchableOpacity>
      )}
      {qrData && (
         // qrData가 존재하는 경우에만 렌더링
        <View style={styles.qrContainer}>
          <QRCode value={qrData} size={200} />
        </View>
      )}
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
    backgroundColor: '#00c473',
    padding: 10,
    borderRadius: 5,
    marginTop: 20,
  },
  buttonText: {
    color: 'white',
    fontSize: 16,
  },
  qrContainer: {
    padding: 10,
    borderWidth: 5,
    borderColor: '#00c473',
    borderRadius: 10,
  },
  title: {
    fontSize: 40,
    fontWeight: 'bold',
    marginBottom: 20,
    color: '#00c473',
  },
  content: {
    fontSize: 20,
    fontWeight: 'bold',
    marginBottom: 20,
    color: 'black',
    textAlign: 'center',
  },
});

export default W2002_QRcode;
