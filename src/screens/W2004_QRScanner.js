import { useState, useEffect } from 'react';
import { View, StyleSheet, Button, Alert, Text, TouchableOpacity } from 'react-native';
import { BarCodeScanner } from 'expo-barcode-scanner';
import { StatusBar } from 'expo-status-bar';
import axios from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';

import { useUserContext } from '../contexts/UserContext';

const W2004_QRScanner = () => {
  const [hasPermission, setHasPermission] = useState(null);
  const [scanned, setScanned] = useState(false);

  useEffect(() => {
    (async () => {
      const { status } = await BarCodeScanner.requestPermissionsAsync();
      setHasPermission(status === 'granted');
    })();
  }, []);

  const showAlertAndReset = (title, message) => {
    Alert.alert(title, message);
    
    // 1.5초 후에 스캔 창을 다시 열기
    setTimeout(() => {
      setScanned(false);
    }, 1500);
  };

  const handleBarCodeScanned = async ({ data }) => {
    if (!hasPermission) {
      showAlertAndReset('권한 오류', '카메라 권한을 허용해주세요.');
      return;
    }

    setScanned(true);

    try {
      const response = await axios.post(
        'http://10.100.203.31:8099/api/app/member/checkInOut',
        { token: data },
        { headers: { 'Content-Type': 'application/json' } }
      );

      console.log('Server Response:', response.data);

      if (response.data.success) {
        if (response.data.checkIn) {
          showAlertAndReset('출근 성공', '출근에 성공했습니다.');
        } else {
          showAlertAndReset('퇴근 성공', '퇴근에 성공했습니다.');
        }
      } else {
        showAlertAndReset('에러', response.data.error || '처리에 실패했습니다.');
      }
    } catch (error) {
      console.error('Error:', error);
      showAlertAndReset('에러', '처리에 실패했습니다.');
    }

    console.log('Scanned Data:', data);
  };


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
      <BarCodeScanner
        onBarCodeScanned={scanned ? undefined : handleBarCodeScanned}
        style={StyleSheet.absoluteFillObject}
      />
      {scanned && (
        <Button title={'다시 스캔'} onPress={() => setScanned(false)} />
      )}
      <View style={{ margin: 50 }}>
        <StatusBar style="dark" />
      </View>
      <TouchableOpacity onPress={handleLogout}>
        <Text style={styles.logoutText}>로그아웃</Text>
      </TouchableOpacity>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'column',
    justifyContent: 'flex-end',
  },
  logoutText: {
    textAlign: 'right',
    color: 'blue',
    padding: 10,
  },
});

export default W2004_QRScanner;
