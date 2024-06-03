import { combineReducers } from '@reduxjs/toolkit';
import { persistReducer } from 'redux-persist';
import storage from 'redux-persist/lib/storage';
import userReducer from './user';

const persistConfig = {
  key: 'root',
  storage, // localStorage에 저장합니다.
  whitelist: ['user'], // reducer 중에 auth reducer만 localstorage에 저장합니다.
  blacklist: [], // 해당 reducer만 제외합니다
};

const reducer = (state , action) => {
  return combineReducers({
    user: userReducer,
  })(state, action);
};

export default persistReducer(persistConfig, reducer);
