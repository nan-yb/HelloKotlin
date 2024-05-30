import Home from "../component/Home";
import { useContext, useState } from "react";
import { fetchApi } from "../utils/fetchUtils";
import { useNavigate } from "react-router-dom";
import { UserContext } from "../context/UserContext";


const HomeContainer = () => {

  const navigate = useNavigate();

  const { user , setUser } = useContext(UserContext);

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
    setUser(resp)
    navigate(`/room`)
  }

  return (
    <>
      <Home
        inputValue={inputValue}
        inputNameValue={inputNameValue}
        onChanage={onChanage}
        onNameChanage={onNameChanage}
        onJoin={onJoin}
      />
    </>
  )
}
export default HomeContainer;
