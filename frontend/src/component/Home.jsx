import { Button, Input } from "@mui/material";
import { useState } from "react";

const divStyle = {
  width: "100%",
  display: "flex",
  justifyContent: "center",
  marginTop: "150px",
};

const Home = ({
  onJoin
}) => {
  const [inputValue, setInputValue] = useState("");
  const [inputNameValue, setInputNameValue] = useState("");

  const onChanage = (e) => {
    setInputValue(e.target.value);
  };

  const onNameChanage = (e) => {
    setInputNameValue(e.target.value);
  };

  const onClick = () => {
    onJoin(inputValue , inputNameValue)
  }

  return (
    <>
      <div>
        <div style={divStyle}>
          * ID , NAME 입력 <br />* 기존에 ID가 이미 있다면 그 ID 로 접속
        </div>
        <div style={divStyle}>
          <div style={{ marginRight: "2em" }}>
            ID : <Input value={inputValue} onChange={onChanage}></Input>
          </div>

          <div>
            NAME :{" "}
            <Input value={inputNameValue} onChange={onNameChanage}></Input>
          </div>
        </div>

        <div style={divStyle}>

          <Button variant="contained" onClick={onClick}>Contained</Button>
        </div>
      </div>
    </>
  );
};

export default Home;
