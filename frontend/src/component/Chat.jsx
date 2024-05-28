const Chat = ({chat}) =>{

  return (
    <div style={{paddingLeft : '1em'}}>
      <span>{chat.senderId} : {chat.msg}</span>
    </div>
  )

}

export default Chat;