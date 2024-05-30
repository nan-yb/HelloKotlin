import { useContext, useEffect, useState } from "react";
import Rooms from "../component/Rooms";
import { useNavigate, useSearchParams } from "react-router-dom";
import { fetchApi } from "../utils/fetchUtils";
import { UserContext } from "../context/UserContext";
import RoomRegister from "../component/RoomRegister";

const RoomContainer = () => {

  const { user } = useContext(UserContext);
  const [rooms , setRooms] =  useState([]);
  const navigate = useNavigate();
  
  useEffect(()=>{
    // const eventSource = new EventSource(`http://localhost:8080/rooms`);
    
    const eventSource = new EventSource(`http://localhost:8080/rooms`);
    eventSource.addEventListener('message', (event) => {
      const {data} = event;
      const room = JSON.parse(data);

      setRooms((prev)=>[...prev , room])
    });
  
    eventSource.onerror = () => {
      //에러 발생시 할 동작
      eventSource.close(); //연결 끊기
    };
    
    return () => {
      eventSource.close();
    };
  } , [])


  const onClickJoinButton = (e , value) =>{
    navigate('/chat?roomId=' + value)
  }

  const createRoom = async (title) => {
    
    const data= {
      title , 
      users : [user]
    }

    await fetchApi("/room" , 'POST' , data);
  }

  return (
    <div>
      <Rooms rooms={rooms} onClickJoinButton={onClickJoinButton}/>
      <RoomRegister createRoom={createRoom}/>
    </div>
  )
}

export default RoomContainer;