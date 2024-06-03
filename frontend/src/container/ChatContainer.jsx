import { useEffect, useRef, useState } from "react";
import { useSelector } from "react-redux";
import { useNavigate, useSearchParams } from "react-router-dom";
import { fetchApi } from "../utils/fetchUtils";
import Chats from "../component/Chats";
import { Box, Button, Container, Input } from "@mui/material";

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
  const navigate = useNavigate();
  const chatDiv = useRef();
  const roomId = searchParams.get("roomId");

  const user = useSelector((store) => store.user);

  const [chat, setChat] = useState([]);

  useEffect(() => {
    const eventSource = new EventSource(
      `http://localhost:8080/chats/${roomId}`
    );
    eventSource.addEventListener("message", (event) => {
      const { data } = event;
      const chat = JSON.parse(data);
      setChat((prev) => [...prev, chat]);
    });

    eventSource.onerror = () => {
      //에러 발생시 할 동작
      eventSource.close(); //연결 끊기
    };

    return () => {
      eventSource.close();
    };
  }, []);

  const onChange = (e) => {
    if (e.target.value) {
      setInputValue(e.target.value);
    }
  };

  const onClick = async () => {
    if (!user._id) navigate("/");

    if (!inputValue) {
      return alert("메시지 입력");
    }

    const data = {
      senderId: user._id,
      senderName: user.userName,
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
      <Container>
        <Box
          height={500}
          width={"100%"}
          my={4}
          display="flex"
          alignItems="center"
          gap={4}
          p={2}
          sx={{ border: "2px solid grey" }}
          overflow={true}
        >
          <Chats chats={chat} />
        </Box>

        <div style={chatInputStyle}>
          <Input onChange={onChange} value={inputValue} />
          <Button variant="contained" onClick={onClick}>
            전송
          </Button>
        </div>
      </Container>
    </div>
  );
};

export default ChatContainer;
