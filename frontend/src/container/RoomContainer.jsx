import { useEffect, useState } from "react";
import Room from "../component/Room";
import { useNavigate, useSearchParams } from "react-router-dom";
import { fetchApi } from "../utils/fetchUtils";

const RoomContainer = () => {
  const [rooms , setRooms] =  useState([]);
  const navigate = useNavigate();
  
  useEffect(()=>{
    getRooms()
  } , [])

  const getRooms = async () => {
    const response = await fetchApi(`/rooms`);

    console.log(response)
    // setRooms(data)
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