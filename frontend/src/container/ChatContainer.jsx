import { useSearchParams } from "react-router-dom";
import Chat from "../component/Chat";
import { fetchApi } from "../utils/fetchUtils";
import { useEffect, useState } from "react";

const chatStyle={
  margin : 'auto' ,
  border : '1px solid' , 
  width : '50%' , 
  minHeight : '300px',
  padding : '1em'
}

const chatInputStyle = {
  margin : 'auto',
  width : '100%',
  display : 'flex',
  justifyContent : 'center',
  marginTop : '1em'
}

const ChatContainer = () =>{
  const [searchParams, setSearchParams] = useSearchParams();
  const [inputValue , setInputValue] = useState();
  const [ chats , setChats ] = useState([]);
  const roomId = searchParams.get("roomId")

  useEffect(()=>{
    getChatsByRoomId(roomId)
  } , [roomId])

  const getChatsByRoomId = async (roomId) => {
    const data = await fetchApi(`/chats/${roomId}`);
    // setChats(data);
  }

  const onChange= (e) => {
    if(e.target.value){
      setInputValue(e.target.value)
    }
  }

  const onClick = async () =>{
    const data ={
      roomId ,
      msg : inputValue
    }

    await fetchApi(`/chats` , "POST" , data);
  }

  if(!roomId) return;

  return (
    <container>
      <div style={chatStyle}>
        {
          chats && chats.map((i)=>{
            return <Chat chat={i}/>
          })
        }
      </div>

      <div style={chatInputStyle}>
        <input onChange={onChange} value={inputValue}/>
        <button onClick={onClick}>전송</button>
      </div>

    </container>
  )
}

export default ChatContainer;