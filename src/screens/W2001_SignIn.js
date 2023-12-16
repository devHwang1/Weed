import {
  Alert,
  Image,
  StyleSheet,
  View,
  Keyboard,
  TouchableOpacity,
  Text,
} from 'react-native';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import Input, {
  IconNames,
  KeyboardTypes,
  ReturnKeyTypes,
} from '../components/Input';
import SafeInputView from '../components/SafeInputView';
import { useEffect, useRef, useState } from 'react';
import Button from '../components/Button';
import { signIn } from '../api/W2001_Auth';
import { useUserContext } from '../contexts/UserContext';
import { useNavigation } from '@react-navigation/native';

const W2001_SignIn = () => {
  const insets = useSafeAreaInsets();
  const { setUser } = useUserContext();

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const passwordRef = useRef(null);
  const [disabled, setDisabled] = useState(true);
  const [isLoading, setIsLoading] = useState(false);

  const navigation = useNavigation();

  const onQRScannerPress = () => {
    navigation.navigate('QRScanner');
  };

  useEffect(() => {
    setDisabled(!email || !password);
  }, [email, password]);

  const onSubmit = async () => {
    if (!isLoading && !disabled) {
      try {
        setIsLoading(true);
        Keyboard.dismiss();
        const data = await signIn(email, password);
        setIsLoading(false);
        setUser(data);
        console.log('W2001_SignIn data', data);
      } catch (error) {
        Alert.alert('로그인 실패', error.message, [
          { text: '확인', onPress: () => setIsLoading(false) },
        ]);
      }
    }
  };

  return (
    <SafeInputView>
      <View
        style={[
          styles.container,
          { paddingTop: insets.top, paddingBottom: insets.bottom },
        ]}
      >
        <Image
          source={require('../../assets/WEED_Main.png')}
          style={styles.image}
        />
        <View style={[styles.content]}>
          <Input
            title={'이메일'}
            placeholder="your@email.com"
            keyboardType={KeyboardTypes.EMAIL}
            returnKeyType={ReturnKeyTypes.NEXT}
            value={email}
            onChangeText={(email) => setEmail(email.trim())}
            iconName={IconNames.EMAIL}
            onSubmitEditing={() => passwordRef.current.focus()}
          />
          <Input
            ref={passwordRef}
            title={'비밀번호'}
            returnKeyType={ReturnKeyTypes.DONE}
            secureTextEntry
            value={password}
            onChangeText={(password) => setPassword(password.trim())}
            iconName={IconNames.PASSWORD}
            onSubmitEditing={onSubmit}
          />
          <TouchableOpacity
            style={styles.qrScannerButton}
            onPress={onQRScannerPress}
          >
            <Text style={styles.qrScannerButtonText}>QRScanner</Text>
          </TouchableOpacity>

          <View style={styles.buttonContainer}>
            <Button
              title="로그인"
              onPress={onSubmit}
              disabled={disabled}
              isLoading={isLoading}
            />
          </View>
        </View>
      </View>
    </SafeInputView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#1EAB98',
  },
  content: {
    flex: 1,
    width: '100%',
    // justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#FFFFFF',
    marginTop: '20%',
    paddingTop: '10%',
    borderTopLeftRadius: 20,
    borderTopRightRadius: 20,
  },
  image: {
    width: '100%',
    height: '30%',
  },
  buttonContainer: {
    width: '100%',
    marginTop: 30,
    paddingHorizontal: 20,
  },
  qrScannerButton: {
    position: 'absolute',
    bottom: 20,
    right: 20,
    padding: 10,
    borderRadius: 5,
  },
  qrScannerButtonText: {
    fontWeight: 'bold',
  },
});

export default W2001_SignIn;
