import { useState, useEffect, useCallback } from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { fetchWorkingData } from '../api/W2003_WorkingData';


const W2003_Working = () => {
  const [workingData, setWorkingData] = useState(null);
  const [loading, setLoading] = useState(true);

  const mapServerResponseToWorkingData = (serverResponse) => {
    return {
      date: serverResponse.date,
      checkInTime: serverResponse.checkInTime,
      checkOutTime: serverResponse.checkOutTime,
      workingHours: serverResponse.workingHours,
    };
  };

  const fetchData = useCallback(async () => {
    try {
      const response = await fetchWorkingData();

      console.log('서버 응답 데이터:', response.data);

      if (response.status === 200 && response.data.success) {
        // 서버 응답 데이터를 프론트엔드에 맞게 가공
        const { recentCheckIn, recentCheckOut } = response.data;

        if (recentCheckIn) {
          const formattedCheckIn =
            mapServerResponseToWorkingData(recentCheckIn);
          setWorkingData(formattedCheckIn);
        } else if (recentCheckOut) {
          const formattedCheckOut =
            mapServerResponseToWorkingData(recentCheckOut);
          setWorkingData(formattedCheckOut);
        }
      } else {
        console.error('서버 응답 에러:', response.status);
      }
    } catch (error) {
      console.error('네트워크 오류:', error);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchData();
  }, [fetchData]);

  console.log('workingData:', workingData);

  return (
    <View style={styles.container}>
      {loading ? (
        <Text>Loading...</Text>
      ) : workingData ? (
        <View>
          <View style={styles.dataContainer1}>
            <Text>출근 날짜: {workingData.date}</Text>
            <Text>출근 시간: {workingData.checkInTime}</Text>
          </View>
          <View style={styles.dataContainer2}>
            <Text>퇴근 날짜: {workingData.checkOutTime}</Text>
            <Text>퇴근 시간: {workingData.checkOutTime}</Text>
            <Text>근무 시간: {workingData.workingHours} 시간</Text>
          </View>
        </View>
      ) : (
        <Text>출근 기록이 없습니다.</Text>
      )}
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 20,
  },
  dataContainer1: {
    marginBottom: 10, // Add margin-bottom to create space between the two containers
    backgroundColor: '#fff',
    borderRadius: 10,
    padding: 20,
    elevation: 3, // for Android shadow
    shadowOffset: { width: 0, height: 2 }, // for iOS shadow
    shadowOpacity: 0.2, // for iOS shadow
  },
  dataContainer2: {
    backgroundColor: '#fff',
    borderRadius: 10,
    padding: 20,
    elevation: 3, // for Android shadow
    shadowOffset: { width: 0, height: 2 }, // for iOS shadow
    shadowOpacity: 0.2, // for iOS shadow
  },
});

export default W2003_Working;
