import { Button } from "@mui/material";
import { useDispatch } from "react-redux";
import { Outlet } from "react-router-dom";
import { LOGOUT_SUCCESS } from "../store/user";

const HEADER_STYLE = {
  margin: "2em 2em 4em 0",
  display: "flex",
  flexDirection: "row-reverse",
};

const DefaultLayout = () => {

  const dispatch = useDispatch();

  const onLogout = () => {
    dispatch(LOGOUT_SUCCESS());
  };

  return (
    <>
      <div>
        <div style={HEADER_STYLE}>
          <Button variant="contained" color={"error"} onClick={onLogout}>
            Logout
          </Button>
        </div>
        <div>
          <Outlet />
        </div>
      </div>
    </>
  );
};

export default DefaultLayout;
