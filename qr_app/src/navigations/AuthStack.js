import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { WHITE } from '../colors';
import W2001_SignIn from '../screens/W2001_SignIn';

const Stack = createNativeStackNavigator();

const AuthStack = () => {
  return (
    <Stack.Navigator
      initialRouteName="SignIn"
      screenOptions={{
        contentStyle: { backgroundColor: WHITE },
        headerShown: false,
      }}
    >
      <Stack.Screen name="SignIn" component={W2001_SignIn} />
    </Stack.Navigator>
  );
};

export default AuthStack;
