import { Button } from "@mui/material";

const Rooms = ({rooms , onClickJoinButton}) => {
  return (
    <div>
      {rooms.map((i , index) => {
        return (
          <div key={index}  style={{display : 'flex' , marginTop : '0.5em'}}>
            <div style={{marginRight : '1em' , width : '200px'}}>
              {i.title} 
            </div>
            <Button variant="contained"  onClick={(e)=>{onClickJoinButton(e , i._id)}}>
              입장
            </Button>
          </div>
        )
      })}
    </div>

  )
}

export default Rooms;
