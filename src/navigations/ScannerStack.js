import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { PRIMARY, WHITE } from '../colors';
import HeaderRightLogout from '../components/HeaderRightLogout';
import W2004_QRScanner from '../screens/W2004_QRScanner';

const Stack = createNativeStackNavigator();

const ScannerStack = () => {
  return (
    <Stack.Navigator
      screenOptions={{
        contentStyle: { backgroundColor: WHITE },
        headerStyle: { backgroundColor: PRIMARY.DEFAULT },
        headerTitleAlign: 'center',
        headerTintColor: WHITE,
        headerTitleStyle: {
          fontWeight: '700',
        },
        headerRight: () => <HeaderRightLogout />,
      }}
    >
      <Stack.Screen
        name="QRScanner"
        component={W2004_QRScanner}
        options={{
          title: 'QR 스캐너',
        }}
      />
    </Stack.Navigator>
  );
};

export default ScannerStack;
