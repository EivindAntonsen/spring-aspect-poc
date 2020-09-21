import React from "react";
import newId from "../utils/newId";

const ExchangeRate = ({currency, rate}) => (
  <div
    id={newId()}
    key={newId()}
  >
    {currency}: {rate}
  </div>
);

export default ExchangeRate;
