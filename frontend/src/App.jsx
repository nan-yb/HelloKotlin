import ChatContainer from "./container/ChatContainer";
import HomeContainer from "./container/HomeContainer";
import RoomContainer from "./container/RoomContainer";

import { Route, Routes } from "react-router-dom";
import DefaultLayout from "./layout/DefaultLayout";

function App() {
  return (
    <>
      <Routes>
        <Route element={<DefaultLayout />}>
          <Route path="/" element={<HomeContainer />} />
          <Route path="/room" element={<RoomContainer />} />
          <Route path="/chat" element={<ChatContainer />} />
        </Route>
      </Routes>
    </>
  );
}

export default App;
