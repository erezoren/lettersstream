import React, { useState } from "react";

function App() {
  const [word, setWord] = useState("");
  const [letter, setLetter] = useState("");
  const [message, setMessage] = useState("");
  const [matchResult, setMatchResult] = useState(null);

  const handleSetWord = async () => {
      if(word===""){
        setMessage("Please input a word");
        return
      }
    const res = await fetch(`/v1/word/${word}`);
    const text = await res.text();
    setMessage(text);
  };

  const handleCheckLetter = async () => {
    const res = await fetch("/v1/letters", {
      method: "POST",
      headers: { "Content-Type": "text/plain" },
      body: letter,
    });
    const data = await res.json();
    setMatchResult(data);
  };

  return (
    <div style={{ maxWidth: 400, margin: "2rem auto", fontFamily: "sans-serif" }}>
      <h1>Letter Stream Web App</h1>
      <h2>Set Word</h2>
      <input value={word} onChange={e => {
        setWord(e.target.value)
        setLetter("")
      }

      } />
      <button onClick={handleSetWord}>Set</button>
      <div><p style={{color: "blue"}}>{message}</p></div>
      <h2>Check Letter</h2>
      <input value={letter} maxLength={1} onChange={e => setLetter(e.target.value)} />
      <button onClick={handleCheckLetter}>Check</button>
      {matchResult && (
        <div>
          {
            matchResult.matching
              ? <p style={{color: "green"}}>Stream matches the word!</p>
              : <p style={{color: "red"}}>{`No match${matchResult.reason ? ": " + matchResult.reason : ""}`}</p>
          }
        </div>
      )}
    </div>
  );
}

export default App;