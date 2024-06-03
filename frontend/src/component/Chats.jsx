const Chats = ({chats}) =>{
  
  if(!chats || chats.length == 0 ){
    return ;
  } 

  return (
    <div style={{paddingLeft : '1em'}}>
      {
        chats.map((chat , index)=>{
          return (
            <div key={index}>
              <span>{chat.senderName} : {chat.msg}</span>
            </div>
          )
        })
      }
    </div>
  )

}

export default Chats;