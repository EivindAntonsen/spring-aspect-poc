import React from "react";
import newId from "../utils/newId";
import * as PropTypes from "prop-types";

class ExchangeRates extends React.Component {
  render() {
    let {base, date, rates} = this.props;

    return (
      <div id={newId()} key={newId()}>
        <p>Base currency: {base}</p>
        <p>Exchange rate date: {date}</p>
        <div>Exchange rates: {rates}</div>
      </div>
    );
  }
}

ExchangeRates.propTypes = {
  base: PropTypes.any,
  date: PropTypes.any,
  rates: PropTypes.any
}

export default ExchangeRates;
