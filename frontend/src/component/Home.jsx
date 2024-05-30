
const divStyle = {
  width : "100%",
  display : "flex",
  justifyContent : 'center',
  marginTop : '150px'
}

const Home = ({
  inputValue,
  inputNameValue,
  onChanage,
  onNameChanage,
  onJoin,
}) =>{

  return (
    <>
      <div >
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
      </div>
    </>
  )
}

export default Home;