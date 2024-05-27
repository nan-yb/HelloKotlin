import { useSearchParams } from "react-router-dom";

const ChatContainer = () =>{
  const [searchParams, setSearchParams] = useSearchParams();
  const roomId = searchParams.get("roomId")

  useEffect(()=>{
    getChatsByRoomId(roomId)
  } , [roomId])

  const getChatsByRoomId = (roomId) => {
    
    return ;
  }

  if(!roomId) return;

  return <>{roomId}</>
}

export default ChatContainer;