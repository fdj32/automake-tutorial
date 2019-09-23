'use strict';

const hexChar = c => {
    var out = c.toString(16);
    return (1 === out.length ? "0" : "") + out;
}

const leftPadding0 = (num, length) => {
    for (var len = (num + "").length; len < length; len = num.length) {
        num = "0" + num;
    }
    return num;
}

const hexOutput = (s, length) => {
    var out = "Offset  ";
    for (var i = 0; i < length; i++) {
        out += hexChar(i) + " ";
    }
    out += "\n";
    var line = 0;
    var lineHex = "";
    var lineAscii = "";
    for (var i = 0; i < s.length; i++) {
        if (i % length === 0) {
            out += lineHex + lineAscii;
            lineHex = "";
            lineAscii = "";
            line++;
            lineHex += "\n" + leftPadding0(line, 6) + "  ";
            lineAscii += "\n" + leftPadding0(line, 6) + "  ";
        }
        lineHex += hexChar(s.charCodeAt(i)) + " ";
        if (s.charCodeAt(i) < 32 || s.charCodeAt(i) > 126) {
            lineAscii += hexChar(s.charCodeAt(i)) + " ";
        } else {
            lineAscii += s[i] + "  ";
        }
        if (i === s.length - 1) {
            out += lineHex + lineAscii;
        }
    }

    console.log(out);
}

module.exports = {
    hexChar: this.hexChar,
    hexOutput: hexOutput
}