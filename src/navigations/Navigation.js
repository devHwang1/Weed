import { useUserContext } from '../contexts/UserContext';
import { NavigationContainer } from '@react-navigation/native';
import MainStack from './MainStack';
import AuthStack from './AuthStack';
import ScannerStack from './ScannerStack';

const Navigation = () => {
  const { user } = useUserContext();

  return (
    <NavigationContainer>
      {user ? (
        user.authority === 'GUEST' ? <ScannerStack /> : <MainStack/>
        ) : (
        <AuthStack />
        )}
    </NavigationContainer>
  );
};

export default Navigation;