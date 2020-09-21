import React, {useState} from 'react';
import './App.css';
import ExchangeRate from "./components/ExchangeRate";
import ExchangeRates from "./components/ExchangeRates";
import newId from "./utils/newId";

function App() {
  const BASE_PATH = "http://localhost:8096/"
  const EXCHANGE_RATES = "exchange-rates/"
  const LATEST = "latest"
  const PREVIOUS = "previous"

  const [response, setResponse] = useState([]);
  const rates = response.exchangeRates || {};

  async function getLatestExchangeRates() {
    fetch(BASE_PATH + EXCHANGE_RATES + LATEST)
      .then(response => response.json())
      .then(data => setResponse(data.value));
  }

  async function getPreviousExchangeRates() {
    fetch(BASE_PATH + EXCHANGE_RATES + PREVIOUS)
      .then(response => response.json())
      .then(data => setResponse(data.value));
  }

  function mapExchangeRates() {
    return Object.entries(rates).map((x) =>
      <ExchangeRate
        id={newId()}
        key={newId()}
        currency={x[0]}
        rate={x[1]}
      />
    );
  }

  return <div className="App">
    <button
      onClick={getLatestExchangeRates}
      className="get-latest-rates-button"
      type="button"
    >
      Latest
    </button>
    <button
      onClick={getPreviousExchangeRates}
      className="get-previous-rates-button"
      type="button"
    >
      Previous
    </button>
    {
      <ExchangeRates
        key={newId()}
        base={response.baseCurrency}
        date={response.date}
        rates={mapExchangeRates()}
      />
    }
  </div>;
}

export default App;
