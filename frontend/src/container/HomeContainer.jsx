import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import Home from "../component/Home";
import { LOGIN_SUCCESS } from "../store/user";
import { fetchApi } from "../utils/fetchUtils";
import RoomContainer from "./RoomContainer";

const HomeContainer = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const { _id } = useSelector((store) => store.user);

  const onJoin = async (inputValue , inputNameValue) => {
    const data = {
      userId: inputValue,
      userName: inputNameValue,
    };

    const resp = await fetchApi("/user", "post", data);
    dispatch(LOGIN_SUCCESS(resp));
  };

  return <>{_id ? <RoomContainer /> : <Home onJoin={onJoin} />}</>;
};
export default HomeContainer;
