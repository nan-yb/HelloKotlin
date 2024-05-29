import { useState } from "react";
import { fetchApi } from "../utils/fetchUtils";
import { useNavigate } from "react-router-dom";

const divStyle = {
  width : "100%",
  display : "flex",
  justifyContent : 'center',
  marginTop : '150px'
}

const Home = () =>{
  const navigate = useNavigate();

  const [inputValue , setInputValue] = useState();
  const [inputNameValue , setInputNameValue] = useState();

  const onChanage = (e) =>{
    setInputValue(e.target.value);
  }

  const onNameChanage = (e) =>{
    setInputNameValue(e.target.value);
  }


  const onJoin = async (e) => {
    const data = {
      userId : inputValue,
      userName : inputNameValue
    }

    const resp = await fetchApi("/user" , "post" , data);
    navigate(`/room`)
  }

  return (
    <>
      <container >
        <div style={divStyle}>
          * ID , NAME 입력 <br/>
          * 기존에 ID가 이미 있다면 그 ID 로 접속
        </div>
        <div style={divStyle}>
          <div style={{marginRight : '2em'}}>
            ID : <input name="" value={inputValue} onChange={onChanage} ></input>
          </div>
          
          <div>
            NAME : <input name="" value={inputNameValue} onChange={onNameChanage} ></input>
          </div>
        </div>

        <div style={divStyle}>
          <button onClick={onJoin}>입장</button>
        </div>
      </container>
    </>
  )
}

export default Home;