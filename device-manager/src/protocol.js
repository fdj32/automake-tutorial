'use strict';
const RFU = String.fromCharCode(0x00);
const STX = String.fromCharCode(0x02);
const ETX = String.fromCharCode(0x03);
const EOT = String.fromCharCode(0x04);
const ENQ = String.fromCharCode(0x05);
const ACK = String.fromCharCode(0x06);
const LF  = String.fromCharCode(0x0a);
const NAK = String.fromCharCode(0x15);
const CAN = String.fromCharCode(0x18);
const ESC = String.fromCharCode(0x1b);
const FS  = String.fromCharCode(0x1c);
const RS  = String.fromCharCode(0x1e);
const SPACE = ' ';

const CPX_58_DISPLAY = "58.0041";

const lrc = s => {
    var checksum = s.charCodeAt(0);
    var startIndex = 1;
    if (s[0] == STX) {
        checksum = s.charCodeAt(1);
        startIndex = 2;
    }
    for (var i = startIndex; i < s.length; i++) {
        checksum ^= s.charCodeAt(i);
    }
    return String.fromCharCode(checksum);
}

const fill = (s, length, c) => {
    if (s.length > length) {
        return s.substring(0, length);
    }
    var out = s;
    for (var i = 0; i < length - s.length; i++) {
        out += c;
    }
    return out;
}

const cpx58display = (line1, line2, line3, line4) => {
    var out = "";
    out += STX;
    out += CPX_58_DISPLAY;
    out += fill(line1, 16, SPACE);
    out += fill(line2, 16, SPACE);
    out += fill(line3, 16, SPACE);
    out += fill(line4, 16, SPACE);
    out += ETX;
    var c = lrc(out);
    out += c;
    return out;
}

module.exports = {
    cpx58display: cpx58display
}