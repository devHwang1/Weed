import { useState, useEffect } from "react";
import { View, StyleSheet, Button, Alert } from "react-native";
import { BarCodeScanner } from "expo-barcode-scanner";
import { StatusBar } from "expo-status-bar";
import axios from "axios";

const App = () => {
  const [hasPermission, setHasPermission] = useState(null);
  const [scanned, setScanned] = useState(false);

  useEffect(() => {
    (async () => {
      const { status } = await BarCodeScanner.requestPermissionsAsync();
      setHasPermission(status === "granted");
    })();
  }, []);

  const handleBarCodeScanned = async ({ data }) => {
    if (!hasPermission) {
      Alert.alert("권한 오류", "카메라 권한을 허용해주세요.");
      return;
    }
  
    setScanned(true);
  
    try {
      // 서버에 토큰을 전송하고 사용자 정보를 가져옴
      const response = await axios.post(
        "http://10.100.203.31:8080/api/app/member/working",
        { token: data },
        {
          headers: { "Content-Type": "application/json" },
        }
      );
  
      // 서버로부터 받은 응답 확인
      console.log("Server Response:", response.data);
  
      // 여기서 서버로부터 받은 응답을 확인하고 필요한 작업 수행
  
      if (response.data.success) {
        // 출근 성공 알림
        Alert.alert("출근 성공", "출근에 성공했습니다.");
      } else {
        // 출근 실패 알림
        Alert.alert("에러", "출근에 실패했습니다.");
      }
  
    } catch (error) {
      console.error("Error:", error);
  
      // 출근 실패 알림
      Alert.alert("에러", "출근에 실패했습니다.");
    }
  };

  return (
    <View style={styles.container}>
      <BarCodeScanner
        onBarCodeScanned={scanned ? undefined : handleBarCodeScanned}
        style={StyleSheet.absoluteFillObject}
      />
      {scanned && (
        <Button title={"다시 스캔"} onPress={() => setScanned(false)} />
      )}
      <View style={{ margin: 50 }}>
        <StatusBar style="dark" />
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: "column",
    justifyContent: "flex-end",
  },
});

export default App;
