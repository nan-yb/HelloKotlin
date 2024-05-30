import { useSearchParams } from "react-router-dom";
import Chat from "../component/Chat";
import { fetchApi } from "../utils/fetchUtils";
import { useContext, useEffect, useRef, useState } from "react";
import { UserContext } from "../context/UserContext";

const chatStyle = {
  margin: "auto",
  border: "1px solid",
  width: "50%",
  minHeight: "300px",
  padding: "1em",
};

const chatInputStyle = {
  margin: "auto",
  width: "100%",
  display: "flex",
  justifyContent: "center",
  marginTop: "1em",
};

const ChatContainer = () => {
  const [searchParams, setSearchParams] = useSearchParams();
  const [inputValue, setInputValue] = useState();
  const [isStreaming, setIsStreaming] = useState(false);
  const chatDiv = useRef();
  const roomId = searchParams.get("roomId");
  
  const { user , setUser } = useContext(UserContext);

  useEffect(() => {
    const eventSource = new EventSource(
      `http://localhost:8080/chats/${roomId}`
    );
    eventSource.addEventListener("message", (event) => {
      const { data } = event;
      const chat = JSON.parse(data);
      createHtml(chat);
    });

    eventSource.onerror = () => {
      //에러 발생시 할 동작
      eventSource.close(); //연결 끊기
    };

    return () => {
      eventSource.close();
    };
  }, []);

  const createHtml = (chat) => {
    const tmp = document.createElement("div");
    tmp.innerHTML = `
      <span>${chat.senderId} : ${chat.msg}</span>
    `;
    chatDiv.current.append(tmp);
  };

  const onChange = (e) => {
    if (e.target.value) {
      setInputValue(e.target.value);
    }
  };

  const onClick = async () => {
    const data = {
      senderId : user._id ,
      roomId,
      msg: inputValue,
      // senderId : user.
    };

    await fetchApi(`/chats`, "POST", data);

    setInputValue("");
  };

  if (!roomId) return;

  return (
    <div>
      <div style={chatStyle} ref={chatDiv}>
        {/*  */}
      </div>

      <div style={chatInputStyle}>
        <input onChange={onChange} value={inputValue} />
        <button onClick={onClick}>전송</button>
      </div>
    </div>
  );
};

export default ChatContainer;
