import ChatContainer from './container/ChatContainer';
import RoomContainer from './container/RoomContainer'

import { Route, Routes } from 'react-router-dom';

function App() {
  return (
    <>
      <Routes>
        <Route path="/" element={<RoomContainer/>} />
        <Route path="/chat" element={<ChatContainer/>} />
      </Routes>

      {/* <div>
        <ChatContainer/>
      </div>   */}
    </>
  )
}

export default App
