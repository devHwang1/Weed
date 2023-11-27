import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { PRIMARY, WHITE } from '../colors';
import HeaderLeftButton from '../components/HeaderLeftButton';
import HeaderRightButton from '../components/HeaderRightButton';
import W2002_QRcode from '../screens/W2002_QRcode';
import W2003_Working from '../screens/W2003_Working';

const Stack = createNativeStackNavigator();

const MainStack = () => {
  return (
    <Stack.Navigator
      screenOptions={{
        contentStyle: { backgroundColor: WHITE },
        headerTitleAlign: 'center',
        headerTintColor: PRIMARY.DEFAULT,
        headerTitleStyle: {
          fontWeight: '700',
        },
        headerLeft: HeaderLeftButton,
      }}
    >
      <Stack.Screen
        name="QR"
        component={W2002_QRcode}
        options={{
          title: '출퇴근 QR',
          headerRight: HeaderRightButton,
        }}
      />
      <Stack.Screen name="List" component={W2003_Working} />
    </Stack.Navigator>
  );
};

export default MainStack;
