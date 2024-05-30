const Chat = ({chat}) =>{
  console.log(chat)
  return (
    <div style={{paddingLeft : '1em'}}>
      <span>{chat.senderId} : {chat.msg}</span>
    </div>
  )

}

export default Chat;