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

  const [title , setTitle] = useState();

  const onChange= (e)=> {
    const value = e.target.value
    setTitle(value)
  }

  const onClick= (e) =>{
    createRoom(title)
  }
  
  return (
    <div style={divStyle}>
      <input value={title} onChange={onChange}></input>
      <button onClick={onClick}>만들기</button>
    </div>
  )
}

export default RoomRegister;