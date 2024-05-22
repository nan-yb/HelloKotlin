import { useEffect, useState } from 'react'

function App() {

  const [room , setRooms] =  useState();
  
  useEffect(()=>{
    getRooms()
  } , [])

  const getRooms = async () => {
    const response = await fetch('http://localhost:8080/room/1' , {
      method : "GET",
    });
    const jsonData = response.json();
    console.log(jsonData)

    setRooms(jsonData)
  }

  return (
    <>
      <div>
        {room}
      </div>  
    </>
  )
}

export default App
