
const Rooms = ({rooms , onClickJoinButton}) => {
  return (
    <div>
      {rooms.map((i) => {
        return (
          <div key={i._id.timestamp}  style={{display : 'flex' , marginTop : '0.5em'}}>
            <div style={{marginRight : '1em'}}>
              {i.title} 
            </div>
            {/* {
              i.users.map((j) => {
                return <span style={{marginLeft:'1em'}}>
                  {j.userId}
                </span>
              })
            } */}
            <button onClick={(e)=>{onClickJoinButton(e , i._id)}}>
              입장
            </button>
          </div>
        )
      })}
    </div>

  )
}

export default Rooms;
