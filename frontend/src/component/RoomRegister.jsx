import { Button, Input } from "@mui/material";
import { useState } from "react";

const divStyle = {
  width : '100%',
  display : 'flex' , 
  justifyContent : 'center',
  bottom: 0,
  position : 'absolute' ,
  marginBottom : '10em'
}

const RoomRegister = ({createRoom}) =>{

  const [title , setTitle] = useState('');

  const onChange= (e)=> {
    const value = e.target.value
    setTitle(value)
  }

  const onClick= (e) =>{
    createRoom(title)
  }
  
  return (
    <div style={divStyle}>
      <Input value={title} onChange={onChange}/>
      <Button variant="contained" onClick={onClick}>만들기</Button>
    </div>
  )
}

export default RoomRegister;