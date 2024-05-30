import ChatContainer from './container/ChatContainer';
import HomeContainer from './container/HomeContainer';
import RoomContainer from './container/RoomContainer'

import { Route, Routes } from 'react-router-dom';

import { UserContext } from "./context/UserContext";
import { useState } from 'react';

function App() {

  const [user , setUser] = useState();

  return (
    <>
      <UserContext.Provider value={{ user, setUser }}>
        <Routes>
          <Route path="/" element={<HomeContainer/>} />
          <Route path="/room" element={<RoomContainer/>} />
          <Route path="/chat" element={<ChatContainer/>} />
        </Routes>
      </UserContext.Provider>
    </>
  )
}

export default App
