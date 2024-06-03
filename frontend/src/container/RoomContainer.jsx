import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import RoomRegister from "../component/RoomRegister";
import Rooms from "../component/Rooms";
import { fetchApi } from "../utils/fetchUtils";
import { useSelector } from "react-redux";

const ROOM_STYLE = {
  display : 'flex' , 
  justifyContent: 'center'
}

const RoomContainer = () => {
  const [rooms, setRooms] = useState([]);
  const user = useSelector((store) => store.user);

  const navigate = useNavigate();

  useEffect(() => {
    // const eventSource = new EventSource(`http://localhost:8080/rooms`);

    const eventSource = new EventSource(`http://localhost:8080/rooms`);
    eventSource.addEventListener("message", (event) => {
      const { data } = event;
      const room = JSON.parse(data);

      setRooms((prev) => [...prev, room]);
    });

    eventSource.onerror = () => {
      //에러 발생시 할 동작
      eventSource.close(); //연결 끊기
    };

    return () => {
      eventSource.close();
    };
  }, []);

  const onClickJoinButton = (e, value) => {
    navigate("/chat?roomId=" + value);
  };

  const createRoom = async (title) => {
    const data = {
      title,
      users: [user],
    };

    await fetchApi("/room", "POST", data);
  };

  return (
    <div>
      <div style={ROOM_STYLE}>
        <Rooms rooms={rooms} onClickJoinButton={onClickJoinButton} />
      </div>
      <RoomRegister createRoom={createRoom} />
    </div>
  );
};

export default RoomContainer;
