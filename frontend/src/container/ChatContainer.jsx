import { useSearchParams } from "react-router-dom";
import Chat from "../component/Chat";
import { fetchApi } from "../utils/fetchUtils";

const ChatContainer = () =>{
  const [searchParams, setSearchParams] = useSearchParams();
  const [inputValue , setInputValue] = useState();
  const [ chats , setChats ] = useState();
  const roomId = searchParams.get("roomId")

  useEffect(()=>{
    getChatsByRoomId(roomId)
  } , [roomId])

  const getChatsByRoomId = async (roomId) => {
    const data = await fetchApi(`/chats/${roomId}`);
    setChats(data);
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
    <div>
      <div>
        {
          chats.length > 0 && chats.map((i)=>{
            <Chat chat={i}/>
          })
        }
      </div>

      <div>
        <input onChange={inputValue} value={inputValue}/>
        <button onClick={onClick}></button>
      </div>

    </div>
  )
}

export default ChatContainer;