import ChatContainer from './container/ChatContainer';
import HomeContainer from './container/HomeContainer';
import RoomContainer from './container/RoomContainer'

import { Route, Routes } from 'react-router-dom';

function App() {
  return (
    <>
      <Routes>
        <Route path="/" element={<HomeContainer/>} />
        <Route path="/room" element={<RoomContainer/>} />
        <Route path="/chat" element={<ChatContainer/>} />
      </Routes>
    </>
  )
}

export default App
