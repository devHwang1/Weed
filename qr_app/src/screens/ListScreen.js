import { useState } from 'react';
import { Text, View, TouchableOpacity, StyleSheet } from 'react-native';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import QRCode from 'react-native-qrcode-svg';
import { signIn } from '../api/auth';

const ListScreen = () => {
  const { bottom } = useSafeAreaInsets();
  const [qrData, setQrData] = useState(null);
  const [showButton, setShowButton] = useState(true);

  const handlePress = async () => {
    try {
      const email = await signIn('1234', '1234');
      setQrData(email);
      setShowButton(false); // 버튼 숨김

      // 30초 후에 qrData를 초기화하여 QR 코드를 만료
      setTimeout(() => {
        setQrData(null);
        setShowButton(true); // 버튼 표시
      }, 30000);
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

export default ListScreen;
