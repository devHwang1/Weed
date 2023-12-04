import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { PRIMARY, WHITE } from '../colors';
import W2004_QRScanner from '../screens/W2004_QRScanner';


const Stack = createNativeStackNavigator();

const ScannerStack = () => {
  return (
    <Stack.Navigator
      screenOptions={{
        contentStyle: { backgroundColor: WHITE },
        headerTitleAlign: 'center',
        headerTintColor: PRIMARY.DEFAULT, 
        headerTitleStyle: {
          fontWeight: '700',
        },
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
