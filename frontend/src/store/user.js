import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  _id : "",
  userId: "",
  userName: "",
};

const userSlice = createSlice({
  name: 'user',
  initialState,
  reducers: {
    LOGIN_SUCCESS: (state, action) => {
      state._id = action.payload._id
      state.userId = action.payload.userId;
      state.userName = action.payload.userName;
    },

    LOGOUT_SUCCESS: () => initialState,
  },
});

export const { LOGIN_SUCCESS, LOGOUT_SUCCESS } = userSlice.actions;

export default userSlice.reducer;
