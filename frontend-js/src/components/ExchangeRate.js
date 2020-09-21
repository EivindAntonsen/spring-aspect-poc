import React from "react";
import newId from "../utils/newId";
import * as PropTypes from "prop-types";

class ExchangeRate extends React.Component {
  render() {
    let {currency, rate} = this.props;

    return (
        <div
            id={newId()}
            key={newId()}
        >
          {currency}: {rate}
        </div>
    );
  }
}

ExchangeRate.propTypes = {
    currency: PropTypes.string,
    rate: PropTypes.number
}

export default ExchangeRate;
