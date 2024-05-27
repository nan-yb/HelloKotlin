import { useEffect, useState } from "react";
import Room from "../component/Room";
import { useNavigate } from "react-router-dom";

const RoomContainer = () => {
  
  const [rooms , setRooms] =  useState();
  const navigate = useNavigate();
  
  useEffect(()=>{
    getRooms()
  } , [])

  const getRooms = async () => {
    const response = await fetch('http://localhost:8080/room/1' , {
      method : "GET",
    });

    const data = await response.json();
    setRooms(data)
  }

  const onClickJoinButton = (e , value) =>{
    navigate('chat?roomId=' + value)
  }

  return (
    <div>
      <Room rooms={rooms} onClickJoinButton={onClickJoinButton}/>
    </div>
  )
}

export default RoomContainer;