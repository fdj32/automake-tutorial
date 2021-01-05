'use strict';
const protocol = require('./protocol');
const STX = String.fromCharCode(0x02);
const ETX = String.fromCharCode(0x03);
const SPACE = ' ';

function Cpx58display(props) {
    this.props = props;
    this.format = () => {
        var s = STX + "58.";
        s += props.mode;
        return s;
    }

    this.parse = data => {
        this.props = {};
    }
}

module.exports = {
    Cpx58display: Cpx58display
}